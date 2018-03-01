package database.tables;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class stores the ingredients of the menu items.
 */
@Entity
public class Ingredient {

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

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "ingredients")
  private Set<MenuItem> menuItems = new HashSet<>();

  /**
   * This empty constructor is used by Hibernate.
   */
  public Ingredient() {
    //Empty Body
  }

  /**
   * This constructor creates new ingredients elements.
   *
   * @param ingredientName The name of the the new ingredient.
   */
  public Ingredient(String ingredientName) {
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

  public Set<MenuItem> getMenuItems() {
    return menuItems;
  }

  public void setMenuItems(Set<MenuItem> menuItems) {
    this.menuItems = menuItems;
  }
}
