package endpoints.order;

public class OrderMenuItemParameters {
  private Long tableNumber;
  private Long menuItemId;
  private String description;

  public OrderMenuItemParameters(Long tableNumber, Long menuItemId, String description) {
    this.tableNumber = tableNumber;
    this.menuItemId = menuItemId;
    this.description = description;
  }

  public Long getTableNumber() {
    return tableNumber;
  }

  public Long getMenuItemId() {
    return menuItemId;
  }

  public String getDescription() {
    return description;
  }
}
