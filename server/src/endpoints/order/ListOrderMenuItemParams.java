package endpoints.order;

/**
 * This class coverts JSON into usable Java Objects.
 *
 * @author Marcus Messer
 */
public class ListOrderMenuItemParams {

  private Long orderNumber;

  public ListOrderMenuItemParams(Long orderId) {
    this.orderNumber = orderId;
  }

  public Long getOrderNumber() {
    return orderNumber;
  }
}
