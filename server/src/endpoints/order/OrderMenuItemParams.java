package endpoints.order;

/**
 * This class coverts JSON from the waiter UI to usable Java Objects.
 *
 * @author Marcus Messer
 */
class OrderMenuItemParams {

  private final Long menuItemId;
  private final String instructions;
  private final Long orderId;

  public OrderMenuItemParams(Long menuItemId, String instructions, Long orderId) {
    this.menuItemId = menuItemId;
    this.instructions = instructions;
    this.orderId = orderId;
  }

  public Long getMenuItemId() {
    return menuItemId;
  }

  public String getInstructions() {
    return instructions;
  }

  public Long getorderId() {
    return orderId;
  }
}
