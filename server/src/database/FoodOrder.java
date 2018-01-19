package database;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * This class to the ORDER table in the database.
 *
 * @author Andrew Nicolalde
 */
@Entity
@Table(name = "FOODORDER")
public class FoodOrder {

  /**
   * This field stores the orderId as a long.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long orderId;

  /**
   * This field stores the status of the order as a String.
   */
  @Column(name = "status")
  private String status;

  /**
   * This field stores the timeConfirmed of the order as a
   * <code>java.sql.Timestamp</code>.
   */
  @Column(name = "timeConfirmed")
  private Timestamp timeConfirmed;

  /**
   * This field is a type Long foreign key referencing the
   * transactionId of Transaction.
   */
  @Column(name = "transactionID")
  private Long transactionId;

  /**
   * This constructor allows us to create new Orders.
   * @param orderId This field stores the orderId as a long.
   * @param status This field stores the status of the order as a String.
   * @param timeConfirmed This field stores the timeConfirmed of the order as a
   *                      <code>java.sql.Timestamp</code>.
   * @param transactionId This field is a type Long foreign key referencing the
   *                      transactionId of Transaction.
   */
  public FoodOrder(Long orderId, String status, Timestamp timeConfirmed, Long transactionId) {
    this.orderId = orderId;
    this.status = status;
    this.timeConfirmed = timeConfirmed;
    this.transactionId = transactionId;
  }

  /**
   * IntelliJ said I had to have this.
   */
  public FoodOrder() {
  }

  public Long getOrderId() {
    return this.orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Timestamp getTimeConfirmed() {
    return timeConfirmed;
  }

  public void setTimeConfirmed(Timestamp timeConfirmed) {
    this.timeConfirmed = timeConfirmed;
  }

  public Long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(Long transactionId) {
    this.transactionId = transactionId;
  }
}
