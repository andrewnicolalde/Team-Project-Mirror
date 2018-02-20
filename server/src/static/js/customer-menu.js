$(document).ready(function () {
  loadMenu();
  getTransactionId();
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
  });
}
