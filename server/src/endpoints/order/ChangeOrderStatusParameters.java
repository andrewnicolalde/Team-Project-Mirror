package endpoints.order;

public class ChangeOrderStatusParameters {

  private String newOrderStatus;
  private Long orderNumber;

  public ChangeOrderStatusParameters(Long orderNumber, String newOrderStatus) {
    this.newOrderStatus = newOrderStatus;
    this.orderNumber = orderNumber;
  }

  public String getNewOrderStatus() {
    return newOrderStatus;
  }

  public Long getFoodOrderId() {
    return orderNumber;
  }
}
