package database;

import java.util.List;

/**
 * This interface is for when using generic data objects.
 * @param <T> The database entity class.
 * @param <PK> The primary key for the entity.
 *
 * @author Marcus Messer
 */
public interface GenericDao<T, PK> {
  /**
   * Creates a new entity in the database.
   * @param t The new item to be added to the database.
   */
  void createItem(T t);

  /**
   * Query the database to find a result of that specific class.
   * @param query The query of what you want to find.
   * @param clazz The class of the enitiy you are trying to find.
   * @return A list of the results of the query.
   */
  List query(String query, Class<T> clazz);

  /**
   * Similar to query, but gets only one item by it's primary key.
   * @param primaryKey The primary key of the item you are trying to find.
   * @param clazz The class of the item you are trying to find.
   * @return The item with the same primary key and same class.
   */
  T getOne(PK primaryKey, Class<T> clazz);

  /**
   * This function removes an object from the database.
   * @param t The item to be removed.
   */
  void remove(T t);

  /**
   * This function updates an entity in the database.
   * @param t The item to be updated.
   */
  void update(T t);
}
