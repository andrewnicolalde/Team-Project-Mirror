/**
 *This script retrieves the current order for a given
 *table so that it can be displayed in the basket page.
 */
$(document).ready(function () {
  // TODO: Update path once real endpoint is set up
  post("/api/authTable/getOrderItems",
      JSON.stringify({
        orderId: sessionStorage.getItem("orderId")
      }), function (data) {
        //parse JSON
        var response = JSON.parse(data);
        for (i = 0; i < response.length; i++) {
          $("#table-body").append("<tr data-toggle=\"collapse\" data-target=\"#row"
              + (i + 1) + "\" class=\"clickable\">" +
              "<th scope=\"row\">" + (i + 1) + "</th>" +
              "<td>" + response[i].name + "</td>" +
              "<td>" + response[i].instructions + "</td>" +
              "<td>£" + response[i].price + "</td>" +
              "</tr>"
          );
        }
      });
});