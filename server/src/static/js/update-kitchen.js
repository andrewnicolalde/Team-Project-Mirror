/**
 * Preliminary method to update the page dynamically with new orders.
 * @param data the data response to an ajax query.
 */
function updatePage(data) {

  var response = JSON.parse(data);

  for (i = 0; i < response.length; i++) {
    // first item or item on same order (assuming grouped by order)
    if (i === 0 || response[i].orderNo === response[i-1].orderNo) { // this might be a bad check.
      // set the order number to be a class of the column to separate them.
      $(".row").append("<div class=\"col "+ response[i].orderNo +"\">" +
          "<div class='card text-center'" +
          "<div class='card-header'>Order No: " + response[i].orderNo + "</div>" +
          "<div class='card-body'>Item: " + response[i].item +
          "<br />Requirements: " + response[i].requirements +
          "</div>" +
          "</div>" +
          "</div>");
    }
  }
}

function orderPresent( orderNum) {
  var present = document.getElementsByClassName(orderNum);
  return present.length !== 0;
}