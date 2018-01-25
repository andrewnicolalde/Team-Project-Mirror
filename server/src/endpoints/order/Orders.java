package endpoints.order;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Or;
import database.Connector;
import database.tables.FoodOrder;
import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import database.tables.Staff;
import database.tables.StaffSession;
import database.tables.TableSession;
import javafx.scene.control.ListCell;
import org.hibernate.criterion.Order;
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
    for (int i = 0; i < customerOrderData.length; i++) {
      customerOrderData[i] = new CustomerOrderData(orderMenuItems.get(i));
    }
    return GSON.toJson(customerOrderData);
  }

  /**
   * Adds an orderMenuItem to an order.
   * JSON input:
   *     tableNumber: An integer representing the table number
   *     menuItemId: An integer representing the id of the MenuItem to add to the order.
   *     description: A string representing a description/extra details for the order.
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String addOrderMenuItem(Request request, Response response) {
    OrderMenuItemParameters omi = GSON.fromJson(request.body(), OrderMenuItemParameters.class);
    Connector connector = Connector.getInstance();
    connector.createConnection();

    StaffSession tempStaff = (StaffSession) connector.getOne(request.attribute("StaffSessionKey"),
        StaffSession.class);

    //TODO reintroduce when table session key exists
//    TableSession tempTableSess = (TableSession) connector.getOne(request.attribute("TableSessionKey"),
//        TableSession.class);

    List<FoodOrder> foodOrders = connector.query("from FoodOrder foodOrder where " +
            "foodOrder.transaction.restaurantTableStaff.restaurantTable." +
            "tableNumber = " + omi.getTableNumber() + " and " +
            "foodOrder.transaction.restaurantTableStaff.restaurantTable.TableSession." +
            "tableSessionId = " + 1 + " and " +
            "foodOrder.transaction.restaurantTableStaff.restaurantTable.franchise.franchiseId = " +
            tempStaff.getStaff().getFranchise().getFranchiseId(),
        FoodOrder.class);

    OrderMenuItem orderMenuItem = new OrderMenuItem((MenuItem) connector.getOne(
        omi.getMenuItemId(), MenuItem.class), foodOrders.get(0), omi.getRequirements());

    connector.createItem(orderMenuItem);

    connector.closeConnection();
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
