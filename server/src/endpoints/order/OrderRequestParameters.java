package endpoints.order;

public class OrderRequestParameters {

  private int tableNumber;

  public OrderRequestParameters(int tableNumber) {
    this.tableNumber = tableNumber;
  }

  public int getTableNumber() {
    return tableNumber;
  }
}
