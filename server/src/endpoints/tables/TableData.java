package endpoints.tables;

/**
 * This class maps the query result for the tables into something GSON can use.
 *
 * @author Marcus Messer
 */
class TableData {

  private Long tableId;
  private int number;
  private final String status;
  private final String franchise;

  public TableData(int number, String status, String franchise, Long tableId) {
    this.number = number;
    this.status = status;
    this.franchise = franchise;
    this.tableId = tableId;
  }

  public int getNumber() {
    return number;
  }

  public String getStatus() {
    return status;
  }

  public String getFranchise() {
    return franchise;
  }

  public Long getTableId() {
    return tableId;
  }
}
