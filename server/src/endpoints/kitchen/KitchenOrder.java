package endpoints.kitchen;

public class KitchenOrder {

  public static String getOrder() {
    return "[{\"orderId\":4,\"orderContents\":" +
        "[{\"orderMenuItemId\":1,\"itemName\":\"Taco\",\"requirements\":\"Extra spicy\"}," +
        "{\"orderMenuItemId\":3,\"itemName\":\"Burrito\",\"requirements\":\"\"}]}," +
        "{\"orderId\":5,\"orderContents\":[{\"orderMenuItemId\":5,\"itemName\":\"taco\",\"requirements\":\"None\"}]}]";
  }


}
