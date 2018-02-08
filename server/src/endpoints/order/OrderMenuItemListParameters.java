package endpoints.order;

public class OrderMenuItemListParameters {

  private Long orderNumber;

  public OrderMenuItemListParameters(Long orderId) {
    this.orderNumber = orderId;
  }

  public Long getOrderNumber() {
    return orderNumber;
  }
}
