package endpoints.stock;

import database.tables.Stock;

/**
 * This class takes the database result and is used to create usable JSON.
 *
 * @author Marcus Messer
 */
public class StockData {

  /**
   * This field is the ingredient name.
   */
  private String ingredient;

  /**
   * This field is the stock count for an ingredient.
   */
  private int stockCount;

  /**
   * This constructor takes data from the database, and takes the key items from it.
   * @param stock A database object.
   */
  public StockData(Stock stock) {
    this.ingredient = stock.getIngredient().getIngredientName();
    this.stockCount = stock.getStockCount();
  }

  public String getIngredient() {
    return ingredient;
  }

  public int getStockCount() {
    return stockCount;
  }
}
