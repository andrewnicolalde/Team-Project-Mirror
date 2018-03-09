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

  public static Department fromString(String val) {
    switch (val.toLowerCase()) {
      case "waiter":
        return WAITER;
      case "kitchen":
        return KITCHEN;
      case "manager":
        return MANAGER;
      default:
        return null;
    }
  }
}
