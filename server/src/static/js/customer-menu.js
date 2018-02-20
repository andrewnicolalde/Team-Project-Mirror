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
      $("#menu").append("<button id=\"" + c.name + "-button\" type=\"button\" class=\"btn btn-block\" data-toggle=\"collapse\" data-target=\"#menu_" + c.name +"\">" + c.name + "</button>");
    }

    // Load menu
    get("/api/authTable/getMenu", function(menuData){
      menu = JSON.parse(menuData);
      for(var i=0; i<menu.length; i++) {
        var menuItem = menu[i];

        console.log(menuItem.name + ":" + menuItem.category);
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
