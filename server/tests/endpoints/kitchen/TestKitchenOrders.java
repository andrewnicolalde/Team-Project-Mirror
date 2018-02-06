package endpoints.kitchen;

import static org.junit.Assert.assertEquals;

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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Request;

public class TestKitchenOrders {

  private EntityManagerFactory entityManagerFactory;

  @Before
  public void setUp() {
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database.dev");
  }

  @After
  public void tearDown() {
    entityManagerFactory.close();
  }

  @Test
  public void testGetCookingOrders() {
//    EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//    entityManager.getTransaction().begin();
//    Franchise franchise = new Franchise("London", "1 London Way",
//        "0123456789", "Password");
//    Staff staff = new Staff("Password", Department.KITCHEN, franchise);
//    Category categoryFood = new Category("Food");
//    RestaurantTable restaurantTable = new RestaurantTable(TableStatus.FILLED, 1,
//        franchise);
//    RestaurantTableStaff restaurantTableStaff = new RestaurantTableStaff(staff, restaurantTable);
//    Transaction transaction = new Transaction(false, 1.00, new Timestamp(1516709651),
//        restaurantTableStaff);
//    MenuItem menuItemBurrito = new MenuItem("Burrito", "None", "A tasty burrito", 4.00, false, false,
//        false, "picture.jpg", categoryFood);
//    MenuItem menuItemTaco = new MenuItem("Taco", "None", "Some tasty Taco's", 3.00, false, false,
//        false, "picture.jpg", categoryFood);
//    MenuItem menuItemEnchiladas = new MenuItem("Enchiladas", "None", "The best cheesy enchilada's", 3.00, false, false,
//        false, "picture.jpg", categoryFood);
//    FoodOrder cookingOrder = new FoodOrder(OrderStatus.COOKING, new Timestamp(1516709651), transaction);
//    FoodOrder cancelledOrder = new FoodOrder(OrderStatus.CANCELLED, new Timestamp(1516709656), transaction);
//
//    OrderMenuItem cookingItem1 = new OrderMenuItem( menuItemBurrito, cookingOrder,  "none");
//    OrderMenuItem cookingItem2 = new OrderMenuItem( menuItemBurrito, cookingOrder,  "Extra Spicy");
//    OrderMenuItem cancelledItem1 = new OrderMenuItem( menuItemTaco, cancelledOrder,  "No Cheese");
//
//
//    entityManager.persist(franchise);
//    entityManager.persist(staff);
//    entityManager.persist(categoryFood);
//    entityManager.persist(restaurantTable);
//    entityManager.persist(restaurantTableStaff);
//    entityManager.persist(transaction);
//    entityManager.persist(menuItemBurrito);
//    entityManager.persist(menuItemEnchiladas);
//    entityManager.persist(menuItemTaco);
//    entityManager.persist(cookingOrder);
//    entityManager.persist(cancelledOrder);
//    entityManager.persist(cookingItem1);
//    entityManager.persist(cookingItem2);
//    entityManager.persist(cancelledItem1);
//
//    entityManager.getTransaction().commit(); commented out due to using dev database. No need to keep adding entities.
//
//    String expected = "[{\"orderId\":1,\"orderContents\":[{\"itemId\":4,\"itemName\":\"Burrito\",\"instructions\":\"none\"},{\"itemId\":4,\"itemName\":\"Burrito\",\"instructions\":\"Extra Spicy\"}]}]";
//    String actual = KitchenOrder.getCookingOrders(req, res);
//    System.out.println(actual);
//    assertEquals("check getCookingOrders", expected, actual);

  }
}
