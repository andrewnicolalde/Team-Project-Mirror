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

  public void createItem(DatabaseTable databaseTable) {
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(databaseTable);
    entityManager.getTransaction().commit();
    entityManager.close();
  }


  public void createConnection() {
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database");
  }

  public void closeConnection() {
    entityManagerFactory.close();
  }

  public List<DatabaseTable> query(String query, Class<?> table) {
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    List<DatabaseTable> temp = (List<DatabaseTable>) entityManager.createQuery(query, table).getResultList();
    entityManager.getTransaction().commit();
    entityManager.close();
    return temp;
  }
}
