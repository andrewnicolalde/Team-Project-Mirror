package database;

import database.tables.Franchise;
import database.tables.MenuItem;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class enables franchise to have separate menus. By linking menu items to the franchises.
 */
@Entity
public class FranchiseMenuItem {

  /**
   * A primary key for the data.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long frachiseMenuItemId;

  /**
   * The franchise the menu item links to.
   */
  @ManyToOne
  private Franchise franchise;

  /**
   * The menu item to be on the menu.
   */
  @ManyToOne
  private MenuItem menuItem;

  /**
   * Empty constructor used by Hibernate.
   */
  public FranchiseMenuItem () {
    // Used by Hibernate
  }

  /**
   * This constructor creates new links between the franchise and the menu items.
   * @param franchise The franchise menu that the menu item is being added to.
   * @param menuItem The menu item being added to the franchise menu.
   */
  public FranchiseMenuItem(Franchise franchise, MenuItem menuItem) {
    this.franchise = franchise;
    this.menuItem = menuItem;
  }

  public Franchise getFranchise() {
    return franchise;
  }

  public void setFranchise(Franchise franchise) {
    this.franchise = franchise;
  }

  public MenuItem getMenuItem() {
    return menuItem;
  }

  public void setMenuItem(MenuItem menuItem) {
    this.menuItem = menuItem;
  }
}
