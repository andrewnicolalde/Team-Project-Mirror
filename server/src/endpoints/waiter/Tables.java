package endpoints.waiter;

import static util.JsonUtil.toJson;

import com.google.gson.Gson;
import database.DatabaseManager;
import database.tables.RestaurantTableStaff;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;

import java.util.List;

public class Tables {

  /**
   * Returns a string holding a list of tables.
   * No JSON as it is a get request.
   * @param request
   * @param response
   * @return
   */
  public static String getTables(Request request, Response response) {
    // TODO: Get tables from the database

    return "[{\"number\":1,\"status\":\"Ready to order\",\"franchise\":3},{\"number\":2,\"status\":\"Eating\",\"franchise\":3}]";
  }

  public static String getTableData(Long staffId) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    entityManager.getTransaction().begin();
    List<RestaurantTableStaff> restaurantTableStaffs = entityManager.createQuery("from " +
        "RestaurantTableStaff tableStaff where tableStaff.staff.employeeNumber = " + staffId,
        RestaurantTableStaff.class).getResultList();

    TableData[] tableData = new TableData[restaurantTableStaffs.size()];
    for (int i = 0; i < tableData.length; i++) {
      tableData[i] = new TableData(restaurantTableStaffs.get(i));
    }

    entityManager.getTransaction().commit();

    return toJson(tableData);
  }
}
