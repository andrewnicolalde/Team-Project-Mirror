package endpoints.menu;

import database.DatabaseManager;
import database.tables.FranchiseMenuItem;
import database.tables.Category;
import database.tables.Franchise;
import database.tables.Ingredient;
import database.tables.MenuItem;
import database.tables.Staff;
import database.tables.StaffSession;
import database.tables.TableSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.persistence.EntityManager;
import javax.xml.crypto.Data;
import org.hibernate.boot.model.relational.Database;
import spark.Request;
import spark.Response;
import util.JsonUtil;

/**
 * This class returns the menu items in a JSON format.
 *
 * @author Marcus Messer
 */
public class Menu {

  /**
   * Gets the full menu from the database and returns it in JSON. No JSON input as it is a get
   * request.
   *
   * @param request A HTML request
   * @param response A HTML response
   * @return The menu in JSON as a string
   */
  public static String getMenu(Request request, Response response) {

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    Franchise franchise = null;

    if (request.session().attribute("TableSessionKey") != null) {
      franchise = entityManager.find(TableSession.class, request.session().attribute(
          "TableSessionKey")).getRestaurantTable().getFranchise();
    } else if (request.session().attribute("StaffSessionKey") != null) {
      franchise = entityManager.find(StaffSession.class, request.session().attribute(
          "StaffSessionKey")).getStaff().getFranchise();
    } else {
      return "";
    }

    List<FranchiseMenuItem> menuItems = entityManager.createQuery("from FranchiseMenuItem "
        + "menuItem where menuItem.franchise = :franchise", FranchiseMenuItem.class)
        .setParameter("franchise", franchise).getResultList();

    entityManager.close();

    if (menuItems.size() == 0) {
      return "[]";
    }

    MenuData[] menuData = new MenuData[menuItems.size()];

    for (int i = 0; i < menuData.length; i++) {
      menuData[i] = new MenuData(menuItems.get(i).getMenuItem());
    }

    return JsonUtil.getInstance().toJson(menuData);
  }

  public static String getCategories() {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();

    List<Category> categories = em.createQuery("from Category ORDER BY displayOrder asc",
        Category.class).getResultList();

    em.close();

    if (categories == null) {
      return "[]";
    }

    return JsonUtil.getInstance().toJson(categories);
  }

  /**
   * This method takes JSON to create a new menu item. For details on what JSON is needed view
   * <code>MenuItemParams</code>.
   *
   * @param request A HTML request.
   * @param response A HTML response.
   * @return Success after it adds the item.
   */
  public static String createMenuItem(Request request, Response response) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    System.out.println(request.body());

    MenuItemParams menuItemParams = JsonUtil.getInstance().fromJson(request.body(),
        MenuItemParams.class);

    MenuItem menuItem = new MenuItem(menuItemParams.getName(), new HashSet<>(
        Arrays.asList(menuItemParams.getIngredients())), menuItemParams.getDescription(),
        menuItemParams.getPrice(), menuItemParams.getCalories(), menuItemParams.getVegan(),
        menuItemParams.getVegertarian(), menuItemParams.getGlutenFree(),
        menuItemParams.getPictureSrc(), menuItemParams.getCategory());

    entityManager.getTransaction().begin();
    entityManager.persist(menuItem);
    entityManager.getTransaction().commit();

    if (menuItemParams.getAddNow()) {
      StaffSession staffSession = entityManager.find(StaffSession.class,
          request.session().attribute("StaffSessionKey"));

      entityManager.getTransaction().begin();
      entityManager
          .persist(new FranchiseMenuItem(staffSession.getStaff().getFranchise(), menuItem));
      entityManager.getTransaction().commit();
    }

    entityManager.close();
    return "success";
  }

  /**
   * This method takes JSON and make menu items editable. For details on what JSON is needed view
   * <code>MenuItemParams</code>.
   *
   * @param request A HTML request.
   * @param response A HTML response.
   * @return Success after it adds the item.
   */
  public static String editMenuItem(Request request, Response response) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    MenuItemParams menuItemParams = JsonUtil.getInstance().fromJson(request.body(),
        MenuItemParams.class);

    MenuItem menuItem = entityManager.find(MenuItem.class, menuItemParams.getId());

    entityManager.getTransaction().begin();

    menuItem.setName(menuItemParams.getName());
    menuItem.setIngredientsSet(new HashSet<>(Arrays.asList(menuItemParams.getIngredients())));
    menuItem.setDescription(menuItemParams.getDescription());
    menuItem.setPrice(menuItemParams.getPrice());
    menuItem.setCalories(menuItemParams.getCalories());
    menuItem.setVegan(menuItemParams.getVegan());
    menuItem.setVegetarian(menuItemParams.getVegertarian());
    menuItem.setGlutenFree(menuItemParams.getGlutenFree());
    menuItem.setPictureSrc(menuItemParams.getPictureSrc());
    menuItem.setCategory(menuItemParams.getCategory());

    entityManager.getTransaction().commit();

    entityManager.close();
    return "success";
  }

  /**
   * This method removes a menu item from the database. See <code>RemoveMenuItemParams</code> for
   * JSON details.
   *
   * @param request A HTML request.
   * @param response A HTML response.
   * @return Success after an item is removed.
   */
  public static String removeMenuItem(Request request, Response response) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    RemoveMenuItemParams removeMenuItemParams = JsonUtil.getInstance().fromJson(request.body(),
        RemoveMenuItemParams.class);

    entityManager.remove(entityManager.find(MenuItem.class, removeMenuItemParams.getId()));

    entityManager.close();
    return "success";
  }

  /**
   * This method sets the franchise menu. See <code>FranchiseMenuParams</code> for JSON details.
   *
   * @param request A HTML request.
   * @param response A HTML response.
   * @return Success after the menu items have been added to the franchise menu.
   */
  public static String setFranchiseMenu(Request request, Response response) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    FranchiseMenuParams franchiseMenuParams = JsonUtil.getInstance().fromJson(request.body(),
        FranchiseMenuParams.class);

    StaffSession staffSession = entityManager.find(StaffSession.class,
        request.session().attribute("StaffSessionKey"));

    entityManager.getTransaction().begin();

    for (MenuItem item : franchiseMenuParams.getMenuItems()) {
      entityManager.persist(new FranchiseMenuItem(staffSession.getStaff().getFranchise(), item));
    }

    entityManager.getTransaction().commit();

    entityManager.close();
    return "success";
  }

}
