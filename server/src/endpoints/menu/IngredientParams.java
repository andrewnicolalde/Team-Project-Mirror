package endpoints.menu;

/**
 * This class takes JSON and creates it into a usable object.
 *
 * @author Marcus Messer
 */
public class IngredientParams {

  private String ingredientName;

  public IngredientParams(String ingredientName) {
    this.ingredientName = ingredientName;
  }

  public String getIngredientName() {
    return ingredientName;
  }
}
