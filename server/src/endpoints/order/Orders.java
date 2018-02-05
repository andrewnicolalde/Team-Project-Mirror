package endpoints.order;

import com.google.gson.Gson;
import database.DatabaseManager;
import database.tables.FoodOrder;
import database.tables.Franchise;
import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import database.tables.Staff;
import database.tables.StaffSession;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Order;
import javax.swing.text.html.parser.Entity;
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
    return getOrderMenuItems(or.getTableNumber(), request.session().attribute("StaffSessionKey"));
  }


  /**
   * Returns the order menu items from the database in JSON format.
   *
   * @param tableNumber The number of the table.
   * @param staffSessionKey The employee number for the staff member.
   * @return The menu items for the table in a JSON format.
   * @author Marcus Messer
   */
  public static String getOrderMenuItems(Long tableNumber, String staffSessionKey) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    entityManager.getTransaction().begin();

    StaffSession staffSession = entityManager.find(StaffSession.class, staffSessionKey);

    List<OrderMenuItem> orderMenuItems = entityManager.createQuery("from OrderMenuItem where "
        + "OrderMenuItem .foodOrder.transaction.restaurantTableStaff.restaurantTable.tableNumber = "
        + tableNumber + " and OrderMenuItem.foodOrder.transaction.restaurantTableStaff.restaurantTable."
        + "franchise = :franchise", OrderMenuItem.class).setParameter("franchise", staffSession.getStaff().
        getFranchise()).getResultList();

    entityManager.getTransaction().commit();

    CustomerOrderData[] customerOrderData = new CustomerOrderData[orderMenuItems.size()];

    for (int i = 0; i < customerOrderData.length; i++) {
      customerOrderData[i] = new CustomerOrderData(orderMenuItems.get(i));
    }
    return GSON.toJson(customerOrderData);
  }

  /**
   * Adds an orderMenuItem to an order. JSON input: tableNumber: An integer representing the table
   * number menuItemId: An integer representing the id of the MenuItem to add to the order.
   * description: A string representing a description/extra details for the order.
   *
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String addOrderMenuItem(Request request, Response response) {
    OrderMenuItemParameters omi = GSON.fromJson(request.body(), OrderMenuItemParameters.class);
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    entityManager.getTransaction().begin();

    StaffSession staffSession = entityManager.find(StaffSession.class,
        request.session().attribute("StaffSessionKey"));

    FoodOrder temp = entityManager.createQuery("from FoodOrder where "
        + "FoodOrder.transaction.restaurantTableStaff.restaurantTable.tableNumber = " +
        omi.getTableNumber() + " and FoodOrder .transaction.restaurantTableStaff."
        + "restaurantTable.franchise = "
        + ":franchise", FoodOrder.class).setParameter("franchise",
        staffSession.getStaff().getFranchise()).getSingleResult();

    OrderMenuItem orderMenuItem = new OrderMenuItem(entityManager.find(
        MenuItem.class, omi.getMenuItemId()), temp, omi.getRequirements());

    entityManager.persist(orderMenuItem);
    entityManager.getTransaction().commit();
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
    return "success";
  }
}
