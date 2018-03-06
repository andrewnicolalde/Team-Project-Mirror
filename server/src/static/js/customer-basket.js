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
                var order = "<tr data-toggle=\"collapse\" data-target=\"#row"
                    + (i + 1) + "\" class=\"clickable\">" +
                    "<th scope=\"row\">" + (i + 1) + "</th>" +
                    "<td>" + response[i].name + "</td>" +
                    "<td>£" + response[i].price + "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td colspan=\"3\">" +
                    "<div id=\"row" + (i + 1) + "\" class=\"collapse\">"
                    + response[i].instructions + "</div>" +
                    "</td>" +
                    "</tr>"
            }
            for (i = 0; i < response.length; i++) {
                $("#panels").append("<div class=\"card\">" +
                    "<div class=\"card-header\" id=\"heading\"" + i + " data-toggle=\"collapse\" data-target=\"#collapse\"" + i + " aria-expanded=\"false\" aria-controls=\"collapse\"" + i + " >" +
                    "<h5 class=\"mb-0\">" +
                    "Order 2" +
                    "</h5>" +
                    "</div>" +
                    "<div id=\"collapse\"" + i + "  class=\"collapse\" aria-labelledby=\"heading\"" + i + " data-parent=\"#accordion\">" +
                    "<div class=\"card-body\">" +
                    "<table class=\"table table-hover\">" +
                    //names of the columns of the table, to be displayed in table head (thead)
                    "<thead>" +
                    "<tr>" +
                    "<th scope=\"col\">#</th>" +
                    "<th scope=\"col\">Description</th>" +
                    "<th scope=\"col\">Instructions</th>" +
                    "<th scope=\"col\">Price</th>" +
                    "</tr>" +
                    "</thead>" +
                    "<tbody id=\"table-body\">" +
                    <!--tr to /tr is one table row-->
                    <!--th is the row number-->
                    <!--td are the columns in a row-->
                    "</tbody>" +
                    "</table>" +
                    "orderinfos2" +
                    "</div>" +
                    "</div>" +
                    "</div>"
                );
            }
            $('#table-body').append(order);
        });
});