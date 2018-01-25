package endpoints.waiter;

import spark.Request;
import spark.Response;

public class Tables {
  /**
   * Returns a string holding a list of tables.
   * No JSON as it is a get request.
   * @param request
   * @param response
   * @return
   */
  public static String getTables(Request request, Response response) {
    // TODO: Get tables from the database

    return "[{\"number\":1,\"status\":\"Ready to order\",\"franchise\":3},{\"number\":2,\"status\":\"Eating\",\"franchise\":3}]";
  }
}
