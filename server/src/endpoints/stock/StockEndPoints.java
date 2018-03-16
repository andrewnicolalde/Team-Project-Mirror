package endpoints.stock;

import database.DatabaseManager;
import database.tables.StaffSession;
import database.tables.Stock;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

/**
 * This class includes the endpoints for getting and setting stock levels.
 *
 * @author Marcus Messer
 */
public class StockEndPoints {

  /**
   * This endpoint returns the stock levels for a franchise.
   * See <code>StockData</code> for the JSON details.
   * @param request A HTML request.
   * @param response A HTML response
   * @return The stock levels in JSON
   */
  public static String getStock(Request request, Response response) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    StaffSession staffSession = entityManager.find(StaffSession.class,
        request.session().attribute("StaffSessionKey"));

    List<Stock> stocks = entityManager.createQuery("from Stock stock where "
        + "stock.franchise = :franchise", Stock.class).setParameter("franchise",
        staffSession.getStaff().getFranchise()).getResultList();

    StockData[] stockData = new StockData[stocks.size()];

    for (int i= 0; i < 0; i++) {
      stockData[i] = new StockData(stocks.get(i));
    }

    return JsonUtil.getInstance().toJson(stockData);
  }

}
