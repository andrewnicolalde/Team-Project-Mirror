package endpoints.notification;

import database.tables.PushSubscription;
import spark.Request;
import spark.Response;
import util.JsonUtil;

/**
 * @author Roger Milroy
 */
public class Notifications {

  public static String saveSubscription(Request request, Response response) {
    PushSubscription subscription = JsonUtil.getInstance().fromJson(request.body(), PushSubscription.class);
    System.out.println(subscription);
    return "something";
  }
}
