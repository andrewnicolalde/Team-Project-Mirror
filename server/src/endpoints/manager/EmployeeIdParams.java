package endpoints.manager;

public class EmployeeIdParams {
  private Long employeeNumber;

  public EmployeeIdParams(Long employeeNumber) {
    this.employeeNumber = employeeNumber;
  }

  public Long getEmployeeNumber() {
    return employeeNumber;
  }

  public void setEmployeeNumber(Long employeeNumber) {
    this.employeeNumber = employeeNumber;
  }
}
