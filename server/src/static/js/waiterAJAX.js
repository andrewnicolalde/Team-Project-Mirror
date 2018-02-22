/**
 * This function returns the selected table.
 * @returns The web element representing the current table
 */
function getActiveOrder() {
  var allOrders = document.getElementById("orders-list").children;
  var activeTable;
  for (var i = 0; i < allOrders.length; i++) {
    if (allOrders[i].classList.contains("active")) {
      activeTable = allOrders[i];
    }
  }
  return activeTable;
}

/**
 * This script is responsible for retrieving and displaying the contents of each
 * Table's current order. It also clears the Current Order column of any existing
 * entries before adding the selected Table's entries to the Current Order column.
 *
 * @param orderNumber The number of the order to load.
 */
function loadOrder(orderNumber) {
  sessionStorage.setItem("orderId", orderNumber);

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
    for (var i = 0; i < response.length; i++) {
      $("#current-order").append("<li class='list-group-item list-group-item-action'"
          + "id= \"order-item-" + i + "\">"
          + "<span class='span-bold'>"
          + response[i].name + " </span> "
          + "<span style='float: right'> £" + response[i].price + "</span>"
          + "<h6>" + response[i].instructions + "</h6>"
          + "</li>");
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
    for (var i = 0; i < response.length; i++) {
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

    for (var i = 0; i < orders.length; i++) {
      var statusIcon;
      var btnDisable = ["true", "true", "true", "true"];

      switch (orders[i].orderStatus) {
        case 'Cancelled':
          statusIcon = '<i class="fa fa-times" style="color: red; float: right; font-size: 25px"></i>';
          // All buttons disabled
          for (var j = 0; j < btnDisable.length; j++){
            btnDisable[j] = "false"
          }
          break;
        case 'Ordering':
          statusIcon = '<i class="fa fa-ellipsis-h" style="float: right;font-size: 25px"></i>';
          // Delivered button disabled
          btnDisable[1] = "false";
          break;
        case 'Ready To Confirm':
          statusIcon = '<i class="fa fa-bell" style="color: yellow; float: right;font-size: 25px"></i>';
          // Delivered button disabled
          btnDisable[1] = "false";
          break;
        case 'Cooking':
          statusIcon = '<i class="fa fa-fire" style="color: orange; float: right;font-size: 25px"></i>';
          // All buttons except cancelled disabled
          for (var j = 0; j < btnDisable.length - 1; j++){
            btnDisable[j] = "false"
          }
          break;
        case 'Ready To Deliver':
          statusIcon = '<i class="fa fa-check" style="color: #00bf00; float: right;font-size: 25px"></i>';
          // Only Delivered and cancelled are enabled
          btnDisable[0] = "false";
          btnDisable[2] = "false";
          break;
        case 'Delivered':
          statusIcon = '<i class="fas fa-utensils" style="float: right;font-size: 25px"></i>';
          // All buttons except cancelled disabled
          for (var j = 0; j < btnDisable.length - 1; j++){
            btnDisable[j] = "false"
          }
          break;
      }

      $("#orders-list").append(
          "<li"
          + " id='table-" + orders[i].foodOrderId + "'"
          + " data-ordernum='" + orders[i].foodOrderId + "'"
          + " class='list-group-item list-group-item-action'"
          + " onclick=\""
            + "setActiveOrder(event); "
            + "loadOrder(this.getAttribute('data-ordernum'));"
            + "document.getElementById('confirm_button').hidden = false;"
            + "document.getElementById('delivered_button').hidden = false;"
            + "document.getElementById('edit_button').hidden = false;"
            + "disableButtons();"
            + "document.getElementById('cancel_button').hidden = false;"
            + "\">"
          + "<span class='span-bold'>Table </span>" + tableNumber
          + "<span> - Order </span>" + orders[i].foodOrderId
          + ": " + orders[i].orderStatus
          + statusIcon
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
function setActiveOrder(event) {
  // Reset all orders to non-active
  var allOrders = document.getElementById("orders-list").children;
  for (var i = 0; i < allOrders.length; i++) {
    allOrders[i].className = "list-group-item list-group-item-action";
  }

  //Checks to see if the order has been deselected or cancelled.
  if (event == null){
    //Hides buttons
    document.getElementById('confirm_button').hidden = true;
    document.getElementById('delivered_button').hidden = true;
    document.getElementById('edit_button').hidden = true;
    document.getElementById('cancel_button').hidden = true;
    //Remove current order
    var currentOrderElement = document.getElementById("current-order");
    while (currentOrderElement.firstChild) {
      currentOrderElement.removeChild(currentOrderElement.firstChild);
    }
    return;
  }
  // Set active element
  var activeOrder = document.getElementById(event.currentTarget.id);
  activeOrder.className += " active";
}

/**
 * This function updates the status of an order in the database.
 * @param orderStatus The status you are making the order
 */
function changeOrderStatus(orderStatus) {
  var activeOrder = getActiveOrder();
  post("/api/authStaff/changeOrderStatus",
      JSON.stringify({
        orderNumber: activeOrder.getAttribute('data-ordernum'),
        newOrderStatus: orderStatus
      }),
      loadTables()
  );
}

/**
 * This function wraps changeOrderStatus. It is triggered as an onclick when
 * the cancel order function is pressed. It serves the purpose of ensuring that
 * a waiter does not cancel an order by mistake.
 */
function confirmCancelOrder() {
  if (getActiveOrder() == null) {
    bootbox.alert("There is no order selected");
  } else {
    bootbox.confirm("Are you sure you want to cancel this order?", function (result) {
      if(result){ // If the user hit okay (result == true)
        changeOrderStatus('CANCELLED');
        setActiveOrder(null);
      }
      // Otherwise do nothing
    });
  }
}

// Loads the menu and tables when the page loads.
$(document).ready(function () {
  loadTables();
});
