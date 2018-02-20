package endpoints.order;

/**
 * This class coverts JSON from the waiter UI to usable Java Objects.
 *
 * @author Marcus Messer
 */
public class OrderMenuItemParams {

  private Long menuItemId;
  private String instructions;
  private Long orderNumber;

  public OrderMenuItemParams(Long menuItemId, String instructions, Long orderNumber) {
    this.menuItemId = menuItemId;
    this.instructions = instructions;
    this.orderNumber = orderNumber;
  }

  public Long getMenuItemId() {
    return menuItemId;
  }

  public String getInstructions() {
    return instructions;
  }

  public Long getOrderNumber() {
    return orderNumber;
  }
}
