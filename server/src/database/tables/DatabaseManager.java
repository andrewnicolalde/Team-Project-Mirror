package database.tables;

public class DatabaseManager {

  private static DatabaseManager ourInstance = new DatabaseManager();

  public static DatabaseManager getInstance() {
    return ourInstance;
  }

  private DatabaseManager() {
  }
}
