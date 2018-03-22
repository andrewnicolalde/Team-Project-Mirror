package endpoints.tables;

/**
 * This class coverts data from the database to JSON.
 */
public class TableAssignData {

  /**
   * This field stores the table number that is related to the number of assignments.
   */
  private int tableNumber;

  /**
   * This fields stores the number of assignments for a table.
   */
  private Long assignments;

  /**
   * This constructor takes data from the database to be used to create JSON.
   * @param tableNumber The table number that is related to the number of assignments.
   * @param assignments The number of assignments for a table.
   */
  public TableAssignData(int tableNumber, Long assignments) {
    this.tableNumber = tableNumber;
    this.assignments = assignments;
  }

  public int getTableNumber() {
    return tableNumber;
  }

  public Long getAssignments() {
    return assignments;
  }
}
