package endpoints.notification;

import database.DatabaseManager;
import database.tables.Department;
import database.tables.FoodOrder;
import database.tables.OrderStatus;
import database.tables.StaffNotification;
import endpoints.order.OrderData;
import java.util.Comparator;
import java.util.List;
import javax.persistence.EntityManager;
import util.JsonUtil;

public class OrderNotificationService extends NotificationService implements Runnable {

  private FoodOrder foodOrder;

  OrderNotificationService(FoodOrder foodOrder) {
    this.foodOrder = foodOrder;
  }

  @Override
  public void run() {
    sendNotifications();
  }

  @Override
  void sendNotifications() {
    List<StaffNotification> staffNotifications = getStaffToNotify(foodOrder);
    // test message to verify it works.
    String message = getDataToNotify(foodOrder);
    // send the notifications.
    send(staffNotifications, message);
  }

  /**
   * Helper method that finds the staff that need to be notified about a change of order status.
   *
   * @param foodOrder The FoodOrder that has changed status.
   * @return A List of StaffNotification objects.
   */
  private static List<StaffNotification> getStaffToNotify(FoodOrder foodOrder) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    List<StaffNotification> staffNotifications = null;
    OrderStatus orderStatus = foodOrder.getStatus();
    // If the order has just been cooked we want the relevant waiter.
    if (orderStatus == OrderStatus.COOKING) {

      staffNotifications = entityManager
          .createQuery("from StaffNotification staffNotification "
              + "where staffNotification.staff.department = :department", StaffNotification.class)
          .setParameter("department", Department.KITCHEN).getResultList();
    } else if (orderStatus == OrderStatus.READY_TO_DELIVER | orderStatus == OrderStatus
        .READY_TO_CONFIRM) {
      // get a list of waiters on a table.
      staffNotifications = entityManager
          .createQuery("from StaffNotification staffNotification"
                  + " where staffNotification.staff.department = :department and staffNotification.staff.employeeNumber = :serverNumber",
              StaffNotification.class).setParameter("department", Department.WAITER)
          .setParameter("serverNumber",
              foodOrder.getTransaction().getRestaurantTableStaff().getStaff().getEmployeeNumber())
          .getResultList();
      // If the order has just been confirmed we want to tell the kitchen.
    }
    return staffNotifications;
  }

  /**
   * Helper method that constructs an appropriate data packet to send in the notification.
   *
   * @param foodOrder The FoodOrder that's change of status triggered the notification.
   * @return A String normally JSON to be sent in the notification.
   */
  private static String getDataToNotify(FoodOrder foodOrder) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    OrderStatus orderStatus = foodOrder.getStatus();
    // some default message.
    String message = "{\"hello\":\"world\"}";
    // The order has just been confirmed we send all the orders to update the page.
    if (orderStatus == OrderStatus.COOKING) {
      List<FoodOrder> foodOrders = entityManager.createQuery("from FoodOrder foodOrder "
              + "where foodOrder.status = :orderStatus",
          FoodOrder.class).setParameter("orderStatus", OrderStatus.COOKING)
          .getResultList();
      // We have to add the order in question because the change hasn't been committed yet.
      foodOrders.add(foodOrder);

      foodOrders.sort(Comparator.comparing(FoodOrder::getTimeConfirmed));

      OrderData[] orderData = new OrderData[foodOrders.size()];
      for (int i = 0; i < orderData.length; i++) {
        orderData[i] = new OrderData(foodOrders.get(i));
      }
      message = JsonUtil.getInstance().toJson(orderData);
    } else if (orderStatus == OrderStatus.READY_TO_DELIVER) {
      message = "An Order is Ready to Deliver!";
    } else if (orderStatus == OrderStatus.READY_TO_CONFIRM) {
      message = "A table is ready to confirm their order!";
    }
    return message;
  }

}
