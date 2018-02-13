package endpoints.order;

public class ListOrderMenuItemParams {

  private Long orderNumber;

  public ListOrderMenuItemParams(Long orderId) {
    this.orderNumber = orderId;
  }

  public Long getOrderNumber() {
    return orderNumber;
  }
}
