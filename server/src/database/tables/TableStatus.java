package database.tables;

/**
 * This enum shows the status of the table.
 *
 * @author Marcus Messer
 */
public enum TableStatus {
  NEEDS_HELP ("Needs Help"),
  FILLED ("Filled"),
  FREE ("Free"),
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
