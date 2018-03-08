package endpoints.order;

import database.tables.OrderMenuItem;
import java.util.List;

public class OrderWithContents {
  private Long orderId;
  private List<OrderMenuItem> orderContents;

  public OrderWithContents(Long orderId, List<OrderMenuItem> orderContents) {
    this.orderId = orderId;
    this.orderContents = orderContents;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public List<OrderMenuItem> getOrderContents() {
    return orderContents;
  }

  public void setOrderContents(List<OrderMenuItem> orderContents) {
    this.orderContents = orderContents;
  }
}
