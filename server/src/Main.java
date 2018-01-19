import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import authentication.EmployeeAuthenticator;
import endpoints.authentication.Authentication;
import endpoints.customer.Menu;
import endpoints.waiter.Tables;

public class Main {

  private static EmployeeAuthenticator ea;

  /**
   * Main method sets up the api end points.
   * @param args Standard for a Java program, any launch arguments are passed onto the function.
   */
  public static void main(String[] args) {
    staticFileLocation("static"); // Lets spark know where the static files are
    ea = new EmployeeAuthenticator();

    // End points
    get("/api/menu", (req, res) -> Menu.getMenu());
    get("/api/tables", Tables::getTables);
    post("/api/login", (req, res) -> Authentication.logInEmployee(req, res, ea));

    System.out.println("Visit: http://localhost:4567");
  }
}
