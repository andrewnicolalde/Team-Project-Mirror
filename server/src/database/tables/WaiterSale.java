package database.tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * This table stores when waiters add menu items to an order, so manament can keep track of there
 * sales.
 *
 * @author Marcus Messer
 */
@Entity
@Table(name = "WAITER_SALE")
public class WaiterSale {

  /**
   * The primary key that is required.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long id;

  /**
   * The staff member that added the extra item.
   */
  @ManyToOne
  private Staff staff;

  /**
   * The Menu Item that the waiter added.
   */
  @ManyToOne
  private MenuItem menuItem;

  /**
   * The order that the item got added too.
   */
  @ManyToOne
  private FoodOrder foodOrder;

  /**
   * Empty constructor used by Hibernate.
   */
  public WaiterSale(){
    //Empty Body
  }

  /**
   * This constructor is how new sales are added for a member of staff.
   * @param staff The staff member who added the extra item.
   * @param menuItem The item added to the order.
   * @param foodOrder The order the item has been added to.
   */
  public WaiterSale(Staff staff, MenuItem menuItem, FoodOrder foodOrder) {
    this.staff = staff;
    this.menuItem = menuItem;
    this.foodOrder = foodOrder;
  }

  public Staff getStaff() {
    return staff;
  }

  public void setStaff(Staff staff) {
    this.staff = staff;
  }

  public MenuItem getMenuItem() {
    return menuItem;
  }

  public void setMenuItem(MenuItem menuItem) {
    this.menuItem = menuItem;
  }

  public FoodOrder getFoodOrder() {
    return foodOrder;
  }

  public void setFoodOrder(FoodOrder foodOrder) {
    this.foodOrder = foodOrder;
  }
}
