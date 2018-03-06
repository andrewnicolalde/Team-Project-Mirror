package database.tables;

/**
 * This enum shows the possible departments the staff could be in.
 *
 * @author Marcus Messer
 */
public enum Department {
  WAITER("Waiter"),
  KITCHEN("Kitchen"),
  MANAGER("Manager");

  private final String name;

  Department(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
