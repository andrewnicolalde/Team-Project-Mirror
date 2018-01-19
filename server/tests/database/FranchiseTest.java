package database;

import static org.junit.Assert.assertEquals;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FranchiseTest {

  private EntityManagerFactory entityManagerFactory;

  @Before
  public void setUp() {
    //Create link to the database
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database");
  }

  @After
  private void tearDown() {
    //Close link to the database
    entityManagerFactory.close();
  }

  @Test
  public void tableTest() {

    //Connect to the database
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    //Add a new staff member to the database
    entityManager.persist(new Franchise("London", "1 London Way", "012345679"));
    entityManager.getTransaction().commit();
    //Close the connection to the database
    entityManager.close();

    //Open a new connection to the database
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    //Gets the list of staff members
    List<Franchise> result = entityManager.createQuery("from Franchise ",
        Franchise.class).getResultList();

    //Check if the new staff member is created correctly
    for (Franchise franchise : result) {
      assertEquals("New Franchise Name = London", "London", franchise.getName());
      assertEquals("New Franchise Address = 1 London Way", "1 London Way",
          franchise.getAddress());
      assertEquals("New Franchise Contact No = 0123456789", "0123456789",
          franchise.getContactNo());
    }

    //Close connection to the server
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}

