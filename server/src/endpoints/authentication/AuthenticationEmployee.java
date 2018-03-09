package endpoints.authentication;

import static database.tables.Department.*;
import static spark.Spark.halt;

import database.DatabaseManager;
import database.tables.Department;
import database.tables.Staff;
import database.tables.StaffSession;
import java.util.List;
import javax.persistence.EntityManager;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

/**
 * This class authenticates a staff member to use the system.
 *
 * @author Toby Such
 */
public class AuthenticationEmployee {

  /**
   * Checks if the given details correctly match an employee stored in the database.
   *
   * @param ap An EmployeeAuthenticationParameters object which holds the given login details.
   * @return The staff entity, or null is the parameters are invalid.
   */
  private static Staff isValidLoginCombination(EmployeeAuthenticationParameters ap) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    Staff employee = em.find(Staff.class, ap.getEmployeeNumber());
    em.close();
    if (employee == null) { // If the employee does not exist, then fail
      return null;
    }

    // Check the password hashes match
    if (BCrypt.checkpw(ap.getPassword(), employee.getPassword())) {
      return employee;
    } else {
      return null;
    }
  }

  /**
   * Authenticates the log in request, and redirects them if successful.
   * @param request The HTTP request
   * @param response The response to give.
   * @return A JSON response showing whether is was successful and if so, the session key.
   */
  public static Response logInEmployee(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();

    // Convert the data from the client into an object
    EmployeeAuthenticationParameters ap = new EmployeeAuthenticationParameters(
        new Long(request.queryParams("employeeNumber")), request.queryParams("password"));

    // Authenticate the given details
    Staff employee = isValidLoginCombination(ap);

    // employee will be null if the details are invalid.
    if (employee != null) {
      // Check if there are any existing sessions with the current user, and end them.
      List<StaffSession> currentSessions = em.createQuery("SELECT s FROM StaffSession s "
          + "WHERE s.staff = :staff", StaffSession.class).setParameter(
          "staff", employee).getResultList();
      for (StaffSession session : currentSessions) {
        em.getTransaction().begin();
        em.remove(session);
        em.getTransaction().commit();
      }

      // Create a new session with the current user.
      String sessionKey = BCrypt.gensalt();
      StaffSession staffSession = new StaffSession(sessionKey, employee);
      em.getTransaction().begin();
      em.persist(staffSession);
      em.getTransaction().commit();

      // Create the spark session and set the session key.
      request.session(true);
      request.session().attribute("StaffSessionKey", sessionKey);

      switch (staffSession.getStaff().getDepartment()) {
        case WAITER:
          response.redirect("/staff/waiter.html");
          break;
        case KITCHEN:
          response.redirect("/staff/kitchen.html");
          break;
        case MANAGER:
          response.redirect("/manager/home.html");
          break;
        default:
          response.redirect("/");
          break;
      }
    } else {
      response.redirect("/");
    }
    em.close();
    return response;
  }

  /**
   * Checks if the request has a valid staff session key. Will halt if not.
   * No JSON input as it is intended to run before most get/posty requests - it just checks the
   * session details.
   * @param request The HTTP request.
   * @param response The HTTP response.
   */
  public static void checkStaffSession(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    // Check if session has a StaffSessionKey
    if (request.session().attribute("StaffSessionKey") == null) {
      // Not authenticated.
      // I'm sorry Dave.
      halt(401, "error_401");
    }

    // Attempt to get the session from the database.
    StaffSession session = em.find(StaffSession.class,
        request.session().attribute("StaffSessionKey"));

    if (session == null) {
      // Has a session key but is not a valid one. Possible logged in on another device since.
      // I'm afraid I can't do that.
      halt(401, "error_401");
    }
    em.close();
  }

  /**
   * Logs out the employee. No JSON input as it just uses the session details.
   *
   * @param request The HTTP request
   * @param response The HTTP response
   * @return A string representing the status
   */
  public static Response logOutEmployee(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    StaffSession session = em.find(StaffSession.class,
        request.session().attribute("StaffSessionKey"));
    em.getTransaction().begin();
    em.remove(session);
    em.getTransaction().commit();
    request.session().removeAttribute("StaffSessionKey");
    response.redirect("/");
    em.close();
    return response;
  }
}
