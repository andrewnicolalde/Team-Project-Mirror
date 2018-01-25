/**
 * This script is responsible for retrieving and displaying the contents of each
 * Table's current order. It also clears the Current Order column of any existing
 * entries before adding the selected Table's entries to the Current Order column.
 *
 * @param tableNumber The number of the table who's current order is to be displayed
 *                    in the Current Order column.
 */
function loadOrder(tableNumber){
  var tableNumberToSend = JSON.stringify({tableNumber: tableNumber});
  post("/api/authStaff/getOrder", tableNumberToSend, function(data){

    // Parse JSON
    var response = JSON.parse(data);

    // Remove any existing elements in the current order list
    var currentOrderElement = document.getElementById("current-order");
    while(currentOrderElement.firstChild){
      currentOrderElement.removeChild(currentOrderElement.firstChild);
      console.log("tried to clear stuff");
    }

    // Add each list item
    for(i = 0; i < response.length; i++) {
      $("#current-order").append("<li class='list-group-item list-group-item-action' id= \"order-item-"+ i +"\"><span class='waiter-ui-span-bold'>"
          + response[i].name + ": </span> " + response[i].price + "</li>");
      // Show dietary information
      if(response[i].is_gluten_free){ // Gluten Free
        $("#order-item-" + i).append(" <img src='../images/gluten-free.svg' alt='Gluten Free'>");
      }
      if(response[i].is_vegetarian){ // Vegetarian
        $("#order-item-" + i).append(" <img src='../images/vegetarian-mark.svg' alt='Vegetarian'>");
      }
      if(response[i].is_vegan){
        $("#order-item-" + i).append(" <img src='../images/vegan-mark.svg' alt='Vegan'>");
      }
    }
  });
}