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

        for (let i = 0; i < tables.length; i++) {
          currentWaiter.append(
              "<li "
              + "id='table-" + tables[i].tableNumber + "'>"
              + "<span>Table </span>" + tables[i].tableNumber
              + "<i class=\"fas fa-arrow-circle-right\"></i>"
              + "</li>"
          )
        }
      }
  );
}

function getTables(staffId) {
  post("/api/authStaff/getAllTablesAssignments", String(staffId), (data) => {
     const tables = JSON.parse(data);
    const list = $("#table-list");
    list.empty();
     for (let i = 0; i < tables.length; i++) {
        list.append("<div class='card' id='cardTable "+ i + "'>"
          + "<div class='card-header' id='heading" + i + "'"
            + ">"
            + "<i class=\"fas fa-arrow-circle-left\"></i>"
            + "<span>Table " + tables[i].tableNumber + "</span>"
            + "<span class='bold'> (" + tables[i].assignments + ")</span>"
            + "</div>"
            + "</div>");
     }
  });
}