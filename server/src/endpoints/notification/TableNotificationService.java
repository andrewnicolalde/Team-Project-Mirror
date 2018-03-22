package endpoints.notification;

import database.DatabaseManager;
import database.tables.Department;
import database.tables.RestaurantTable;
import database.tables.RestaurantTableStaff;
import database.tables.StaffNotification;
import database.tables.TableStatus;
import java.util.List;
import javax.persistence.EntityManager;

public class TableNotificationService extends NotificationService implements Runnable {

  private RestaurantTable table;

  TableNotificationService(RestaurantTable table) {
    this.table = table;
  }

  private static List<StaffNotification> getStaffToNotify(RestaurantTable table) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<StaffNotification> staffNotifications = null;
    TableStatus tableStatus = table.getStatus();

    if (tableStatus == TableStatus.NEEDS_HELP | tableStatus == TableStatus.NEEDS_CLEANING) {
      RestaurantTableStaff tableStaff = entityManager.createQuery("from RestaurantTableStaff "
          + "tableStaff where tableStaff.restaurantTable = :table", RestaurantTableStaff.class)
          .setParameter("table", table).getSingleResult();
      staffNotifications = entityManager.createQuery("from StaffNotification staffNotification"
          + " where staffNotification.staff.department = :department and "
          + "staffNotification.staff = :waiter", StaffNotification.class)
          .setParameter("department", Department.WAITER).setParameter("waiter",
              tableStaff.getStaff()).getResultList();
    } else {
      staffNotifications = entityManager.createQuery("from StaffNotification staffNotification "
          + "where staffNotification.staff.department = :department", StaffNotification.class)
          .setParameter("department", Department.WAITER).getResultList();
    }

    entityManager.close();
    return staffNotifications;
  }

  private static String getDataToNotify(RestaurantTable table) {
    String message = "A table status has changed";
    if (table.getStatus() == TableStatus.NEEDS_HELP) {
      message = "Table " + table.getTableNumber() + " needs assistance!";
    } else if (table.getStatus() == TableStatus.NEEDS_CLEANING) {
      message = "Table " + table.getTableNumber() + " needs cleaning.";
    }
    return message;
  }

  @Override
  public void run() {
    sendNotifications();
  }

  @Override
  void sendNotifications() {
    List<StaffNotification> staffNotifications = getStaffToNotify(table);
    // test message to verify it works.
    String message = getDataToNotify(table);
    // send the notifications.
    send(staffNotifications, message);
  }
}
