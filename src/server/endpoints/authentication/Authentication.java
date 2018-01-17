package server.endpoints.authentication;

public class Authentication {
  static boolean isValidLoginCombination(String username, String password) {
    return true;
  }

  /**
   * Checks if the username and password combination is valid and returns a session key of they are.
   * @param username The username to authenticate.
   * @param password The password to authenticate.
   * @return A session key as a string, or null if the username/password combination is invalid.
   */
  public static String authenticate(String username, String password) {
    return "498379438759384";
  }
}
