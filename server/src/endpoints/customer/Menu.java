package endpoints.customer;

import com.google.gson.Gson;
import database.Connector;
import database.tables.MenuItem;

import java.util.List;


public class Menu {

  private static Gson GSON = new Gson();

  /**
   * Gets the full menu from the database and returns it in JSON.
   * No JSON input as it is a get request.
   * @return The menu in JSON as a string.
   */
  public static String getMenu() {
    Connector connector = Connector.getInstance();

    List<MenuItem> menuItems = connector.query("from MenuItem", MenuItem.class);

    MenuData[] menuData = new MenuData[menuItems.size()];

    for (int i = 0; i < menuData.length; i++) {
      menuData[i] = new MenuData(menuItems.get(i));
    }

    return GSON.toJson(menuData);
  }
}
