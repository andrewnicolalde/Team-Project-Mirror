package endpoints.order;

import database.tables.OrderMenuItem;
import java.util.List;

public class OrderWithContents {
  private Long orderId;
  private List<OrderItemsData> orderContents;
  private String status;

  public OrderWithContents(Long orderId, String status, List<OrderItemsData> orderContents) {
    this.orderId = orderId;
    this.orderContents = orderContents;
    this.status = status;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public List<OrderItemsData> getOrderContents() {
    return orderContents;
  }

  public void setOrderContents(List<OrderItemsData> orderContents) {
    this.orderContents = orderContents;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
