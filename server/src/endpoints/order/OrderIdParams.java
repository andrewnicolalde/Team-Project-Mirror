package endpoints.order;

/**
 * This class converts JSON into a useful Java object
 *
 * @author Andrew Nicolalde
 */
public class OrderIdParams {

  private Long orderId;

  public OrderIdParams(Long orderId){
    this.orderId = orderId;
  }

  public Long getOrderId(){
    return orderId;
  }
}
