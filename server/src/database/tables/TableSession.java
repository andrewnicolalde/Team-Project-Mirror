package database.tables;

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
@Table(name = "TABLE_SESSION")
public class TableSession {

  /**
   * This field is the session id, it is a random hash produced by the server and not the database.
   */
  @Id
  private String tableSessionId;
  /**
   * This field sets the relationship with the <code>RestaurantTable</code> table.
   */
  @ManyToOne
  @JoinColumn(name = "tableId", nullable = false)
  private RestaurantTable restaurantTable;

  /**
   * Empty Constructor used by hibernate.
   */
  public TableSession() {
    //Empty Body
  }

  /**
   * This constructor is used to create new sessions.
   *
   * @param tableSessionId The random hash for the session.
   * @param tableId The table in the session.
   */
  public TableSession(String tableSessionId, RestaurantTable tableId) {
    this.tableSessionId = tableSessionId;
    this.restaurantTable = tableId;
  }

  public String getTableSessionId() {
    return tableSessionId;
  }

  public void setTableSessionId(String sessionId) {
    this.tableSessionId = sessionId;
  }

  public RestaurantTable getRestaurantTable() {
    return restaurantTable;
  }

  public void setRestaurantTable(RestaurantTable restaurantTable) {
    this.restaurantTable = restaurantTable;
  }

  @Override
  public String toString() {
    return "TableSession{" +
        "tableSessionId='" + tableSessionId + '\'' +
        ", restaurantTable=" + restaurantTable +
        '}';
  }
}
