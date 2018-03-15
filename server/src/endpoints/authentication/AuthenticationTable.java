package endpoints.authentication;

import static spark.Spark.halt;

import database.DatabaseManager;
import database.tables.Franchise;
import database.tables.RestaurantTable;
import database.tables.TableSession;
import java.util.List;
import javax.persistence.EntityManager;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

@SuppressWarnings("SpellCheckingInspection")
public class AuthenticationTable {

  /**
   * Checks if the given details correctly match a franchise stored in the database, and that the
   * table exists
   * @param ap A TableAuthenticationParams object which holds the given login details.
   * @return The RestaurantTable entity, or null is the parameters are invalid.
   */
  private static RestaurantTable isValidLoginCombination(TableAuthenticationParams ap) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();

    // Check the franchise with the given username exists.
    Franchise franchise = em.find(Franchise.class, ap.getName());
    if (franchise == null) {
      return null;
    }

    // Check the password given matches the franchise.
    if (!BCrypt.checkpw(ap.getPassword(), franchise.getPassword())) {
      return null;
    }

    // Check the table with the given number exists for the given franchise.
    List<RestaurantTable> table = em.createQuery("SELECT t "
            + "FROM RestaurantTable t "
            + "WHERE t.franchise = :franchise "
            + "AND t.tableNumber = :tablenumber", RestaurantTable.class).setParameter(
                    "franchise", franchise).setParameter(
                            "tablenumber", ap.getTableNumber()).getResultList();
    if (table.size() != 1) {
      // Either the table does not exist, or there are too many tables?
      // Either way, bail out.
      return null;
    }

    return table.get(0);
  }

  /**
   * Authenticates the log in request, and redirects them if successful.
   * @param request The HTTP request
   * @param response The response to give.
   * @return The response object passed in.
   */
  public static Response logInTable(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    TableAuthenticationParams tap = new TableAuthenticationParams(request.queryParams(
        "franchiseName"), request.queryParams("tablepwd"), Integer.parseInt(request.queryParams(
            "tableNumber")));

    RestaurantTable table = isValidLoginCombination(tap);

    if (table == null) {
      response.redirect("/");
      return response;
    }

    // Delete any old sessions
    List<TableSession> sessions = em.createQuery("SELECT s FROM TableSession s WHERE "
        + "s.restaurantTable = :table", TableSession.class).setParameter("table", table)
        .getResultList();

    for (TableSession s : sessions) {
      em.getTransaction().begin();
      em.remove(s);
      em.getTransaction().commit();
    }

    // Create a new session
    String sessionKey = BCrypt.gensalt();
    TableSession session = new TableSession(sessionKey, table);
    em.getTransaction().begin();
    em.persist(session);
    em.getTransaction().commit();

    // Assign the session key to the session.
    request.session().attribute("TableSessionKey", sessionKey);

    response.redirect("customer/home.html");
    return response;
  }

  public static void checkTableSession(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    // Check if the session has a TableSessionKey
    if (request.session().attribute("TableSessionKey") == null) {
      if (request.session().attribute("StaffSessionKey") == null) {
        halt(401, "error_401");
      }
    }

    try {
      TableSession session = em.find(TableSession.class, request.session()
          .attribute("TableSessionKey"));

      if (session == null) {
        halt(401, "error_401");
      }
    } catch (Exception e) {
      if (request.session().attribute("StaffSessionKey") == null) {
        halt(401, "error_401");
      }
    }
  }

  public static Response logOutTable(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    TableSession session = em.find(TableSession.class,
        request.session().attribute("TableSessionKey"));
    em.getTransaction().begin();
    em.remove(session);
    em.getTransaction().commit();
    request.session().removeAttribute("TableSessionKey");
    response.redirect("/");
    em.close();
    return response;
  }
}
