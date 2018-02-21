package endpoints.order;

/**
 * This class converts JSON into a useful Java Object
 *
 * @author Marcus Messer
 */
public class OrderIdParams {

  private Long transactionId;

  public OrderIdParams (Long transactionId) {
    this.transactionId = transactionId;
  }

  public Long getTransactionId() {
    return transactionId;
  }
}
