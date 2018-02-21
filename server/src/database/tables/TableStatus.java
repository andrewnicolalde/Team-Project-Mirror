package database.tables;

/**
 * This enum shows the status of the table.
 *
 * @author Marcus Messer
 */
public enum TableStatus {
  FREE ("Free"),
  FILLED ("Filled"),
  NEEDS_CLEANING ("Needs Cleaning");

  private final String status;

  TableStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return this.status;
  }
}
