package endpoints.manager;

import database.tables.Department;
import database.tables.Staff;

public class EmployeeData {
  private Long employeeNumber;
  private String firstName;
  private String lastName;
  private Department department;

  public EmployeeData(Staff staff) {
    this.employeeNumber = staff.getEmployeeNumber();
    this.firstName = staff.getFirstName();
    this.lastName = staff.getSurname();
    this.department = staff.getDepartment();
  }

  public Long getEmployeeNumber() {
    return employeeNumber;
  }

  public void setEmployeeNumber(Long employeeNumber) {
    this.employeeNumber = employeeNumber;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }
}
