/**
 * This function returns the selected table.
 * @returns The web element representing the current table
 */
function getActiveTable() {
  // Construct a list of all orders in the document
  const allOrders = document.querySelectorAll('[id^="order-title-"]');
  for (let i = 0; i < allOrders.length; i++) {
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
 * @param orderId The number of the order to load.
 */
function loadOrder(orderId) {
  sessionStorage.setItem("orderId", orderId);

  const orderIdToSend = JSON.stringify({orderId: orderId});
  post("/api/authStaff/getOrderItems", orderIdToSend, function (data) {

    // Parse JSON
    const response = JSON.parse(data);

    // Remove any existing elements in the current order list
    const currentOrderElement = document.getElementById("current-order");
    while (currentOrderElement.firstChild) {
      currentOrderElement.removeChild(currentOrderElement.firstChild);
    }

    // Add each list item
    for (let i = 0; i < response.length; i++) {
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
    const response = JSON.parse(data);
    const currentOrderElement = document.getElementById("tables-list");
    while (currentOrderElement.firstChild) {
      currentOrderElement.removeChild(currentOrderElement.firstChild);
    }
    // Add each Table to the list of tables
    for (var i = 0; i < response.length; i++) {
      const statusIcon = getTableIcon(response[i].status);
      $("#tables-list").append(
          "<li data-tablenum='" + response[i].number + "' id='table-"
          + response[i].number
          + "' class='list-group-item list-group-item-action' data-toggle='collapse' data-target='#table-"
          + response[i].number + "-orders-list'><span><div class='lst-table'>Table "
          + response[i].number + " - " + response[i].status
          + statusIcon
          + tableBtns(response[i].status, response[i].tableId) +"</div>"
          + "<ul id='table-" + response[i].number + "-orders-list' class='collapse'></ul>"
          + "</li>");
    }
    // Load all orders for each table
    for (var i = 0; i < response.length; i++) {
      loadOrderList(response[i].number);
    }
  });
}

function tableBtns(status, tableId) {
  if (status === "Needs Help") {
    return "<button id ='helpedButton-" + tableId + "' data-tableId=" + tableId
        + " type='button' class='btn btn-helped' onclick=\"changeTableStatus(event, 'FILLED')\">Helped</button>"
  } else if (status === "Needs Cleaning") {
    return "<button id = 'cleanButton-" + tableId + "'data-tableId=" + tableId
        + " type='button' class='btn btn-cleaned' onclick=\"changeTableStatus(event, 'FREE')\">Cleaned</button>"
  } else if (status === "Free") {
    return "<button id ='filledButton-" + tableId + "' data-tableId=" + tableId
        + " type='button' class='btn btn-filled' onclick=\"changeTableStatus(event, 'FILLED')\">Filled</button>"
  } else {
    return "";
  }
}

/**
 * Loads the orders for a given table number.
 * @param tableNumber The table which the orders should be from.
 */
function loadOrderList(tableNumber) {
  post("/api/authStaff/getOrdersByTable", JSON.stringify({
    tableNumber: tableNumber
  }), function (data) {
    const orders = JSON.parse(data);
    const currentOrderElement = document.getElementById("table-" + tableNumber
        + "-orders-list");
    while (currentOrderElement.firstChild) {
      currentOrderElement.removeChild(currentOrderElement.firstChild);
    }
    for (let i = 0; i < orders.length; i++) {
      const statusIcon = getOrderIcon(orders[i].orderStatus);
      $(currentOrderElement).append( // Change back to orders list
          "<li"
          + " id='order-title-" + orders[i].foodOrderId + "'"
          + " data-ordernum='" + orders[i].foodOrderId + "'"
          + " data-orderStatus='" + orders[i].orderStatus + "'"
          + " class='list-group-item list-group-item-action'"
          + " onclick=\""
          + "event.stopPropagation();" // This prevents clicking on the orders resulting in collapsing the table
          + "setActiveOrder(event); "
          + "loadOrder(this.getAttribute('data-ordernum'));"
          + "setButtons(this.getAttribute('data-orderStatus'));"
          + "\">"
          + "<span class='span-bold'>Order </span>" + orders[i].foodOrderId
          + " - " + orders[i].orderStatus
          + statusIcon
          + "</li>"
      );
    }
  });
}

/**
 * Sets the buttons per the order
 */
function setButtons(orderStatus) {
  document.getElementById('confirm_button').hidden = false;
  document.getElementById('delivered_button').hidden = false;
  document.getElementById('edit_button').hidden = false;
  document.getElementById('cancel_button').hidden = false;
  getDisabled(orderStatus);
}

/**
 * This function sets which buttons should be disabled dependant on the the status
 */
function getDisabled(orderStatus) {
  const buttons = [
    document.getElementById("confirm_button"),
    document.getElementById("delivered_button"),
    document.getElementById("edit_button"),
    document.getElementById("cancel_button")
  ];

  for (var i = 0; i < buttons.length; i++) {
    if (buttons[i].getAttribute("disabled")) {
      buttons[i].removeAttribute("disabled");
    }
  }

  switch (orderStatus) {
    case 'Cancelled':
      for (var i = 0; i < buttons.length; i++) {
        buttons[i].setAttribute("disabled", "disabled");
      }
      break;
    case 'Ordering':
      buttons[1].setAttribute("disabled", "disabled");
      break;
    case 'Ready To Confirm':
      buttons[1].setAttribute("disabled", "disabled");
      break;
    case 'Cooking':
      for (var i = 0; i < buttons.length - 1; i++) {
        buttons[i].setAttribute("disabled", "disabled");
      }
      break;
    case 'Ready To Deliver':
      buttons[0].setAttribute("disabled", "disabled");
      buttons[2].setAttribute("disabled", "disabled");
      break;
    case 'Delivered':
      for (var i = 0; i < buttons.length; i++) {
        buttons[i].setAttribute("disabled", "disabled");
      }
      break;
  }
}

function getTableIcon(tableStatus) {
  let statusIcon;
  switch (tableStatus) {
    case 'Free':
      statusIcon = '<i class="far fa-circle table-icon" style="color: black; float: right; font-size: 25px"></i>';
      break;
    case 'Filled':
      statusIcon = '<i class="fas fa-circle table-icon" style="color: black; float: right;font-size: 25px"></i>';
      break;
    case 'Needs Cleaning':
      statusIcon = '<i class="fa fa-trash table-icon" style="color: red; float: right;font-size: 25px"></i>';
      break;
    case 'Needs Help':
      statusIcon = '<i class="fa fa-exclamation table-icon" style="color: red; float: right;font-size: 25px"></i>';
  }
  return statusIcon;
}

/**
 * This method sets the icon for the order
 */
function getOrderIcon(orderStatus) {
  let statusIcon;
  switch (orderStatus) {
    case 'Cancelled':
      statusIcon = '<i class="fa fa-times" style="color: red; float: right; font-size: 25px"></i>';
      break;
    case 'Ordering':
      statusIcon = '<i class="fa fa-ellipsis-h" style="float: right;font-size: 25px"></i>';
      break;
    case 'Ready To Confirm':
      statusIcon = '<i class="fa fa-bell" style="color: #ffdb00; float: right;font-size: 25px"></i>';
      break;
    case 'Cooking':
      statusIcon = '<i class="fa fa-fire" style="color: orange; float: right;font-size: 25px"></i>';
      break;
    case 'Ready To Deliver':
      statusIcon = '<i class="fa fa-check" style="color: #00bf00; float: right;font-size: 25px"></i>';
      break;
    case 'Delivered':
      statusIcon = '<i class="fas fa-utensils" style="float: right;font-size: 25px"></i>';
      break;
  }
  return statusIcon;

}

/**
 * This script is responsible for setting whichever table is selected,
 * making it active.
 *
 * @param event The event which was triggered by the clicked element
 */
function setActiveOrder(event) {


  // Get a list of every order in the entire document
  const allTables = document.querySelectorAll('[id^="order-title-"]');

  // Reset all orders to non-active
  for (let i = 0; i < allTables.length; i++) {
    allTables[i].className = "list-group-item list-group-item-action";
  }

  //Checks to see if the order has been deselected or cancelled.
  if (event == null) {
    //Hides buttons
    document.getElementById('confirm_button').hidden = true;
    document.getElementById('delivered_button').hidden = true;
    document.getElementById('edit_button').hidden = true;
    document.getElementById('cancel_button').hidden = true;
    //Remove current order
    const currentOrderElement = document.getElementById("current-order");
    while (currentOrderElement.firstChild) {
      currentOrderElement.removeChild(currentOrderElement.firstChild);
    }
    return;
  }
  // Set active element
  const activeOrder = document.getElementById(event.currentTarget.id);
  activeOrder.className += " active";
}

/**
 * This function updates the status of an order in the database.
 * @param orderStatus The status you are making the order
 */
function changeOrderStatus(orderStatus) {
  const activeOrder = getActiveTable();
  post("/api/authStaff/changeOrderStatus",
      JSON.stringify({
        orderId: activeOrder.getAttribute('data-ordernum'),
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
  if (getActiveTable() == null) {
    bootbox.alert("There is no order selected");
  } else {
    bootbox.confirm("Are you sure you want to cancel this order?",
        function (result) {
          if (result) { // If the user hit okay (result == true)
            changeOrderStatus('CANCELLED');
            setActiveOrder(null);
          }
          // Otherwise do nothing
        });
  }
}

/**
 * This method changes the table status, after the waiter has helped the customer.
 */
function changeTableStatus(event, status) {
  event.stopPropagation();
  const btn = document.getElementById(event.currentTarget.id);
  console.log(event.currentTarget.id);
  console.log(btn.id);
  console.log(btn.dataset.tableid);
  post("/api/authStaff/changeTableStatus",
      JSON.stringify({newStatus: status, tableId: btn.dataset.tableid.toString()}),
      function (data) {});
}

// Loads the menu and tables when the page loads.
$(document).ready(function () {
  loadTables();
});
