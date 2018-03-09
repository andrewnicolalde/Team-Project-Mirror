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
  var order = "";
  for (var i = 0; i < current.length; i++) {
    order += "<tr>"
        + "<td>" + current[i].name + "</td>"
        + "<td>" + current[i].description + "</td>"
        + "<td>" + current[i].instructions + "</td>"
        + "<td>£" + current[i].price + "</td>"
        + "</tr>"
  }
  return order;
}

 * This function is a helper function which gives getTransactionTotal a
 * transactionId.
 * @returns {Promise} transaction ID
 */
function getTransactionId(){
  return new Promise(function (resolve, reject) {
    get("/api/authTable/getTransactionId", function(data) {
      var response = JSON.parse(data);
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
  var dataToSend = JSON.stringify({
    transactionId: transactionId
  });
  return new Promise(function (resolve, reject){
    post("/api/authTable/getTransactionTotal", dataToSend, function(data){
      resolve(data);
    });
  });
}

/**
 * This function hides the payment options modal
 */
function hidePaymentModal(){
  $('#paymentModal').modal('hide');
}

/**
 * This function hides the cash payment modal
 */
function hideCashPaymentModal(){
  $('#cashPaymentModal').modal('hide');
}

/**
 * This function calls a waiter to the table
 */
function callWaiterToTable() {
  bootbox.alert("Your waiter has been called, and will be with you shortly.");
}
