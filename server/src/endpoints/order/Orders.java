package endpoints.order;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class Orders {
  private static final Gson GSON = new Gson();

  public static String getOrder(Request request, Response response) {
    OrderRequestParameters or = GSON.fromJson(request.body(), OrderRequestParameters.class);
    return getOrderMenuItems(or.getTableNumber());
  }

  public static String getOrderMenuItems(Long tableNumber) {
    // TODO: Search database for order contents
    return "[{\"id\":1,\"name\":\"Taco\",\"category\":\"Main\",\"allergy_info\":\"None\"," +
        "\"description\":\"Some meat in hard shell plus some lettuce\",\"price\":7.99,\"is_vegan\":false," +
        "\"is_vegetarian\":false,\"is_gluten_free\":false,\"picture_src\":\"images/taco.jpg\"},{\"id\":2," +
        "\"name\":\"Pepsi Max\",\"allergy_info\":\"None\",\"category\":\"Drinks\"," +
        "\"description\":\"Coca cola of the diet variety\",\"price\":4.99,\"is_vegan\":true,\"is_vegetarian\":true," +
        "\"is_gluten_free\":true,\"picture_src\":\"images/diet_coke.jpg\"}]";
  }

  public static String addOrderMenuItem(Request request, Response response) {
    OrderMenuItemParameters omi = GSON.fromJson(request.body(), OrderMenuItemParameters.class);
    return "success";
  }

  public static String changeOrderStatus(Request request, Response response) {
    ChangeOrderStatusParameters cos = GSON.fromJson(request.body(), ChangeOrderStatusParameters.class);
    return "success";
  }

  public static String removeOrderMenuItem(Request request, Response response) {
    OrderMenuItemParameters omi = GSON.fromJson(request.body(), OrderMenuItemParameters.class);
    return "success";
  }
}
