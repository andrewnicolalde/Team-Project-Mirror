package endpoints.menu;

import database.DatabaseManager;
import database.tables.Category;
import database.tables.Ingredient;
import java.util.List;
import javax.persistence.EntityManager;
import javax.xml.crypto.Data;

/**
 * This class converts JSON to a usable Java object.
 *
 * @author Marcus Messer
 */
public class MenuItemParams {

  /**
   * This stores the id of the menu item, NULL if its a new object.
   */
  private Long id;
  /**
   * This stores the name of the menu item. NULL if not edited.
   */
  private String name;

  /**
   * This stores the list of ingredients of the menu item. NULL if not edited.
   */
  private Long[] ingredients;

  /**
   * This stores the description of the item. NULL if not edited.
   */
  private String description;

  /**
   * This stores the price of the menu item. NULL if not edited.
   */
  private Double price;

  /**
   * This stores the calories of the menu item. NULL if not edited.
   */
  private Double calories;

  /**
   * This stores if the menu item is Vegan. NULL if not edited.
   */
  private Boolean isVegan;

  /**
   * This stores if the menu item is Vegetarian. NULL if not edited.
   */
  private Boolean isVegetarian;

  /**
   * This stores if the menu item is gluten free. NULL if not edited.
   */
  private Boolean isGlutenFree;

  /**
   * This stores where the picture for the item is stored. NULL if not edited.
   */
  private String pictureSrc;

  /**
   * This stores the category of item that is being added. NULL if not edited.
   */
  private String category;

  /**
   * This stores if the menu item is being added to the franchise menu immediately or if its just
   * going to be added the global menu. NULL when edited menu item.
   */
  private Boolean addNow;

  /**
   * This constructor is used to convert the JSON into a usable object that can be inserted into the
   * database.
   * @param name The name of the item.
   * @param ingredients The list of ingredients.
   * @param description The description of the item.
   * @param price The price of the item.
   * @param calories The calories of the item.
   * @param isVegan If the item is vegan.
   * @param isVegetarian If the item is vegetarian.
   * @param isGlutenFree If the item is gluten free.
   * @param pictureSrc Where the picture is stored.
   * @param category The category of the item.
   * @param addNow If the item is being added to the franchise table.
   */
  public MenuItemParams(Long id, String name, Long[] ingredients, String description, Double price,
      Double calories, Boolean isVegan, Boolean isVegetarian, Boolean isGlutenFree,
      String pictureSrc, String category, Boolean addNow) {

    this.id = id;
    this.name = name;
    this.ingredients = ingredients;
    this.description = description;
    this.price = price;
    this.calories = calories;
    this.isVegan = isVegan;
    this.isVegetarian = isVegetarian;
    this.isGlutenFree = isGlutenFree;
    this.pictureSrc = pictureSrc;
    this.category = category;
    this.addNow = addNow;

  }

  public String getName() {
    return name;
  }

  public Ingredient[] getIngredients() {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    Ingredient[] ingredientsArr = new Ingredient[ingredients.length];

    for (int i = 0; i < ingredients.length; i++) {
      ingredientsArr[i] = entityManager.find(Ingredient.class, ingredients[i]);
    }

    entityManager.close();
    return ingredientsArr;
  }

  public String getDescription() {
    return description;
  }

  public Double getPrice() {
    return price;
  }

  public Double getCalories() {
    return calories;
  }

  public Boolean getVegan() {
    return isVegan;
  }

  public Boolean getVegertarian() {
    return isVegetarian;
  }

  public Boolean getGlutenFree() {
    return isGlutenFree;
  }

  public String getPictureSrc() {
    return pictureSrc;
  }

  public Category getCategory() {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<Category> categoryList = entityManager.createQuery("from Category category where "
        + "category.name = :name", Category.class).setParameter("name", this.category).getResultList();
    entityManager.close();
    return categoryList.get(0);
  }

  public Boolean getAddNow() {
    return addNow;
  }

  public Long getId() {
    return id;
  }
}
