$(document).ready(function () {
    get("/api/authTable/getAllOrdersForTable",
        function (data) {
            var response = JSON.parse(data);
            var current;
            for (i = 0; i < response.length; i++) {
                current = response[i];
                $("#accordion").append("<div class=\"card\">" +
                    "<div class=\"card-header\" id=\"heading\"" + i + " data-toggle=\"collapse\" data-target=\"#collapse" + i + "\" aria-expanded=\"false\" aria-controls=\"collapse\"" + i + " >" +
                    "<h5 class=\"mb-0\">" +
                    "Order " + (i+1) +
                    "</h5>" +
                    "</div>" +
                    "<div id=\"collapse" + i + "\"  class=\"collapse\" aria-labelledby=\"heading\"" + i + " data-parent=\"#accordion\">" +
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
                    "<tbody id=\"table-body" + i + "\">" +
                    <!--tr to /tr is one table row-->
                    <!--th is the row number-->
                    <!--td are the columns in a row-->
                    "</tbody>" +
                    "</table>" +
                    "</div>" +
                    "</div>" +
                    "</div>"
                );
                //Page currently doesnt load when this function is called
                //Page can take a few seconds to load and sometimes freezes when right clicking
                //loadOrder(current.orderContents);
            }
        });
});

/**
 * Loads all the items for one order.
 */
function loadOrder(current){
            //parse JSON
            var response = current;
            var order = "<tr data-toggle=\"collapse\" data-target=\"#row\"";
            for (i = 0; i < response.length; i++) {
                order += (i + 1) + "\" class=\"clickable\">" +
                    "<th scope=\"row\">" + (i + 1) + "</th>" +
                    "<td>" + response.name + "</td>" +
                    "<td>" + response.instructions + "</td>" +
                    "<td>£" + response.price + "</td>" +
                    "</tr>"
            }
            $("#table-body"+i).append(order);
}