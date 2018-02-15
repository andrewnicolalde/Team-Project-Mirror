/**
 *This script retrieves the current order for a given
 *table so that it can be displayed in the basket page.
 */
$(document).ready(function () {
  post("/api/authStaff/getOrderItems", function (data) {
    //parse JSON
    var response = JSON.parse(data);
    for (i = 0; i < response.length; i++) {
      $("#table-body").append("<tr>" +
          "<th scope=\"row\">" + (i + 1) + "</th>" +
          "<td>" + response[i].name + "</td>" +
          "<td>" + response[i].price + "</td>" +
          "</tr>");
    }
  });
});