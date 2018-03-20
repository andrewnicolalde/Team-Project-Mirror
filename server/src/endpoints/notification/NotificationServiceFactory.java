package endpoints.notification;

import database.tables.FoodOrder;
import database.tables.RestaurantTable;
import database.tables.Transaction;

public class NotificationServiceFactory {

  public static Thread getNotificationService(Object input)
      throws IncorrectTypeException {
    if (input instanceof FoodOrder) {
      return new Thread(new OrderNotificationService((FoodOrder) input));
    } else if (input instanceof Transaction) {
      return new Thread(new TransactionNotificationService((Transaction) input));
    } else if (input instanceof RestaurantTable) {
      return new Thread(new TableNotificationService((RestaurantTable) input));
    } else {
      throw new IncorrectTypeException();
    }
  }

}
