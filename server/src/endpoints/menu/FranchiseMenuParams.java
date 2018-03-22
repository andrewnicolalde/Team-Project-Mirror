package endpoints.menu;

import database.tables.MenuItem;

/**
 * This class converts JSON to a usable object.
 */
public class FranchiseMenuParams {

  /**
   * This field is the list of items that are part of the franchise menu.
   */
  private MenuItem[] menuItems;

  /**
   * This constructor takes JSON and converts it into a usable object.
   * @param menuItems The list of menu items in the franchise menu.
   */
  public FranchiseMenuParams(MenuItem[] menuItems) {
    this.menuItems = menuItems;
  }

  public MenuItem[] getMenuItems() {
    return menuItems;
  }
}
