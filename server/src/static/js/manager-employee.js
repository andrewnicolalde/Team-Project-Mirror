$(document).ready(function () {
  loadEmployees();
});

function loadEmployees() {
  get("/api/authStaff/getEmployees", function(data) {
    console.log(data);
  });
}