package endpoints.order;

/**
 * This class coverts JSON into usable Java Objects.
 *
 * @author Marcus Messer
 */
class ChangeStatusParams {

  private final String newOrderStatus;
  private final Long orderId;

  public ChangeStatusParams(Long orderId, String newOrderStatus) {
    this.newOrderStatus = newOrderStatus;
    this.orderId = orderId;
  }

  public String getNewOrderStatus() {
    return newOrderStatus;
  }

  public Long getFoodOrderId() {
    return orderId;
  }
}
