package endpoints.notification;

import database.tables.FoodOrder;

public class OrderNotificationService implements NotificationService, Runnable{

  public OrderNotificationService(FoodOrder foodOrder) {
  }

  @Override
  public void run() {

  }
}
