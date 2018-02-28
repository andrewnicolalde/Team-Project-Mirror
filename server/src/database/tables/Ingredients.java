package database.tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class stores the ingredients of the menu items.
 */
@Entity
public class Ingredients {

  /**
   * Ths primary key so it can be stored in the database.
   *
   * @author Marcus Messer
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long ingredientId;

  /**
   * Ths name of the ingredient.
   */
  private String ingredientName;

  /**
   * This empty constructor is used by Hibernate.
   */
  public Ingredients() {
    //Empty Body
  }

  /**
   * This constructor creates new ingredients elements.
   *
   * @param ingredientName The name of the the new ingredient.
   */
  public Ingredients(String ingredientName) {
    this.ingredientName = ingredientName;
  }

  public Long getIngredientId() {
    return ingredientId;
  }

  public void setIngredientId(Long ingrerdientId) {
    this.ingredientId = ingrerdientId;
  }

  public String getIngredientName() {
    return ingredientName;
  }

  public void setIngredientName(String ingredientName) {
    this.ingredientName = ingredientName;
  }
}
