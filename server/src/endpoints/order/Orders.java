package endpoints.order;

import database.DatabaseManager;
import database.tables.Department;
import database.tables.FoodOrder;
import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import database.tables.OrderStatus;
import database.tables.RestaurantTableStaff;
import database.tables.StaffNotification;
import database.tables.StaffSession;
import database.tables.TableSession;
import database.tables.Transaction;
import endpoints.notification.Notifications;
import endpoints.transaction.TransactionIdParams;
import java.io.UnsupportedEncodingException;
import database.tables.WaiterSale;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

/**
 * This class is used to to communicate between the front end and back end in regards to orders.
 */
public class Orders {

  /**
   * Returns an order as JSON. JSON input: foodOrderId: The ID for the food order being listed.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string containing JSON which holds the current order.
   */
  public static String getOrderItems(Request request, Response response) {
    ListOrderMenuItemParams omiList = JsonUtil.getInstance().fromJson(request.body(),
        ListOrderMenuItemParams.class);

    if (isNotValidOrder(request, omiList.getOrderId())) {
      return "";
    }

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<OrderMenuItem> orderMenuItems = entityManager
        .createQuery("from OrderMenuItem orderMenuItem where "
            + "orderMenuItem.foodOrder.id = :orderId", OrderMenuItem.class).setParameter("orderId",
            omiList.getOrderId()).getResultList();

    entityManager.close();

    OrderItemsData[] orderItemsData = new OrderItemsData[orderMenuItems.size()];

    for (int i = 0; i < orderItemsData.length; i++) {
      orderItemsData[i] = new OrderItemsData(orderMenuItems.get(i));
    }
    return JsonUtil.getInstance().toJson(orderItemsData);
  }

