var basket = [];
var menuItems = [];

// Hides the modal if the user clicks off of it.
window.onclick = function(event) {
  var modal = document.getElementById("addToOrderModal");
  if (event.target === modal) {
    modal.style.display = "none";
  }
};

$(document).ready(function () {
  loadMenu();
  loadOrder();
});

function loadMenu() {
  // Load categories
  var categories = null;

  get("/api/authTable/getCategories", function (categoryData) {
    categories = JSON.parse(categoryData);

    for (var i=0; i<categories.length; i++) {
      var c = categories[i];
      $("#categories").append("<div class='category'>\n"
                              + "<button id='category-" + c.categoryId + "-button' type='button' class='btn btn-block' data-toggle='collapse' data-target='#category-" + c.categoryId + "'>" + c.name + "</button>\n"
                              + "<div id='category-" + c.categoryId + "' class='collapse'>\n"
                                + "<ul id='category-" + c.categoryId + "-list' class='menuitems list-group collapse'>\n"
                                + "</ul>\n"
                              + "</div>\n"
                            + "</div>");
    }

    // Load menu
    get("/api/authTable/getMenu", function(menuData){
      menuItems = JSON.parse(menuData);
      for(var i=0; i<menuItems.length; i++) {
        var menuItem = menuItems[i];
        $("#category-" + menuItem.categoryId + "-list").append("<li id='menuitem-" + menuItem.id + "' class='menuitem list-group-item list-group-item-action' onclick='showItemModal(" + menuItem.id + ")'>\n"
                                                               + "<span class='bold'>" + menuItem.name + "</span> - £" + menuItem.price + "\n"
                                                               + "<br>\n"
                                                               + menuItem.description + "\n"
                                                               + "<br>\n"
                                                             + "</li>");
        if (menuItem.is_gluten_free) {
          $("#menuitem-" + menuItem.id).append("<img src='../images/gluten-free.svg' alt='Gluten Free'>");
        }
        if (menuItem.is_vegetarian) {
          $("#menuitem-" + menuItem.id).append("<img src='../images/vegetarian-mark.svg' alt='Vegetarian'>");
        }
        if (menuItem.is_vegan) {
          $("#menuitem-" + menuItem.id).append("<img src='../images/vegan-mark.svg' alt='Vegan'>");
        }
      }
    });
  });
}


function loadOrder() {
  var postData = {orderNumber: sessionStorage.getItem("orderId")};
  post("/api/authTable/getOrderItems", JSON.stringify(postData), function(data) {
    var orderMenuItems = JSON.parse(data);
    for (var i=0; i<orderMenuItems.length; i++) {
      var item = orderMenuItems[i];
      addItemToBasket(item);
    }
    calculateTotal();
  });
}

function addItemToBasket(item) {
  var parent = $("#order");

  // Add item
  basket.push(item);
  parent.append("<li id='ordermenuitem-" + item.id + "' class='list-group-item list-group-item-action'>\n"
              + "  <span class='bold'>" + item.name + "</span>"
              + "  <span class='span-right'>£" + item.price + "</span>\n"
              + "  <br>\n"
              + "  <span id='omi-instructions-" + item.id + "'><span id='omi-instructions-" + item.id + "-text'>" + item.instructions + "</span></span>\n"
              + "  <span class='span-right'><i id='omi-edit-" + item.id + "' class='fa fa-edit fa-lg edit' onclick='showEditOrderMenuItem(" + item.id + ", \"" + item.instructions + "\");'></i><i class='fa fa-times fa-lg remove' onclick='confirmRemoveOrderMenuItem(" + item.id + ");'></i></span>\n"
              + "</li>");
}

function calculateTotal() {
  // Remove old total order if it exists
  var parent = document.getElementById("order");
  var totalPrice = document.getElementById("total-price");
  if (totalPrice != null) {
    parent.removeChild(totalPrice);
  }


  // Calculate total
  var total = 0.0;
  for (var i=0; i<basket.length; i++) {
    var item = basket[i];
    total += parseFloat(item.price);
  }

  // Display it.
  $("#order").append("<li id='total-price' class='list-group-item list-group-item-info'>\n"
      + "<span class='bold'>Total:</span>"
      + "<span class='span-right'>£" + total.toFixed(2) + "</span>\n"
      + "</li>");
}

