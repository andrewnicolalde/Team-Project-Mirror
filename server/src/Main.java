import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import database.DatabaseManager;
import endpoints.authentication.AuthenticationEmployee;
import endpoints.authentication.AuthenticationTable;
import endpoints.menu.Menu;
import endpoints.order.KitchenOrder;
import endpoints.order.Orders;
import endpoints.tables.Tables;

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
    get("/logout", AuthenticationEmployee::logOutEmployee);
    post("/loginStaff", AuthenticationEmployee::logInEmployee);
    post("/loginTable", AuthenticationTable::logInTable);

    // These end points all return JSON and are meant to be requested via AJAX requests.
    get("/api/authStaff/getMenu", (req, res) -> Menu.getMenu());
    get("/api/authStaff/getTables", Tables::getTables);
    get("api/authStaff/kitchen", KitchenOrder::getCookingOrders);
    post("api/authStaff/getOrderList", Orders::getOrderList);
    post("/api/authStaff/getOrderItems", Orders::getOrderItems);
    post("/api/authStaff/addItemToOrder", Orders::addOrderMenuItem);
    post("/api/authStaff/removeItemFromOrder", Orders::removeOrderMenuItem);
    post("/api/authStaff/changeOrderStatus", Orders::changeOrderStatus);

    System.out.println("Visit: http://localhost:4567");

    DatabaseManager.getInstance();
  }
}
