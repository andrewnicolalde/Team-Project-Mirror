package endpoints.waiter;

import database.tables.RestaurantTableStaff;

/**
 * This class maps the query result for the tables into something GSON can use.
 *
 * @author Marcus Messer
 */
public class TableData {

  private int number;
  private String status;
  private String franchise;

  public TableData(RestaurantTableStaff restaurantTableStaff) {
    this.number = restaurantTableStaff.getRestaurantTable().getTableNumber();
    this.status = restaurantTableStaff.getRestaurantTable().getStatus().toString();
    this.franchise = restaurantTableStaff.getRestaurantTable().getFranchise().getName();
  }

  public int getNumber() {
    return number;
  }

  public String getStatus() {
    return status;
  }

  public String getFranchise() {
    return franchise;
  }
}
