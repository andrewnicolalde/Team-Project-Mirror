package database.tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * This class represents the table of restaurant tables.
 *
 * @author Marcus Messer
 */
@Entity
@Table(name = "RESTAURANT_TABLE")
public class RestaurantTable {

  /**
   * This field is id of the table no matter what restaurant it is part of.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long tableId;
  /**
   * This field is the table number that is used within a restaurant.
   */
  private int tableNumber;
  /**
   * This field is the status of the table, it can only be a status from
   * the <code>TableStatus.java</code> enum.
   */
  private TableStatus status;
  /**
   * This field is the foreign key for the franchise it references the franchise table.
   */
  @ManyToOne
  @JoinColumn(name = "franchise_name", nullable = false)
  private Franchise franchise;

  /**
   * An empty constructor that hibernate uses.
   */
  public RestaurantTable() {
    //Empty Body
  }

  /**
   * This constructor is to create new tables in any restaurant.
   *
   * @param status      This is the status table from <code>TableStatus.java</code>.
   * @param tableNumber This is the table number within the restaurant.
   * @param franchise   This is the franchise branch that the tables belong to.
   */
  public RestaurantTable(TableStatus status, int tableNumber, Franchise franchise) {
    this.status = status;
    this.tableNumber = tableNumber;
    this.franchise = franchise;
  }

  public Long getTableId() {
    return tableId;
  }

  public void setTableId(Long tableId) {
    this.tableId = tableId;
  }

  public int getTableNumber() {
    return tableNumber;
  }

  public void setTableNumber(int tableNumber) {
    this.tableNumber = tableNumber;
  }

  public TableStatus getStatus() {
    return status;
  }

  public void setStatus(TableStatus status) {
    this.status = status;
  }

  public Franchise getFranchise() {
    return franchise;
  }

  public void setFranchise(Franchise franchise) {
    this.franchise = franchise;
  }
}
