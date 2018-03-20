package endpoints.tables;

import database.DatabaseManager;
import database.tables.RestaurantTableStaff;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

public class TableAssign {

  public static String getTableAssignments(Request request, Response response) {
    TableAssignParams tableAssignParams = JsonUtil.getInstance()
        .fromJson(request.body(), TableAssignParams.class);
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<RestaurantTableStaff> restaurantTableStaffs = entityManager.createQuery(
        "from RestaurantTableStaff tableStaff where "
            + "tableStaff.restaurantTable.franchise = :franchise", RestaurantTableStaff.class)
        .setParameter("franchise", tableAssignParams.getFranchise()).getResultList();

    TableAssignParams[] out = new TableAssignParams[restaurantTableStaffs.size()];


    for (int i = 0; i < out.length; i++) {
      out[i] = new TableAssignParams(restaurantTableStaffs.get(i));
    }
    return JsonUtil.getInstance().toJson(out);
  }
}
