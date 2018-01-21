package database;

import java.sql.Timestamp;
import javax.persistence.*;

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
   * This field stores the paid status of the order as a
   * Boolean.
   */
  @Column(name = "isPaid")
  private Boolean isPaid;

  /**
   * This field stores a reference to the session the transaction took place on.
   */

  /**
   * This field stores the combined price of every Menu_Item in every order
   * belonging to this transaction.
   */
  @Column(name = "total")
  private Double total;

  /**
   * This field stores the precise time at which a database.Transaction was paid for
   * as a <code>java.sql.Timestamp</code>
   */
  @Column(name = "datetimePaid")
  private Timestamp datetimePaid;

  /**
   * This field stores a link to the employee who is in charge of the transaction
   */
  @ManyToOne
  @JoinColumn(name = "server_id", nullable = false)
  private RestaurantTableStaff restaurantTableStaff;

  /**
   * This constructor allows us to create Transactions.
   *
   * @param transactionId        This field stores the transaction id as a Long.
   * @param isPaid               This field stores the paid status of the order as a Boolean.
   * @param total                This field stores the combined price of every Menu_Item in every
   *                             order belonging to this transaction.
   * @param datetimePaid         This field stores the precise time at which a Transaction was paid
   *                             for as a <code>java.sql.Timestamp</code>
   * @param restaurantTableStaff This field stores link to the employee who is in charge of the
   *                             transaction
   */
  public Transaction(Long transactionId, Boolean isPaid, Double total, Timestamp datetimePaid,
                     RestaurantTableStaff restaurantTableStaff) {
    this.transactionId = transactionId;
    this.isPaid = isPaid;
    this.total = total;
    this.datetimePaid = datetimePaid;
    this.restaurantTableStaff = restaurantTableStaff;
  }

  /**
   * Blank constructor for Hibernate ORM
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
}
