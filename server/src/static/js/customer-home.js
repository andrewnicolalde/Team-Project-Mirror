function getTransactionId() {
  get("/api/authTable/getTransactionId", function (data) {
    const response = JSON.parse(data);

    const transactionId = response.transactionId;

    getOrderId(transactionId);
  });
}

function getOrderId(transactionId) {
  post("/api/authTable/getOrderId", JSON.stringify({
    transactionId: transactionId
  }), function (data) {
    const response = JSON.parse(data);

    sessionStorage.setItem("orderId", response.orderId);
    window.location.replace("/customer/menu.html");
  });
}
