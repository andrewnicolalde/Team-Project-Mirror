package endpoints.order;

import database.tables.FoodOrder;
import java.sql.Timestamp;

/**
 * This class coverts results from the database into usable Java Objects.
 *
 * @author Marcus Messer
 */
public class OrderData {

  private final Long foodOrderId;
  private final String orderStatus;
  private final Timestamp timeConfirmed;

  public OrderData(FoodOrder foodOrder) {
    foodOrderId = foodOrder.getOrderId();
    orderStatus = foodOrder.getStatus().toString();
    timeConfirmed = foodOrder.getTimeConfirmed();
  }

  public Long getFoodOrderId() {
    return foodOrderId;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public Timestamp getTimeConfirmed() {
    return timeConfirmed;
  }
}
