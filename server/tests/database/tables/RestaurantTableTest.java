package database.tables;

import database.tables.Franchise;
import database.tables.RestaurantTable;
import database.tables.TableStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RestaurantTableTest {

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
  public void createTableTest() {
    EntityManager entityManager;

    //Create new franchise for the purpose of this test.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Franchise franchise = new Franchise("London", "1 London Way", "0123456789");
    entityManager.persist(franchise);
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
      assertEquals("Check franchise", franchise.getFranchiseId(),
          restaurantTable.getFranchise().getFranchiseId());
    }

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
