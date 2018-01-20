package endpoints.order;

public class OrderRequestParameters {
  private Long orderNumber;

  public OrderRequestParameters(Long orderNumber) {
    this.orderNumber = orderNumber;
  }

  public Long getOrderNumber() {
    return orderNumber;
  }
}
