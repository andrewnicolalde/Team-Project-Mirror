package database;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
   * This field stores the paid status of the order as a
   * Boolean.
   */
  @Column(name = "isPaid")
  private Boolean isPaid;

  /**
   * This field stores a reference to the session the transaction took place on.
   */
  @ManyToOne
  @JoinColumn(name = "sessionId", nullable = false)
  private Session session;

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
   * This is a type Long foreign key referencing the franchiseId of a
   * Franchise.
   */
  @Column(name = "franchiseId")
  private Long franchiseId;

  /**
   * This constructor allows us to create Transactions.
   *
   * @param transactionId This field stores the transaction id as a Long.
   * @param isPaid        This field stores the paid status of the order as a Boolean.
   * @param session       This field stores a reference to the session the transaction took place on
   * @param total         This field stores the combined price of every Menu_Item in every order
   *                      belonging to this transaction.
   * @param datetimePaid  This field stores the precise time at which a Transaction was paid for
   *                      as a <code>java.sql.Timestamp</code>
   */
  public Transaction(Long transactionId, Boolean isPaid, Session session, Double total,
                     Timestamp datetimePaid) {
    this.transactionId = transactionId;
    this.isPaid = isPaid;
    this.session = session;
    this.total = total;
    this.datetimePaid = datetimePaid;
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

  public Session getSession() {
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
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
}
