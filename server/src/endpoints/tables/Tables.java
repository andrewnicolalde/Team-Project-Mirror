package endpoints.tables;

import database.DatabaseManager;
import database.tables.RestaurantTable;
import database.tables.RestaurantTableStaff;
import database.tables.StaffSession;
import database.tables.TableSession;
import database.tables.TableStatus;
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

  /**
   * This method changes the table status.
   *
   * @param request A html request
   * @param response A html response
   * @return Success after it change the status.
   */
  public static String changeTableStatus(Request request, Response response) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    Long tableId = entityManager
        .find(TableSession.class, request.session().attribute("TableSessionKey"))
        .getRestaurantTable().getTableId();

    ChangeTableStatus cts = JsonUtil.getInstance()
        .fromJson(request.body(), ChangeTableStatus.class);

    entityManager.getTransaction().begin();

    RestaurantTable restaurantTable = entityManager
        .createQuery("from RestaurantTable table where table.tableId = :id", RestaurantTable.class)
        .setParameter("id", tableId).getSingleResult();

    restaurantTable.setStatus(TableStatus.valueOf(cts.getNewStatus()));

    entityManager.getTransaction().commit();
    entityManager.close();

    return "success";
  }
}
