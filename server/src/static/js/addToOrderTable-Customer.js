$(document).ready(function () {
  /*$(".add-row").click(function () {
    var name = $(".name").val();
    var price = $(".price").val();
    var markup = "<tr><td></td><td>" + name + "</td><td>" + price
        + "</td></tr>";
    $("table.cart tbody").append(markup);
  });*/

  /*$("td").click(function () {
    $(".name").clone().appendTo(".cart");
  });*/

  // Find and remove selected table rows
  /*$(".add-order").click(function () {
    $("table tbody").find('input[name="record"]').each(function () {
      if ($(this).is(":checked")) {
        $(".name").clone().appendTo(".cart");
      }
    });
  });*/
});

function addToOrder(menuItemId) {
  // Create name-value pairs for HTTP post request, see
  // https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
  var nameValuePairs = JSON.stringify({
    tableNumber: 1,
    menuItemId: menuItemId,
    instructions: "none"
  });

  // Handle possible responses
  post("/api/authStaff/addToOrder", nameValuePairs, function (status) {
    loadOrder();
    if (status === "") {
      // Refresh current order table to show new change
      console.log("Add item to order failed");
      console.log(status);
    }
  });
}