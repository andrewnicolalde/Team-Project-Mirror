package database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StaffTest {
  private EntityManagerFactory entityManagerFactory;

  @Before
  public void setUp() {
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database");
  }

  @After
  public void tearDown() {
    entityManagerFactory.close();
  }

  @Test
  public void addUserTest() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(new Staff("Password", "Waiter"));
    entityManager.getTransaction().commit();
    entityManager.close();

    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<Staff> result = entityManager.createQuery("from Staff", Staff.class).getResultList();

    for (Staff staff : result) {
      assertEquals("New Staff Member Password = Query Result", "Password",
          staff.getPassword());
      assertEquals("New Staff Member Department = Query Result", "Waiter",
          staff.getDepartment());
    }
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}