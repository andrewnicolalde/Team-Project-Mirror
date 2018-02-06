package endpoints.order;

public class ChangeOrderStatusParameters {

  private Long tableNumber;
  private String newOrderStatus;

  public ChangeOrderStatusParameters(Long tableNumber, String newOrderStatus) {
    this.tableNumber = tableNumber;
    this.newOrderStatus = newOrderStatus;
  }

  public Long getTableNumber() {
    return tableNumber;
  }

  public String getNewOrderStatus() {
    return newOrderStatus;
  }
}
