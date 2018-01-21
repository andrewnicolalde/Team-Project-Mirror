package database;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.crypto.Data;

/**
 * This class is a facade to communicate with the database.
 *
 * @author Marcus Messer
 */
public class Connector {

  /**
   * This field is a the singlton instance of the class, as there will be only one connection to
   * the server.
   */
  private static Connector instance = new Connector();

  /**
   * This field is used to create <code>EntityManager</code> objects.
   */
  private EntityManagerFactory entityManagerFactory;
  /**
   * This field is what actually communicates to the database.
   */
  private EntityManager entityManager;

  /**
   * Gets the current instance of the connector.
   * @return the instance of the class.
   */
  public static Connector getInstance() {
    return instance;
  }

  /**
   * Empty private constructor to stop creating more than one connector.
   */
  private Connector() {
  }

  /**
   * This function creates the <code>EntityManger</code> that will be used to communicate with the
   * database.
   */
  public void createConnection() {
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database");
  }

  /**
   * This function closes the possibility of creating any more connections.
   */
  public void closeConnection() {
    entityManagerFactory.close();
  }

  /**
   * This function allows you to create a new item in the database.
   * @param databaseTable The new item to be added to the database.
   */
  public void createItem(DatabaseTable databaseTable) {
    connectEntityManager();
    entityManager.persist(databaseTable);
    closeEntityManger();
  }

  /**
   * This function allows you to query the database.
   * @param query The query you would like the result to.
   * @param table The table you are querring.
   * @return The result from the query as a list.
   */
  public List<DatabaseTable> query(String query, Class<?> table) {
    connectEntityManager();
    List<DatabaseTable> temp = (List<DatabaseTable>) entityManager.createQuery(query, table)
        .getResultList();
    closeEntityManger();
    return temp;
  }

  /**
   * Queries the database for a single object with the given primary key.
   * @author Toby Such
   * @param primaryKey The primary key of the object.
   * @param table The table of the type the object is.
   * @return The object with type table, and with the primary key given.
   */
  public DatabaseTable get(Object primaryKey, Class<?> table) {
    connectEntityManager();
    DatabaseTable result = (DatabaseTable)entityManager.find(table, primaryKey);
    closeEntityManger();
    return result;
  }

  /**
   * Removes an entity instance from the database.
   * @author Toby Such
   * @param entity The entity to remove.
   */
  public void remove(DatabaseTable entity) {
    connectEntityManager();
    // Have to merge as the object will have been gotten by a different entity manager.
    entityManager.remove(entityManager.merge(entity));
    closeEntityManger();
  }

  /**
   * This function is the start of the communication with the database.
   */
  private void connectEntityManager() {
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
  }

  /**
   * This function is the end of the communication with the database.
   */
  private void closeEntityManger() {
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
