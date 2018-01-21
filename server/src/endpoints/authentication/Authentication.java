package endpoints.authentication;

import com.google.gson.Gson;
import database.Connector;
import database.Staff;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

import java.util.List;

public class Authentication {

  private static final Gson GSON = new Gson();
  private static Connector connector;

  /**
   * Checks if the given details correctly match an employee stored in the database
   * @param ap An EmployeeAuthenticationParameters object which holds the given login details.
   * @return A boolean value showing whether or not the details match an employee's.
   */
  private static boolean isValidLoginCombination(EmployeeAuthenticationParameters ap) {
    Staff employee = (Staff)connector.get(ap.getEmployeeNumber(), Staff.class);
    if (employee == null) { // If the employee does not exist, then fail
      return false;
    }

    // Check the password hashes match
    return BCrypt.checkpw(ap.getPassword(), employee.getPassword());
  }

  /**
   * Checks if the username and password combination is valid and returns a session key of they are.
   * @param employeeAuthenticationParameters An object holding the username and password combination
   * @return A session key as a string, or null if the username/password combination is invalid.
   */
  private static String authenticate(EmployeeAuthenticationParameters employeeAuthenticationParameters) {
    if (isValidLoginCombination(employeeAuthenticationParameters)) {
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
      // TODO: Save the session key in the current session
      return "{\"validlogin\":true,\"redirection\":\"/\"}";
    } else {
      return "{\"validlogin\":false,\"redirection\":null}";
    }
  }
}
