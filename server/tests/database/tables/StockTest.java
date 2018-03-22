package database.tables;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StockTest {

  private EntityManagerFactory entityManagerFactory = Persistence
      .createEntityManagerFactory("server.database.test");
  private EntityManager entityManager;

  @Before
  public void setUp() {
    entityManager = entityManagerFactory.createEntityManager();
  }

  @After
  public void tearDown() {
    entityManager.close();
  }

  @Test
  public void addStock() {

    entityManager.getTransaction().begin();
    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");
    entityManager.persist(franchise);
    entityManager.getTransaction().commit();

    //Create new Staff member
    entityManager.getTransaction().begin();
    Staff staff = new Staff("John", "Doe", "Password", Department.KITCHEN, franchise);
    entityManager.persist(staff);
    entityManager.getTransaction().commit();

    //Create a new Table
    entityManager.getTransaction().begin();
    RestaurantTable restaurantTable = new RestaurantTable(TableStatus.FILLED, 1,
        franchise);
    entityManager.persist(restaurantTable);
    entityManager.getTransaction().commit();

    //Create new Server
    entityManager.getTransaction().begin();
    RestaurantTableStaff restaurantTableStaff = new RestaurantTableStaff(staff, restaurantTable, );
    entityManager.persist(restaurantTableStaff);
    entityManager.getTransaction().commit();

    //Create new Transaction
    entityManager.getTransaction().begin();
    Transaction transaction = new Transaction(false, 1.00, new Timestamp(1516709651), false,
        restaurantTableStaff);
    entityManager.persist(transaction);
    entityManager.getTransaction().commit();

    //Create new order
    entityManager.getTransaction().begin();
    FoodOrder foodOrder = new FoodOrder(OrderStatus.CANCELLED, new Timestamp(1516709651),
        transaction);
    entityManager.persist(foodOrder);
    entityManager.getTransaction().commit();

    //Create new category
    entityManager.getTransaction().begin();
    Category category = new Category("Food", 1L);
    entityManager.persist(category);
    entityManager.getTransaction().commit();

    //Create new Ingredient
    entityManager.getTransaction().begin();
    Ingredient ingredient = new Ingredient("Lettuce");
    entityManager.persist(ingredient);
    entityManager.getTransaction().commit();

    //Create new menu item
    entityManager.getTransaction().begin();
    Set<Ingredient> ingredients = new HashSet<>();
    ingredients.add(new Ingredient("Beef"));
    ingredients.add(ingredient);
    MenuItem menuItem = new MenuItem("Burger", ingredients,
        "Well it's a burger", 500.00, 1.00, false, false,
        false, "picSrc", category);
    entityManager.persist(menuItem);
    entityManager.getTransaction().commit();

    //Create new menu order item
    entityManager.getTransaction().begin();
    OrderMenuItem orderMenuItem = new OrderMenuItem(menuItem, foodOrder,
        "Special instrcutions");
    entityManager.persist(orderMenuItem);
    entityManager.getTransaction().commit();

    //Create new stock line
    entityManager.getTransaction().begin();
    Stock stock = new Stock(franchise, ingredient, 5);
    entityManager.persist(stock);
    entityManager.getTransaction().commit();

    Stock returnedStock = entityManager.createQuery("from Stock ", Stock.class).getSingleResult();

    assertEquals("Check franchise", franchise.getName(), returnedStock.getFranchise().getName());
    assertEquals("Check ingredient", ingredient.getIngredientId(), returnedStock.getIngredient().getIngredientId());
    assertEquals("Check stock count", returnedStock.getStockCount(), 5);
  }

  @Test
  public void changeStockLevel(){
    entityManager.getTransaction().begin();
    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");
    entityManager.persist(franchise);
    entityManager.getTransaction().commit();

    //Create new Staff member
    entityManager.getTransaction().begin();
    Staff staff = new Staff("John", "Doe", "Password", Department.KITCHEN, franchise);
    entityManager.persist(staff);
    entityManager.getTransaction().commit();

    //Create a new Table
    entityManager.getTransaction().begin();
    RestaurantTable restaurantTable = new RestaurantTable(TableStatus.FILLED, 1,
        franchise);
    entityManager.persist(restaurantTable);
    entityManager.getTransaction().commit();

    //Create new Server
    entityManager.getTransaction().begin();
    RestaurantTableStaff restaurantTableStaff = new RestaurantTableStaff(staff, restaurantTable, );
    entityManager.persist(restaurantTableStaff);
    entityManager.getTransaction().commit();

    //Create new Transaction
    entityManager.getTransaction().begin();
    Transaction transaction = new Transaction(false, 1.00, new Timestamp(1516709651), false,
        restaurantTableStaff);
    entityManager.persist(transaction);
    entityManager.getTransaction().commit();

    //Create new order
    entityManager.getTransaction().begin();
    FoodOrder foodOrder = new FoodOrder(OrderStatus.CANCELLED, new Timestamp(1516709651),
        transaction);
    entityManager.persist(foodOrder);
    entityManager.getTransaction().commit();

    //Create new category
    entityManager.getTransaction().begin();
    Category category = new Category("Food", 1L);
    entityManager.persist(category);
    entityManager.getTransaction().commit();

    //Create new Ingredient
    entityManager.getTransaction().begin();
    Ingredient ingredient = new Ingredient("Lettuce");
    entityManager.persist(ingredient);
    entityManager.getTransaction().commit();

    //Create new menu item
    entityManager.getTransaction().begin();
    Set<Ingredient> ingredients = new HashSet<>();
    ingredients.add(new Ingredient("Beef"));
    ingredients.add(ingredient);
    MenuItem menuItem = new MenuItem("Burger", ingredients,
        "Well it's a burger", 500.00, 1.00, false, false,
        false, "picSrc", category);
    entityManager.persist(menuItem);
    entityManager.getTransaction().commit();

    //Create new menu order item
    entityManager.getTransaction().begin();
    OrderMenuItem orderMenuItem = new OrderMenuItem(menuItem, foodOrder,
        "Special instrcutions");
    entityManager.persist(orderMenuItem);
    entityManager.getTransaction().commit();

    //Create new stock line
    entityManager.getTransaction().begin();
    Stock stock = new Stock(franchise, ingredient, 5);
    entityManager.persist(stock);
    entityManager.getTransaction().commit();

    Stock returnedStock = entityManager.createQuery("from Stock ", Stock.class).getSingleResult();
    entityManager.getTransaction().begin();
    returnedStock.setStockCount(3);
    entityManager.getTransaction().commit();

    returnedStock = entityManager.createQuery("from Stock ", Stock.class).getSingleResult();
    assertEquals("Stock count change", 3, returnedStock.getStockCount());
  }
}
