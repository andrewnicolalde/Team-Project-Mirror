/**
 * The document ready function for the kitchen page.
 * Sets up all the pages jQuery functions.
 */
$(document).ready(function () {
  var tid = setInterval(getCookingOrders, 5000);

  getCookingOrders();
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

  // If there are more than 4 orders. Add the last ones to the sidebar.
  if (response.length > 4) {
    for (var i = 4; i < response.length; i++) {
      if(!orderPresent(response[i].foodOrderId)){
        $(".order-list").append("<li class='" + response[i].foodOrderId + "'>\n"
        + "<h6>Order No: " + response[i].foodOrderId + "</h6>"
        + "</li>");
      }
    }
  }
  // Add the first 4 to the page in lists of items.
  for (var j = 0; j < response.length && j < 4; j++) {
    if(!orderPresent(response[j].foodOrderId)){
      $(".row").append("<div class='col " + response[j].foodOrderId + " text-center'>"
          + "<h2>Order " + response[j].foodOrderId +"</h2></div>");
      getOrderItems(response[j].foodOrderId);
    }
  }
}

/**
 * Gets the items from the order from the database. Displays them.
 * @param foodOrderId The ID of the Order which we are getting the items of.
 */
function getOrderItems(foodOrderId) {
  // get the order items.
  post("/api/authStaff/getOrderItems",
      JSON.stringify({orderId:foodOrderId}),
      function (data) {
        displayOrderItems(data, foodOrderId);
      });
}

/**
 * Add the Order Items to the DOM to be displayed.
 * @param data The JSON returned by the post request.
 * @param foodOrder The ID of the order. To add the elements to the right column.
 */
function displayOrderItems(data, foodOrder) {
  var response = JSON.parse(data);

  for (var i = 0; i < response.length; i++) {
    $("." +foodOrder).append("<div class='card'>" + response[i].name +"<br> "+ response[i].instructions +"</div>");
  }
}

/**
 * Check if an order number is being displayed on the screen.
 * @param orderNum The order number to be checked.
 * @returns {boolean} True if present on page, False if not.
 */
function orderPresent(orderNum) {
  var present = document.getElementsByClassName(orderNum);
  return Boolean(present.length !== 0);
}