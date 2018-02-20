package database.tables;

/**
 * This enum is used to represent the status of the FoodOrder.
 *
 * @author Marcus Messer
 */
public enum OrderStatus {
  CANCELLED ("Cancelled"),
  ORDERING ("Ordering"),
  READY_TO_CONFIRM ("Ready To Confirm"),
  COOKING ("Cooking"),
  READY_TO_DELIVER ("Ready To Deliver"),
  DELIVERED ("Delivered");

  /**
   * Stores the readable name for the status.
   */
  private final String status;

  /**
   * Constructs the status item in the enum
   * @param status The readable version of the status
   */
  OrderStatus(String status) {
    this.status = status;
  }

  /**
   * Outputs the readable status
   * @return A string of the enum
   */
  @Override
  public String toString() {
    return this.status;
  }
}

