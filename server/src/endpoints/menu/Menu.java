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
import spark.Request;
import spark.Response;
import util.JsonUtil;

/**
 * This class returns the menu items in a JSON format.
 *
 * @author Marcus Messer
 *
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
   * This method take JSON to create a new menu item. For details on what JSON is needed view
   * <code>MenuItemParams</code>.
   * @param request A HTML request.
   * @param response A HTML response.
   * @return Success after it adds the item.
   */
  public static String createMenuItem(Request request, Response response) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

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
      entityManager.persist(new FranchiseMenuItem(staffSession.getStaff().getFranchise(), menuItem));
      entityManager.getTransaction().commit();
    }

    entityManager.close();
    return "success";
  }
}
