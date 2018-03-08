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
              "<td>£" + response[i].price + "</td>" +
              "</tr>" +
              "<tr>" +
              "<td colspan=\"3\">" +
              "<div id=\"row" + (i + 1) + "\" class=\"collapse\">"
              + response[i].instructions + "</div>" +
              "</td>" +
              "</tr>"
          );
        }
      });
});

/**
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
