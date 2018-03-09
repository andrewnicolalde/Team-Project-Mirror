$(document).ready(function () {
  get("/api/authTable/getAllOrdersForTable",
      function (data) {
        var response = JSON.parse(data);
        for (var i = 0; i < response.length; i++) {
          $("#accordion").append("<div class=\"card\">" +
              "<div class=\"card-header\" id=\"heading\"" + i
              + " data-toggle=\"collapse\" data-target=\"#collapse" + i
              + "\" aria-expanded=\"false\" aria-controls=\"collapse\"" + i
              + " >" +
              "<h5 class=\"mb-0\">" +
              "Order " + (i + 1) +
              "</h5>" +
              "</div>" +
              "<div id=\"collapse" + i
              + "\"  class=\"collapse\" aria-labelledby=\"heading\"" + i
              + " data-parent=\"#accordion\">" +
              "<div class=\"card-body\">" +
              "<table class=\"table table-hover\">" +
              //names of the columns of the table, to be displayed in table head (thead)
              "<thead>" +
              "<tr>" +
              "<th scope=\"col\">Name</th>" +
              "<th scope=\"col\">Description</th>" +
              "<th scope=\"col\">Instructions</th>" +
              "<th scope=\"col\">Price</th>" +
              "</tr>" +
              "</thead>" +
              "<tbody id=\"table-body" + i + "\">" +
              loadOrder(response[i].orderContents) +
              "</tbody>" +
              "</table>" +
              "</div>" +
              "</div>" +
              "</div>"
          );
          //Page currently doesnt load when this function is called
          //Page can take a few seconds to load and sometimes freezes when right clicking
        }
      });
});

/**
 * Loads all the items for one order.
 */
function loadOrder(current) {
  //parse JSON
  for (var j = 0; j < current.length; j++) {
    var order = "<tr data-toggle=\"collapse\" data-target=\"#row\"";
    order += (j + 1) + "\" class=\"clickable\">" +
        "<td>" + current[j].name + "</td>" +
        "<td>" + current[j].description + "</td>" +
        "<td>" + current[j].instructions + "</td>" +
        "<td>£" + current[j].price + "</td>" +
        "</tr>";
    console.log(order);
    $("#table-body" + j).append(order);
  }
}