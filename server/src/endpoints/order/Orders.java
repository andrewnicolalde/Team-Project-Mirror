package endpoints.order;

import com.google.gson.Gson;
import database.DatabaseManager;
import database.tables.FoodOrder;
import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import database.tables.OrderStatus;
import database.tables.RestaurantTableStaff;
import database.tables.TableSession;
import database.tables.TableStatus;
import database.tables.Transaction;
import java.sql.Timestamp;
import java.util.Collections;
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

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<OrderMenuItem> orderMenuItems = entityManager
        .createQuery("from OrderMenuItem orderMenuItem where "
            + "orderMenuItem.foodOrder.id = :orderId", OrderMenuItem.class).setParameter("orderId",
            omiList.getOrderNumber()).getResultList();

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

    // Apdated from https://stackoverflow.com/questions/4018090/sorting-listclass-by-one-of-its-variable
    foodOrders.sort((t0, t1) -> {
      if (t0.getOrderId() < t1.getOrderId()) {
        return -1;
      }
      if (t0.getOrderId() > t1.getOrderId()) {
        return 1;
      }
      return 0;
    });

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

    OrderData[] orderData = new OrderData[foodOrders.size()];
    for (int i = 0; i < orderData.length; i++) {
      orderData[i] = new OrderData(foodOrders.get(i));
    }

    entityManager.close();
    return JsonUtil.getInstance().toJson(orderData);
  }

  /**
   * Adds an orderMenuItem to an order. JSON input: foodOrderId, menuItemId
   * requirements: A string representing a description/extra details for the order.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A JSON string representing the MenuItem, or "failure"
   */
  public static String addOrderMenuItem(Request request, Response response) {
    OrderMenuItemParams omi = JsonUtil.getInstance()
        .fromJson(request.body(), OrderMenuItemParams.class);

    //TODO check which franchise to add the order to.

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    FoodOrder foodOrder = entityManager.createQuery("from FoodOrder foodOrder where "
            + " foodOrder.id = :orderId",
        FoodOrder.class).setParameter("orderId", omi.getOrderNumber()).getSingleResult();

    entityManager.getTransaction().begin();
    OrderMenuItem orderMenuItem = new OrderMenuItem(entityManager.find(
        MenuItem.class, omi.getMenuItemId()), foodOrder, omi.getInstructions());

    entityManager.persist(orderMenuItem);
    entityManager.getTransaction().commit();
    entityManager.close();
    return JsonUtil.getInstance().toJson(new OrderItemsData(orderMenuItem));
  }

  /**
   * Changes the order status JSON input: foodOrderId,
   * newOrderStatus: A string representing the new order status. This can be CANCELLED, ORDERING,
   * READY_TO_CONFIRM, COOKING, READY_TO_DELIVER or DELIVERED.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String changeOrderStatus(Request request, Response response) {
    ChangeStatusParams cos = JsonUtil.getInstance()
        .fromJson(request.body(), ChangeStatusParams.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    entityManager.getTransaction().begin();

    FoodOrder foodOrder = entityManager.createQuery("from FoodOrder foodOrder where "
        + "foodOrder.id = :id", FoodOrder.class)
        .setParameter("id", cos.getFoodOrderId()).getSingleResult();

    foodOrder.setStatus(OrderStatus.valueOf(cos.getNewOrderStatus()));

    if (OrderStatus.valueOf(cos.getNewOrderStatus()) == OrderStatus.COOKING) {
      foodOrder.setTimeConfirmed(new Timestamp(System.currentTimeMillis()));
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

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    entityManager.getTransaction().begin();

    OrderMenuItem item = entityManager.find(OrderMenuItem.class, omi.getOrderMenuItemId());

    entityManager.remove(item);

    entityManager.getTransaction().commit();
    entityManager.close();

    return "success";
  }

  /**
   * This method gets the current transaction for a table. If one doesn't exist it creates a new
   * one.
   *
   * @param request A HTML request.
   * @param response A HTML response.
   * @return A transactionId in the form of a string.
   */
  public static String getTransactionId(Request request, Response response) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
  // TODO implement after table session has properly been implemented.
    TableSession tableSession = entityManager.find(TableSession.class,
        request.session().attribute("TableSessionKey"));

    Transaction transaction = entityManager.createQuery("from Transaction transaction where "
        + "transaction.restaurantTableStaff.restaurantTable.tableNumber = :tableNo AND "
        + "transaction.isPaid = false ", Transaction.class).setParameter("tableNo",
        tableSession.getRestaurantTable().getTableNumber()).getSingleResult();

    if (transaction == null) {
      entityManager.getTransaction().begin();
      RestaurantTableStaff temp = entityManager.createQuery("from RestaurantTableStaff tableStaff "
          + "where tableStaff.restaurantTable = :table", RestaurantTableStaff.class).setParameter(
          "table", 1).getSingleResult();
      transaction = new Transaction(false, null, null, false, temp);
      entityManager.persist(transaction);
      entityManager.getTransaction().commit();
    }

    entityManager.close();

    TransactionIdData transactionIdData = new TransactionIdData(transaction.getTransactionId());
    return JsonUtil.getInstance().toJson(transactionIdData);
  }

  /**
   * This method gets the current foodOrderId for the table. If one doesn't exist it creates it.
   * @param request A HTML request.
   * @param response A HTML response.
   * @return A foodOrderId in the form of a string.
   */
  public static String getOrderId(Request request, Response response) {
    OrderIdParams orderIdParams = JsonUtil.getInstance().fromJson(request.body(), OrderIdParams.class);

    System.out.println(orderIdParams.getTransactionId());

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    FoodOrder foodOrder;
    try {
      foodOrder = entityManager.createQuery("from FoodOrder foodOrder where "
              + "foodOrder.transaction.id = :transactionId and foodOrder.status = :ordering",
          FoodOrder.class).setParameter("transactionId", orderIdParams.getTransactionId())
          .setParameter("ordering", OrderStatus.ORDERING).getSingleResult();
    } catch (Exception e) {
      e.printStackTrace();
      foodOrder = null;
    }

    if (foodOrder == null) {
      entityManager.getTransaction().begin();

      foodOrder = new FoodOrder(OrderStatus.ORDERING, null, entityManager.find(Transaction.class,
          orderIdParams.getTransactionId()));

      entityManager.persist(foodOrder);

      entityManager.getTransaction().commit();
    }

    entityManager.close();

    OrderIdData orderIdData = new OrderIdData(foodOrder.getOrderId());

    return JsonUtil.getInstance().toJson(orderIdData);
  }

  /**
   * Updates the instructions for a given OrderMenuItem. JSON input: orderMenuItemId, instructions
   * @param request The HTTP request object.
   * @param response The HTTP response object.
   * @return A string saying 'success' or 'failure'
   */
  public static String changeOrderInstructions(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    ChangeInstructionsParams changeInstructionsParams = JsonUtil.getInstance().fromJson(
        request.body(), ChangeInstructionsParams.class);

    em.getTransaction().begin();
    OrderMenuItem orderMenuItem = em.find(OrderMenuItem.class, changeInstructionsParams.getOrderMenuItemId());
    orderMenuItem.setInstructions(changeInstructionsParams.getInstructions());
    em.getTransaction().commit();
    em.close();
    return "success";
  }
}
