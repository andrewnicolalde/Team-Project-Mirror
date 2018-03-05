package endpoints.notification;

import database.DatabaseManager;
import database.tables.PushSubscription;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;
import javax.persistence.EntityManager;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.jose4j.lang.JoseException;
import spark.Request;
import spark.Response;
import util.JsonUtil;

/**
 * Class containing most server side logic for sending push notifications.
 * @author Roger Milroy
 */
public class Notifications {

  /**
   * Route to save PushSubscriptions from the client.
   * @param request the Http Request object
   * @param response the Http Response object
   * @return Success.
   */
  public static String saveSubscription(Request request, Response response){
    PushSubscription subscription = JsonUtil.getInstance().fromJson(request.body(), PushSubscription.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(subscription);
    entityManager.getTransaction().commit();
    entityManager.close();

    return "success";
  }

  /**
   * Sends a push message containing a byte array to the client via a PushService.
   * @param pushSubscription The PushSubscription object from the client.
   * @param payload The byte array payload to send in the notification
   */
  public static void sendPushMessage(PushSubscription pushSubscription, byte[] payload) {
    Subscription subscription = new Subscription(pushSubscription);
    PushService pushService = new PushService();
    Notification notification;
    try {
      notification = new Notification(
          subscription.getEndpoint(),
          subscription.getUserPublicKey(),
          subscription.getAuthAsBytes(),
          payload
          );
      pushService.setPrivateKey("qiaDwfworQzdvrTXW0-mXLYH2Cs4qmkAWABmUHfw32k");
      pushService.setPublicKey("BIz9luhpKgx76RcIhqU4fmdIC1ve7fT5gm2Y632w_lsd_od2B87XschASGbi7EfgTIWpBAPKh2IWTOMt1Gux7tA");
      pushService.send(notification);
    } catch (IOException | GeneralSecurityException | ExecutionException | JoseException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
