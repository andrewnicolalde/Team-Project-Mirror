package endpoints.order;

public class OrderMenuItemParameters {

  private Long menuItemId;
  private String instructions;
  private Long orderNumber;

  public OrderMenuItemParameters(Long menuItemId, String instructions, Long orderNumber) {
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
