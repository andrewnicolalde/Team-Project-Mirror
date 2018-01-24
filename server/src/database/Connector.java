package database;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This class is a facade to communicate with the database.
 *
 * @author Marcus Messer
 */
public class Connector<T, PK> implements GenericDao<T, PK> {

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

  /**
   * Creates a new entity in the database.
   *
   * @param t The new item to be added to the database.
   */
  @Override
  public void createItem(T t) {
    connectEntityManager();
    entityManager.persist(t);
    closeEntityManger();
  }

  /**
   * Query the database to find a result of that specific class.
   *
   * @param query The query of what you want to find.
   * @param clazz The class of the enitiy you are trying to find.
   * @return A list of the results of the query.
   */
  @Override
  public List query(String query, Class<T> clazz) {
    connectEntityManager();
    List<T> temp = entityManager.createQuery(query, clazz).getResultList();
    closeEntityManger();
    return temp;
  }

  /**
   * Similar to query, but gets only one item by it's primary key.
   *
   * @param primaryKey The primary key of the item you are trying to find.
   * @param clazz      The class of the item you are trying to find.
   * @return The item with the same primary key and same class.
   */
  @Override
  public T getOne(PK primaryKey, Class<T> clazz) {
    return null;
  }

  /**
   * This function removes an object from the database.
   *
   * @param t The item to be removed.
   */
  @Override
  public void remove(T t) {

  }

  /**
   * This function updates an entity in the database.
   *
   * @param t The item to be updated.
   */
  @Override
  public void update(T t) {

  }
}
