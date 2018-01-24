package endpoints.order;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class Orders {
  private static final Gson GSON = new Gson();

  /**
   * Returns an order as JSON.
   * JSON input:
   *     tableNumber: an integer representing the table number
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string containing JSON which holds the current order.
   */
  public static String getOrder(Request request, Response response) {
    OrderRequestParameters or = GSON.fromJson(request.body(), OrderRequestParameters.class);
    return getOrderMenuItems(or.getTableNumber());
  }


  private static String getOrderMenuItems(Long tableNumber) {
    // TODO: Search database for order contents
    return "[{\"id\":1,\"name\":\"Taco\",\"category\":\"Main\",\"allergy_info\":\"None\"," +
        "\"description\":\"Some meat in hard shell plus some lettuce\",\"price\":7.99,\"is_vegan\":false," +
        "\"is_vegetarian\":false,\"is_gluten_free\":false,\"picture_src\":\"images/taco.jpg\"},{\"id\":2," +
        "\"name\":\"Pepsi Max\",\"allergy_info\":\"None\",\"category\":\"Drinks\"," +
        "\"description\":\"Coca cola of the diet variety\",\"price\":4.99,\"is_vegan\":true,\"is_vegetarian\":true," +
        "\"is_gluten_free\":true,\"picture_src\":\"images/diet_coke.jpg\"}]";
  }

  /**
   * Adds an orderMenuItem to an order.
   * JSON input:
   *     tableNumber: An integer representing the table number
   *     menuItemId: An integer representing the id of the MenuItem to add to the order.
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String addOrderMenuItem(Request request, Response response) {
    OrderMenuItemParameters omi = GSON.fromJson(request.body(), OrderMenuItemParameters.class);
    return "success";
  }

  /**
   * Changes the order status
   * JSON input:
   *     tableNumber: An integer representing the table number.
   *     newOrderStatus: A string representing the new order status. This can be CANCELLED,
   ORDERING, READY_TO_CONFIRM, COOKING, READY_TO_DELIVER or DELIVERED.
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String changeOrderStatus(Request request, Response response) {
    ChangeOrderStatusParameters cos = GSON.fromJson(request.body(), ChangeOrderStatusParameters.class);
    return "success";
  }

  /**
   * Removes an item from an order
   * JSON input:
   *     tableNumber: An integer representing the table number.
   *     menuItemId: An integer representing the id of the MenuItem to remove from the order.
   * @param request A HTTP request object.
   * @param response A HTTP response object.
   * @return A string saying either "success" or "failed"
   */
  public static String removeOrderMenuItem(Request request, Response response) {
    OrderMenuItemParameters omi = GSON.fromJson(request.body(), OrderMenuItemParameters.class);
    return "success";
  }
}
