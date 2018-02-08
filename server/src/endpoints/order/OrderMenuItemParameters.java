package endpoints.order;

public class OrderMenuItemParameters {

  private Long tableNumber;
  private Long menuItemId;
  private String instructions;

  public OrderMenuItemParameters(Long tableNumber, Long menuItemId, String instructions) {
    this.tableNumber = tableNumber;
    this.menuItemId = menuItemId;
    this.instructions = instructions;
  }

  public Long getTableNumber() {
    return tableNumber;
  }

  public Long getMenuItemId() {
    return menuItemId;
  }

  public String getInstructions() {
    return instructions;
  }
}
