package endpoints.notification;

import database.tables.RestaurantTable;

public class TableNotificationService implements NotificationService, Runnable{

  private RestaurantTable table;

  public TableNotificationService(RestaurantTable table) {
    this.table = table;
  }

  @Override
  public void run() {

  }

  @Override
  public void sendNotifications() {

  }
}
