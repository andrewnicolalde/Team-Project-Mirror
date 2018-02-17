package endpoints.order;

/**
 * This class converts JSON from the Waiter Javascript into usable Java Objects.
 *
 * @author Marcus Messer
 */
public class TableOrderParams {

  private int tableNumber;

  public TableOrderParams(int tableNumber) {
    this.tableNumber = tableNumber;
  }

  public int getTableNumber() {
    return tableNumber;
  }
}
