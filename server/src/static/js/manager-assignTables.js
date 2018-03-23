/**
 * @module Manager-Assign-Tables
 */

$(document).ready(() => {
  getEmployees();
});

/**
 * This function gets a list of waiters and displays them.
 */
function getEmployees() {
  get("/api/authStaff/getWaiters", (data) => {
    const employees = JSON.parse(data);
    for (let i = 0; i < employees.length; i++) {
      const employee = employees[i];
      $("#accordian-staff").append("<div class=\"card\">"
          + "<div class=\"card-header\" id=\"heading" + i + "\""
          + " data-toggle=\"collapse\" data-target=\"#collapse" + i
          + "\" aria-expanded=\"false\" aria-controls=\"collapse" + i
          + "\" onclick='getTables(" + employee.employeeNumber + ")'>"
          + employee.firstName + " " + employee.lastName
          + "<div id=\"collapse" + i + "\""
          + "\" class=\"collapse\" aria-labelledby=\"heading\"" + i
          + " data-parent=\"#accordion\">"
          + "<div class=\"card-body\">"
          + "<ul id='list-tables-" + employee.employeeNumber + "' data-staff='"
          + employee.employeeNumber + "'>"
          + "</ul>"
          + "</div>"
          + "</div>"
          + "</div>");
      getTableAssignments(employee.employeeNumber);
    }
  });
}

/**
 * This function gets a list of assigned tables for a waiter.
 * @param staffId The employee number of the waiter.
 */
function getTableAssignments(staffId) {
  post("/api/authStaff/getTableAssignments", JSON.stringify({
        staffId: staffId
      }),
      (data) => {
        const tables = JSON.parse(data);
        const currentWaiter = $("#list-tables-" + staffId);
        currentWaiter.empty();
        for (let i = 0; i < tables.length; i++) {
          currentWaiter.append(
              "<li data-tableNum='" + tables[i].tableNumber
              + "' class='list-group-item' "
              + "id='table-" + tables[i].tableNumber + "'>"
              + "<span>Table </span>" + tables[i].tableNumber
              + "<i onclick='event.stopPropagation(); removeAssignment("
              + staffId + ", " + tables[i].tableNumber
              + ");' class=\"fas fa-arrow-circle-right right\"></i>"
              + "</li>"
          )
        }
      }
  );
}

/**
 * This function gets all the tables that aren't assigned to the clicked staff
 * member.
 * @param staffId The staff member we want to assign tables to.
 */
function getTables(staffId) {
  post("/api/authStaff/getUnAssignedTables", String(staffId), (data) => {
    const tables = JSON.parse(data);
    const list = $("#table-list");
    list.empty();
    for (let i = 0; i < tables.length; i++) {
      list.append("<div class='card' id='cardTable " + i + "'>"
          + "<div data-tableNum='" + tables[i].tableNumber
          + "' class='card-header' id='heading" + i + "'"
          + ">"
          + "<i class=\"fas fa-arrow-circle-left\" onclick='event.stopPropagation(); addAssignment("
          + staffId + ", " + tables[i].tableNumber + ")'></i>"
          + "<span class='right'>Table " + tables[i].tableNumber + "</span>"
          + "</div>"
          + "</div>");
    }
  });
}

/**
 * This function adds table assignments to a waiter.
 * @param staffId The staff member that is being assigned a table.
 * @param tableId The table that is being assigned.
 */
function addAssignment(staffId, tableId) {
  post("/api/authStaff/setTableAssignment",
      JSON.stringify({staffId: staffId, tableNumber: tableId}), (data) => {
        if (data !== "failure") {
          getTableAssignments(staffId);
          getTables(staffId);
        }
      }
  );
}

/**
 * This function removes a table assignment from a waiter.
 * @param staffId The staff member that the assignment is being removed
 * @param tableId The table that is being unassigned from the waiter.
 */
function removeAssignment(staffId, tableId) {
  post("/api/authStaff/removeTableAssignment",
      JSON.stringify({staffId: staffId, tableNumber: tableId}), (data) => {
        if (data !== "failure") {
          getTableAssignments(staffId);
          getTables(staffId);
        }
      });
}