  /**
   * Returns a list of orders for a table in JSON. JSON input: tableNumber
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string containing the JSON for the orders on a table.
   */
  public static String getOrdersByTable(Request request, Response response) {
    TableOrderParams tableOrderParams = JsonUtil.getInstance().fromJson(request.body(),
        TableOrderParams.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<FoodOrder> foodOrders = entityManager.createQuery("from FoodOrder foodOrder "
            + "where foodOrder.transaction.restaurantTableStaff.restaurantTable.tableNumber = :tableNo",
        FoodOrder.class).setParameter("tableNo", tableOrderParams.getTableNumber())
        .getResultList();

    foodOrders.sort(Comparator.comparing(FoodOrder::getStatus));

    OrderData[] orderData = new OrderData[foodOrders.size()];
    for (int i = 0; i < orderData.length; i++) {
      orderData[i] = new OrderData(foodOrders.get(i));
    }

    entityManager.close();
    return JsonUtil.getInstance().toJson(orderData);
  }

  /**
   * Returns a list of orders for a particular status ie. Cooking in JSON. JSON input: orderStatus.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A String containing the JSON for the orders that have a status.
   */
  public static String getOrdersByStatus(Request request, Response response) {
    StatusOrderParams statusOrderParams = JsonUtil.getInstance().fromJson(request.body(),
        StatusOrderParams.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<FoodOrder> foodOrders = entityManager.createQuery("from FoodOrder foodOrder "
            + "where foodOrder.status = :orderStatus",
        FoodOrder.class).setParameter("orderStatus", statusOrderParams.getOrderStatus())
        .getResultList();

    foodOrders.sort(Comparator.comparing(FoodOrder::getTimeConfirmed));

    OrderData[] orderData = new OrderData[foodOrders.size()];
    for (int i = 0; i < orderData.length; i++) {
      orderData[i] = new OrderData(foodOrders.get(i));
    }

    entityManager.close();
    return JsonUtil.getInstance().toJson(orderData);
  }

  /**
   * Adds an orderMenuItem to an order. JSON input: foodOrderId, menuItemId requirements: A string
   * representing a description/extra details for the order.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A JSON string representing the MenuItem, or "failure"
   */
  public static String addOrderMenuItem(Request request, Response response) {
    OrderMenuItemParams omi = JsonUtil.getInstance()
        .fromJson(request.body(), OrderMenuItemParams.class);

    if (isNotValidOrder(request, omi.getorderId())) {
      return "";
    }

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    //Gets the food order that the item is going to be added to.
    FoodOrder foodOrder = entityManager.createQuery("from FoodOrder foodOrder where "
            + " foodOrder.id = :orderId",
        FoodOrder.class).setParameter("orderId", omi.getorderId()).getSingleResult();

    //Creates a new menu item and adds it to the order.
    entityManager.getTransaction().begin();
    OrderMenuItem orderMenuItem = new OrderMenuItem(entityManager.find(
        MenuItem.class, omi.getMenuItemId()), foodOrder, omi.getInstructions());

    entityManager.persist(orderMenuItem);

    // Updates the waiter sale if they added the menu item.
    if (request.session().attribute("StaffSessionKey") != null) {
      StaffSession staffSession = entityManager
          .find(StaffSession.class, request.session().attribute("StaffSessionKey"));
      WaiterSale waiterSale = new WaiterSale(staffSession.getStaff(),
          entityManager.find(MenuItem.class, omi.getMenuItemId()), foodOrder);
      entityManager.persist(waiterSale);
    }

    //Updates the transaction total
    foodOrder.getTransaction()
        .setTotal(foodOrder.getTransaction().getTotal() + orderMenuItem.getMenuItem().getPrice());

    //Updates the order total
    foodOrder.setTotal(foodOrder.getTotal() + orderMenuItem.getMenuItem().getPrice());
    entityManager.getTransaction().commit();

    entityManager.close();
    return JsonUtil.getInstance().toJson(new OrderItemsData(orderMenuItem));
  }

  /**
   * Changes the order status JSON input: foodOrderId, newOrderStatus: A string representing the new
   * order status. This can be CANCELLED, ORDERING, READY_TO_CONFIRM, COOKING, READY_TO_DELIVER or
   * DELIVERED.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String changeOrderStatus(Request request, Response response) {
    ChangeStatusParams cos = JsonUtil.getInstance()
        .fromJson(request.body(), ChangeStatusParams.class);

    if (isNotValidOrder(request, cos.getFoodOrderId())) {
      return "";
    }

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    entityManager.getTransaction().begin();

    FoodOrder foodOrder = entityManager.createQuery("from FoodOrder foodOrder where "
        + "foodOrder.id = :id", FoodOrder.class)
        .setParameter("id", cos.getFoodOrderId()).getSingleResult();

    foodOrder.setStatus(OrderStatus.valueOf(cos.getNewOrderStatus()));

    if (OrderStatus.valueOf(cos.getNewOrderStatus()) == OrderStatus.COOKING) {
      foodOrder.setTimeConfirmed(new Timestamp(System.currentTimeMillis()));
      List<StaffNotification> staffNotifications = entityManager
          .createQuery("from StaffNotification staffNotification "
              + "where staffNotification.staff.department = :department", StaffNotification.class)
          .setParameter("department", Department.KITCHEN).getResultList();

      List<FoodOrder> foodOrders = entityManager.createQuery("from FoodOrder foodOrder "
              + "where foodOrder.status = :orderStatus",
          FoodOrder.class).setParameter("orderStatus", OrderStatus.COOKING)
          .getResultList();

      foodOrders.sort(Comparator.comparing(FoodOrder::getTimeConfirmed));

      OrderData[] orderData = new OrderData[foodOrders.size()];
      for (int i = 0; i < orderData.length; i++) {
        orderData[i] = new OrderData(foodOrders.get(i));
      }
      String message = JsonUtil.getInstance().toJson(orderData);
      for (StaffNotification n : staffNotifications) {
        try {
          Notifications.sendPushMessage(n.getPushSubscription(), message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }

    }

    if (OrderStatus.valueOf(cos.getNewOrderStatus()) == OrderStatus.CANCELLED) {
      foodOrder.getTransaction()
          .setTotal(foodOrder.getTransaction().getTotal() - foodOrder.getTotal());
    }

    entityManager.getTransaction().commit();
    entityManager.close();

    return "success";
  }

  /**
   * Removes an item from an order JSON input: orderMenuItemId
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String removeOrderMenuItem(Request request, Response response) {
    RemoveOrderMenuItemParams omi = JsonUtil.getInstance()
        .fromJson(request.body(), RemoveOrderMenuItemParams.class);

    if (isNotValidOrder(request, omi.getOrderMenuItemId())) {
      return "";
    }

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    entityManager.getTransaction().begin();

    OrderMenuItem item = entityManager.find(OrderMenuItem.class, omi.getOrderMenuItemId());

    entityManager.remove(item);

    entityManager.getTransaction().commit();
    entityManager.close();

    return "success";
  }

  /**
   * This method gets the current foodOrderId for the table. If one doesn't exist it creates it.
   *
   * @param request A HTML request.
   * @param response A HTML response.
   * @return A foodOrderId in the form of a string.
   */
  public static String getOrderId(Request request, Response response) {
    TransactionIdParams transactionIdParams = JsonUtil.getInstance()
        .fromJson(request.body(), TransactionIdParams.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    List<FoodOrder> foodOrders = entityManager.createQuery("from FoodOrder foodOrder where "
            + "foodOrder.transaction.id = :transactionId and foodOrder.status = :ordering",
        FoodOrder.class).setParameter("transactionId", transactionIdParams.getTransactionId())
        .setParameter("ordering", OrderStatus.ORDERING).getResultList();

    FoodOrder foodOrder;
    if (foodOrders.size() == 0) {
      entityManager.getTransaction().begin();

      foodOrder = new FoodOrder(OrderStatus.ORDERING, null, entityManager.find(Transaction.class,
          transactionIdParams.getTransactionId()));

      entityManager.persist(foodOrder);

      entityManager.getTransaction().commit();
    } else {
      foodOrder = foodOrders.get(0);
    }

    entityManager.close();

    OrderIdData orderIdData = new OrderIdData(foodOrder.getOrderId());

    return JsonUtil.getInstance().toJson(orderIdData);
  }

  /**
   * Updates the instructions for a given OrderMenuItem. JSON input: orderMenuItemId, instructions
   *
   * @param request The HTTP request object.
   * @param response The HTTP response object.
   * @return A string saying 'success' or 'failure'
   */
  public static String changeOrderInstructions(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    ChangeInstructionsParams changeInstructionsParams = JsonUtil.getInstance().fromJson(
        request.body(), ChangeInstructionsParams.class);

    em.getTransaction().begin();
    OrderMenuItem orderMenuItem = em
        .find(OrderMenuItem.class, changeInstructionsParams.getOrderMenuItemId());
    orderMenuItem.setInstructions(changeInstructionsParams.getInstructions());
    em.getTransaction().commit();
    em.close();
    return "success";
  }

  /**
   * Checks if the order belongs to the table.
   *
   * @param request The HTML request.
   * @param orderId The orderId being checked.
   * @return True if the orderId is not valid.
   */
  private static boolean isNotValidOrder(Request request, Long orderId) {
    // Checks if the staff member is accessing the order.
    if (request.session().attribute("StaffSessionKey") != null) {
      return false;
    }
    if (request.session().attribute("TableSessionKey") != null) {
      // Gets the order.
      EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
      FoodOrder foodOrder = entityManager.find(FoodOrder.class, orderId);

      // Gets the table session.
      TableSession tableSession = entityManager.find(TableSession.class,
          request.session().attribute("TableSessionKey"));

      //Checks the transaction id.
      if (foodOrder.getTransaction().getRestaurantTableStaff().getRestaurantTable()
          == tableSession
          .getRestaurantTable()) {
        //Checks if the table is in the correct status.
        return foodOrder.getStatus() != OrderStatus.ORDERING
            && foodOrder.getStatus() != OrderStatus.READY_TO_CONFIRM;
      }
    }
    return true;
  }

  /**
   * Returns the total price of an order.
   * @return
   */
  public static int getOrderTotal(Request request){
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    return 10;
  }
}
