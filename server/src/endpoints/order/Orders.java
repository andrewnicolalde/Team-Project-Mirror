package endpoints.order;

import database.DatabaseManager;
import database.tables.Department;
import database.tables.FoodOrder;
import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import database.tables.OrderStatus;
import database.tables.StaffNotification;
import database.tables.StaffSession;
import database.tables.TableSession;
import database.tables.Transaction;
import database.tables.WaiterSale;
import endpoints.notification.NotificationEndpoint;
import endpoints.transaction.TransactionIdParams;
import endpoints.transaction.Transactions;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    sendNotifications(foodOrder);

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
   * Returns a string representing a JSON array of all the orders, their status and their contents
   *
   * @param request The HTTP request
   * @param response The HTTP response
   * @return A string formatted to represent JSON.
   */
  public static String getAllOrdersForTable(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    TableSession tableSession = em.find(TableSession.class,
        request.session().attribute("TableSessionKey"));
    Transaction transaction = Transactions.getCurrentTransaction(tableSession.getRestaurantTable());

    List<FoodOrder> orders = em
        .createQuery("FROM FoodOrder foodorder WHERE foodorder.transaction = :transaction",
            FoodOrder.class).setParameter("transaction", transaction).getResultList();
    List<OrderWithContents> orderDetailsToSend = new ArrayList<>();

    for (FoodOrder order : orders) {
      List<OrderMenuItem> orderContents = em
          .createQuery("FROM OrderMenuItem ordermenuitem WHERE ordermenuitem.foodOrder = :order",
              OrderMenuItem.class).setParameter("order", order).getResultList();
      List<OrderItemsData> orderItemDetails = new ArrayList<>();
      for (OrderMenuItem item : orderContents) {
        orderItemDetails.add(new OrderItemsData(item));
      }
      orderDetailsToSend.add(new OrderWithContents(order.getOrderId(), order.getStatus().toString(),
          orderItemDetails));
    }

    em.close();

    orderDetailsToSend.sort(Comparator.comparing(OrderWithContents::getOrderId));
    return JsonUtil.getInstance().toJson(orderDetailsToSend);
  }

  /**
   * Helper method that finds the staff that need to be notified about a change of order status.
   *
   * @param foodOrder The FoodOrder that has changed status.
   * @return A List of StaffNotification objects.
   */
  private static List<StaffNotification> getStaffToNotify(FoodOrder foodOrder) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<StaffNotification> staffNotifications = null;
    // If the order has just been cooked we want the relevant waiter.
    if (foodOrder.getStatus() == OrderStatus.READY_TO_DELIVER) {
      // get a list of waiters on a table.
      staffNotifications = entityManager
          .createQuery("from StaffNotification staffNotification"
                  + " where staffNotification.staff.department = :department and staffNotification.staff.employeeNumber = :serverNumber",
              StaffNotification.class).setParameter("department", Department.WAITER)
          .setParameter("serverNumber",
              foodOrder.getTransaction().getRestaurantTableStaff().getStaff().getEmployeeNumber())
          .getResultList();
      // If the order has just been confirmed we want to tell the kitchen.
    } else if (foodOrder.getStatus() == OrderStatus.COOKING) {
      foodOrder.setTimeConfirmed(new Timestamp(System.currentTimeMillis()));
      staffNotifications = entityManager
          .createQuery("from StaffNotification staffNotification "
              + "where staffNotification.staff.department = :department", StaffNotification.class)
          .setParameter("department", Department.KITCHEN).getResultList();
    }
    return staffNotifications;
  }

  /**
   * Helper method that constructs an appropriate data packet to send in the notification.
   *
   * @param foodOrder The FoodOrder that's change of status triggered the notification.
   * @return A String normally JSON to be sent in the notification.
   */
  private static String getDataToNotify(FoodOrder foodOrder) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    // some default message.
    String message = "{\"hello\":\"world\"}";
    // The order has just been confirmed we send all the orders to update the page.
    if (foodOrder.getStatus() == OrderStatus.COOKING) {
      List<FoodOrder> foodOrders = entityManager.createQuery("from FoodOrder foodOrder "
              + "where foodOrder.status = :orderStatus",
          FoodOrder.class).setParameter("orderStatus", OrderStatus.COOKING)
          .getResultList();
      // We have to add the order in question because the change hasn't been committed yet.
      foodOrders.add(foodOrder);

      foodOrders.sort(Comparator.comparing(FoodOrder::getTimeConfirmed));

      OrderData[] orderData = new OrderData[foodOrders.size()];
      for (int i = 0; i < orderData.length; i++) {
        orderData[i] = new OrderData(foodOrders.get(i));
      }
      message = JsonUtil.getInstance().toJson(orderData);
    } else if (foodOrder.getStatus() == OrderStatus.READY_TO_DELIVER) {
      message = "An Order is Ready to Deliver!";
    }
    return message;
  }

  /**
   * Wrapper method that handles all of the notification handling at a high level.
   *
   * @param foodOrder The FoodOrder that status changed.
   */
  private static void sendNotifications(FoodOrder foodOrder) {
    List<StaffNotification> staffNotifications = getStaffToNotify(foodOrder);
    // test message to verify it works.
    String message = getDataToNotify(foodOrder);
    // send the notifications.
    for (StaffNotification n : staffNotifications) {
      try {
        NotificationEndpoint.sendPushMessage(n.getPushSubscription(), message.getBytes("UTF-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
  }
}
