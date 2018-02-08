package endpoints.order;

import com.google.gson.Gson;
import database.DatabaseManager;
import database.tables.FoodOrder;
import database.tables.Franchise;
import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import database.tables.OrderStatus;
import database.tables.StaffSession;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;

public class Orders {

  private static final Gson GSON = new Gson();

  /**
   * Returns an order as JSON. JSON input: tableNumber: an integer representing the table number
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string containing JSON which holds the current order.
   */
  public static String getOrder(Request request, Response response) {
    OrderRequestParameters or = GSON.fromJson(request.body(), OrderRequestParameters.class);
    return getOrderMenuItems(or.getTableNumber(), request.session()
        .attribute("StaffSessionKey"));
  }

  public static String getOrderList(Request request, Response response) {
    OrderRequestParameters orderRequestParameters = GSON.fromJson(request.body(),
        OrderRequestParameters.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<FoodOrder> foodOrders = entityManager.createQuery("from FoodOrder foodOrder "
        + "where foodOrder.transaction.restaurantTableStaff.restaurantTable.tableNumber = :tableNo",
        FoodOrder.class).setParameter("tableNo", orderRequestParameters.getTableNumber())
        .getResultList();

    OrderListData[] orderListData = new OrderListData[foodOrders.size()];
    for (int i = 0; i < orderListData.length; i++) {
      orderListData[i] = new OrderListData(foodOrders.get(i));
    }

    return GSON.toJson(orderListData);
  }

  /**
   * Returns the order menu items from the database in JSON format.
   *
   * @param tableNumber The number of the table.
   * @param staffSessionKey The session key for the staff member.
   * @return The menu items for the table in a JSON format.
   * @author Marcus Messer
   */
  public static String getOrderMenuItems(Long tableNumber, String staffSessionKey) {
    //TODO check which franchise the order is part of.

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<OrderMenuItem> orderMenuItems = entityManager
        .createQuery("from OrderMenuItem orderMenuItem where "
            + "orderMenuItem.foodOrder.transaction.restaurantTableStaff.restaurantTable.tableNumber = "
            + tableNumber, OrderMenuItem.class).getResultList();

    entityManager.close();

    CustomerOrderData[] customerOrderData = new CustomerOrderData[orderMenuItems.size()];

    for (int i = 0; i < customerOrderData.length; i++) {
      customerOrderData[i] = new CustomerOrderData(orderMenuItems.get(i));
    }
    return GSON.toJson(customerOrderData);
  }

  /**
   * Adds an orderMenuItem to an order. JSON input: tableNumber: An integer representing the table
   * number menuItemId: An integer representing the id of the MenuItem to add to the order.
   * requirements: A string representing a description/extra details for the order.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String addOrderMenuItem(Request request, Response response) {
    OrderMenuItemParameters omi = GSON.fromJson(request.body(), OrderMenuItemParameters.class);

    //TODO check which franchise to add the order to.

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    List<FoodOrder> temp = entityManager.createQuery("from FoodOrder foodOrder where "
            + "foodOrder.transaction.restaurantTableStaff.restaurantTable.tableNumber = " +
            omi.getTableNumber() + " and foodOrder.status = " + OrderStatus.ORDERING.ordinal() +
            " or foodOrder.status = " + OrderStatus.READY_TO_CONFIRM.ordinal(),
        FoodOrder.class).getResultList();

    if (temp.size() == 0) {
      return "failed";
    }

    entityManager.getTransaction().begin();
    OrderMenuItem orderMenuItem = new OrderMenuItem(entityManager.find(
        MenuItem.class, omi.getMenuItemId()), temp.get(0), omi.getInstructions());

    entityManager.persist(orderMenuItem);
    entityManager.getTransaction().commit();
    entityManager.close();
    return "success";
  }

  /**
   * Changes the order status JSON input: tableNumber: An integer representing the table number.
   * newOrderStatus: A string representing the new order status. This can be CANCELLED, ORDERING,
   * READY_TO_CONFIRM, COOKING, READY_TO_DELIVER or DELIVERED.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String changeOrderStatus(Request request, Response response) {
    ChangeOrderStatusParameters cos = GSON
        .fromJson(request.body(), ChangeOrderStatusParameters.class);

    //TODO check which franchise the order is part of.

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    entityManager.getTransaction().begin();

    List<FoodOrder> foodOrders = entityManager.createQuery("SELECT f FROM FoodOrder f WHERE f" +
        ".transaction.restaurantTableStaff.restaurantTable.tableNumber = :tableNumber AND f" +
        ".status = :status", FoodOrder.class).setParameter("tableNumber", cos.getTableNumber())
        .setParameter
        ("status", OrderStatus.READY_TO_CONFIRM.ordinal()).getResultList();

    entityManager.getTransaction().commit();
    entityManager.close();

    return "success";
  }

  /**
   * Removes an item from an order JSON input: tableNumber: An integer representing the table
   * number. menuItemId: An integer representing the id of the MenuItem to remove from the order.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String removeOrderMenuItem(Request request, Response response) {
    OrderMenuItemParameters omi = GSON.fromJson(request.body(), OrderMenuItemParameters.class);

    //TODO Check franchise.
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    entityManager.getTransaction().begin();

    OrderMenuItem orderMenuItem = entityManager
        .createQuery("from FoodOrder foodOrder where foodOrder.transaction.restaurantTableStaff"
                + ".restaurantTable.tableNumber = " + omi.getTableNumber(),
            OrderMenuItem.class).getSingleResult();

    entityManager.remove(orderMenuItem);

    entityManager.getTransaction().commit();
    entityManager.close();

    return "success";
  }
}
