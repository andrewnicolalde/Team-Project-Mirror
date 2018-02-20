package endpoints.order;

import database.tables.FoodOrder;

/**
 * This class coverts results from the database into usable Java Objects.
 *
 * @author Marcus Messer
 */
public class OrderData {

  private Long foodOrderId;
  private String orderStatus;
  private String timeConfirmed;

  public OrderData(FoodOrder foodOrder) {
    foodOrderId = foodOrder.getOrderId();
    orderStatus = foodOrder.getStatus().name();
    timeConfirmed = foodOrder.getTimeConfirmed().toString();
  }

  public Long getFoodOrderId() {
    return foodOrderId;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public String getTimeConfirmed() {
    return timeConfirmed;
  }
}
