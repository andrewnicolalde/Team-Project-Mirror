$(document).ready(function () {
  loadMenu();
  loadOrder();
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


function loadOrder() {
  var postData = {orderNumber: localStorage.getItem("orderId")};
  post("/api/authTable/getOrderItems", JSON.stringify(postData), function(data) {
    var orderMenuItems = JSON.parse(data);
    for (var i=0; i<orderMenuItems.length; i++) {
      var item = orderMenuItems[i];
      $("#order").append("<li id='ordermenuitem-" + item.id + "' class='list-group-item list-group-item-action'>\n"
                         + "<span class='span-bold'>" + item.name + "</span>"
                         + "<span class='span-price'>£" + item.price + "</span>\n"
                         + "<br>\n"
                         + item.instructions + "\n"
                       + "</li>");
    }
  });
}
