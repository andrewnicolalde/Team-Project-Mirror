package database;

import java.sql.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;

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
   * This field is a type Long foreign key referencing the sessionId of
   * a TableSession.
   */
  @Column(name = "sessionId")
  private Long sessionId;

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
   * @param sessionId     This field is a type Long foreign key referencing the sessionId
   *                      of a TableSession.
   * @param total         This field stores the combined price of every Menu_Item in every order
   *                      belonging to this transaction.
   * @param datetimePaid  This field stores the precise time at which a Transaction was paid for
   *                      as a <code>java.sql.Timestamp</code>
   * @param franchiseId   This is a type Long foreign key referencing the
   *                      franchiseId of a Franchise.
   */
  public Transaction(Long transactionId, Boolean isPaid, Long sessionId, Double total,
                     Timestamp datetimePaid, Long franchiseId) {
    this.transactionId = transactionId;
    this.isPaid = isPaid;
    this.sessionId = sessionId;
    this.total = total;
    this.datetimePaid = datetimePaid;
    this.franchiseId = franchiseId;
  }

  /**
   * IntelliJ said I had to have this.
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

  public Long getSessionId() {
    return sessionId;
  }

  public void setSessionId(Long sessionId) {
    this.sessionId = sessionId;
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

  public Long getFranchiseId() {
    return franchiseId;
  }

  public void setFranchiseId(Long franchiseId) {
    this.franchiseId = franchiseId;
  }

}
