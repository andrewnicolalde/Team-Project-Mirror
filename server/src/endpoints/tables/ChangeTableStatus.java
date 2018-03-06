package endpoints.tables;

public class ChangeTableStatus {

  private String newStatus;

  public ChangeTableStatus(String newStatus) {
    this.newStatus = newStatus;

  }

  public String getNewStatus() {
    return newStatus;
  }
}
