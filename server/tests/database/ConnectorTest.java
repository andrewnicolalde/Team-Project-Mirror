package database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConnectorTest {

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
  public void createFranchiseUsingConnector() {
    Connector connector = Connector.getInstance();
    connector.createConnection();

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123465789");
    connector.createItem(franchise);

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<Franchise> result = entityManager.createQuery(
        "from Franchise ", Franchise.class).getResultList();
    entityManager.getTransaction().commit();
    entityManager.close();

    for (Franchise item : result) {
      assertEquals("Check new franchise", franchise.getFranchiseId(), item.getFranchiseId());
    }

    connector.closeConnection();

  }

  @Test
  public void queryFranchiseUsingConnector() {
    Connector connector = Connector.getInstance();
    connector.createConnection();

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789");
    connector.createItem(franchise);

    List<Franchise> result = connector.query("from Franchise", Franchise.class);

    for (Franchise item : result) {
      assertEquals("Check query", franchise.getFranchiseId(), item.getFranchiseId());
    }

    connector.closeConnection();
  }

  @Test
  public void createStaffUsingConnector() {
    Connector connector = Connector.getInstance();
    connector.createConnection();

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789");

    connector.createItem(franchise);

    Staff staff = new Staff("Password", Department.WAITER, franchise);
    connector.createItem(staff);

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<Staff> result = entityManager.createQuery(
        "from Staff ", Staff.class).getResultList();
    entityManager.getTransaction().commit();
    entityManager.close();

    for (Staff item : result) {
      assertEquals("Check new franchise", franchise.getFranchiseId(),
          item.getFranchise().getFranchiseId());
    }

    connector.closeConnection();
  }

  @Test
  public void queryStaffUsingConnector() {
    Connector connector = Connector.getInstance();
    connector.createConnection();

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789");
    connector.createItem(franchise);

    Staff staff = new Staff("Password", Department.WAITER, franchise);
    connector.createItem(staff);

    List<Staff> result = (List<Staff>) (List<?>) connector.query("from Staff", Staff.class);

    for (Staff item : result) {
      assertEquals("Check query", staff.getEmployeeNumber(), item.getEmployeeNumber());
    }

    connector.closeConnection();
  }

}
