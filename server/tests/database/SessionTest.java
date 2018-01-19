package database;

import static org.junit.Assert.assertEquals;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SessionTest {
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
  public void createSessionTest() {

    EntityManager entityManager;
    //Create new staff member.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Staff tempStaff = new Staff("Password", "Waiter");
    entityManager.persist(tempStaff);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new franchise.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Franchise franchise = new Franchise("London", "1 London Way",
        "0123465789");
    entityManager.persist(franchise);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new table.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    RestaurantTable table = new RestaurantTable(TableStatus.FREE, 1, franchise);
    entityManager.persist(table);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new session.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(new Session("Random Hash", tempStaff, table, true));
    entityManager.getTransaction().commit();
    entityManager.close();

    //Get sessions.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<Session> result = entityManager.createQuery("from Session ",
        Session.class).getResultList();

    for ( Session session: result) {
      assertEquals("Check sessionId", "Random Hash", session.getSessionId());
      assertEquals("Check staffId", tempStaff.getEmployeeNumber(), session.getStaff().getEmployeeNumber());
      assertEquals("Check tableId", table.getTableId(), session.getRestaurantTable().getTableId());
      assertEquals("Check isActive", true, session.isActive());
    }

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
