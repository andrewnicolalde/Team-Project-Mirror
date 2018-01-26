package database.tables;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StaffSessionTest {

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
  public void createStaffSessionTest() {

    EntityManager entityManager;
    //Create new Franchise
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Franchise franchise = new Franchise("London", "1 London Way",
        "012346789", "Password");
    entityManager.persist(franchise);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new Staff member
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Staff staff = new Staff("Password", Department.WAITER, franchise);
    entityManager.persist(staff);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Create new StaffSession
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(new StaffSession("Random Hash", staff));
    entityManager.getTransaction().commit();
    entityManager.close();

    //Get StaffSession from database.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<StaffSession> result = entityManager.createQuery("from StaffSession ",
        StaffSession.class).getResultList();

    for (StaffSession staffSession : result) {
      assertEquals("Check sessionID", "Random Hash",
          staffSession.getStaffSessionId());
      assertEquals("Check staff Id", staff.getEmployeeNumber(),
          staffSession.getStaff().getEmployeeNumber());
    }

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
