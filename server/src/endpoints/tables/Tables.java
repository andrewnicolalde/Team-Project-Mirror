package endpoints.tables;

import static util.JsonUtil.toJson;

import database.DatabaseManager;
import database.tables.RestaurantTableStaff;
import database.tables.StaffSession;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;

public class Tables {

  /**
   * Returns a string holding a list of tables. No JSON as it is a get request.
   */
  public static String getTables(Request request, Response response) {

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

    return toJson(tableData);
  }
}
