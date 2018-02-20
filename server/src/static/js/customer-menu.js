$(document).ready(function () {
  loadMenu();
  getTransactionId();
});

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
