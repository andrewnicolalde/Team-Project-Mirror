package endpoints.order;

/**
 * Converts JSON from front end to a useful Java Object.
 *
 * @author Roger Milroy
 */
public class StatusOrderParams {

  private OrderStatus orderStatus;

  public StatusOrderParams(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }
}
