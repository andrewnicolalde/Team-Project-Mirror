package endpoints.customer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static endpoints.customer.Menu.getMenu;

public class TestMenu {
  @Test
  public void testGetMenu() {
    assertEquals("Asserts the correct json is returned from the database",
            "[{\"id\":1,\"name\":\"Taco\",\"category\":\"Main\",\"allergy_info\":\"None\"," +
                "\"description\":\"Some meat in hard shell plus some lettuce\",\"price\":7.99,\"is_vegan\":false," +
                "\"is_vegetarian\":false,\"is_gluten_free\":false,\"picture_src\":\"images/taco.jpg\"},{\"id\":2," +
                "\"name\":\"Pepsi Max\",\"allergy_info\":\"None\",\"category\":\"Drinks\"," +
                "\"description\":\"Coca cola of the diet variety\",\"price\":4.99,\"is_vegan\":true," +
                "\"is_vegetarian\":true,\"is_gluten_free\":true,\"picture_src\":\"images/diet_coke.jpg\"}]",
            getMenu());
  }
}
