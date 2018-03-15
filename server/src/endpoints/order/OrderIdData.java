package endpoints.order;

class OrderIdData {

  private final Long orderId;

  OrderIdData(Long orderId) {
    this.orderId = orderId;
  }

  public Long getOrderId() {
    return orderId;
  }
}
