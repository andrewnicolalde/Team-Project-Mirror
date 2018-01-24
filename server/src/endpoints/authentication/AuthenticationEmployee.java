package endpoints.authentication;

import static spark.Spark.halt;

import com.google.gson.Gson;
import database.Connector;
import database.tables.Staff;
import database.tables.StaffSession;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

public class AuthenticationEmployee {

  private static final Gson GSON = new Gson();
  private static Connector connector;

  /**
   * Checks if the given details correctly match an employee stored in the database.
   * @param ap An EmployeeAuthenticationParameters object which holds the given login details.
   * @return A boolean value showing whether or not the details match an employee's.
   */
  private static boolean isValidLoginCombination(EmployeeAuthenticationParameters ap) {
    Staff employee = (Staff)connector.getOne(ap.getEmployeeNumber(), Staff.class);
    if (employee == null) { // If the employee does not exist, then fail
      return false;
    }

    // Check the password hashes match
    return BCrypt.checkpw(ap.getPassword(), employee.getPassword());
  }

  /**
   * Checks if the username and password combination is valid and returns a session key of they are.
   * @param eap An object holding the username and password combination
   * @return A session key as a string, or null if the username/password combination is invalid.
   */
  private static String authenticate(EmployeeAuthenticationParameters eap) {
    if (isValidLoginCombination(eap)) {
      return BCrypt.gensalt(); // Salt is a random string of characters, perfect for a session key.
    } else {
      return null;
    }
  }

  /**
   * Authenticates the log in request, and redirects them if successful.
   * @param request The HTTP request
   * @param response The response to give.
   * @return The a JSON response showing whether is was successful and if so, the session key.
   */
  public static String logInEmployee(Request request, Response response) {
    connector = Connector.getInstance();

    // Convert the data from the client into an object
    EmployeeAuthenticationParameters ap = GSON.fromJson(request.body(),
            EmployeeAuthenticationParameters.class);

    // Authenticate the given details
    String sessionKey = authenticate(ap);

    // sessionKey will be null if the details are invalid.
    if (sessionKey != null) {
      // Check if there are any existing sessions with the current user, and end them.
      List<StaffSession> currentSessions = (List<StaffSession>)(List<?>)connector.query(
              "from StaffSession where employeeNumber = " + ap.getEmployeeNumber(),
              StaffSession.class);
      for (StaffSession session : currentSessions) {
        connector.remove(session);
      }

      // Create a new session with the current user.
      StaffSession staffSession = new StaffSession(sessionKey,
              (Staff)connector.getOne(ap.getEmployeeNumber(), Staff.class));
      connector.createItem(staffSession);

      // Create the spark session and set the session key.
      request.session(true);
      request.session().attribute("StaffSessionKey", sessionKey);

      return "{\"validlogin\":true,\"redirection\":\"/\"}";
    } else {
      return "{\"validlogin\":false,\"redirection\":null}";
    }
  }

  /**
   * Checks if the request has a valid staff session key. Will halt if not.
   * @param request The HTTP request.
   * @param response The HTTP response.
   */
  public static void checkStaffSession(Request request, Response response) {
    // Check if session has a StaffSessionKey
    if (request.session().attribute("StaffSessionKey") == null) {
      // Not authenticated.
      // I'm sorry Dave.
      halt(401, "error_401");
    }

    // Attempt to get the session from the database.
    StaffSession session = (StaffSession)connector.getOne(request.session().attribute(
            "StaffSessionKey"), StaffSession.class);

    if (session == null) {
      // Has a session key but is not a valid one. Possible logged in on another device since.
      // I'm afraid I can't do that.
      halt(401, "error_401");
    }
  }

  /**
   * Logs out the employee.
   * @param request The HTTP request
   * @param response The HTTP response
   * @return A string representing the status
   */
  public static String logOutEmployee(Request request, Response response) {
    StaffSession session = (StaffSession)connector.get(request.session().attribute(
        "StaffSessionKey"), StaffSession.class);
    connector.remove(session);
    return "success";
  }
}
