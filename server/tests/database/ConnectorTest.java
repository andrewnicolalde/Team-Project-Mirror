package database;

import database.tables.Department;
import database.tables.Franchise;
import database.tables.Staff;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ConnectorTest {

  private EntityManagerFactory entityManagerFactory;
  private Connector connector;

  @Before
  public void setUp() {
    //Create link to the database
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database.test");
    connector = new Connector("server.database.test");
  }

  @After
  public void tearDown() {
    //Close link to the database
    entityManagerFactory.close();
    connector.closeConnection();
  }

  @Test
  public void createFranchiseUsingConnector() {

    connector = new Connector("server.database.test");
    Franchise franchise = new Franchise("London", "1 London Way",
        "0123465789", "Password");
    connector.createItem(franchise);

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<Franchise> result = entityManager.createQuery(
        "from Franchise ", Franchise.class).getResultList();
    entityManager.getTransaction().commit();
    entityManager.close();

    for (Franchise item : result) {
      assertEquals("Check new franchise", franchise.getName(), item.getName());
    }


  }

  @Test
  public void queryFranchiseUsingConnector() {

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");
    connector.createItem(franchise);

    List<Franchise> result = connector.query("from Franchise", Franchise.class);

    for (Franchise item : result) {
      assertEquals("Check query", franchise.getName(), item.getName());
    }

  }

  @Test
  public void getOneFranchiseUsingConnector() {

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");
    connector.createItem(franchise);

    Franchise result = (Franchise) connector.getOne(franchise.getName(), Franchise.class);
    assertEquals("Check get", result.getName(), franchise.getName());
  }

  @Test
  public void removeFranchiseUsingConnector() {

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");
    connector.createItem(franchise);

    List<Franchise> temp = connector.query("from Franchise", Franchise.class);
    if (temp.size() == 0) {
      fail("No item added");
    }

    connector.remove(franchise);

    temp = connector.query("from Franchise", Franchise.class);
    assertEquals("Check size after remove", temp.size(), 0);
  }

  /*  TODO reintroduce once a fix is available NEXT SPRINT!
  @Test
  public void updateFranchiseUsingConnector() {
    Connector connector = Connector.getInstance();
    connector.createConnection();

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");
    connector.createItem(franchise);

    franchise.setName("New London");
    connector.update(franchise);

    List<Franchise> result = connector.query("from Franchise", Franchise.class);

    for (Franchise item : result) {
      assertEquals("Check updated", item.getName(), "New London");
    }
  }
  @Test
  public void updateTwiceFranchiseUsingConnector() {
    Connector connector = Connector.getInstance();
    connector.createConnection();

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");
    connector.createItem(franchise);

    franchise.setName("New London");
    franchise.setAddress("2 London Way");
    connector.update(franchise);

    List<Franchise> result = connector.query("from Franchise", Franchise.class);

    for (Franchise item : result) {
      assertEquals("Check updated", item.getName(), "New London");
      assertEquals("Check updated address", item.getAddress(), "2 London Way");
    }
  }
  */

  @Test
  public void createStaffUsingConnector() {

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");

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
      assertEquals("Check new franchise", franchise.getName(),
          item.getFranchise().getName());
    }

  }

  @Test
  public void queryStaffUsingConnector() {

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");
    connector.createItem(franchise);

    Staff staff = new Staff("Password", Department.WAITER, franchise);
    connector.createItem(staff);

    List<Staff> result = (List<Staff>) (List<?>) connector.query("from Staff", Staff.class);

    for (Staff item : result) {
      assertEquals("Check query", staff.getEmployeeNumber(), item.getEmployeeNumber());
    }

  }

  @Test
  public void updateStaffUsingConnector() {

    Franchise franchise = new Franchise("London", "1 London Way",
        "0123456789", "Password");
    connector.createItem(franchise);

    Staff staff = new Staff("Password", Department.WAITER, franchise);
    connector.createItem(staff);

    staff.setDepartment(Department.KITCHEN);
    connector.update(staff);

    Staff temp = (Staff) connector.getOne(staff.getEmployeeNumber(), Staff.class);

    assertEquals("Check update", temp.getDepartment(), Department.KITCHEN);

  }
}