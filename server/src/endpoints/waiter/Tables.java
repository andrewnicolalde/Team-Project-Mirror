package endpoints.waiter;

import com.google.gson.Gson;
import database.Connector;
import database.tables.RestaurantTable;
import database.tables.RestaurantTableStaff;
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

    return "[{\"number\":1,\"status\":\"Ready to order\",\"franchise\":3},{\"number\":2,\"status\":\"Eating\",\"franchise\":3}]";
  }

  public static String getTableData(Long staffId) {
    Connector connector = Connector.getInstance();
    connector.createConnection();
    List<RestaurantTableStaff> restaurantTableStaffs = connector.query("from " +
        "RestaurantTableStaff tableStaff where tableStaff.staff.employeeNumber = " + staffId,
        RestaurantTableStaff.class);
    connector.closeConnection();

    TableData[] tableData = new TableData[restaurantTableStaffs.size()];
    for (int i = 0; i < tableData.length; i++) {
      tableData[i] = new TableData(restaurantTableStaffs.get(i));
    }

    return GSON.toJson(tableData);
  }
}
