package endpoints.tables;

import database.DatabaseManager;
import database.tables.RestaurantTableStaff;
import database.tables.StaffSession;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

/**
 * This class gets the list of tables from the database.
 */
public class Tables {

  public static String getTables(Request request, Response response) {

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    StaffSession staffSession = entityManager.find(StaffSession.class, request.session().attribute(
        "StaffSessionKey"));
    return getTableData(staffSession.getStaff().getEmployeeNumber());
  }

  private static String getTableData(Long staffId) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    List<RestaurantTableStaff> restaurantTableStaffs = entityManager.createQuery("from "
        + "RestaurantTableStaff tableStaff where tableStaff.staff.employeeNumber = " + staffId,
        RestaurantTableStaff.class).getResultList();

    restaurantTableStaffs.sort((t0, t1) -> {
      if (t0.getRestaurantTable().getTableNumber() < t1.getRestaurantTable().getTableNumber()) {
        return -1;
      }
      if (t0.getRestaurantTable().getTableNumber() > t1.getRestaurantTable().getTableNumber()) {
        return 1;
      }
      return 0;
    });

    TableData[] tableData = new TableData[restaurantTableStaffs.size()];
    for (int i = 0; i < tableData.length; i++) {
      tableData[i] = new TableData(restaurantTableStaffs.get(i));
    }

    entityManager.close();

    return JsonUtil.getInstance().toJson(tableData);
  }

  public static String changeTableStatus(Request request, Response response) {
    return "";
  }
}
