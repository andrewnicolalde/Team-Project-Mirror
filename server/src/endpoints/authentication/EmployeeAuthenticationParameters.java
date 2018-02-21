package endpoints.authentication;

/**
 * This class converts JSON from the employee authentication javascript into usable Java objects.
 *
 * @author Toby Such
 */
class EmployeeAuthenticationParameters {

  private Long employeeNumber;
  private String password;

  EmployeeAuthenticationParameters(Long employeeNumber, String password) {
    this.employeeNumber = employeeNumber;
    this.password = password;
  }

  Long getEmployeeNumber() {
    return employeeNumber;
  }

  String getPassword() {
    return password;
  }
}
