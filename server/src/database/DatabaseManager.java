package database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This Singleton class is used to connect to the database.
 *
 * @author Marcus Messer
 */
public class DatabaseManager {

  /**
   * This is the instance of the this class. It will be the only ever instance of this class.
   */
  private static DatabaseManager ourInstance = new DatabaseManager();

  /**
   * This is the instance of the EntityManagerFactory that connects the database.
   */
  private EntityManagerFactory entityManagerFactory;

  /**
   * This method returns the instance of this class.
   * @return the instance
   */
  public static DatabaseManager getInstance() {
    return ourInstance;
  }

  /**
   * This private constructor creates a new EntityManagerFactory, if it is null.
   */
  private DatabaseManager() {
    if (entityManagerFactory == null) {
      entityManagerFactory = Persistence.createEntityManagerFactory("server.database.dev");
    }
  }

  public EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
  }
}
