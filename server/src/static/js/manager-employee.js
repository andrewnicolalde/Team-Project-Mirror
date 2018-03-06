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
                             + "<td id='emp-id-" + employee.employeeNumber + "'>" + employee.employeeNumber + "</td>\n"
                             + "<td id='emp-first-" + employee.employeeNumber + "'>" + employee.firstName + "</td>\n"
                             + "<td id='emp-last-" + employee.employeeNumber + "'>" + employee.lastName + "</td>\n"
                             + "<td id='emp-department-" + employee.employeeNumber + "'>" + employee.department + "</td>\n"
                             + "<td id='actions-" + employee.employeeNumber + "'><i id='edit-" + employee.employeeNumber + "' class=\"fas fa-edit fa-lg edit\" onclick='startEdit(" + employee.employeeNumber + ");'></i><i class=\"fa fa-times fa-lg remove\" onclick='remove(" + employee.employeeNumber + ");'></i></td>\n"
                           + "</tr>");
    }
  });
}

function startEdit(id) {
  $("#emp-first-" + id).html("<input>");
  $("#emp-last-" + id).html("<input>");
  $("#emp-department-" + id).html("<input>");
  $("#edit-" + id).remove();
  $("#actions-" + id).prepend("<i id='confirm-" + id + "' class='fa fa-check fa-lg confirm'");
}

function remove(id) {
  bootbox.confirm("Are you sure you want to remove this item?", function (result) {
  });
}