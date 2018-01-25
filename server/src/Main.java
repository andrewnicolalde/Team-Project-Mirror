import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import endpoints.authentication.Authentication;
import endpoints.customer.Menu;

public class Main {

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

    // End points
    get("/api/menu", (req, res) -> Menu.getMenu());
    post("/api/login", Authentication::logInUser);

  }
}
