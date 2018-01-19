package endpoints.authentication;

import authentication.EmployeeAuthenticator;
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
   * @param employeeAuthenticationParameters An object holding the username and password combination
   * @return A session key as a string, or null if the username/password combination is invalid.
   */
  static String authenticate(EmployeeAuthenticationParameters employeeAuthenticationParameters) {
    return "498379438759384";
  }

  /**
   * Authenticates the log in request, and redirects them if successful.
   * @param request The HTTP request
   * @param response The response to give.
   * @return The a JSON response showing whether is was successful and if so, the session key.
   */
  public static String logInEmployee(Request request, Response response, EmployeeAuthenticator em) {
    // Convert the data from the client into an object
    EmployeeAuthenticationParameters ap = GSON.fromJson(request.body(),
            EmployeeAuthenticationParameters.class);

    //em.authenticateEmployee(ap.getEmployeeNumber(), ap.getPassword());

    // TODO: Save the session key in the current session

    // Generate response to send to the user.
    if (true) { //(sessionkey == null) {
      return "{\"validlogin\":false,\"redirection\":null}";
    } else {
      return "{\"validlogin\":true,\"redirection\":\"/\"}";
    }
  }
}
