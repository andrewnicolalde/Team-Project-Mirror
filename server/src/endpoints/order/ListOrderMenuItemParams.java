package endpoints.order;

/**
 * This class coverts JSON into usable Java Objects.
 *
 * @author Marcus Messer
 */
public class ListOrderMenuItemParams {

  private Long orderId;

  public ListOrderMenuItemParams(Long orderId) {
    this.orderId = orderId;
  }

  public Long getOrderId() {
    return orderId;
  }
}
