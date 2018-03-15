package endpoints.menu;

import database.tables.MenuItem;
import java.text.DecimalFormat;

/**
 * This class is to map the query result into some GSON can convert into JSON.
 *
 * @author Marcus Messer
 */
class MenuData {

  private final Long id;
  private final String name;
  private final Long categoryId;
  private final String category;
  private final String ingredients;
  private final String description;
  private final Double calories;
  private final String price;
  private final Boolean is_vegan;
  private final Boolean is_vegetarian;
  private final Boolean is_gluten_free;
  private final String picture_src;

  /**
   * This constructor create new menu data items that can be converted into JSON.
   *
   * @param menuItem the item from the database.
   */
  public MenuData(MenuItem menuItem) {
    this.id = menuItem.getMenuItemId();
    this.name = menuItem.getName();
    this.categoryId = menuItem.getCategory().getCategoryId();
    this.category = menuItem.getCategory().getName();
    this.ingredients = menuItem.getIngredients();
    this.description = menuItem.getDescription();
    this.calories = menuItem.getCalories();
    DecimalFormat priceFormat = new DecimalFormat("#.00");
    this.price = priceFormat.format(menuItem.getPrice());
    this.is_vegan = menuItem.getVegan();
    this.is_vegetarian = menuItem.getVegetarian();
    this.is_gluten_free = menuItem.getGlutenFree();
    this.picture_src = menuItem.getPictureSrc();
  }

  public Long getId() {
    return id;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public String getCategory() {
    return category;
  }

  public String getIngredients() {
    return ingredients;
  }

  public String getDescription() {
    return description;
  }

  public String getPrice() {
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

  public String getPicture_src() {
    return picture_src;
  }

  public String getName() {
    return name;
  }

  public Double getCalories() {
    return calories;
  }
}
