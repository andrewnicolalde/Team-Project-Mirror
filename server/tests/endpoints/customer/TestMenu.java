package endpoints.customer;

import static endpoints.customer.Menu.getMenu;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestMenu {

  @Test
  public void testGetMenu() {
    assertEquals("Asserts the correct json is returned from the database",
        "{items:[Beef Burger, Chicken Salad, Water, Diet Coke, Fish and Chips]}",
        getMenu());
  }
}
