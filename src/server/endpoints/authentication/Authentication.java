package server.endpoints.authentication;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class Authentication {
  private static final Gson GSON = new Gson();

  static boolean isValidLoginCombination(String username, String password) {
    return true;
  }

  /**
   * Checks if the username and password combination is valid and returns a session key of they are.
   * @param username The username to authenticate.
   * @param password The password to authenticate.
   * @return A session key as a string, or null if the username/password combination is invalid.
   */
  static String authenticate(String username, String password) {
    return "498379438759384";
  }

  /**
   * Authenticates the log in request, and redirects them if successful.
   * @param request The HTTP request
   * @param response The response to give.
   * @return null.
   */
  public static Response logInUser(Request request, Response response) {

    System.out.println(request.body());
    response.redirect("/api/menu");
    return response;
  }
}
