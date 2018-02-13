package endpoints.order;

public class ChangeStatusParams {

  private String newOrderStatus;
  private Long orderNumber;

  public ChangeStatusParams(Long orderNumber, String newOrderStatus) {
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
