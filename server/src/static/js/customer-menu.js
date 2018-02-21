$(document).ready(function () {
  getTransactionId();
  loadMenu();
});

function loadMenu() {
  // Load categories
  var categories = null;

  get("/api/authTable/getCategories", function (categoryData) {
    categories = JSON.parse(categoryData);

    for (var i=0; i<categories.length; i++) {
      var c = categories[i];
      $("#categories").append("<div class='category'>\n"
                              + "<button id='category-" + c.categoryId + "-button' type='button' class='btn btn-block' data-toggle='collapse' data-target='#category-" + c.categoryId + "'>" + c.name + "</button>\n"
                              + "<div id='category-" + c.categoryId + "' class='collapse'>\n"
                                + "<ul id='category-" + c.categoryId + "-list' class='menuitems list-group collapse'>\n"
                                + "</ul>\n"
                              + "</div>\n"
                            + "</div>");
    }

    // Load menu
    get("/api/authTable/getMenu", function(menuData){
      menu = JSON.parse(menuData);
      for(var i=0; i<menu.length; i++) {
        var menuItem = menu[i];
        $("#category-" + menuItem.categoryId + "-list").append("<li id='menuitem-" + menuItem.id + "' class='menuitem list-group-item list-group-item-action'>\n"
                                                               + "<span class='span-bold'>" + menuItem.name + "</span> - £" + menuItem.price + "\n"
                                                               + "<br>\n"
                                                               + menuItem.description + "\n"
                                                             + "</li>");
      }
    });
  });
}

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
    // Load order now, when the orderId has definitely been set.
    loadOrder();
  });
}

function loadOrder() {
  var postData = {orderNumber: localStorage.getItem("orderId")};
  post("/api/authTable/getOrderItems", JSON.stringify(postData), function(data) {
    var orderMenuItems = JSON.parse(data);
    var total = 0.0;
    for (var i=0; i<orderMenuItems.length; i++) {
      var item = orderMenuItems[i];
      $("#order").append("<li id='ordermenuitem-" + item.id + "' class='list-group-item list-group-item-action'>\n"
                         + "<span class='span-bold'>" + item.name + "</span>"
                         + "<span class='span-right'>£" + item.price + "</span>\n"
                         + "<br>\n"
                         + item.instructions
                         + "<span class='span-right'><i class='fa fa-edit fa-lg edit'></i><i class='fa fa-times fa-lg remove' onclick='removeOrderMenuItem(" + item.id + ");'></i></span>"
                       + "</li>");
      total += parseFloat(item.price);
    }

    $("#order").append("<li id='total-price' class='list-group-item list-group-item-info'>\n"
                       + "<span class='span-bold'>Total:</span>"
                       + "<span class='span-right'>£" + total.toFixed(2) + "</span>\n"
                     + "</li>");
  });
}

function removeOrderMenuItem(itemId) {
  var dataToSend = JSON.stringify({orderMenuItemId: itemId});
  post("/api/authTable/removeItemFromOrder", dataToSend, function(data) {
    if (data === "success") {
      var parent = document.getElementById("order");
      var child = document.getElementById("ordermenuitem-" + itemId);
      parent.removeChild(child);
    }
  })
}