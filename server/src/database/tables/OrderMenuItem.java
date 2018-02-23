package database.tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class maps the order menu item table.
 *
 * @author Marcus Messer
 */
@Entity
@Table(name = "ORDER_MENU_ITEM")
public class OrderMenuItem {

  /**
   * This field is the primary key of the entity.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long orderMenuItemId;

  /**
   * This field shows what menu item is related to the order.
   */
  @ManyToOne
  @JoinColumn(name = "menuItemId")
  private MenuItem menuItem;

  /**
   * This field shows what order the menu item is related to.
   */
  @ManyToOne
  @JoinColumn(name = "orderId")
  private FoodOrder foodOrder;

  /**
   * This field shows the special instructions that the customer enters.
   */
  private String instructions;

  /**
   * This empty constructor is used by Hibernate.
   */
  public OrderMenuItem() {
    //Empty Body
  }

  /**
   * This constructor create new entities.
   *
   * @param menuItem The menu item that the order is related to do.
   * @param foodOrder The order that the menu item is related to.
   * @param instructions The instructions that the customers enter.
   */
  public OrderMenuItem(MenuItem menuItem, FoodOrder foodOrder, String instructions) {
    this.menuItem = menuItem;
    this.foodOrder = foodOrder;
    this.instructions = instructions;
  }

  public Long getOrderMenuItemId() {
    return orderMenuItemId;
  }

  public void setOrderMenuItemId(Long orderMenuItemId) {
    this.orderMenuItemId = orderMenuItemId;
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

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  @Override
  public String toString() {
    return "OrderMenuItem{" +
        "orderMenuItemId=" + orderMenuItemId +
        ", menuItem=" + menuItem +
        ", foodOrder=" + foodOrder +
        ", instructions='" + instructions + '\'' +
        '}';
  }
}
