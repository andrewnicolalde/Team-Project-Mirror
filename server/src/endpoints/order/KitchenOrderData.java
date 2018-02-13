package endpoints.order;

import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    orderContents = new ArrayList<>();
  }

  public void addKitchenOrderItemData(MenuItem item, OrderMenuItem orderMenuItem) {
    orderContents.add(new KitchenOrderItemData(item.getMenuItemId(), item.getName(), orderMenuItem.getInstructions()));
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KitchenOrderData that = (KitchenOrderData) o;
    return Objects.equals(orderId, that.orderId);
  }

  @Override
  public int hashCode() {

    return Objects.hash(orderId, orderContents);
  }
}
