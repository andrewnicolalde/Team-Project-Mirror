package endpoints.order;

public class RemoveOrderMenuItemParams {
  private Long orderMenuItemId;

  public RemoveOrderMenuItemParams(Long orderMenuItemId) {
    this.orderMenuItemId = orderMenuItemId;
  }

  public Long getOrderMenuItemId() {
    return orderMenuItemId;
  }

  public void setOrderMenuItemId(Long orderMenuItemId) {
    this.orderMenuItemId = orderMenuItemId;
  }
}
