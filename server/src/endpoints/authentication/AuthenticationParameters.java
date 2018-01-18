package endpoints.authentication;

public class AuthenticationParameters {
  private String username;
  private String password;

  public AuthenticationParameters(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
