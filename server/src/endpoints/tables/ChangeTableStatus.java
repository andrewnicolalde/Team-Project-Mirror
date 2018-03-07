package endpoints.tables;

public class ChangeTableStatus {

  private Long tableId;
  private String newStatus;

  public ChangeTableStatus(String newStatus, Long tableId) {
    this.newStatus = newStatus;
    this.tableId = tableId;

  }

  public String getNewStatus() {
    return newStatus;
  }

  public Long getTableId() {
    return tableId;
  }
}
