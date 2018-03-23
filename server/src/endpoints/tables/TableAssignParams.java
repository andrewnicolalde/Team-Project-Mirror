package endpoints.tables;

import database.tables.RestaurantTableStaff;

/**
 * This class is used to convert JSON to a usable object a visa versa.
 *
 * @author Marcus Messer
 */
public class TableAssignParams {

  /**
   * This field stores the staff member that is assigned
   */
  private Long staffId;

  /**
   * This field stores the table that the staff member is assigned to.
   */
  private Integer tableNumber;

  /**
   * This is used to convert JSON to a usable object.
   * @param staffId The staff member that is being assigned.
   * @param tableNumber The table that a staff member is assigned to.
   */
  public TableAssignParams(Long staffId, Integer tableNumber) {
    this.staffId = staffId;
    this.tableNumber = tableNumber;
  }

  public Long getStaffId() {
    return staffId;
  }

  public Integer getTableNumber() {
    return tableNumber;
  }
}
