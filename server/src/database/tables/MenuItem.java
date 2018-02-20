package database.tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class maps the menu item table.
 *
 * @author Marcus Messer
 */
@Entity
@Table(name = "MENU_ITEM")
public class MenuItem {

  /**
   * This field is the primary key of the entity, it is auto generated, by the increment strategy.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long menuItemId;

  /**
   * This field is the name of the item on the menu.
   */
  private String name;

  /**
   * This field holds the information about the ingredients information.
   */
  private String ingredients;

  /**
   * This field holds the description of the menu item.
   */
  private String description;

  /**
   * This field holds the price for the item.
   */
  private Double price;

  /**
   * This field states if the item is suitable for vegans.
   */
  private Boolean isVegan;

  /**
   * This field states if the item is suitable for vegetarians.
   */
  private Boolean isVegetarian;

  /**
   * This field states if the item is gluten free.
   */
  private Boolean isGlutenFree;

  /**
   * Thus field stores where the picure is stored
   */
  private String pictureSrc;

  /**
   * This field states what category the item belongs too.
   */
  @ManyToOne
  @JoinColumn(name = "categoryId")
  private Category category;

  /**
   * This empty constructor is used by Hibernate.
   */
  public MenuItem() {
    //Empty Body
  }

  /**
   * This constructor is used to create new menu items.
   *
   * @param name The name of menu item.
   * @param ingredients The ingredients info for the item.
   * @param description The description for the item.
   * @param price The price of the item.
   * @param isVegan If the item is suitable for Vegans.
   * @param isVegetarian If the item is suitable for vegetarians.
   * @param isGlutenFree If the item is gluten free.
   * @param category The category the item belongs in.
   */
  public MenuItem(String name, String ingredients, String description, Double price,
      Boolean isVegan, Boolean isVegetarian, Boolean isGlutenFree, String pictureSrc,
      Category category) {
    this.name = name;
    this.ingredients = ingredients;
    this.description = description;
    this.price = price;
    this.isVegan = isVegan;
    this.isGlutenFree = isGlutenFree;
    this.isVegetarian = isVegetarian;
    this.pictureSrc = pictureSrc;
    this.category = category;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIngredients() {
    return ingredients;
  }

  public void setingredients(String ingredients) {
    this.ingredients = ingredients;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Boolean getVegan() {
    return isVegan;
  }

  public void setVegan(Boolean vegan) {
    isVegan = vegan;
  }

  public Boolean getVegitarrenan() {
    return isVegetarian;
  }

  public void setVegitarrenan(Boolean vegitarrenan) {
    isVegetarian = vegitarrenan;
  }

  public Boolean getGlutenFree() {
    return isGlutenFree;
  }

  public void setGlutenFree(Boolean glutenFree) {
    isGlutenFree = glutenFree;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Long getMenuItemId() {
    return menuItemId;
  }

  public void setMenuItemId(Long menuItemId) {
    this.menuItemId = menuItemId;
  }

  public String getPictureSrc() {
    return pictureSrc;
  }

  public void setPictureSrc(String pictureSrc) {
    this.pictureSrc = pictureSrc;
  }
}
