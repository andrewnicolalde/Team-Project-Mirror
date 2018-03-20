import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import database.DatabaseManager;
import endpoints.authentication.AuthenticationEmployee;
import endpoints.authentication.AuthenticationTable;
import endpoints.manager.Employee;
import endpoints.menu.Menu;
import endpoints.notification.NotificationEndpoint;
import endpoints.order.Orders;
import endpoints.tables.Tables;
import endpoints.transaction.Transactions;

public class Main {

  /**
   * Main method sets up the api end points.
   *
   * @param args Standard for a Java program, any launch arguments are passed onto the function.
   */
  public static void main(String[] args) {
    staticFileLocation("static"); // Lets spark know where the static files are

    if (System.getenv("PORT") != null) {
      int port = Integer.parseInt(System.getenv("PORT"));
      port(port);
    }

    // End points
    // Before is used to verify the user has access to the content they are requesting.
    before("/api/authStaff/*", AuthenticationEmployee::checkStaffSession);
    before("/api/authTable/*", AuthenticationTable::checkTableSession);

    // Endpoints which are meant to be connected to directly, not via AJAX requests.
    get("/logoutStaff", AuthenticationEmployee::logOutEmployee);
    get("/logoutTable", AuthenticationTable::logOutTable);
    post("/loginStaff", AuthenticationEmployee::logInEmployee);
    post("/loginTable", AuthenticationTable::logInTable);

    // These end points all return JSON and are meant to be requested via AJAX requests.
    get("/api/authStaff/getMenu", Menu::getMenu);
    get("/api/authStaff/getTables", Tables::getTables);
    get("/api/authStaff/getAllTables", Tables::getAllTables);
    get("/api/authStaff/getEmployees", Employee::getEmployees);
    get("/api/authStaff/getDepartments", Employee::getDepartments);
    post("/api/authStaff/getOrdersByTable", Orders::getOrdersByTable);
    post("/api/authStaff/getOrdersByStatus", Orders::getOrdersByStatus);
    post("/api/authStaff/getOrderItems", Orders::getOrderItems);
    post("/api/authStaff/addItemToOrder", Orders::addOrderMenuItem);
    post("/api/authStaff/removeItemFromOrder", Orders::removeOrderMenuItem);
    post("/api/authStaff/changeOrderStatus", Orders::changeOrderStatus);
    post("/api/authStaff/editStaff", Employee::editEmployee);
    post("/api/authStaff/addStaff", Employee::addEmployee);
    post("/api/authStaff/removeStaff", Employee::removeEmployee);
    post("/api/authStaff/changeTableStatus", Tables::changeTableStatus);
    post("/api/saveSubscription", NotificationEndpoint::saveSubscription);

    get("/api/authTable/getMenu", Menu::getMenu);
    get("/api/authTable/getCategories", (req, res) -> Menu.getCategories());
    get("/api/authTable/getTransactionId", Transactions::getTransactionId);
    get("/api/authTable/getAllOrdersForTable", Orders::getAllOrdersForTable);
    post("/api/authTable/getOrderId", Orders::getOrderId);
    post("/api/authTable/getOrderItems", Orders::getOrderItems);
    post("/api/authTable/addItemToOrder", Orders::addOrderMenuItem);
    post("/api/authTable/removeItemFromOrder", Orders::removeOrderMenuItem);
    post("/api/authTable/changeOrderStatus", Orders::changeOrderStatus);
    post("/api/authTable/changeOrderInstructions", Orders::changeOrderInstructions);
    post("/api/authTable/changeTableStatus", Tables::changeTableStatus);
    post("/api/authTable/getTransactionTotal", Transactions::getTransactionTotal);

    System.out.println("Visit: http://localhost:4567");

    DatabaseManager.getInstance();
  }
}
