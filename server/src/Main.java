import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import endpoints.authentication.AuthenticationEmployee;
import database.Connector;
import database.tables.Department;
import database.tables.Franchise;
import database.tables.RestaurantTable;
import database.tables.Staff;
import database.tables.StaffSession;
import database.tables.TableStatus;
import endpoints.customer.Menu;
import endpoints.kitchen.KitchenOrder;
import endpoints.order.Orders;
import endpoints.waiter.Tables;
import endpoints.kitchen.KitchenOrder;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class Main {

  private static Connector connector;

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

    // Setup the database connector
    connector = new Connector();

    // Check if there are any existing sessions, and end them.
    List<StaffSession> currentSessions = connector.query("from StaffSession", StaffSession.class);
    for (StaffSession session : currentSessions) {
      connector.remove(session);
    }

    // Create dummy employees for testing
//    Franchise f = new Franchise("Egham", "Egham High Street",
//        "0123456789", BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()));
//    connector.createItem(f);
//    Staff staff = new Staff(BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()), Department.WAITER, f);
//    connector.createItem(staff);
//    System.out.println("Staff ID: " + staff.getEmployeeNumber());
//    Staff staff2 = new Staff(BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()), Department.WAITER, f);
//    connector.createItem(staff2);
//    System.out.println("Staff ID: " + staff2.getEmployeeNumber());

    // End points
    // Before is used to verify the user has access to the content they are requesting.
    before("/api/authStaff/*", AuthenticationEmployee::checkStaffSession);

    // These end points all return JSON and are meant to be requested via AJAX requests.
    get("/api/authStaff/menu", (req, res) -> Menu.getMenu());
    get("/api/authStaff/tables", Tables::getTables);
    post("/api/loginStaff", AuthenticationEmployee::logInEmployee);
    post("/api/authStaff/getOrder", Orders::getOrder);
    post("/api/authStaff/addToOrder", Orders::addOrderMenuItem);
    post("/api/authStaff/removeFromOrder", Orders::removeOrderMenuItem);
    post("/api/authStaff/changeOrderStatus", Orders::changeOrderStatus);
    post("api/authStaff/kitchen", (req, res) -> KitchenOrder.getOrder());
    get("/api/authStaff/logout", AuthenticationEmployee::logOutEmployee);

    System.out.println("Visit: http://localhost:4567");
  }
}