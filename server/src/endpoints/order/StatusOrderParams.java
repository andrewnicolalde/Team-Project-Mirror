package endpoints.order;

/**
 * Converts JSON from front end to a useful Java Object.
 *
 * @author Roger Milroy
 */
public class StatusOrderParams {

  private int orderStatus;

  public StatusOrderParams(int orderStatus) {
    this.orderStatus = orderStatus;
  }

  public int getOrderStatus() {
    return orderStatus;
  }
}
