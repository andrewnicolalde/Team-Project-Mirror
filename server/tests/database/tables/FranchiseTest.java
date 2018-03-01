package database.tables;

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
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database.test");
  }

  @After
  public void tearDown() {
    //Close link to the database
    entityManagerFactory.close();
  }

  @Test
  public void createFranchiseTest() {

    EntityManager entityManager;
    //Create new Franchise
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(new Franchise("London", "1 London Way",
        "0123456789", "Password"));
    entityManager.getTransaction().commit();
    entityManager.close();

    //Get franchises from database.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<Franchise> result = entityManager.createQuery("from Franchise ",
        Franchise.class).getResultList();

    for (Franchise franchise : result) {
      assertEquals("New Franchise Name = London", "London", franchise.getName());
      assertEquals("New Franchise Address = 1 London Way", "1 London Way",
          franchise.getAddress());
      assertEquals("New Franchise Contact No = 0123456789", "0123456789",
          franchise.getContactNo());
    }

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}

