package endpoints.authentication;

import database.DatabaseManager;
import database.tables.Franchise;
import database.tables.RestaurantTable;
import java.util.List;
import javax.persistence.EntityManager;

import database.tables.TableSession;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

public class AuthenticationTable {

  /**
   * Checks if the given details correctly match a franchise stored in the database, and that the
   * table exists
   * @param ap A TableAuthenticationParameters object which holds the given login details.
   * @return The RestaurantTable entity, or null is the parameters are invalid.
   */
  private static RestaurantTable isValidLoginCombination(TableAuthenticationParameters ap) {
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
    TableAuthenticationParameters tap = new TableAuthenticationParameters(request.queryParams(
        "franchiseName"), request.queryParams("tablepwd"), Integer.parseInt(request.queryParams(
            "tableNumber")));

    RestaurantTable table = isValidLoginCombination(tap);

    if (table == null) {
      response.redirect("/");
      return response;
    }

    List<TableSession> sessions = em.createQuery("SELECT s FROM TableSession s WHERE "
        + "s.restaurantTable = :table", TableSession.class).setParameter("table", table)
        .getResultList();

    for (TableSession s : sessions) {
      em.getTransaction().begin();
      em.remove(s);
      em.getTransaction().commit();
    }

    String sessionKey = BCrypt.gensalt();
    TableSession session = new TableSession(sessionKey, table);

    response.redirect("customer-ui/customerdisplay.html");
    return response;
  }

  public static void checkTableSession(Request request, Response response) {
    return;
  }
}
