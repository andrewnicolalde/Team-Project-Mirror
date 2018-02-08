$(document).ready(function () {
  loadOrder();
  loadMenu();
  /*$(".add-row").click(function () {
    var name = $(".name").val();
    var price = $(".price").val();
    var markup = "<tr><td></td><td>" + name + "</td><td>" + price
        + "</td></tr>";
    $("table.cart tbody").append(markup);
  });*/

  /*$("td").click(function () {
    $(".name").clone().appendTo(".cart");
  });*/

  // Find and remove selected table rows
  /*$(".add-order").click(function () {
    $("table tbody").find('input[name="record"]').each(function () {
      if ($(this).is(":checked")) {
        $(".name").clone().appendTo(".cart");
      }
    });
  });*/
});

function loadMenu() {
  // Send get request to server for menu JSON
  get("/api/authStaff/menu", function (data) {
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

function addToOrder(menuItemId) {
  // Create name-value pairs for HTTP post request, see
  // https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
  var nameValuePairs = JSON.stringify({
    orderNumber: 2,
    menuItemId: menuItemId,
    instructions: "none"
  });

  // Handle possible responses
  post("/api/authStaff/addToOrder", nameValuePairs, function (status) {
    loadOrder();
    if (status === "") {
      // Refresh current order table to show new change
      console.log("Add item to order failed");
      console.log(status);
    }
  });
}

function loadOrder() {
  var orderNumberToSend = JSON.stringify({orderNumber: 2});
  post("/api/authStaff/getOrder", orderNumberToSend, function (data) {

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