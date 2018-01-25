/**
 * Preliminary method to update the page dynamically with new orders.
 * @param data the data response to an ajax query.
 */
function updatePage() {
  $.post("/api/auth/kitchen", function(data) {
    var response = JSON.parse(data);
    var displayedOrders = [];

    for (var i = 0; i < response.length; i++) {
      // check if in temp array, -1 means its not.
      if (displayedOrders.indexOf(response[i].orderId) === -1) {
        // if its not check if its on the page.
        if (orderPresent(response[i].orderId)) {
          displayedOrders.push(response[i].orderId);
          var column = "<div class='col '+ response[i].orderId>\n";
          for (var j = 0; j < response[i].orderContents.length; j++) {
            column = column + "<div class='card text-center'>\n"
                + "<div class='card-header'> Order: " + response[i].orderId + "</div>\n"
                + "<div class='card-body'> Item: " + response[i].orderContents[j].itemName + "\n"
                + "<br />Requirements: " + response[i].orderContents[j].requirements + "</div>\n"
                + "</div>\n";
          }
          column = column + "</div>"
          $(".row").append(column);
        }
      }
    }
  });
}

function orderPresent( orderNum) {
  var present = document.getElementsByClassName(orderNum);
  return present.length !== 0;
}