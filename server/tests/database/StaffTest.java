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
    //Create link to the database
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database");
  }

  @After
  public void tearDown() {
    //Close link to the database
    entityManagerFactory.close();
  }

  @Test
  public void addUserTest() {
    //Connect to the database
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    //Create a new Franchise
    entityManager.getTransaction().begin();
    Franchise franchise = new Franchise("London", "1 London Way", ""
        + "0123465789");
    entityManager.persist(franchise);
    entityManager.getTransaction().commit();
    entityManager.close();

    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    //Add a new staff member to the database
    entityManager.persist(new Staff("Password", Department.WAITER, franchise));
    entityManager.getTransaction().commit();
    //Close the connection to the database
    entityManager.close();

    //Open a new connection to the database
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    //Gets the list of staff members
    List<Staff> result = entityManager.createQuery("from Staff", Staff.class).getResultList();

    //Check if the new staff member is created correctly
    for (Staff staff : result) {
      assertEquals("New Staff Member Password = Query Result", "Password",
          staff.getPassword());
      assertEquals("New Staff Member Department = Query Result", Department.WAITER,
          staff.getDepartment());
      assertEquals("Check franchise Id", franchise.getFranchiseId(),
          staff.getFranchise().getFranchiseId());
    }

    //Close connection to the server
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}