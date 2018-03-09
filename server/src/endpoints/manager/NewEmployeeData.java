package endpoints.manager;

import database.tables.Department;

public class NewEmployeeData {

  private String firstName;
  private String lastName;
  private String department;
  private String password;

  public NewEmployeeData() {
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

  public String getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department.toString();
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
