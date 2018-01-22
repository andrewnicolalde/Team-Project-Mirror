import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import database.*;
import endpoints.authentication.Authentication;
import endpoints.customer.Menu;
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

    // Setup the database connector
    connector = Connector.getInstance();
    connector.createConnection();

    // Check if there are any existing sessions, and end them.
    List<StaffSession> currentSessions = (List<StaffSession>)(List<?>)connector.query(
            "from StaffSession", StaffSession.class);
    for (StaffSession session : currentSessions) {
      connector.remove(session);
    }

    // Create dummy employees for testing
    Franchise f = new Franchise("Egham", "Egham High Street", "0123456789");
    connector.createItem(f);
    Staff staff = new Staff(BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()), Department.WAITER, f);
    connector.createItem(staff);
    System.out.println("Staff ID: " + staff.getEmployeeNumber());
    Staff staff2 = new Staff(BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()), Department.WAITER, f);
    connector.createItem(staff2);
    System.out.println("Staff ID: " + staff2.getEmployeeNumber());

    // End points
    // Before is used to verify the user has access to the content they are requesting.
    before("/api/auth/*", Authentication::checkStaffSession);

    // These end points all return JSON and are meant to be requested via AJAX requests.
    get("/api/auth/menu", (req, res) -> Menu.getMenu());
    get("/api/auth/tables", Tables::getTables);
    post("/api/loginStaff", Authentication::logInEmployee);

    System.out.println("Visit: http://localhost:4567");
  }
}
