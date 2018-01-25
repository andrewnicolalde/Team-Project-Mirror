package database.tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * This class maps this class to the database.
 *
 * @author Marcus Messer
 */
@Entity
@Table(name = "RESTURANT_TABLE_STAFF")
public class RestaurantTableStaff {

  /**
   * This field if used to identify rtsId.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long restaurntTableStaffId;

  /**
   * This field is used to map the relationship between this table and the <code>Staff</code> table.
   */
  @ManyToOne
  @JoinColumn(name = "employeeNumber")
  private Staff staff;

  /**
   * This field is used to map the relationship between this table and the
   * <code>RestaurantTable</code> table.
   */
  @ManyToOne
  @JoinColumn(name = "tableId")
  private RestaurantTable restaurantTable;

  /**
   * This empty constructor is used by Hibernate.
   */
  public RestaurantTableStaff() {
    //Empty Body
  }

  /**
   * This constructor is used to create new entities in this table.
   *
   * @param staff           This is the staff member that will be serving the table.
   * @param restaurantTable This is the table that will be served by the staff member.
   */
  public RestaurantTableStaff(Staff staff, RestaurantTable restaurantTable) {
    this.staff = staff;
    this.restaurantTable = restaurantTable;
  }

  public Long getRestaurntTableStaffId() {
    return restaurntTableStaffId;
  }

  public void setRestaurntTableStaffId(Long restaurntTableStaffId) {
    this.restaurntTableStaffId = restaurntTableStaffId;
  }

  public Staff getStaff() {
    return staff;
  }

  public void setStaff(Staff staff) {
    this.staff = staff;
  }

  public RestaurantTable getRestaurantTable() {
    return restaurantTable;
  }

  public void setRestaurantTable(RestaurantTable restaurantTable) {
    this.restaurantTable = restaurantTable;
  }
}
