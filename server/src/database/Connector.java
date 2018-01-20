package database;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Connector {

  private static Connector instance = new Connector();

  private EntityManagerFactory entityManagerFactory;
  private EntityManager entityManager;

  public static Connector getInstance() {
    return instance;
  }

  private Connector() {
  }

  public void createConnection() {
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database");
  }

  public void closeConnection() {
    entityManagerFactory.close();
  }

  public void createItem(DatabaseTable databaseTable) {
    connectEntityManager();
    entityManager.persist(databaseTable);
    closeEntityManger();
  }

  public List<DatabaseTable> query(String query, Class<?> table) {
    connectEntityManager();
    List<DatabaseTable> temp = (List<DatabaseTable>) entityManager.createQuery(query, table)
        .getResultList();
    closeEntityManger();
    return temp;
  }

  private void connectEntityManager() {
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
  }

  private void closeEntityManger() {
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
