package database.tables;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * This class is for the Transaction Table in the database.
 *
 * @author Andrew Nicolalde
 */
@Entity
@Table(name = "TRANSACTION")
public class Transaction {

  /**
   * This field stores the transaction id as a Long.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long transactionId;

  /**
   * This field stores the paid status of the order as a Boolean.
   */
  @Column(name = "isPaid")
  private Boolean isPaid;

  /**
   * This field stores the combined price of every Menu_Item in every order belonging to this
   * transaction.
   */
  @Column(name = "total", nullable = false)
  private Double total;

  /**
   * This field stores the precise time at which a database.tables.Transaction was paid for as a
   * <code>java.sql.Timestamp</code>
   */
  @Column(name = "datetimePaid")
  private Timestamp datetimePaid;

  /**
   * This field states if the customers have left.
   */
  @Column(name = "hasLeft")
  private boolean hasLeft;

  /**
   * This field stores a link to the employee who is in charge of the transaction.
   */
  @ManyToOne
  @JoinColumn(name = "server_id", nullable = false)
  private RestaurantTableStaff restaurantTableStaff;

  /**
   * This constructor allows us to create Transactions.
   *  @param isPaid This field stores the paid status of the order as a Boolean.
   * @param total This field stores the combined price of every Menu_Item in every order belonging
   * to this transaction.
   * @param datetimePaid This field stores the precise time at which a Transaction was paid for as a
 * <code>java.sql.Timestamp</code>
   * @param hasLeft This field stores if the customer has left.
   * @param restaurantTableStaff This field stores link to the employee who is in charge of the
   */
  public Transaction(Boolean isPaid, Double total, Timestamp datetimePaid,
      boolean hasLeft, RestaurantTableStaff restaurantTableStaff) {
    this.isPaid = isPaid;
    this.total = total;
    this.datetimePaid = datetimePaid;
    this.hasLeft = hasLeft;
    this.restaurantTableStaff = restaurantTableStaff;
  }

  /**
   * Blank constructor for Hibernate ORM.
   */
  public Transaction() {
  }

  public Long getTransactionId() {
    return this.transactionId;
  }

  public void setTransactionId(Long transactionId) {
    this.transactionId = transactionId;
  }

  public Boolean getIsPaid() {
    return this.isPaid;
  }

  public void setIsPaid(Boolean isPaid) {
    this.isPaid = isPaid;
  }

  public Double getTotal() {
    return total;
  }

  public void setTotal(Double total) {
    this.total = total;
  }

  public Timestamp getDatetimePaid() {
    return datetimePaid;
  }

  public void setDatetimePaid(Timestamp datetimePaid) {
    this.datetimePaid = datetimePaid;
  }

  public RestaurantTableStaff getRestaurantTableStaff() {
    return restaurantTableStaff;
  }

  public void setRestaurantTableStaff(RestaurantTableStaff restaurantTableStaff) {
    this.restaurantTableStaff = restaurantTableStaff;
  }

  public boolean isHasLeft() {
    return hasLeft;
  }

  public void setHasLeft(boolean hasLeft) {
    this.hasLeft = hasLeft;
  }

  @Override
  public String toString() {
    return "Transaction{" +
        "transactionId=" + transactionId +
        ", isPaid=" + isPaid +
        ", total=" + total +
        ", datetimePaid=" + datetimePaid +
        ", hasLeft=" + hasLeft +
        ", restaurantTableStaff=" + restaurantTableStaff +
        '}';
  }
}
