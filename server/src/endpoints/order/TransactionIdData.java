package endpoints.order;

public class TransactionIdData {
  private Long transactionId;

  public TransactionIdData(Long transactionId){
    this.transactionId = transactionId;
  }

  public Long getTransactionId() {
    return transactionId;
  }
}
