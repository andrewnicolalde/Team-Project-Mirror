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
@Table(name = "RESTAURANT_TABLE_STAFF")
public class RestaurantTableStaff {

  /**
   * This field if used to identify rtsId.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long restaurantTableStaffId;

  /**
   * This field is used to map the relationship between this table and the <code>Staff</code>
   * table.
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
   * This field is used to store if the table assignment is the current assignment.
   */
  private Boolean isActive;

  /**
   * This empty constructor is used by Hibernate.
   */
  public RestaurantTableStaff() {
    //Empty Body
  }

  /**
   * This constructor is used to create new entities in this table.
   *  @param staff This is the staff member that will be serving the table.
   * @param restaurantTable This is the table that will be served by the staff member.
   * @param isActive This stores if the table assignement is current.
   */
  public RestaurantTableStaff(Staff staff, RestaurantTable restaurantTable,
      Boolean isActive) {
    this.staff = staff;
    this.restaurantTable = restaurantTable;
    this.isActive = isActive;
  }

  public Long getRestaurantTableStaffId() {
    return restaurantTableStaffId;
  }

  public void setRestaurantTableStaffId(Long restaurantTableStaffId) {
    this.restaurantTableStaffId = restaurantTableStaffId;
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

  @Override
  public String toString() {
    return "RestaurantTableStaff{" +
        "restaurantTableStaffId=" + restaurantTableStaffId +
        ", staff=" + staff +
        ", restaurantTable=" + restaurantTable +
        '}';
  }

  public Boolean getActive() {
    return isActive;
  }

  public void setIsActive(Boolean active) {
    this.isActive = active;
  }
}
