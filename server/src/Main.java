import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import endpoints.authentication.AuthenticationEmployee;
import database.Connector;
import database.tables.Department;
import database.tables.Franchise;
import database.tables.Staff;
import database.tables.StaffSession;
import endpoints.customer.Menu;
import endpoints.order.Orders;
import endpoints.waiter.Tables;
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

    /*
    // Uncomment this if you are pushing to Heroku
    int port = Integer.parseInt(System.getenv("PORT"));
    port(port);
    */

    // Setup the database connector
    connector = Connector.getInstance();
    connector.createConnection();

    // Check if there are any existing sessions, and end them.
    List<StaffSession> currentSessions = connector.query("from StaffSession", StaffSession.class);
    for (StaffSession session : currentSessions) {
      connector.remove(session);
    }

    // Create dummy employees for testing
    Franchise f = new Franchise("Egham", "Egham High Street",
        "0123456789", BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()));
    connector.createItem(f);
    Staff staff = new Staff(BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()), Department.WAITER, f);
    connector.createItem(staff);
    System.out.println("Staff ID: " + staff.getEmployeeNumber());
    Staff staff2 = new Staff(BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()), Department.WAITER, f);
    connector.createItem(staff2);
    System.out.println("Staff ID: " + staff2.getEmployeeNumber());
    RestaurantTable table = new RestaurantTable(TableStatus.FREE, 1, f);
    connector.createItem(table);

    // End points
    // Before is used to verify the user has access to the content they are requesting.
    before("/api/authStaff/*", AuthenticationEmployee::checkStaffSession);
    before("/api/authTable/*", AuthenticationTable::checkTableSession);

    // These end points all return JSON and are meant to be requested via AJAX requests.
    get("/api/authStaff/menu", (req, res) -> Menu.getMenu());
    get("/api/authStaff/tables", Tables::getTables);
    post("/api/loginStaff", AuthenticationEmployee::logInEmployee);
    post("/api/loginTable", AuthenticationTable::logInTable);
    get("/api/authTable/logout", AuthenticationTable::logOutTable);
    get("/api/authStaff/logout", AuthenticationEmployee::logOutEmployee);

    System.out.println("Visit: http://localhost:4567");
  }
}
