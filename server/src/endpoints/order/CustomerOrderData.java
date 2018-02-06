package endpoints.order;

import database.tables.OrderMenuItem;

/**
 * This class is used to map the query results into something GSON can convert into JSON.
 *
 * @author Marcus Messer
 */
public class CustomerOrderData {

  private Long id;
  private String name;
  private String category;
  private String allergy_info;
  private String description;
  private Double price;
  private Boolean is_vegan;
  private Boolean is_vegetarian;
  private Boolean is_gluten_free;
  private String picture_src;

  /**
   * This function creates the objects that can be converted to JSON.
   */
  public CustomerOrderData(OrderMenuItem orderMenuItem) {
    this.id = orderMenuItem.getOrderMenuItemId();
    this.name = orderMenuItem.getMenuItem().getName();
    this.category = orderMenuItem.getMenuItem().getCategory().getName();
    this.allergy_info = orderMenuItem.getMenuItem().getAllergyInfo();
    this.description = orderMenuItem.getMenuItem().getDescription();
    this.price = orderMenuItem.getMenuItem().getPrice();
    this.is_vegan = orderMenuItem.getMenuItem().getVegan();
    this.is_vegetarian = orderMenuItem.getMenuItem().getVegitarrenan();
    this.is_gluten_free = orderMenuItem.getMenuItem().getGlutenFree();
    this.picture_src = orderMenuItem.getMenuItem().getPictureSrc();
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public String getAllergy_info() {
    return allergy_info;
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

  public String getPicture_src() {
    return picture_src;
  }
}
