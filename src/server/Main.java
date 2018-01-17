package server;

import static spark.Spark.get;
import static spark.Spark.post;

import server.endpoints.authentication.Authentication;
import server.endpoints.customer.Menu;

public class Main {
  /**
   * Main method sets up the api end points.
   * @param args Standard for a Java program, any launch arguments are passed onto the function.
   */
  public static void main(String[] args) {
    get("/api/menu", (req, res) -> Menu.getMenu());
    post("/api/login", (req, res) -> Authentication.authenticate("admin",
            "pa55w0rd"));
  }
}
