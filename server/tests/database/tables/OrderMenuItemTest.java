package database.tables;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderMenuItemTest {

  private EntityManagerFactory entityManagerFactory;

  @Before
  public void setUp() {
    //Create link to the database
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database.test");
  }

  @After
  public void tearDown() {
    //Close link to the database
    entityManagerFactory.close();
  }

  @Test
  public void createTransactionTest() {

    EntityManager entityManager;
    //Create new Franchise
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");
    entityManager.persist(franchise);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new Staff member
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Staff staff = new Staff("John", "Doe", "Password", Department.KITCHEN, franchise);
    entityManager.persist(staff);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create a new Table
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    RestaurantTable restaurantTable = new RestaurantTable(TableStatus.FILLED, 1,
        franchise);
    entityManager.persist(restaurantTable);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new Server
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    RestaurantTableStaff restaurantTableStaff = new RestaurantTableStaff(staff, restaurantTable, true);
    entityManager.persist(restaurantTableStaff);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new Transaction
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Transaction transaction = new Transaction(false, 1.00, new Timestamp(1516709651), false,
        restaurantTableStaff);
    entityManager.persist(transaction);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new order
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    FoodOrder foodOrder = new FoodOrder(OrderStatus.CANCELLED, new Timestamp(1516709651),
        transaction);
    entityManager.persist(foodOrder);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new category
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Category category = new Category("Food", 1L);
    entityManager.persist(category);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new menu item
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Set<Ingredient> ingredients = new HashSet<>();
    ingredients.add(new Ingredient("Beef"));
    MenuItem menuItem = new MenuItem("Burger", ingredients,
        "Well it's a burger", 500.00, 1.00, false, false,
        false, "picSrc", category);
    entityManager.persist(menuItem);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new menu order item
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    OrderMenuItem orderMenuItem = new OrderMenuItem(menuItem, foodOrder,
        "Special instrcutions");
    entityManager.persist(orderMenuItem);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Get menu order item from database.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<OrderMenuItem> result = entityManager.createQuery("from OrderMenuItem ",
        OrderMenuItem.class).getResultList();

    for (OrderMenuItem item : result) {
      assertEquals("Check id", item.getOrderMenuItemId(), orderMenuItem.getOrderMenuItemId());
      assertEquals("Check menu item", item.getMenuItem().getMenuItemId(),
          menuItem.getMenuItemId());
      assertEquals("Check food order", item.getFoodOrder().getOrderId(),
          foodOrder.getOrderId());
    }

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
