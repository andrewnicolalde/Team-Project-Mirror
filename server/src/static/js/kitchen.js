/**
 * @module Kitchen
 */

/**
 * The document ready function for the kitchen page.
 * Sets up all the pages jQuery functions.
 */
$(document).ready(function () {
  getCookingOrders();

  if (browserSupportsPush()) {
    // add a button users can click to get push notifications.
    if (!havePermissions()) {
      const button = "<button id='notify' class='btn' onclick='getPermissionAndSubscribe(\"kitchen-notification-worker.js\")'>Notifications</button>";
      $('.nav').append(button);
    } else {
      setUpPush('kitchen-notification-worker.js');
    }
  }

  navigator.serviceWorker.addEventListener('message', (event) => {
    displayOrders(JSON.stringify(event.data));
    console.log(event.data); // TODO remove!
  });
});

function getCookingOrders() {
  const statusCooking = "COOKING";
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

  const response = JSON.parse(data);

  const currentOrderElement = $("sidebar-orders");
  currentOrderElement.empty();

  // If there are more than 4 orders. Add the last ones to the sidebar.
  if (response.length > 4) {
    for (let i = 4; i < response.length; i++) {
      const sideId = response[i].foodOrderId;

      if(!orderPresent(sideId)){
        $("#sidebar-orders").append("<li id='" + sideId + "' data-timeConfirmed='" + response[i].timeConfirmed + "'>\n"
            + "<h4>Order " + sideId + "</h4>"
        + "</li>");
      }
    }
  }
  // Add the first 4 to the page in lists of items.
  for (let j = 0; j < response.length && j < 4; j++) {
    const Id = response[j].foodOrderId;
    if(!orderPresent(Id)){

      $("#row").append("<div class='col' id='"+ Id +"' data-timeConfirmed = '"+ response[j].timeConfirmed +"'>"
          + "<ul class='list-group' id='list-"+ Id +"'>"
          + "<li class='list-group-item'> "
          + "<h2>Order " + Id + "</h2> "
          + "<div>" + shortTime(response[j].timeConfirmed)
          + "<button type='button' class='btn btn-success' data-orderId='"+ Id +"' onclick='orderDone(this.getAttribute(\"data-orderId\"))'>Done</button></div>"
          + "</li>"
          + "</ul>"
          + "</div>");
      getOrderItems(Id);
    }
  }
}

/**
 * Reduces the full time stamp to just the hours minutes and seconds.
 * @param time The full timestamp.
 * @return {string} The condensed time.
 */
function shortTime(time) {
  const orderTime = new Date(time);
  const shortTime = paddedTime(orderTime.getHours()) + ":" + paddedTime(
      orderTime.getMinutes());
  return shortTime;
}

/**
 * Helper function to ensure time is formatted correctly.
 * @param time The original time.
 * @return {string} A formatted string of the time.
 */
function paddedTime(time) {
  let padded;
  if (time < 10) {
    padded = "0" + time;
  } else {
    padded = time;
  }
  return padded;
}

/**
 * Callback function to change the status of an order.
 * @param orderId The ID of the order to be changed.
 */
function orderDone(orderId) {
  // post to api/authStaff/changeOrderStatus
  const statusReady = "READY_TO_DELIVER";
  post("/api/authStaff/changeOrderStatus",
      JSON.stringify({
        orderId:orderId,
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
  const response = JSON.parse(data);

  for (let i = 0; i < response.length; i++) {
    let item = "<li class='list-group-item'><div><b>" + response[i].name
        + "</b><input type='checkbox' class='custom-checkbox'></div>";
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
    const parent = document.getElementById("row");
    const child = document.getElementById(Id);
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
 * Helper function to calculate the difference between the current time and the given time in milliseconds.
 * @param id Id of the order to get the time from.
 * @return {number} The difference in milliseconds.
 */
function getTimeDifference(id) {
  const currentTime = Date.now();
  const orderDate = new Date(
      document.getElementById(id).getAttribute("data-timeConfirmed"));
  const orderTime = orderDate.getTime();
  return (currentTime - orderTime);
}

function checkSidebarTimes() {
  const orders = $("#sidebar-orders").find("> li");
  for (let i = 0; i < orders.length; i++) {
    const difference = getTimeDifference(orders[i].id);
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
  const orders = $("#row").find("> .col");
  for (let i = 0; i < orders.length; i++) {
    const difference = getTimeDifference(orders[i].id);
    const items = $("#list-" + orders[i].id).find("> li");
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