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
