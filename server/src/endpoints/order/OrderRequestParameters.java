package endpoints.order;

public class OrderRequestParameters {
  private Long tableNumber;

  public OrderRequestParameters(Long tableNumber) {
    this.tableNumber = tableNumber;
  }

  public Long getTableNumber() {
    return tableNumber;
  }
}
