package endpoints.order;

public class OrderMenuItemParameters {
  private Long tableNumber;
  private Long menuItemId;

  public OrderMenuItemParameters(Long tableNumber, Long menuItemId) {
    this.tableNumber = tableNumber;
    this.menuItemId = menuItemId;
  }

  public Long getTableNumber() {
    return tableNumber;
  }

  public Long getMenuItemId() {
    return menuItemId;
  }
}
