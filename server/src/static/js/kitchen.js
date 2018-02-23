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
        checkSidebarTimes();
  });
}

/**
 * Takes JSON of all orders that are cooking. Appends the least important ones
 * to the sidebar.
 * @param data JSON of cooking orders.
 */
function displayOrders(data) {

  var response = JSON.parse(data);

  var currentOrderElement = document.getElementById("sidebar-orders");
  while (currentOrderElement.firstChild) {
    currentOrderElement.removeChild(currentOrderElement.firstChild);
  }

  // If there are more than 4 orders. Add the last ones to the sidebar.
  if (response.length > 4) {
    for (var i = 4; i < response.length; i++) {
      var sideId = response[i].foodOrderId;

      if(!orderPresent(sideId)){
        $("#sidebar-orders").append("<li id='" + sideId + "' data-timeConfirmed='" + response[i].timeConfirmed + "'>\n"
        + "<h4>Order No: " + sideId + "</h4>"
        + "</li>");
      }
    }
  }
  // Add the first 4 to the page in lists of items.
  for (var j = 0; j < response.length && j < 4; j++) {
    var Id = response[j].foodOrderId;
    if(!orderPresent(Id)){

      $("#row").append("<div class='col' id='"+ Id +"' data-timeConfirmed = '"+ response[j].timeConfirmed +"'>"
          + "<ul class='list-group' id='list-"+ Id +"'>"
          + "<li class='list-group-item'> "
          + "<h2>Order " + Id + "</h2> "
          + "<div>20:12.12"
          + "<button type='button' class='btn btn-success' data-orderId='"+ Id +"' onclick='orderDone(this.getAttribute(\"data-orderId\"))'>Done</button></div>"
          + "</li>"
          + "</ul>"
          + "</div>");
      getOrderItems(Id);
    }
  }
}

/**
 * Callback function to change the status of an order.
 * @param orderId The ID of the order to be changed.
 */
function orderDone(orderId) {
  // post to api/authStaff/changeOrderStatus
  var statusReady = "READY_TO_DELIVER";
  post("/api/authStaff/changeOrderStatus",
      JSON.stringify({
        orderNumber:orderId,
        newOrderStatus:statusReady
      }), function (data) {
        removeFromScreen(data, orderId);
      });
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
        checkMainPageTimes();
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
    var item = "<li class='list-group-item'><div><b>"+ response[i].name +"</b><input type='checkbox' class='custom-checkbox'></div>";
    if (!(response[i].instructions === "")) {
      item += response[i].instructions
    }
    item += "</li>";
    $("#list-" + foodOrder).append(item);
  }
}

/**
 * Removes a particular Order from the screen.
 * @param data The response from the server.
 * @param Id The Id of the Order to be removed.
 */
function removeFromScreen(data, Id) {

  if (data !== "success") {
    throw new Error();
  } else {
    var parent = document.getElementById("row");
    var child = document.getElementById(Id);
    parent.removeChild(child);
    getCookingOrders();
  }
}

/**
 * Check if an order number is being displayed on the screen.
 * @param orderNum The order number to be checked.
 * @returns {boolean} True if present on page, False if not.
 */
function orderPresent(orderNum) {
  return Boolean(document.getElementById(orderNum) !== null);
}

/**
 * Helper function to calculate the difference between the current time and the given time in millliseconds.
 * @param id Id of the order to get the time from.
 * @return {number} The difference in milliseconds.
 */
function getTimeDifference(id) {
  var currentTime = Date.now();
  var orderDate = new Date(document.getElementById(id).getAttribute("data-timeConfirmed"));
  var orderTime = orderDate.getTime();
  return (currentTime - orderTime);
}

function checkSidebarTimes() {
  var orders = $("#sidebar-orders").find("> li");
  for (var i = 0; i< orders.length; i++) {
    var difference = getTimeDifference(orders[i].id);
    if (difference > 1200000) {
      if ($(orders[i].id).hasClass("yellow")) {
        $(orders[i].id).removeClass("yellow");
      }
      orders[i].classList.add("red");
    } else if (difference > 600000) {
      if ($(orders[i].id).hasClass("green")) {
        $(orders[i].id).removeClass("green");
      }
      orders[i].classList.add("yellow");
    } else {
      orders[i].classList.add("green");
    }
  }
}

function checkMainPageTimes() {
  var orders = $("#row").find("> .col");
  for (var i = 0; i < orders.length; i++) {
    var difference = getTimeDifference(orders[i].id);
    var items = $("#list-" + orders[i].id).find("> li");
    if (difference > 1200000) {
      for(var j = 0; j < items.length; j++) {
        if ($(items[j].id).hasClass("yellow")) {
          $(items[j].id).removeClass("yellow");
        }
        items[j].classList.add("red");
      }
    } else if (difference > 600000) {
      for(var j = 0; j < items.length; j++) {
        if ($(items[j].id).hasClass("green")) {
          $(items[j].id).removeClass("green");
        }
        items[j].classList.add("yellow");
      }
    } else {
      for(var j = 0; j < items.length; j++) {
        items[j].classList.add("green");
      }
    }
  }
}