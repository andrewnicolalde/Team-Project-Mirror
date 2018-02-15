package endpoints.authentication;

/**
 * This class converts JSON from the table authentication into usable Java Objects
 */
class TableAuthenticationParameters {

  private String name;
  private String password;
  private int tableNumber;

  TableAuthenticationParameters(String name, String password, int tableNumber) {
    this.name = name;
    this.password = password;
    this.tableNumber = tableNumber;
  }

  String getName() {
    return name;
  }

  String getPassword() {
    return password;
  }

  int getTableNumber() {
    return tableNumber;
  }
}
