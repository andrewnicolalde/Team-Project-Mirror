package endpoints.order;

import database.tables.OrderMenuItem;
import java.text.DecimalFormat;

/**
 * This class is used to map the query results into something GSON can convert into JSON.
 *
 * @author Marcus Messer
 */
class OrderItemsData {


  private final Long id;
  private final String name;
  private final String category;
  private final String ingredients;
  private final String instructions;
  private final String description;
  private final Double calories;
  private final String price;
  private final Boolean is_vegan;
  private final Boolean is_vegetarian;
  private final Boolean is_gluten_free;
  private final String picture_src;

  /**
   * This function creates the objects that can be converted to JSON.
   */
  public OrderItemsData(OrderMenuItem orderMenuItem) {
    this.id = orderMenuItem.getOrderMenuItemId();
    this.name = orderMenuItem.getMenuItem().getName();
    this.category = orderMenuItem.getMenuItem().getCategory().getName();
    this.ingredients = orderMenuItem.getMenuItem().getIngredients();
    this.instructions = orderMenuItem.getInstructions();
    this.description = orderMenuItem.getMenuItem().getDescription();
    this.calories = orderMenuItem.getMenuItem().getCalories();
    DecimalFormat priceFormat = new DecimalFormat("#.00");
    this.price = priceFormat.format(orderMenuItem.getMenuItem().getPrice());
    this.is_vegan = orderMenuItem.getMenuItem().getVegan();
    this.is_vegetarian = orderMenuItem.getMenuItem().getVegetarian();
    this.is_gluten_free = orderMenuItem.getMenuItem().getGlutenFree();
    this.picture_src = orderMenuItem.getMenuItem().getPictureSrc();
  }

  public String getName() {
    return name;
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

  public String getInstructions() {
    return instructions;
  }

  public Long getId() {
    return id;
  }

  public Double getCalories() {
    return calories;
  }
}
