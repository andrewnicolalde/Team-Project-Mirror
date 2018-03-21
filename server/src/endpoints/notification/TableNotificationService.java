package endpoints.notification;

import database.DatabaseManager;
import database.tables.Department;
import database.tables.RestaurantTable;
import database.tables.RestaurantTableStaff;
import database.tables.StaffNotification;
import database.tables.TableStatus;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.persistence.EntityManager;

public class TableNotificationService implements NotificationService, Runnable{

  private RestaurantTable table;

  public TableNotificationService(RestaurantTable table) {
    this.table = table;
  }

  private static List<StaffNotification> getStaffToNotify(RestaurantTable table) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<StaffNotification> staffNotifications = null;

    if (table.getStatus() == TableStatus.NEEDS_HELP) {
      RestaurantTableStaff tableStaff = entityManager.createQuery("from RestaurantTableStaff "
          + "tableStaff where tableStaff.restaurantTable = :table", RestaurantTableStaff.class)
          .setParameter("table", table).getSingleResult();
      staffNotifications = entityManager.createQuery("from StaffNotification staffNotification"
          + " where staffNotification.staff.department = :department and "
          + "staffNotification.staff = :waiter", StaffNotification.class)
          .setParameter("department", Department.WAITER).setParameter("waiter",
              tableStaff.getStaff()).getResultList();
    }

    return staffNotifications;
  }

  private static String getDataToNotify(RestaurantTable table) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    String message = "";
    if (table.getStatus() == TableStatus.NEEDS_HELP) {
      message = "Table " + table
          .getTableNumber() + " "
          + "needs "
          + "assistance!";
    }

    return message;
  }

  @Override
  public void run() {
    sendNotifications();
  }

  @Override
  public void sendNotifications() {
    List<StaffNotification> staffNotifications = getStaffToNotify(table);
    // test message to verify it works.
    String message = getDataToNotify(table);
    // send the notifications.
    if (staffNotifications != null) {
      for (StaffNotification n : staffNotifications) {
        try {
          NotificationEndpoint.sendPushMessage(n.getPushSubscription(), message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
