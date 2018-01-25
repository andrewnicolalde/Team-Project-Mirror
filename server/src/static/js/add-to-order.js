function addToOrder(tableNumber, menuItemId, description){
  // Create name-value pairs for HTTP post request, see
  // https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
  var nameValuePairs = JSON.stringify({
    tableNumber: tableNumber,
    menuItemID: menuItemId,
    description: description
  });
  // Handle possible responses
  post("/api/auth/addToOrder", nameValuePairs, function(status){
    if(status === "success"){
      loadOrder();
    } else {
      // Refresh current order table to show new change
      console.log("Add item to order failed");
      console.log(status);
    }
  });
}