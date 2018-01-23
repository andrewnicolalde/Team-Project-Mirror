package database.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

import org.hibernate.annotations.GenericGenerator;

/**
 * This class to the ORDER table in the database.
 *
 * @author Andrew Nicolalde
 */
@Entity
@Table(name = "FOOD_ORDER")
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
  private OrderStatus status;

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
  @ManyToOne
  @JoinColumn(name = "transactionId")
  private Transaction transaction;

  /**
   * This constructor allows us to create new Orders.
   *
   * @param status        This field stores the status of the order as a String.
   * @param timeConfirmed This field stores the timeConfirmed of the order as a
   *                      <code>java.sql.Timestamp</code>.
   * @param transaction   This field is a type Long foreign key referencing the
   *                      transaction of Transaction.
   */
  public FoodOrder(OrderStatus status, Timestamp timeConfirmed, Transaction transaction) {
    this.status = status;
    this.timeConfirmed = timeConfirmed;
    this.transaction = transaction;
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

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public Timestamp getTimeConfirmed() {
    return timeConfirmed;
  }

  public void setTimeConfirmed(Timestamp timeConfirmed) {
    this.timeConfirmed = timeConfirmed;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }
}
