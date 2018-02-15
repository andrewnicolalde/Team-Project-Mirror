package endpoints.order;

/**
 * This class converts JSON from the Waiter Javascript into usable Java Objects.
 *
 * @author Marcus Messer
 */
public class OrderRequestParams {

  private int tableNumber;

  public OrderRequestParams(int tableNumber) {
    this.tableNumber = tableNumber;
  }

  public int getTableNumber() {
    return tableNumber;
  }
}
