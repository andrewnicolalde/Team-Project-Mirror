$(document).ready(function() {
  get("/api/authStaff/getMenu", function(data) {
    console.log(data);
  });
});