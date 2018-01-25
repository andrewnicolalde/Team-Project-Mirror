package endpoints.order;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Or;
import database.Connector;
import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import javafx.scene.control.ListCell;
import spark.Request;
import spark.Response;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Orders {
  private static final Gson GSON = new Gson();

  /**
   * Returns an order as JSON.
   * JSON input:
   *     tableNumber: an integer representing the table number
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string containing JSON which holds the current order.
   */
  public static String getOrder(Request request, Response response) {
    OrderRequestParameters or = GSON.fromJson(request.body(), OrderRequestParameters.class);
    return getOrderMenuItems(or.getTableNumber(), request.attribute("StaffSessionKey"));
  }


  /**
   * Returns the order menu items from the database in JSON format.
   * @param tableNumber The number of the table.
   * @param staffId The employee number for the staff member.
   * @return The menu items for the table in a JSON format.
   *
   * @author Marcus Messer
   */
  public static String getOrderMenuItems(Long tableNumber, Long staffId) {

    Connector connector = Connector.getInstance();
    connector.createConnection();
    List<OrderMenuItem> orderMenuItems = connector.query("from OrderMenuItem "
        + "orderMenuItem where orderMenuItem.foodOrder.transaction.restaurantTableStaff."
        + "restaurantTable.tableNumber = " + tableNumber + " and "
        + "orderMenuItem.foodOrder.transaction.restaurantTableStaff.staff.employeeNumber = "
        + staffId, OrderMenuItem.class);


    connector.closeConnection();

    CustomerOrderData[] customerOrderData = new CustomerOrderData[orderMenuItems.size()];
    for (int i = 0; i < customerOrderData.length; i ++) {
      customerOrderData[i] = new CustomerOrderData(orderMenuItems.get(i));
    }
    return GSON.toJson(customerOrderData);
  }

  /**
   * Adds an orderMenuItem to an order.
   * JSON input:
   *     tableNumber: An integer representing the table number
   *     menuItemId: An integer representing the id of the MenuItem to add to the order.
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String addOrderMenuItem(Request request, Response response) {
    OrderMenuItemParameters omi = GSON.fromJson(request.body(), OrderMenuItemParameters.class);
    return "success";
  }

  /**
   * Changes the order status
   * JSON input:
   *     tableNumber: An integer representing the table number.
   *     newOrderStatus: A string representing the new order status. This can be CANCELLED,
   ORDERING, READY_TO_CONFIRM, COOKING, READY_TO_DELIVER or DELIVERED.
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String changeOrderStatus(Request request, Response response) {
    ChangeOrderStatusParameters cos = GSON.fromJson(request.body(), ChangeOrderStatusParameters.class);
    return "success";
  }

  /**
   * Removes an item from an order
   * JSON input:
   *     tableNumber: An integer representing the table number.
   *     menuItemId: An integer representing the id of the MenuItem to remove from the order.
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String removeOrderMenuItem(Request request, Response response) {
    OrderMenuItemParameters omi = GSON.fromJson(request.body(), OrderMenuItemParameters.class);
    return "success";
  }
}
