function loadOrder(tableNumber){
  var tableNumberToSend = JSON.stringify({tableNumber: tableNumber});
  post("/api/auth/getOrder", tableNumberToSend, function(data){
    var response = JSON.parse(data);
    var superelement = document.getElementById("current-order");
    while(superelement.firstChild){
      superelement.removeChild(superelement.firstChild);
      console.log("tried to clear stuff");
    }
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