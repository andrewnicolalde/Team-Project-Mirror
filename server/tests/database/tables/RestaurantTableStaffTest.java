package database.tables;

import static org.junit.Assert.assertEquals;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RestaurantTableStaffTest {

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
  public void createRestaurantTableStaffTest() {

    EntityManager entityManager;
    //Create new franchise
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Franchise franchise = new Franchise("London", "1 London Way",
        "0123465789", "Password");
    entityManager.persist(franchise);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new table
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    RestaurantTable restaurantTable = new RestaurantTable(TableStatus.FREE, 1, franchise);
    entityManager.persist(restaurantTable);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new Staff
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Staff staff = new Staff("John", "Doe", "Password", Department.WAITER, franchise);
    entityManager.persist(staff);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new RestaurantTableStaff
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(new RestaurantTableStaff(staff, restaurantTable));
    entityManager.getTransaction().commit();
    entityManager.close();

    //Get RestaurantTableStaff from database.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<RestaurantTableStaff> result = entityManager.createQuery("from RestaurantTableStaff ",
        RestaurantTableStaff.class).getResultList();

    for (RestaurantTableStaff restaurantTableStaff : result) {
      assertEquals("Check staff member", staff.getEmployeeNumber(),
          restaurantTableStaff.getStaff().getEmployeeNumber());
      assertEquals("Check table", restaurantTable.getTableId(),
          restaurantTableStaff.getRestaurantTable().getTableId());
    }

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
