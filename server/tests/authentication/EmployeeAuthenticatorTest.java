package authentication;

import database.Staff;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmployeeAuthenticatorTest {
  private EmployeeAuthenticator ea;
  private EntityManagerFactory emf;
  private EntityManager em;

  @Before
  public void before() {
    ea = new EmployeeAuthenticator();
    emf = Persistence.createEntityManagerFactory("server.database");
    em = emf.createEntityManager();
  }

  @After
  public void after() {
    ea.close();
  }

  @Test
  public void checkCredentialsTest() {
    // Connect to database
    em.getTransaction().begin();
    // Create new staff member
    Staff newStaff = new Staff(BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()), "Waiter");
    em.persist(newStaff);
    em.getTransaction().commit();
    em.close();

    assertTrue("Asserts the EmployeeAuthenticator returns true on a correct password", ea.checkCredentials(newStaff.getEmployeeNumber(), "pa55w0rd"));
  }

  @Test
  public void checkInvalidPasswordTest() {
    // Connect to database
    em.getTransaction().begin();
    // Create new staff member
    Staff newStaff = new Staff(BCrypt.hashpw("pa55w0rd", BCrypt.gensalt()), "Waiter");
    em.persist(newStaff);
    em.getTransaction().commit();
    em.close();

    assertFalse("Asserts the EmployeeAuthenticator returns true on a correct password", ea.checkCredentials(newStaff.getEmployeeNumber(), "NotTheRightPassword"));
  }
}
