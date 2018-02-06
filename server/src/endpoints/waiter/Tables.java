package endpoints.waiter;

import com.google.gson.Gson;
import database.DatabaseManager;
import database.tables.RestaurantTableStaff;
import database.tables.StaffSession;
import javax.persistence.EntityManager;
import javax.xml.crypto.Data;
import spark.Request;
import spark.Response;

import java.util.List;

public class Tables {
  private static final Gson GSON = new Gson();

  /**
   * Returns a string holding a list of tables.
   * No JSON as it is a get request.
   * @param request
   * @param response
   * @return
   */
  public static String getTables(Request request, Response response) {
    // TODO: Get tables from the database

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    StaffSession staffSession = entityManager.find(StaffSession.class, request.session().attribute(
        "StaffSessionKey"));
    return getTableData(staffSession.getStaff().getEmployeeNumber());
  }

  public static String getTableData(Long staffId) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<RestaurantTableStaff> restaurantTableStaffs = entityManager.createQuery("from " +
        "RestaurantTableStaff tableStaff where tableStaff.staff.employeeNumber = " + staffId,
        RestaurantTableStaff.class).getResultList();

    TableData[] tableData = new TableData[restaurantTableStaffs.size()];
    for (int i = 0; i < tableData.length; i++) {
      tableData[i] = new TableData(restaurantTableStaffs.get(i));
    }

    return GSON.toJson(tableData);
  }
}
