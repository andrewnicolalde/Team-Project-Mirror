package database.tables;

/**
 * This enum is used to represent the status of the FoodOrder.
 *
 * @author Marcus Messer
 */
public enum OrderStatus {
  CANCELLED,
  ORDERING,
  READY_TO_CONFIRM,
  COOKING,
  READY_TO_DELIVER,
  DELIVERED
}
