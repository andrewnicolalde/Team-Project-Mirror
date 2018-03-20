package endpoints.tables;

import database.tables.RestaurantTableStaff;

public class TableAssignParams {
  private String franchise;

  private Long staffId;

  private Long tableId;

  public TableAssignParams(String franchise, Long staffId, Long tableId) {
    this.franchise = franchise;
    this.staffId = staffId;
    this.tableId = tableId;
  }

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
