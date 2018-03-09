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
  $("#emp-first-" + id).html("<input id='emp-first-" + id + "-input' value='" + $("#emp-first-" + id).text() + "'>");
  $("#emp-last-" + id).html("<input id='emp-last-" + id + "-input' value='" + $("#emp-last-" + id).text() + "'>");
  $("#emp-department-" + id).html("<input id='emp-department-" + id + "-input' value='" + $("#emp-department-" + id).text() + "'>");
  $("#edit-" + id).remove();
  $("#actions-" + id).prepend("<i id='confirm-" + id + "' class='fa fa-check fa-lg confirm' onclick='confirmEdit(" + id + ")'></i>");
}

function confirmEdit(id) {
  $("#emp-first-" + id).html($("#emp-first-" + id + "-input").val());
  $("#emp-last-" + id).html($("#emp-last-" + id + "-input").val());
  $("#emp-department-" + id).html($("#emp-department-" + id + "-input").val());
  $("#actions-" + id).prepend("<i id='edit-" + id + "' class=\"fas fa-edit fa-lg edit\" onclick='startEdit(" + id + ");'></i>")
  $("#confirm-" + id).remove();
}

function remove(id) {
  bootbox.confirm("Are you sure you want to remove this item?", function (result) {
  });
}