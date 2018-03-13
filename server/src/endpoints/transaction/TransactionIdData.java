package endpoints.transaction;

class TransactionIdData {

  private final Long transactionId;

  TransactionIdData(Long transactionId) {
    this.transactionId = transactionId;
  }

  public Long getTransactionId() {
    return transactionId;
  }
}
