/**
 * This script is responsible for retrieving and displaying the menu
 * item elements in the Menu column in waiter-ui.html.
 *
 * Note: This script should be used only for the waiter's UI. This is because this script
 * only displays the name of each menu item & its price, along with dietary info.
 * Obviously a customer would want to see more than that.
 */
$(document).ready(function() {
  // Send get request to server for menu JSON
  get("/api/authStaff/menu", function(data){
    // Parse JSON
    var response = JSON.parse(data);

    // Add items to menu list
    for(i = 0; i < response.length; i++) {
      $("#menu-list").append("<li class='list-group-item list-group-item-action'"
          + "id= \"menu-item-"+ i +"\""
          + "data-menuItemNum='" + response[i].id + "'"
          + "onclick='addToOrder(this.getAttribute(\"data-menuItemNum\"))'>"
          + "<span class='waiter-ui-span-bold'>"
          + response[i].name + ": </span> " + response[i].price + "</li>");
      // Show dietary information
      if(response[i].is_gluten_free){ // Gluten Free
        $("#menu-item-" + i).append(" <img src='../../images/gluten-free.svg' alt='Gluten Free'>");
      }
      if(response[i].is_vegetarian){ // Vegetarian
        $("#menu-item-" + i).append(" <img src='../../images/vegetarian-mark.svg' alt='Vegetarian'>");
      }
      if(response[i].is_vegan){
        $("#menu-item-" + i).append(" <img src='../../images/vegan-mark.svg' alt='Vegan'>");
      }
    }
  });
});