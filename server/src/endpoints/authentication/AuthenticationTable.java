package endpoints.authentication;

import static spark.Spark.halt;

import com.google.gson.Gson;
import database.Connector;
import database.tables.Franchise;
import database.tables.RestaurantTable;
import database.tables.TableSession;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AuthenticationTable {

  private static final Gson GSON = new Gson();
  private static Connector connector;

  /**
   * Checks if the given details correctly match a store franchise/table in the database.
   * @param ap A TableAuthenticationParameters object which holds the given login details.
   * @return A boolean value showing whether or not the details match an franchises.
   */
  private static boolean isValidLoginCombination(TableAuthenticationParameters ap) {
    Franchise franchise = (Franchise) connector.getOne(ap.getName(), Franchise.class);
    if (franchise == null) { // If the franchise does not exist, then fail
      return false;
    }

    // Check the password hashes match
    if (!BCrypt.checkpw(ap.getPassword(), franchise.getPassword())) {
      return false;
    }

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("server.database");
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    List<RestaurantTable> tables = em.createQuery("from RestaurantTable where franchise_name like :fName and " +
            "tableNumber = :tNumber", RestaurantTable.class).setParameter("fName", ap.getName()).setParameter(
                "tNumber", ap.getTableNumber()).getResultList();
    em.getTransaction().commit();
    em.close();
    return tables.size() == 0;
  }

  /**
   * Checks if the username and password combination is valid and returns a session key of they are.
   * @param ap An object holding the username and password combination
   * @return A session key as a string, or null if the username/password combination is invalid.
   */
  private static String authenticate(TableAuthenticationParameters ap) {
    if (isValidLoginCombination(ap)) {
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
  public static String logInTable(Request request, Response response) {
    connector = Connector.getInstance();

    System.out.println(request.body());

    // Convert the data from the client into an object
    TableAuthenticationParameters ap = GSON.fromJson(request.body(),
            TableAuthenticationParameters.class);

    // Authenticate the given details
    String sessionKey = authenticate(ap);

    // Get the table
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("server.database");
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    List<RestaurantTable> tables = em.createQuery("from RestaurantTable where franchise_name like :fName and " +
        "tableNumber = :tNumber", RestaurantTable.class).setParameter("fName", ap.getName()).setParameter(
        "tNumber", ap.getTableNumber()).getResultList();
    em.getTransaction().commit();
    em.close();
    RestaurantTable table = tables.get(0);

    // sessionKey will be null if the details are invalid.
    if (sessionKey != null) {
      // Check if there are any existing sessions with the current user, and end them.
      List<TableSession> currentSessions = (List<TableSession>)(List<?>)connector.query(
              "from TableSession where tableId = " + table.getTableId(), TableSession.class);

      for (TableSession session : currentSessions) {
        connector.remove(session);
      }

      // Create a new session with the current user.
      TableSession tableSession = new TableSession(sessionKey, table);
      connector.createItem(tableSession);

      // Create the spark session and set the session key.
      request.session(true);
      request.session().attribute("TableSessionKey", sessionKey);

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
  public static void checkTableSession(Request request, Response response) {
    // Check if session has a TableSessionKey
    if (request.session().attribute("TableSessionKey") == null) {
      // Not authenticated.
      // I'm sorry Dave.
      halt(401, "error_401");
    }

    // Attempt to get the session from the database.
    TableSession session = (TableSession)connector.getOne(request.session().attribute(
            "TableSessionKey"), TableSession.class);

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
  public static String logOutTable(Request request, Response response) {
    TableSession session = (TableSession)connector.getOne(request.session().attribute(
        "TableSessionKey"), TableSession.class);
    connector.remove(session);
    return "success";
  }
}
