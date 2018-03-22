package endpoints.tables;

import database.DatabaseManager;
import database.tables.RestaurantTable;
import database.tables.RestaurantTableStaff;
import database.tables.StaffSession;
import database.tables.TableSession;
import database.tables.TableStatus;
import endpoints.notification.NotificationEndpoint;
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
    return getWaiterTableData(staffSession.getStaff().getEmployeeNumber());
  }

  public static String getAllTables(Request request, Response response) {
    return getAllTableData();
  }

  private static String getWaiterTableData(Long staffId) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    List<RestaurantTableStaff> restaurantTableStaffs = entityManager.createQuery("from "
            + "RestaurantTableStaff tableStaff where tableStaff.staff.employeeNumber = :staffId and tableStaff.isActive = true",
        RestaurantTableStaff.class).setParameter("staffId", staffId).getResultList();

    // This sorts by the table status.
    restaurantTableStaffs.sort((t0, t1) -> {
      if (t0.getRestaurantTable().getStatus().compareTo(t1.getRestaurantTable().getStatus())
          == 0 && t0.getRestaurantTable().getNeedsHelpTime() != null
          && t1.getRestaurantTable().getNeedsHelpTime() != null) {
        return t0.getRestaurantTable().getNeedsHelpTime()
            .compareTo(t1.getRestaurantTable().getNeedsHelpTime());
      } else {
        return t0.getRestaurantTable().getStatus().compareTo(t1.getRestaurantTable().getStatus());
      }
    });

    TableData[] tableData = new TableData[restaurantTableStaffs.size()];
    for (int i = 0; i < tableData.length; i++) {
      RestaurantTable temp = restaurantTableStaffs.get(i).getRestaurantTable();
      tableData[i] = new TableData(temp.getTableNumber(), temp.getStatus().toString(),
          temp.getFranchise().toString(), temp.getTableId());
    }

    entityManager.close();

    return JsonUtil.getInstance().toJson(tableData);
  }

  private static String getAllTableData() {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    List<RestaurantTableStaff> restaurantTableStaffs = entityManager.createQuery("from "
            + "RestaurantTableStaff tableStaff",
        RestaurantTableStaff.class).getResultList();

    // This sorts by the table status.
    restaurantTableStaffs.sort((t0, t1) -> {
      if (t0.getRestaurantTable().getStatus().compareTo(t1.getRestaurantTable().getStatus())
          == 0 && t0.getRestaurantTable().getNeedsHelpTime() != null
          && t1.getRestaurantTable().getNeedsHelpTime() != null) {
        return t0.getRestaurantTable().getNeedsHelpTime()
            .compareTo(t1.getRestaurantTable().getNeedsHelpTime());
      } else {
        return t0.getRestaurantTable().getStatus().compareTo(t1.getRestaurantTable().getStatus());
      }
    });

    TableData[] tableData = new TableData[restaurantTableStaffs.size()];
    for (int i = 0; i < tableData.length; i++) {
      RestaurantTable temp = restaurantTableStaffs.get(i).getRestaurantTable();
      tableData[i] = new TableData(temp.getTableNumber(), temp.getStatus().toString(),
          temp.getFranchise().toString(), temp.getTableId());
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

    ChangeTableStatus cts = JsonUtil.getInstance()
        .fromJson(request.body(), ChangeTableStatus.class);

    Long tableId;
    if (request.session().attribute("TableSessionKey") != null) {
      tableId = entityManager
          .find(TableSession.class, request.session().attribute("TableSessionKey"))
          .getRestaurantTable().getTableId();
    } else if (request.session().attribute("StaffSessionKey") != null) {
      tableId = cts.getTableId();
    } else {
      return "";
    }

    entityManager.getTransaction().begin();

    RestaurantTable restaurantTable = entityManager
        .createQuery("from RestaurantTable table where table.tableId = :id", RestaurantTable.class)
        .setParameter("id", tableId).getSingleResult();

    restaurantTable.setStatus(TableStatus.valueOf(cts.getNewStatus()));

    NotificationEndpoint.startNotificationService(restaurantTable);

    entityManager.getTransaction().commit();
    entityManager.close();

    return "success";
  }
}
