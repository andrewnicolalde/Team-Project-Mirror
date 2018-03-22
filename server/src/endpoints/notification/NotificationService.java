package endpoints.notification;

import database.tables.StaffNotification;
import java.io.UnsupportedEncodingException;
import java.util.List;

class NotificationService {

  void send(List<StaffNotification> staffNotifications, String message) {
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

  void sendNotifications() {
  }

}
