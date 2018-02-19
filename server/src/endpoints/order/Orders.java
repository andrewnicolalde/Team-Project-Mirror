package endpoints.order;

import database.DatabaseManager;
import database.tables.FoodOrder;
import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import database.tables.OrderStatus;
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
            omiList.getOrderId()).getResultList();

    entityManager.close();

    OrderData[] orderData = new OrderData[orderMenuItems.size()];

    for (int i = 0; i < orderData.length; i++) {
      orderData[i] = new OrderData(orderMenuItems.get(i));
    }
    return JsonUtil.getInstance().toJson(orderData);
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

    ListOrderData[] listOrderData = new ListOrderData[foodOrders.size()];
    for (int i = 0; i < listOrderData.length; i++) {
      listOrderData[i] = new ListOrderData(foodOrders.get(i));
    }

    return JsonUtil.getInstance().toJson(listOrderData);
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

    ListOrderData[] listOrderData = new ListOrderData[foodOrders.size()];
    for (int i = 0; i < listOrderData.length; i++) {
      listOrderData[i] = new ListOrderData(foodOrders.get(i));
    }

    return JsonUtil.getInstance().toJson(listOrderData);
  }

  /**
   * Adds an orderMenuItem to an order. JSON input: foodOrderId, menuItemId
   * requirements: A string representing a description/extra details for the order.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
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
    return "success";
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

    entityManager.getTransaction().commit();
    entityManager.close();

    return "success";
  }

  /**
   * Removes an item from an order JSON input: foodOrderId
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String removeOrderMenuItem(Request request, Response response) {
    OrderMenuItemParams omi = JsonUtil.getInstance()
        .fromJson(request.body(), OrderMenuItemParams.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    entityManager.getTransaction().begin();

    OrderMenuItem orderMenuItem = entityManager
        .createQuery("from FoodOrder foodOrder where foodOrder.id = :orderId",
            OrderMenuItem.class).setParameter("orderId", omi.getOrderNumber()).getSingleResult();

    entityManager.remove(orderMenuItem);

    entityManager.getTransaction().commit();
    entityManager.close();

    return "success";
  }
}
