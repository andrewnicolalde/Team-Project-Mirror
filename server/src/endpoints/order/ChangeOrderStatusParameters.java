package endpoints.order;

public class ChangeOrderStatusParameters {

  private Long tableNumber;
  private String newOrderStatus;
  private Long foodOrderId;

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

  public Long getFoodOrderId() {
    return foodOrderId;
  }
}
