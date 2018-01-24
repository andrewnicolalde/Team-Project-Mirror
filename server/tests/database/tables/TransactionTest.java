package database.tables;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TransactionTest {
  private EntityManagerFactory entityManagerFactory;

  @Before
  public void setUp() {
    //Create link to the database
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database");
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
    Staff staff = new Staff("Password", Department.KITCHEN, franchise);
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
    RestaurantTableStaff restaurantTableStaff = new RestaurantTableStaff(staff, restaurantTable);
    entityManager.persist(restaurantTableStaff);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new Transaction
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Transaction transaction = new Transaction(false, 1.00, new Timestamp(1516709651),
        restaurantTableStaff);
    entityManager.persist(transaction);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Get transaction from database.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<Transaction> result = entityManager.createQuery("from Transaction ",
        Transaction.class).getResultList();

    for (Transaction item : result) {
      assertEquals("Check id", item.getTransactionId(), transaction.getTransactionId());
      assertEquals("Check isPaid", item.getIsPaid(), transaction.getIsPaid());
      assertEquals("Check total", item.getTotal(), transaction.getTotal());
      assertEquals("Check timestamp", item.getDatetimePaid(), transaction.getDatetimePaid());
      assertEquals("Check resturant table id",
          item.getRestaurantTableStaff().getRestaurntTableStaffId(),
          transaction.getRestaurantTableStaff().getRestaurntTableStaffId());
    }

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
