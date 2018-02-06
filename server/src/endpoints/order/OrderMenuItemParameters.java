package endpoints.order;

public class OrderMenuItemParameters {

  private Long tableNumber;
  private Long menuItemId;
  private String requirements;

  public OrderMenuItemParameters(Long tableNumber, Long menuItemId, String requirements) {
    this.tableNumber = tableNumber;
    this.menuItemId = menuItemId;
    this.requirements = requirements;
  }

  public Long getTableNumber() {
    return tableNumber;
  }

  public Long getMenuItemId() {
    return menuItemId;
  }

  public String getRequirements() {
    return requirements;
  }
}
