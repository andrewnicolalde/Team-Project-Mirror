package endpoints.customer;

import static util.JsonUtil.toJson;

import com.google.gson.Gson;
import database.DatabaseManager;
import database.tables.MenuItem;

import java.util.List;
import javax.persistence.EntityManager;


public class Menu {

  /**
   * Gets the full menu from the database and returns it in JSON.
   * No JSON input as it is a get request.
   * @return The menu in JSON as a string.
   */
  public static String getMenu() {

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    entityManager.getTransaction().begin();
    List<MenuItem> menuItems = entityManager.createQuery("from MenuItem ", MenuItem.class).getResultList();
    entityManager.getTransaction().commit();

    MenuData[] menuData = new MenuData[menuItems.size()];

    for (int i = 0; i < menuData.length; i++) {
      menuData[i] = new MenuData(menuItems.get(i));
    }

    return toJson(menuData);
  }
}
