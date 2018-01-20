/*
This script should be used only for the waiter's UI. This is because this script
only displays the name of each menu item & its price. Obviously a customer would
want to see more than that.
 */
$(document).ready(function() {
  // Send get request to server for menu JSON
  $.get("/api/menu", function(data){
    // Parse JSON
    var response = JSON.parse(data);

    // Add items to menu list
    for(i = 0; i < response.length; i++) {
      $("#menu-list").append("<li class='list-group-item' id= \"menu-item-"+ i +"\"><span class='waiter-ui-span-bold'>"
          + response[i].name + ":</span> " + response[i].price + "</li>");
      // TODO: Show dietary information
      if(response[i].is_gluten_free){
        $("#menu-item-" + i).append("<img src='../images/gluten_free.png' alt='Gluten Free'>");
      }
    }
  });
});