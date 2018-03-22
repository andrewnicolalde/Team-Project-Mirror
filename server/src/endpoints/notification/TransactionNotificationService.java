package endpoints.notification;

import database.DatabaseManager;
import database.tables.Department;
import database.tables.StaffNotification;
import database.tables.Transaction;
import java.util.List;
import javax.persistence.EntityManager;

public class TransactionNotificationService extends NotificationService implements Runnable {

  private Transaction transaction;

  TransactionNotificationService(Transaction transaction) {
    this.transaction = transaction;
  }

  private static String getDataToNotify(Transaction transaction) {
    String message = "";
    if (transaction.getIsPaid()) {
      message = "Table " + transaction.getRestaurantTableStaff().getRestaurantTable()
          .getTableNumber() + " "
          + "has PAID!";
    }
    return message;
  }

  private static List<StaffNotification> getStaffToNotify(Transaction transaction) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<StaffNotification> staffNotifications = null;

    if (transaction.getIsPaid()) {
      staffNotifications = entityManager.createQuery("from StaffNotification staffNotification"
          + " where staffNotification.staff.department = :department and "
          + "staffNotification.staff = :waiter", StaffNotification.class)
          .setParameter("department", Department.WAITER).setParameter("waiter",
              transaction.getRestaurantTableStaff().getStaff()).getResultList();
    }
    entityManager.close();

    return staffNotifications;
  }

  @Override
  public void run() {
    sendNotifications();
  }

  @Override
  void sendNotifications() {
    List<StaffNotification> staffNotifications = getStaffToNotify(transaction);
    // test message to verify it works.
    String message = getDataToNotify(transaction);
    // send the notifications.
    send(staffNotifications, message);
  }
}
