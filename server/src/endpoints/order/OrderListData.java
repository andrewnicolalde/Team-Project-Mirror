package endpoints.order;

import database.tables.FoodOrder;

public class OrderListData {

  private Long foodOrderId;
  private String orderStatus;
  private String timeConfirmed;

  public OrderListData(FoodOrder foodOrder) {
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
