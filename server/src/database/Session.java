package database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class maps the session table.
 *
 * @author Marcus Messer
 */
@Entity
@Table(name = "SESSION")
public class Session {

  /**
   * This field is the session id, it is a random hash produced by the server and not the database.
   */
  @Id
  private String sessionId;
  /**
   * This field sets the relationship with the <code>Staff</code> table.
   */
  @ManyToOne
  @JoinColumn(name = "employeeNumber", nullable = false)
  private Staff staff;
  /**
   * This field sets the relationship with the <code>RestaurantTable</code> table.
   */
  @ManyToOne
  @JoinColumn(name = "tableId", nullable = false)
  private RestaurantTable restaurantTable;

  /**
   * Empty Constructor used by hibernate.
   */
  public Session() {
    //Empty Body
  }

  /**
   * This constructor is used to create new sessions.
   * @param sessionId The random hash for the session.
   * @param staff The staff member responsible for the session.
   * @param tableId The table in the session.
   */
  public Session(String sessionId, Staff staff, RestaurantTable tableId) {
    this.sessionId = sessionId;
    this.staff = staff;
    this.restaurantTable = tableId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
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