function confirmRemoveOrderMenuItem(itemId) {
  bootbox.confirm("Are you sure you want to remove this item?", function (result) {
    if(result) {
      removeOrderMenuItem(itemId);
    }
  });
}

function removeOrderMenuItem(itemId) {
  var dataToSend = JSON.stringify({orderMenuItemId: itemId});
  post("/api/authTable/removeItemFromOrder", dataToSend, function(data) {
    if (data === "success") {
      var parent = document.getElementById("order");
      var child = document.getElementById("ordermenuitem-" + itemId);
      parent.removeChild(child);

      // Remove it from the basket array
      for (var i=0; i<basket.length; i++) {
        if (basket[i].id === itemId) {
          basket.splice(i, 1);
        }
      }

      // Recalculate the price
      calculateTotal();
    }
  })
}

function addToOrder(itemId, instructions) {
  var dataToSend = JSON.stringify({
    menuItemId: itemId,
    instructions: instructions,
    orderNumber: localStorage.getItem("orderId")
  });

  post("/api/authTable/addItemToOrder", dataToSend, function(data) {
    if (data !== "failure") {
      var item = JSON.parse(data);
      addItemToBasket(item);
      calculateTotal();
    }
  })
}

function showItemModal(itemId) {
  for (var i=0; i<menuItems.length; i++) {
    if (menuItems[i].id === itemId) {
      var item = menuItems[i];
      var modal = document.getElementById("addToOrderModal");
      modal.setAttribute("data-menuitemid", item.id);
      document.getElementById("name").innerText = item.name;
      document.getElementById("category").innerText = item.category;
      document.getElementById("description").innerText = item.description;
      document.getElementById("price").innerText = item.price;
      document.getElementById("calories").innerText = item.calories;
      document.getElementById("ingredients").innerText = item.ingredients;
      document.getElementById("picture").setAttribute("src", "../images/" + item.picture_src);

      // Remove any content info symbols that are already there
      var node = document.getElementById("content-info");
      while(node.firstChild) {
        node.removeChild(node.firstChild);
      }

      if (item.is_gluten_free) {
        $("#content-info").append("<img src='../images/gluten-free.svg' alt='Gluten Free'>");
      }
      if (item.is_vegetarian) {
        $("#content-info").append("<img src='../images/vegetarian-mark.svg' alt='Vegetarian'>");
      }
      if (item.is_vegan) {
        $("#content-info").append("<img src='../images/vegan-mark.svg' alt='Vegan'>");
      }

      // Clear the extra instructions text input
      document.getElementById("instructions").value = "";

      // Set the onclick for the add to order button
      document.getElementById("addToOrderButton").onclick = function() {
        addToOrder(document.getElementById("addToOrderModal").getAttribute("data-menuitemid"), document.getElementById("instructions").value);
        document.getElementById('addToOrderModal').style.display = 'none';
      };

      modal.style.display = "block";
      break;
    }
  }
}

function showEditOrderMenuItem(orderMenuItemId, instructions) {
  var span = $("#omi-instructions-" + orderMenuItemId);
  span.empty();
  span.append("<input id='omi-instructions-input-" + orderMenuItemId + "' name='omi-instructions-input-" + orderMenuItemId + "' class='instructions-box' type='text' placeholder='Any extra instructions' value='" + instructions + "'>");
  span.append("<i id='omi-confirm-" + orderMenuItemId + "' class='fa fa-check fa-lg confirm' onclick='confirmEditOrderMenuItem(" + orderMenuItemId + ")'></i>");
  $("#omi-edit-" + orderMenuItemId).hide();
}

function confirmEditOrderMenuItem(orderMenuItemId) {
  var span = $("#omi-instructions-" + orderMenuItemId);
  var instructions = $("#omi-instructions-input-" + orderMenuItemId).val();
  var data = JSON.stringify({orderMenuItemId: orderMenuItemId,
                             instructions: instructions});
  post("/api/authTable/changeOrderInstructions", data, function(data) {
    if (data === "success") {
      $("#omi-confirm-" + orderMenuItemId).remove();
      $("#omi-instructions-input-" + orderMenuItemId).remove();
      $("#omi-edit-" + orderMenuItemId).show();
      $("#omi-instructions-" + orderMenuItemId).append("<span id='omi-instructions-" + orderMenuItemId + "-text'>" + instructions + "</span>")
    }
  });
}