package endpoints.waiter;

import spark.Request;
import spark.Response;

public class Tables {
  public static String getTables(Request request, Response response) {
    // TODO: Get tables from the database

    return "[{\"number\":1,\"status\":\"Ready to order\",\"franchise\":3},{\"number\":2,\"status\":\"Eating\",\"franchise\":3}]";
  }
}
