package endpoints.order;

/**
 * Wrapper class for OrderMenuItem and MenuItem to be more easily serializable into JSON.
 *
 * @author Roger Milroy
 */
public class KitchenOrderItemData {

  private Long itemId;
  private String itemName;
  private String instructions;

  public KitchenOrderItemData(Long itemId, String itemName, String instructions) {
    this.itemId = itemId;
    this.itemName = itemName;
    this.instructions = instructions;
  }
}
