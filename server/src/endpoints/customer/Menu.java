package endpoints.customer;

public class Menu {
  /**
   * Gets the full menu from the database and returns it in JSON.
   * @return The menu in JSON as a string.
   */
  public static String getMenu() {
    return "{\"items\":[\"Beef Burger\",\"Chicken Salad\",\"Water\",\"Diet Coke\",\"Fish and Chips\"]}";
  }
}
