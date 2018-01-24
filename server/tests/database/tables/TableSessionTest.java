package database.tables;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TableSessionTest {

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
  public void createTableSessionTest() {

    EntityManager entityManager;

    //Create new franchise.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Franchise franchise = new Franchise("London", "1 London Way",
        "0123465789", "Password");
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

    //Create new table session.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(new TableSession("Random Hash", table));
    entityManager.getTransaction().commit();
    entityManager.close();

    //Get sessions.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<TableSession> result = entityManager.createQuery("from TableSession ",
        TableSession.class).getResultList();

    for (TableSession tableSession : result) {
      assertEquals("Check sessionId", "Random Hash", tableSession.getTableSessionId());
      assertEquals("Check tableId", table.getTableId(),
          tableSession.getRestaurantTable().getTableId());
    }

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
