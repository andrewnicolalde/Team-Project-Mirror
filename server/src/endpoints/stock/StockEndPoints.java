package endpoints.stock;

import database.DatabaseManager;
import database.tables.Ingredient;
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

    for (int i= 0; i < stocks.size(); i++) {
      stockData[i] = new StockData(stocks.get(i));
    }

    return JsonUtil.getInstance().toJson(stockData);
  }

  /**
   * This method takes JSON and makes changes to the stock file.
   * For JSON details see <code>StockData</code>
   * @param request A HTML request.
   * @param response A HTML response.
   * @return Success if it succeeds and failed if it crashes.
   */
  public static String setStock(Request request, Response response) {
    StockData stockData = JsonUtil.getInstance().fromJson(request.body(), StockData.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    boolean failed = false;
    try {
      entityManager.getTransaction().begin();
      Stock stock = entityManager.find(Stock.class, stockData.getId());

      if (stockData.getIngredient() != null) {
        stock.setIngredient(entityManager.createQuery("from Ingredient ingredient where ingredientName = :name", Ingredient.class).setParameter("name",
            stockData.getIngredient()).getSingleResult());
      }

      if (stockData.getStockCount() != null) {
        stock.setStockCount(stockData.getStockCount());
      }
    } catch (Exception e) {
      failed = false;
    } finally {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      entityManager.close();
    }

    if (failed) {
      return "failed";
    }

    return "success";
  }
}
