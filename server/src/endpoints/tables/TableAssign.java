package endpoints.tables;

import database.DatabaseManager;
import database.tables.RestaurantTableStaff;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

/**
 * This class has the endpoints for assigning waiters to tables.
 *
 * @author  Marcus Messer
 */
public class TableAssign {

  /**
   * This gets the assigments of watiers to tables.
   * For the JSON details see <code>TableAssignParams</code>
   * @param request A HTML request.
   * @param response A HTML response.
   * @return A list of table assignments in JSON.
   */
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
