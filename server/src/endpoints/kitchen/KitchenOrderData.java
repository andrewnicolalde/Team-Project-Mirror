package endpoints.kitchen;

import java.util.List;

/**
 * A wrapper class to provide an easily serializable format to JSON.
 *
 * @author Roger Milroy
 */
public class KitchenOrderData {

  private Long orderId;
  private List<KitchenOrderItemData> orderContents;

  /**
   * Constructs a KitchenOrderData.
   * @param orderId the ID of the order it is being created from.
   */
  public KitchenOrderData(Long orderId) {
    this.orderId = orderId;
  }

}
