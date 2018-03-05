package database.tables;

import static org.junit.Assert.assertEquals;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RestaurantTableTest {
  //TODO Figure out why this test never completes.

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
  public void createTableTest() {
    EntityManager entityManager;

    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Franchise franchise = entityManager.find(Franchise.class, "Egham");
    entityManager.getTransaction().commit();
    entityManager.close();
    //Create new table.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(new RestaurantTable(TableStatus.FREE, 1, franchise));
    entityManager.getTransaction().commit();
    entityManager.close();

    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    //Get the tables from the database.
    List<RestaurantTable> result = entityManager.createQuery("from RestaurantTable ",
        RestaurantTable.class).getResultList();

    for (RestaurantTable restaurantTable : result) {
      assertEquals("Check table status", TableStatus.FREE, restaurantTable.getStatus());
      assertEquals("Check table number", 1, restaurantTable.getTableNumber());
      assertEquals("Check franchise", franchise.getName(),
          restaurantTable.getFranchise().getName());
    }

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
