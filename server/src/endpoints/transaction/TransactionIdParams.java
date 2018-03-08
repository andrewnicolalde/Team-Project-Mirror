package endpoints.transaction;

/**
 * This class converts JSON into a useful Java Object
 *
 * @author Marcus Messer
 */
public class TransactionIdParams {

  private Long transactionId;

  public TransactionIdParams(Long transactionId) {
    this.transactionId = transactionId;
  }

  public Long getTransactionId() {
    return transactionId;
  }
}
