package database.tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * This table links menu items to ingredients.
 *
 * @author Marcus Messer
 */
@Entity
@Table(name = "MENU_ITEM_INGREDIENTS")
public class MenuItemIngredients {

  /**
   * This field is the primary key so the link can be stored in the database.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long menuItemIngredientId;

  /**
   * This field links the menu item to the ingredient.
   */
  @ManyToOne
  private MenuItem menuItem;

  /**
   * This field links the ingredient to the menu item.
   */
  @ManyToOne
  private Ingredients ingredients;

  /**
   * Empty constructor used by Hibernate.
   */
  public MenuItemIngredients() {
    //Empty Body
  }

  /**
   * This constructor creates new links between menu items and ingredients.
   * @param menuItem The menu item to be linked
   * @param ingredients The ingredient to be linked
   */
  public MenuItemIngredients(MenuItem menuItem, Ingredients ingredients) {
    this.menuItem = menuItem;
    this.ingredients = ingredients;
  }

  public Long getMenuItemIngredientId() {
    return menuItemIngredientId;
  }

  public void setMenuItemIngredientId(Long menuItemIngredientId) {
    this.menuItemIngredientId = menuItemIngredientId;
  }

  public MenuItem getMenuItem() {
    return menuItem;
  }

  public void setMenuItem(MenuItem menuItem) {
    this.menuItem = menuItem;
  }

  public Ingredients getIngredients() {
    return ingredients;
  }

  public void setIngredients(Ingredients ingredients) {
    this.ingredients = ingredients;
  }
}
