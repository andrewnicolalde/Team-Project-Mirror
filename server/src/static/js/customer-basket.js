var cost = [];

/**
 * This is the script that runs when the page loads. It creates an accordion that will contain one order in each of its cards.
 */
$(document).ready(function () {
    get("/api/authTable/getAllOrdersForTable",
        function (data) {
            const response = JSON.parse(data);
            for (let i = 0; i < response.length; i++) {
                // noinspection SpellCheckingInspection
                $("#accordion").append("<div class=\"card\">" +
                    "<div class=\"card-header\" id=\"heading\"" + i
                    + " data-toggle=\"collapse\" data-target=\"#collapse" + i
                    + "\" aria-expanded=\"false\" aria-controls=\"collapse\"" + i
                    + " >" +
                    "<h5 class=\"mb-0\">" +
                    "Order " + (i + 1) + " - " + response[i].status +
                    "<div class='total' style='float: right'>" + "£" + (cost[i] = calculateOneTotal(response[i].orderContents, cost[i])) + "</div>" +
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
                //Page currently doesn't load when this function is called
                //Page can take a few seconds to load and sometimes freezes when right clicking
            }
            $("#total").append("<button type=\"button\" class=\"btn btn-info\">" + "Total: £" + calculateAllTotals(cost) + "</button>");
        });
});

/**
 * This function loads one order
 * @param current the current order to be loaded
 * @returns {string} A string of html to be appended to the table-body in the script that calls this function.
 */
function loadOrder(current) {
    let order = "";
    for (let i = 0; i < current.length; i++) {
        order += "<tr>"
            + "<td>" + current[i].name + "</td>"
            + "<td>" + current[i].description + "</td>"
            + "<td>" + current[i].instructions + "</td>"
            + "<td>£" + current[i].price + "</td>"
            + "</tr>"
    }
    return order;
}

/**
 * This function returns the cost of all the items in one order.
 * @param current the current order.
 * @param cost The table for which we want to calculate the cost for.
 */
function calculateOneTotal(current, cost) {
    let currentcost = 0.0;
    if (current.length == 0) {
        return "0";
    }
    for (let i = 0; i < current.length; i++) {
        const item = current[i].price;
        currentcost += parseFloat(item);
    }
    return currentcost.toFixed(2);
}

/**
 * This function returns the cost to two decimal places of all orders for a transaction.
 * @param cost An array holding the costs for all the orders in the transaction.
 * @returns {string} The cost is returned as a string to be appended to the 'total' div in basket.html.
 */
function calculateAllTotals(cost) {
    let total = 0.0;
    for (let i = 0; i < cost.length; i++) {
        const order = cost[i];
        total += parseFloat(order);
    }
    return total.toFixed(2);
}

/**
 * This function is a helper function which gives getTransactionTotal a
 * transactionId.
 * @returns {Promise} transaction ID
 */
function getTransactionId() {
    return new Promise(function (resolve, reject) {
        get("/api/authTable/getTransactionId", function (data) {
            const response = JSON.parse(data);
            //console.log(response.transactionId);
            resolve(response.transactionId);
        });
    });
}

/**
 * This function is responsible for
 * @param transactionId
 * @returns the total price of the transaction
 */
function getTransactionTotal(transactionId) {
    const dataToSend = JSON.stringify({
        transactionId: transactionId
    });
    return new Promise(function (resolve, reject) {
        post("/api/authTable/getTransactionTotal", dataToSend, function (data) {
            resolve(data);
        });
    });
}

/**
 * This function hides the payment options modal
 */
function hidePaymentModal() {
    $('#paymentModal').modal('hide');
}

/**
 * This function hides the cash payment modal
 */
function hideCashPaymentModal() {
    $('#cashPaymentModal').modal('hide');
}

/**
 * This function calls a waiter to the table
 */
function callWaiterToTable() {
    bootbox.alert("Your waiter has been called, and will be with you shortly.");
}
