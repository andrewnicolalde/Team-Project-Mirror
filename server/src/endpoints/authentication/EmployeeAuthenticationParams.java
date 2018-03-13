package endpoints.authentication;

/**
 * This class converts JSON from the employee authentication javascript into usable Java objects.
 *
 * @author Toby Such
 */
class EmployeeAuthenticationParams {

  private final Long employeeNumber;
  private final String password;

  EmployeeAuthenticationParams(Long employeeNumber, String password) {
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
