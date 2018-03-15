package endpoints.Ingredient;

/**
 * This class takes JSON and creates it into a usable object.
 *
 * @author Marcus Messer
 */
public class IngredientParams {

  /**
   * This field is the id of the ingredient, should be null when creating a new one.
   */
  private Long id;

  /**
   * This field is the name of the ingredient, should be null when removing a ingredient.
   */
  private String ingredientName;

  public IngredientParams(String ingredientName) {
    this.ingredientName = ingredientName;
  }

  public String getIngredientName() {
    return ingredientName;
  }

  public Long getId() {
    return id;
  }
}
