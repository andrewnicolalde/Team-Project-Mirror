import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import database.Connector;
import database.Department;
import database.Franchise;
import database.Staff;
import endpoints.authentication.Authentication;
import endpoints.customer.Menu;
import endpoints.waiter.Tables;
import org.mindrot.jbcrypt.BCrypt;

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

    // Create dummy employee for testing
    Franchise f = new Franchise("Egham", "Egham High Street", "0123456789");
    connector.createItem(f);
    Staff staff = new Staff(BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()), Department.WAITER, f);
    connector.createItem(staff);
    System.out.println("Staff ID: " + staff.getEmployeeNumber());

    // End points
    get("/api/menu", (req, res) -> Menu.getMenu());
    get("/api/tables", Tables::getTables);
    post("/api/loginStaff", Authentication::logInEmployee);

    System.out.println("Visit: http://localhost:4567");
  }
}
