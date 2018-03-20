package endpoints.tables;

import database.tables.RestaurantTableStaff;

/**
 * This class is used to convert JSON to a usable object a visa versa.
 *
 * @author Marcus Messer
 */
public class TableAssignParams {

  /**
   * This field stores the franchise for that has the staff member and the table
   */
  private String franchise;

  /**
   * This field stores the staff member that is assigned
   */
  private Long staffId;

  /**
   * This field stores the table that the staff member is assigned to.
   */
  private Long tableId;

  /**
   * This is used to convert JSON to a usable object.
   * @param franchise The store the staff member and table belong to.
   * @param staffId The staff member that is being assigned.
   * @param tableId The table that a staff member is assigned to.
   */
  public TableAssignParams(String franchise, Long staffId, Long tableId) {
    this.franchise = franchise;
    this.staffId = staffId;
    this.tableId = tableId;
  }

  /**
   * This is used to convert database results to JSON..
   * @param restaurantTableStaff The result from the database.
   */
  public TableAssignParams(RestaurantTableStaff restaurantTableStaff) {
    this.franchise = restaurantTableStaff.getRestaurantTable().getFranchise().getName();
    this.staffId = restaurantTableStaff.getStaff().getEmployeeNumber();
    this.tableId = restaurantTableStaff.getRestaurantTable().getTableId();
  }

  public String getFranchise() {
    return franchise;
  }

  public Long getStaffId() {
    return staffId;
  }

  public Long getTableId() {
    return tableId;
  }
}
