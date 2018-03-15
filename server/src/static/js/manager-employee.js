let departments;

$(document).ready(function () {
  loadEmployees();
  get("/api/authStaff/getDepartments",
      function(data) {
        departments = JSON.parse(data);
      });
});

function loadEmployees() {
  get("/api/authStaff/getEmployees", function(data) {
    const employees = JSON.parse(data);
    for (let i = 0; i < employees.length; i++) {
      const employee = employees[i];
      $("#employees").append("<tr id='emp-" + employee.employeeNumber + "'>\n"
                             + "<td id='emp-id-" + employee.employeeNumber + "'>" + employee.employeeNumber + "</td>\n"
                             + "<td id='emp-first-" + employee.employeeNumber + "'>" + employee.firstName + "</td>\n"
                             + "<td id='emp-last-" + employee.employeeNumber + "'>" + employee.lastName + "</td>\n"
                             + "<td id='emp-department-" + employee.employeeNumber + "'>" + employee.department + "</td>\n"
                             + "<td id='actions-" + employee.employeeNumber + "'><i id='edit-" + employee.employeeNumber + "' class=\"fas fa-edit fa-lg edit\" onclick='startEdit(" + employee.employeeNumber + ");'></i><i class=\"fa fa-times fa-lg remove\" onclick='remove(" + employee.employeeNumber + ");'></i></td>\n"
                           + "</tr>");
    }
  });
}

function createDepartmentOptions(department) {
  let departmentsOptions = "";
  for (let i = 0; i < departments.length; i++) {
    if (departments[i] === department) {
      departmentsOptions += "<option selected>" + departments[i] + "</option>\n";
    } else {
      departmentsOptions += "<option>" + departments[i] + "</option>\n";
    }
  }
  return departmentsOptions;
}

function startEdit(id) {
  $("#emp-first-" + id).html("<input id='emp-first-" + id + "-input' value='" + $("#emp-first-" + id).text() + "'>");
  $("#emp-last-" + id).html("<input id='emp-last-" + id + "-input' value='" + $("#emp-last-" + id).text() + "'>");
  $("#emp-department-" + id).html("<select id='emp-department-" + id + "-input'>" + createDepartmentOptions($("#emp-department-" + id).text()) + "</select>");
  $("#edit-" + id).remove();
  $("#actions-" + id).prepend("<i id='confirm-" + id + "' class='fa fa-check fa-lg confirm' onclick='confirmEdit(" + id + ")'></i>");
}

function confirmEdit(id) {
  const dataToSend = JSON.stringify({
    employeeNumber: id,
    firstName: $("#emp-first-" + id + "-input").val(),
    lastName: $("#emp-last-" + id + "-input").val(),
    department: $("#emp-department-" + id + "-input").val()
  });

  post("/api/authStaff/editStaff",
      dataToSend,
      function(data) {
        if (data === "success") {
          $("#emp-first-" + id).html($("#emp-first-" + id + "-input").val());
          $("#emp-last-" + id).html($("#emp-last-" + id + "-input").val());
          $("#emp-department-" + id).html($("#emp-department-" + id + "-input").val());
          $("#actions-" + id).prepend("<i id='edit-" + id + "' class=\"fas fa-edit fa-lg edit\" onclick='startEdit(" + id + ");'></i>");
          $("#confirm-" + id).remove();
        }
      });
}

function startAdd() {
  $("#employees").append("<tr id='new-employee-form'>\n"
        + "<td id='emp-id-new'></td>\n"
        + "<td id='emp-first-new'><input id='emp-first-new-input'></td>\n"
        + "<td id='emp-last-new'><input id='emp-last-new-input'></td>\n"
        + "<td id='emp-department-new'><select id='emp-department-new-input'>" + createDepartmentOptions("") + "</select></td>\n"
        + "<td id='actions-new'><i id='confirm-new' class='fas fa-check fa-lg confirm' data-toggle='modal' data-target='#password-modal'></i></td>\n"
      + "</tr>");
  $("#add-staff-btn").hide();
}

function showPasswordModal() {

}

function confirmAdd() {
  const dataToSend = JSON.stringify({
    firstName: $("#emp-first-new-input").val(),
    lastName: $("#emp-last-new-input").val(),
    department: $("#emp-department-new-input").val()
  });

  post("/api/authStaff/addStaff",
      dataToSend,
      function(data) {
        employee = JSON.parse(data);
        // Remove new employee form
        $("#new-employee-form").remove();

        // Add new employee
        $("#employees").append("<tr id='emp-" + employee.employeeNumber + "'>\n"
            + "<td id='emp-id-" + employee.employeeNumber + "'>" + employee.employeeNumber + "</td>\n"
            + "<td id='emp-first-" + employee.employeeNumber + "'>" + employee.firstName + "</td>\n"
            + "<td id='emp-last-" + employee.employeeNumber + "'>" + employee.lastName + "</td>\n"
            + "<td id='emp-department-" + employee.employeeNumber + "'>" + employee.department + "</td>\n"
            + "<td id='actions-" + employee.employeeNumber + "'><i id='edit-" + employee.employeeNumber + "' class=\"fas fa-edit fa-lg edit\" onclick='startEdit(" + employee.employeeNumber + ");'></i><i class=\"fa fa-times fa-lg remove\" onclick='remove(" + employee.employeeNumber + ");'></i></td>\n"
            + "</tr>");
      });
}

function checkPasswordsMatch() {
  const pwd1 = $("#pwd1");
  const pwd2 = $("#pwd2");
  const btn = $("#confirm-pwd");
  if (pwd1.val() === pwd2.val() && pwd1.val().length > 0) {
    btn.removeClass("disabled");
    btn.addClass("active");
  } else {
    btn.removeClass("active");
    btn.addClass("disabled");
  }
}

function remove(id) {
  bootbox.confirm("Are you sure you want to remove this employee account?", function (result) {
    if(result) {
      const dataToSend = JSON.stringify({employeeNumber: id});
      post("/api/authStaff/removeStaff",
          dataToSend,
          function (data) {
            if (data === "success") {
              $("#emp-" + id).remove();
            }
          })
    }
  });
}