$(document).ready(function () {
  loadEmployees();
});

function loadEmployees() {
  get("/api/authStaff/getEmployees", function(data) {
    var employees = JSON.parse(data);
    console.log(data);
    for (var i = 0; i < employees.length; i++) {
      var employee = employees[i];
      $("#employees").append("<tr>\n"
                             + "<td>" + employee.employeeNumber + "</td>\n"
                             + "<td>" + employee.firstName + "</td>\n"
                             + "<td>" + employee.lastName + "</td>\n"
                             + "<td>" + employee.department + "</td>\n"
                             + "<td>Actions to do</td>\n"
                           + "</tr>");
    }
  });
}