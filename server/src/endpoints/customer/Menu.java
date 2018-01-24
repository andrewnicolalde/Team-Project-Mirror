package endpoints.customer;

public class Menu {

  /**
   * Gets the full menu from the database and returns it in JSON.
   *
   * @return The menu in JSON as a string.
   */
  public static String getMenu() {
    return "[{\"id\":1,\"name\":\"Taco\",\"category\":\"Main\",\"allergy_info\":\"None\"," +
        "\"description\":\"Some meat in hard shell plus some lettuce\",\"price\":7.99,\"is_vegan\":false," +
        "\"is_vegetarian\":false,\"is_gluten_free\":false,\"picture_src\":\"images/taco.jpg\"},{\"id\":2,\"name\":\"" +
        "Pepsi Max\",\"allergy_info\":\"None\",\"category\":\"Drinks\",\"description\":" +
        "\"Coca cola of the diet variety\",\"price\":4.99,\"is_vegan\":true,\"is_vegetarian\":true," +
        "\"is_gluten_free\":true,\"picture_src\":\"images/diet_coke.jpg\"}]";
  }
}
