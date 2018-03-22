package database.tables;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinTable(name = "INGREDIENT_MENU_ITEM",
      joinColumns = @JoinColumn(name = "menuItemId"),
      inverseJoinColumns = @JoinColumn(name = "ingredientId"))
  private Set<Ingredient> ingredients = new HashSet<>();

  /**
   * This field holds the description of the menu item.
   */
  private String description;

  /**
   * This field holds the price for the item.
   */
  private Double price;

  /**
   * This field stores the calories for the item.
   */
  private Double calories;

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
   * Thus field stores where the picture is stored
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
   * @param description The description for the item,
   * @param price The price of the item.
   * @param calories The amount of calories for the item.
   * @param isVegan If the item is suitable for Vegans.
   * @param isVegetarian If the item is suitable for vegetarians.
   * @param isGlutenFree If the item is gluten free.
   * @param category The category the item belongs in.
   */
  public MenuItem(String name, Set<Ingredient> ingredients, String description, Double price,
      Double calories, Boolean isVegan, Boolean isVegetarian, Boolean isGlutenFree,
      String pictureSrc, Category category) {
    this.name = name;
    this.ingredients = ingredients;
    this.description = description;
    this.calories = calories;
    this.price = price;
    this.isVegan = isVegan;
    this.isGlutenFree = isGlutenFree;
    this.isVegetarian = isVegetarian;
    this.pictureSrc = pictureSrc;
    this.category = category;
  }

  /**
   * Adds an ingredient to the list of ingredients.
   *
   * @param ingredient The new ingredient
   */
  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
    ingredient.getMenuItems().add(this);
  }

  /**
   * Removes an ingredient from the set of ingredients.
   *
   * @param ingredient The ingredient to remove
   */
  public void removeIngredient(Ingredient ingredient) {
    ingredients.remove(ingredient);
    ingredient.getMenuItems().remove(this);
  }

  public String getIngredients() {
    StringBuilder stringBuilder = new StringBuilder();

    //Taken from https://stackoverflow.com/questions/3395286/remove-last-character-of-a-stringbuilder
    for (Ingredient ingredient : ingredients) {
      stringBuilder.append(ingredient.getIngredientName()).append(", ");
    }

    if (stringBuilder.length() > 0) {
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    }

    return stringBuilder.toString();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Ingredient> getIngredientsSet() {
    return ingredients;
  }

  public void setIngredientsSet(Set<Ingredient> ingredients) {
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

  public Boolean getVegetarian() {
    return isVegetarian;
  }

  public void setVegetarian(Boolean vegetarian) {
    isVegetarian = vegetarian;
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

  public Double getCalories() {
    return calories;
  }

  public void setCalories(Double calories) {
    this.calories = calories;
  }


  @Override
  public String toString() {
    return "MenuItem{" +
        "menuItemId=" + menuItemId +
        ", name='" + name + '\'' +
        ", ingredients='" + ingredients + '\'' +
        ", description='" + description + '\'' +
        ", price=" + price +
        ", calories=" + calories +
        ", isVegan=" + isVegan +
        ", isVegetarian=" + isVegetarian +
        ", isGlutenFree=" + isGlutenFree +
        ", pictureSrc='" + pictureSrc + '\'' +
        ", category=" + category +
        '}';
  }
}
