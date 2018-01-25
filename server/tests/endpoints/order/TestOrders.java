package endpoints.order;

import database.Connector;
import database.tables.Category;
import database.tables.Department;
import database.tables.FoodOrder;
import database.tables.Franchise;
import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import database.tables.OrderStatus;
import database.tables.RestaurantTable;
import database.tables.RestaurantTableStaff;
import database.tables.Staff;
import database.tables.TableStatus;
import database.tables.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static endpoints.order.Orders.getOrderMenuItems;
import static org.junit.Assert.assertEquals;

public class TestOrders {

  @Test
  public void testGetOrderMenuItem() {
    assertEquals("Asserts correct JSON is returned from the database",
    "[{\"id\":1,\"name\":\"Taco\",\"category\":\"Main\",\"allergy_info\":\"None\"," +
        "\"description\":\"Some meat in hard shell plus some lettuce\",\"price\":7.99,\"is_vegan\":false," +
        "\"is_vegetarian\":false,\"is_gluten_free\":false,\"picture_src\":\"images/taco.jpg\"},{\"id\":2," +
        "\"name\":\"Pepsi Max\",\"allergy_info\":\"None\",\"category\":\"Drinks\"," +
        "\"description\":\"Coca cola of the diet variety\",\"price\":4.99,\"is_vegan\":true,\"is_vegetarian\":true," +
        "\"is_gluten_free\":true,\"picture_src\":\"images/diet_coke.jpg\"}]",
        getOrderMenuItems(1L, 9L));
  }
}
