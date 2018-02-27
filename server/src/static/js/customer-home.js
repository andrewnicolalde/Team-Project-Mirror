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

    sessionStorage.setItem("orderId", response.orderId);
    window.location.replace("/customer/menu.html");
  });
}
