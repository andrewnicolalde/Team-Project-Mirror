/**
 * Updates the page to show orders returned by the server.
 */
function updatePage(data) {
  var response = JSON.parse(data);

  for (var i = 0; i < response.length; i++) {
    // if its not check if its on the page.
    if (!orderPresent(response[i].orderId)) {
      var column = "<div class='col " + response[i].orderId + "'>\n";
      for (var j = 0; j < response[i].orderContents.length; j++) {
        column = column + "<div class='card text-center'>\n"
            + "<div class='card-header'> Order: " + response[i].orderId
            + "</div>\n"
            + "<div class='card-body'> Item: "
            + response[i].orderContents[j].itemName + "\n"
            + "<br />Instructions: "
            + response[i].orderContents[j].instructions + "</div>\n"
            + "</div>\n";
      }
      column = column + "</div>";
      $(".row").append(column);
    }
  }
}

/**
 * Wrapper function to simplify interval.
 */
function checkPage() {
  get("/api/authStaff/kitchen", function (data) {
    updatePage(data);
  });

}

/**
 * Check if an order number is being displayed on the screen.
 * @param orderNum The order number to be checked.
 * @returns {boolean} True if present on page, False if not.
 */
function orderPresent(orderNum) {
  var present = document.getElementsByClassName(orderNum);
  return present.length !== 0;
}