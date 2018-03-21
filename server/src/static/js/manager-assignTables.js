$(document).ready(() => {
  getEmployees();
});


function getEmployees() {
  get("/api/authStaff/getWaiters", (data) => {
    const employees = JSON.parse(data);
    for (let i = 0; i < employees.length; i++) {
      const employee = employees[i];
      $("#accordian-staff").append("<div class=\"card\">"
          + "<div class=\"card-header\" id=\"heading\"" + i
          + " data-toggle=\"collapse\" data-target=\"#collapse" + i
          + "\" aria-expanded=\"false\" aria-controls=\"collapse\"" + i
          + " >"
          + employee.firstName + " " + employee.lastName);
          + <div 

    }
  });
}