package endpoints.menu;

import database.DatabaseManager;
import database.tables.Category;
import database.tables.MenuItem;
import java.util.List;
import javax.persistence.EntityManager;
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
   * @return The menu in JSON as a string.
   */
  public static String getMenu() {

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    List<MenuItem> menuItems = entityManager.createQuery("from MenuItem ", MenuItem.class)
        .getResultList();

    entityManager.close();

    if (menuItems == null) {
      return "[]";
    }

    MenuData[] menuData = new MenuData[menuItems.size()];

    for (int i = 0; i < menuData.length; i++) {
      menuData[i] = new MenuData(menuItems.get(i));
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
}
