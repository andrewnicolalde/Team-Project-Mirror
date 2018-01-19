package database;

import static org.junit.Assert.assertEquals;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
  public void tableTest() {

    //Connect to the database
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    //Add a new staff member to the database
    entityManager.persist(new RestaurantTable(TableStatus.FREE, 1,
        new Franchise("London", "1 London Way", "0123456789")));
    entityManager.getTransaction().commit();
    //Close the connection to the database
    entityManager.close();

    //Open a new connection to the database
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    //Gets the list of staff members
    List<RestaurantTable> result = entityManager.createQuery("from RestaurantTable ",
        RestaurantTable.class).getResultList();

    //Check if the new staff member is created correctly
    for (RestaurantTable restaurantTable : result) {
      assertEquals("Check table status", TableStatus.FREE, restaurantTable.getStatus());
      assertEquals("Check table number", 1, restaurantTable.getTableNumber());
      assertEquals("Check franchise", new Franchise("London", "1 London Way",
          "0123456789"), restaurantTable.getFranchise());
    }

    //Close connection to the server
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
