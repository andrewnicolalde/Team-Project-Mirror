package endpoints.stock;

import database.tables.Stock;

/**
 * This class takes the database result and is used to create usable JSON.
 *
 * @author Marcus Messer
 */
public class StockData {

  /**
   * This field stores the id of the ingredient. Used for editing. Can be null.
   */
  private Long id;
  /**
   * This field is the ingredient name. null if not changed.
   */
  private String ingredient;

  /**
   * This field is the stock count for an ingredient. null if not changed.
   */
  private Integer stockCount;

  /**
   * This constructor takes data from the database, and takes the key items from it.
   *
   * @param stock A database object.
   */
  public StockData(Stock stock) {
    this.ingredient = stock.getIngredient().getIngredientName();
    this.stockCount = stock.getStockCount();
  }

  /**
   * This constructor take JSON and makes it into a usable object.
   *
   * @param id The id of the stock item.
   * @param ingredient The name of the ingredient.
   * @param stockCount The stock count.
   */
  public StockData(Long id, String ingredient, int stockCount) {
    this.id = id;
    this.ingredient = ingredient;
    this.stockCount = stockCount;
  }

  public String getIngredient() {
    return ingredient;
  }

  public Integer getStockCount() {
    return stockCount;
  }

  public Long getId() {
    return id;
  }
}