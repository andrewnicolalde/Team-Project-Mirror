package database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseManager {

  private static DatabaseManager ourInstance = new DatabaseManager();

  public static DatabaseManager getInstance() {
    return ourInstance;
  }

  private EntityManagerFactory entityManagerFactory;

  private DatabaseManager() {
    if (entityManagerFactory == null) {
      entityManagerFactory = Persistence.createEntityManagerFactory("server.database.dev");
    }
  }

  public EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
  }
}
