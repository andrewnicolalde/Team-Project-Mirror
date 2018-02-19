/**
 * The document ready function for the kitchen page.
 * Sets up all the pages jQuery functions.
 */
$(document).ready(function () {
  //var tid = setInterval(checkPage, 15000);

  //checkPage();
});

function getCookingOrders() {
  var statusCooking = "COOKING";
  post("/api/authStaff/getOrdersByStatus",
      JSON.stringify({ orderStatus:statusCooking }),
      function (data) {
        displayOrders(data);
  });
}

/**
 * Takes JSON of all orders that are cooking. Appends the least important ones
 * to the sidebar.
 * @param data JSON of cooking orders. TODO Ordered by timeconfirmed??
 */
function displayOrders(data) {

  var response = JSON.parse(data);

  if (response.length > 4) {
    for (var i = 4; i < response.length; i++) {
      $(".order-list").append("<li class='" + response[i].foodOrderId + "'>\n"
      + "<h6>Order No: " + response[i].foodOrderId + "</h6>"
      + "</li>");
    }
  }
  for (var j = 0; j < response.length && j < 4; j++) {
    getOrderItems(response[j].foodOrderId);
  }
  // Do something with the first 4.
  // Add the rest to the sidebar.
}

function getOrderItems(foodOrderId) {
  // get the order items.
  post("/api/authStaff/getOrderItems",
      JSON.stringify({orderId:foodOrderId}),
      function (data) {
        dispayOrderItems(data, foodOrderId);
      });
}

function displayOrderItems(data, foodOrderId) {
  var response = JSON.parse(data);

  var column = "<div class='col" + foodOrderId + "'>Order "+ foodOrderId;
  for (var i = 0; i < response.length; i++) {
    // Add cards etc.
  }

}

/**
 * Check if an order number is being displayed on the screen.
 * @param orderNum The order number to be checked.
 * @returns {boolean} True if present on page, False if not.
 */
function orderPresent(orderNum) {
  var present = document.getElementsByClassName(orderNum);
  return present.length !== 0;
}