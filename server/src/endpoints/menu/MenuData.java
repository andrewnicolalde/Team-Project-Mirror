package endpoints.menu;

import database.tables.MenuItem;

/**
 * This class is to map the query result into some GSON can convert into JSON.
 *
 * @author Marcus Messer
 */
public class MenuData {

  private Long id;
  private String name;
  private String category;
  private String ingredients;
  private String description;
  private Double price;
  private Boolean is_vegan;
  private Boolean is_vegetarian;
  private Boolean is_gluten_free;
  private String picture_src;

  /**
   * This constructor create new menu data items that can be converted into JSON.
   *
   * @param menuItem the item from the database.
   */
  public MenuData(MenuItem menuItem) {
    this.id = menuItem.getMenuItemId();
    this.name = menuItem.getName();
    this.category = menuItem.getCategory().getName();
    this.ingredients = menuItem.getIngredients();
    this.description = menuItem.getDescription();
    this.price = menuItem.getPrice();
    this.is_vegan = menuItem.getVegan();
    this.is_vegetarian = menuItem.getVegitarrenan();
    this.is_gluten_free = menuItem.getGlutenFree();
    this.picture_src = menuItem.getPictureSrc();
  }

  public Long getId() {
    return id;
  }

  public String getCategory() {
    return category;
  }

  public String getingredients() {
    return ingredients;
  }

  public String getDescription() {
    return description;
  }

  public Double getPrice() {
    return price;
  }

  public Boolean getIs_vegan() {
    return is_vegan;
  }

  public Boolean getIs_vegetarian() {
    return is_vegetarian;
  }

  public Boolean getIs_gluten_free() {
    return is_gluten_free;
  }

  public void setIs_gluten_free(Boolean is_gluten_free) {
    this.is_gluten_free = is_gluten_free;
  }

  public String getPicture_src() {
    return picture_src;
  }

  public String getName() {
    return name;
  }
}
