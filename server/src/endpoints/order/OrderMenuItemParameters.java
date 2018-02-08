package endpoints.order;

public class OrderMenuItemParameters {

  private Long menuItemId;
  private String instructions;
  private Long orderId;

  public OrderMenuItemParameters(Long menuItemId, String instructions, Long orderId) {
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

  public Long getOrderId() {
    return orderId;
  }
}
