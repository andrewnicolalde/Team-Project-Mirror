$(document).ready(function () {
  loadMenu();
  getTransactionId();
  //TODO make generic/ get the ordernumber from the server.
  loadOrder(localStorage.getItem("orderId"));
});

function getTransactionId() {
  get("/api/authTable/getTransactionId", function (data) {
    var response = JSON.parse(data);

    var transactionId = response.transactionId;

    getOrderId(transactionId);
  });
}

function getOrderId(transactionId) {
  post("/api/authTable/getOrderId", JSON.stringify({
    transactionId: transactionId
  }), function (data) {
    var response = JSON.parse(data);

    localStorage.setItem("orderId", response.orderId);
  });
}

function loadMenu() {
  get("/api/authTable/getMenu", function(data){
    response = JSON.parse(data);
    categories = [];
    for(var i=0; i<response.length; i++) {
      menuItem = response[i];
      console.log(menuItem.name + ":" + menuItem.category);
      // Add the category to the list if it is not there already
      if (categories.indexOf(menuItem.category) < 0) {
        categories.push(menuItem.category);
      }
    }
    console.log(categories);
  });
}

function addToOrder(menuItemId) {
  // Create name-value pairs for HTTP post request, see
  // https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
  var nameValuePairs = JSON.stringify({
    orderNumber: localStorage.getItem("orderId"),
    menuItemId: menuItemId,
    instructions: "none"
  });

  // Handle possible responses
  post("/api/authStaff/addItemToOrder", nameValuePairs, function (status) {
    loadOrder(localStorage.getItem("orderId"));
    if (status === "") {
      // Refresh current order table to show new change
      console.log("Add item to order failed");
      console.log(status);
    }
  });
}

// TODO make generic.
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

// TODO make generic, get the ordernumber.
function changeOrderStatus(orderStatus) {
  post("/api/authStaff/changeOrderStatus",
      JSON.stringify({
        orderNumber: localStorage.getItem("orderId"),
        newOrderStatus: orderStatus
      }),
      confirmPopup()
  );
}

function confirmPopup() {
  alert("The waiter will come soon to confirm your order!");
}
