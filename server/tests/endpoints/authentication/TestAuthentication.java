package endpoints.authentication;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static endpoints.authentication.Authentication.authenticate;
import static endpoints.authentication.Authentication.isValidLoginCombination;

public class TestAuthentication {
  @Test
  public void testIsValidLoginCombination() {
    assertTrue("Asserts the method returns true on valid login details",
            isValidLoginCombination("admin", "pa55w0rd"));
  }

  @Test
  public void testAuthenticate() {
    EmployeeAuthenticationParameters ap = new EmployeeAuthenticationParameters(12345L, "pa55w0rd");
    assertEquals("Asserts a valid session key is returned when valid login details are given.",
            "498379438759384", authenticate(ap));
  }
}
