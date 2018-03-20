package endpoints.notification;

import database.tables.Transaction;

public class TransactionNotificationService implements NotificationService, Runnable {

  public TransactionNotificationService(Transaction transaction) {
  }

  @Override
  public void run() {

  }
}
