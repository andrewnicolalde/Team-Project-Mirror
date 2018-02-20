/**
 * This function returns the selected table.
 * @returns The web element representing the current table
 */
function getActiveOrder() {
  var allOrders = document.getElementById("orders-list").children;
  var activeTable;
  for (i = 0; i < allOrders.length; i++) {
    if (allOrders[i].classList.contains("active")) {
      activeTable = allOrders[i];
    }
  }
  return activeTable;
}

/**
 * This function is responsible for adding menu items to the current order
 * of the currently active table.
 * @param menuItemId the ID of the menu item to be added to the order
 */

function addToOrder(menuItemId) {
  var activeOrder = getActiveOrder();

  // TODO: Remove this and add an actual way to add instructions.
  var requirements = "These are test instructions";

  // Create name-value pairs for HTTP post request, see
  // https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
  var nameValuePairs = JSON.stringify({
    orderNumber: activeOrder.getAttribute('data-ordernum'),
    menuItemId: menuItemId,
    requirements: requirements
  });

  // Handle possible responses
  post("/api/authStaff/addItemToOrder", nameValuePairs, function (status) {
    loadOrder(activeOrder.getAttribute('data-ordernum'));
    if (status === "") {
      // Refresh current order table to show new change
      console.log("Add item to order failed");
      console.log(status);
    }
  });
}

/**
 * This script is responsible for retrieving and displaying the contents of each
 * Table's current order. It also clears the Current Order column of any existing
 * entries before adding the selected Table's entries to the Current Order column.
 *
 * @param orderNumber The number of the order to load.
 */
function loadOrder(orderNumber) {
  var orderNumberToSend = JSON.stringify({orderNumber: orderNumber});
  post("/api/authStaff/getOrderItems", orderNumberToSend, function (data) {

    // Parse JSON
    var response = JSON.parse(data);

    // Remove any existing elements in the current order list
    var currentOrderElement = document.getElementById("current-order");
    while (currentOrderElement.firstChild) {
      currentOrderElement.removeChild(currentOrderElement.firstChild);
    }

    // Add each list item
    for (i = 0; i < response.length; i++) {
      $("#current-order").append("<li class='list-group-item list-group-item-action'"
          + "id= \"order-item-" + i + "\">"
          + "<span class='waiter-ui-span-bold'>"
          + response[i].name + ": </span> "
          + response[i].price + "</li>");
      // Show dietary information
      if (response[i].is_gluten_free) { // Gluten Free
        $("#order-item-" + i).append(" <img src="
            + "'../images/gluten-free.svg'alt='Gluten Free'>");
      }
      if (response[i].is_vegetarian) { // Vegetarian
        $("#order-item-" + i).append(" <img src="
            + "'../images/vegetarian-mark.svg'alt='Vegetarian'>");
      }
      if (response[i].is_vegan) {
        $("#order-item-" + i).append(" <img src="
            + "'../images/vegan-mark.svg'alt='Vegan'>");
      }
    }
  });
}

/**
 * This function is responsible for retrieving and displaying the menu
 * item elements in the Menu column in waiter.html.
 */
function loadMenu() {
  // Send get request to server for menu JSON
  get("/api/authStaff/getMenu", function (data) {
    // Parse JSON
    var response = JSON.parse(data);

    // Add items to menu list
    for (i = 0; i < response.length; i++) {
      $("#menu-list").append("<li class='list-group-item list-group-item-action'"
          + "id= \"menu-item-" + i + "\""
          + "data-menuItemNum='" + response[i].id + "'"
          + "onclick='addToOrder(this.getAttribute(\"data-menuItemNum\"))'>"
          + "<span class='waiter-ui-span-bold'>"
          + response[i].name + ": </span> " + response[i].price + "</li>");
      // Show dietary information
      if (response[i].is_gluten_free) { // Gluten Free
        $("#menu-item-" + i).append(
            " <img src='../images/gluten-free.svg' alt='Gluten Free'>");
      }
      if (response[i].is_vegetarian) { // Vegetarian
        $("#menu-item-" + i).append(
            " <img src='../images/vegetarian-mark.svg' alt='Vegetarian'>");
      }
      if (response[i].is_vegan) {
        $("#menu-item-" + i).append(
            " <img src='../images/vegan-mark.svg' alt='Vegan'>");
      }
    }
  });
}

/**
 * This function is responsible for retrieving and displaying the tables
 * (i.e. Table 1) in the Tables column in waiter-ui.html
 */
function loadTables() {
  get("/api/authStaff/getTables", function (data) {
    var response = JSON.parse(data);
    var currentOrderElement = document.getElementById("orders-list");
    while (currentOrderElement.firstChild) {
      currentOrderElement.removeChild(currentOrderElement.firstChild);
    }
    for (i = 0; i < response.length; i++) {
      loadOrderList(response[i].number);
    }
  });
}

/**
 * Loads the orders for a given table number.
 * @param tableNumber The table which the orders should be from.
 */
function loadOrderList(tableNumber) {
  post("/api/authStaff/getOrdersByTable", JSON.stringify({
    tableNumber: tableNumber
  }), function (data) {
    var orders = JSON.parse(data);
    var currentOrderElement = document.getElementById("orders-list");
    while (currentOrderElement.firstChild) {
      currentOrderElement.removeChild(currentOrderElement.firstChild);
    }
    for (i = 0; i < orders.length; i++) {
      $("#orders-list").append(
          "<li"
          + " id='table-" + orders[i].foodOrderId + "'"
          + " data-ordernum='" + orders[i].foodOrderId + "'"
          + " class='list-group-item list-group-item-action'"
          + " onclick=\"setCurrentOrder(event); loadOrder(this.getAttribute('data-ordernum'));\">"
          + "<span class='waiter-ui-span-bold'>Table </span>" + tableNumber
          + "<span> - Order </span>" + orders[i].foodOrderId
          + ": " + orders[i].orderStatus
          + "</li>"
      );
    }
  });
}

/**
 * This script is responsible for setting whichever table is selected,
 * making it active.
 *
 * @param event The event which was triggered by the clicked element
 */
function setCurrentOrder(event) {
  // Reset all orders to non-active
  var allOrders = document.getElementById("orders-list").children;
  for (i = 0; i < allOrders.length; i++) {
    allOrders[i].className = "list-group-item list-group-item-action";
  }

  // Set active element
  var activeOrder = document.getElementById(event.currentTarget.id);
  activeOrder.className += " active";
}

function changeOrderStatus(orderStatus) {
  var activeOrder = getActiveOrder();
  post("/api/authStaff/changeOrderStatus",
      JSON.stringify({
        orderNumber: activeOrder.getAttribute('data-ordernum'),
        newOrderStatus: orderStatus
      }),
      loadTables
  );
}

// Loads the menu and tables when the page loads.
$(document).ready(function () {
  loadMenu();
  loadTables()
});
