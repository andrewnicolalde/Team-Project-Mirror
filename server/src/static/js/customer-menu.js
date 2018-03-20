const basket = [];
let menuItems = [];

// Hides the modal if the user clicks off of it.
window.onclick = function (event) {
  const modal = document.getElementById("addToOrderModal");
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
  let categories = null;

  get("/api/authTable/getCategories", function (categoryData) {
    categories = JSON.parse(categoryData);

    for (var i = 0; i < categories.length; i++) {
      const c = categories[i];
      $("#categories").append("<div class='category'>\n"
          + "<button id='category-" + c.categoryId
          + "-button' type='button' class='btn btn-block category-button' data-toggle='collapse' data-target='#category-"
          + c.categoryId + "'>" + c.name + "</button>\n"
          + "<div id='category-" + c.categoryId + "' class='collapse'>\n"
          + "<ul id='category-" + c.categoryId
          + "-list' class='menuitems list-group collapse'>\n"
          + "</ul>\n"
          + "</div>\n"
          + "</div>");
    }

    // Load menu
    get("/api/authTable/getMenu", function (menuData) {
      menuItems = JSON.parse(menuData);
      for (let i = 0; i < menuItems.length; i++) {
        const menuItem = menuItems[i];
        $("#category-" + menuItem.categoryId
            + "-list").append("<li id='menuitem-" + menuItem.id
            + "' class='menuitem list-group-item list-group-item-action' onclick='showItemModal("
            + menuItem.id + ")' data-glutenfree='" + menuItem.is_gluten_free + "' data-vegetarian='" + menuItem.is_vegetarian + "' data-vegan='" + menuItem.is_vegetarian + "'>\n"
            + "<span class='bold'>" + menuItem.name + "</span> - £"
            + menuItem.price + "\n"
            + "<br>\n"
            + menuItem.description + "\n"
            + "<br>\n"
            + "</li>");
        if (menuItem.is_gluten_free) {
          $("#menuitem-" + menuItem.id).append(
              "<img class='img1' src='../images/gluten-free.svg' alt='Gluten Free'>");
        }
        if (menuItem.is_vegetarian) {
          $("#menuitem-" + menuItem.id).append(
              "<img class='img2' src='../images/vegetarian-mark.svg' alt='Vegetarian'>");
        }
        if (menuItem.is_vegan) {
          $("#menuitem-" + menuItem.id).append(
              "<img class='img3' src='../images/vegan-mark.svg' alt='Vegan'>");
        }
      }
    });
  });
}

function meetsDietaryRequirements(mi) {
  gluteninput = document.getElementById("glutencheckbox");
  vegetarianinput = document.getElementById("vegetariancheckbox");
  veganinput = document.getElementById("vegancheckbox");

  if (!gluteninput.checked | (gluteninput.checked == true & mi.dataset.glutenfree === "true")) {
    if (!vegetarianinput.checked | (vegetarianinput.checked == true & mi.dataset.vegetarian === "true")) {
     if (!veganinput.checked | (veganinput.checked == true & mi.dataset.vegan === "true")) {
       return true;
     }
    }
  }
  return false;

}

function filtername() {

  var input, filter, displayMenuItems, gluteninput, vegetarianinput, veganinput;

  input = document.getElementById("mysearchbox");
  filter = input.value.toUpperCase();
  displayMenuItems = document.getElementsByClassName("menuitem");


  // If the filter has any text in it, then expand all the categories.
  // Otherwise, close them all

  if (filter.length === 0) {
    const elementsToHide = document.getElementsByClassName("collapse show");
    for (var i = 0; i < elementsToHide.length; i++) {
      elementsToHide[i].classList.remove("show");
    }
  } else {
    const elementsToShow = document.getElementsByClassName("collapse");
    for (var i = 0; i < elementsToShow.length; i++) {
      elementsToShow[i].classList.add("show");
    }
  }

  for (var i = 0; i < displayMenuItems.length; i++) {
    const mi = displayMenuItems[i];
    console.log(mi.dataset.glutenfree);
    for (var j = 0; j < mi.childNodes.length; j++) {
      const node = mi.childNodes[j];
      if (node.className === "bold") {
        if(node.innerHTML.toUpperCase().indexOf(filter) > -1) {
           if (meetsDietaryRequirements(mi)) {
            mi.style.display = "";
           } else {
            mi.style.display = "none";
           }

        } else {
           mi.style.display = "none";
        }
      }
    }
   }
}

function loadmenufilter(){


 var displayMenuItems;

  displayMenuItems = document.getElementsByClassName("menuitem");

    get("/api/authTable/getMenu", function (menuData) {
          menuItems = JSON.parse(menuData);
          for (let i = 0; i < menuItems.length; i++) {
            const menuItem = menuItems[i];


            for (var i = 0; i < displayMenuItems.length; i++) {
                  const mi = displayMenuItems[i];
                  for (var j = 0; j < mi.childNodes.length; j++) {
                    const node = mi.childNodes[j];
                        if (menuItem.is_gluten_free) {
                            mi.style.display = "";
                        } else {
                            mi.style.display = "none";
                        }
                  }
            }
          }
    });
}
/*
function checked() {

    var displayMenuItems = document.getElementsByClassName("menuitem");
    var gluteninput = document.getElementById("glutencheckbox");

    for (var i = 0; i < displayMenuItems.length; i++) {
      var mi = displayMenuItems[i];
        for (var j = 0; j < mi.childNodes.length; j++) {
        var node = mi.childNodes[j];
          if(gluteninput.checked = true) {
            if(node.className === "img1") {
              mi.style.display = "";
            }
          }
        }
    }
}
*/
function loadOrder() {
  const postData = {orderId: sessionStorage.getItem("orderId")};
  post("/api/authTable/getOrderItems", JSON.stringify(postData),
      function (data) {
        const orderMenuItems = JSON.parse(data);
        for (let i = 0; i < orderMenuItems.length; i++) {
          const item = orderMenuItems[i];
          addItemToBasket(item);
        }
        calculateTotal();
      });
}

function addItemToBasket(item) {
  const parent = $("#order");

  // Add item
  basket.push(item);
  parent.append("<li id='ordermenuitem-" + item.id
      + "' class='list-group-item list-group-item-action'>\n"
      + "  <span class='bold'>" + item.name + "</span>"
      + "  <span class='span-right'>£" + item.price + "</span>\n"
      + "  <br>\n"
      + "  <span id='omi-instructions-" + item.id
      + "'><span id='omi-instructions-" + item.id + "-text'>"
      + item.instructions + "</span></span>\n"
      + "  <span class='span-right'><i id='omi-edit-" + item.id
      + "' class='fa fa-edit fa-lg edit' onclick='showEditOrderMenuItem("
      + item.id + ", \"" + item.instructions
      + "\");'></i><i class='fa fa-times fa-lg remove' onclick='confirmRemoveOrderMenuItem("
      + item.id + ");'></i></span>\n"
      + "</li>");
}

function calculateTotal() {
  // Remove old total order if it exists
  const parent = document.getElementById("order");
  const totalPrice = document.getElementById("total-price");
  if (totalPrice != null) {
    parent.removeChild(totalPrice);
  }

  // Calculate total
  let total = 0.0;
  for (let i = 0; i < basket.length; i++) {
    const item = basket[i];
    total += parseFloat(item.price);
  }

  // Display it.
  $("#order").append("<li id='total-price' class='list-group-item list-group-item-info'>\n"
      + "<span class='bold'>Total:</span>"
      + "<span class='span-right'>£" + total.toFixed(2) + "</span>\n"
      + "</li>");
}

function confirmRemoveOrderMenuItem(itemId) {
  bootbox.confirm("Are you sure you want to remove this item?",
      function (result) {
        if (result) {
          removeOrderMenuItem(itemId);
        }
      });
}

function removeOrderMenuItem(itemId) {
  const dataToSend = JSON.stringify({orderMenuItemId: itemId});
  post("/api/authTable/removeItemFromOrder", dataToSend, function (data) {
    if (data === "success") {
      const parent = document.getElementById("order");
      const child = document.getElementById("ordermenuitem-" + itemId);
      parent.removeChild(child);

      // Remove it from the basket array
      for (let i = 0; i < basket.length; i++) {
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
  const dataToSend = JSON.stringify({
    menuItemId: itemId,
    instructions: instructions,
    orderId: sessionStorage.getItem("orderId")
  });

  post("/api/authTable/addItemToOrder", dataToSend, function (data) {
    if (data !== "failure") {
      const item = JSON.parse(data);
      addItemToBasket(item);
      calculateTotal();
    }
  })
}

function showItemModal(itemId) {
  for (let i = 0; i < menuItems.length; i++) {
    if (menuItems[i].id === itemId) {
      const item = menuItems[i];
      const modal = document.getElementById("addToOrderModal");
      modal.setAttribute("data-menuitemid", item.id);
      document.getElementById("name").innerText = item.name;
      document.getElementById("category").innerText = item.category;
      document.getElementById("description").innerText = item.description;
      document.getElementById("price").innerText = item.price;
      document.getElementById("calories").innerText = item.calories;
      document.getElementById("ingredients").innerText = item.ingredients;
      document.getElementById("picture").setAttribute("src", "../images/"
          + item.picture_src);

      // Remove any content info symbols that are already there
      const node = document.getElementById("content-info");
      while (node.firstChild) {
        node.removeChild(node.firstChild);
      }

      if (item.is_gluten_free) {
        $("#content-info").append(
            "<img src='../images/gluten-free.svg' alt='Gluten Free'>");
      }
      if (item.is_vegetarian) {
        $("#content-info").append(
            "<img src='../images/vegetarian-mark.svg' alt='Vegetarian'>");
      }
      if (item.is_vegan) {
        $("#content-info").append(
            "<img src='../images/vegan-mark.svg' alt='Vegan'>");
      }

      // Clear the extra instructions text input
      document.getElementById("instructions").value = "";

      // Set the onclick for the add to order button
      document.getElementById("addToOrderButton").onclick = function () {
        addToOrder(document.getElementById("addToOrderModal").getAttribute(
            "data-menuitemid"), document.getElementById("instructions").value);
        document.getElementById('addToOrderModal').style.display = 'none';
      };

      modal.style.display = "block";
      break;
    }
  }
}

function showEditOrderMenuItem(orderMenuItemId, instructions) {
  const span = $("#omi-instructions-" + orderMenuItemId);
  span.empty();
  span.append("<input id='omi-instructions-input-" + orderMenuItemId
      + "' name='omi-instructions-input-" + orderMenuItemId
      + "' class='instructions-box' type='text' placeholder='Any extra instructions' value='"
      + instructions + "'>");
  span.append("<i id='omi-confirm-" + orderMenuItemId
      + "' class='fa fa-check fa-lg confirm' onclick='confirmEditOrderMenuItem("
      + orderMenuItemId + ")'></i>");
  $("#omi-edit-" + orderMenuItemId).hide();
}

function confirmEditOrderMenuItem(orderMenuItemId) {
  const span = $("#omi-instructions-" + orderMenuItemId);
  const instructions = $("#omi-instructions-input-" + orderMenuItemId).val();
  const data = JSON.stringify({
    orderMenuItemId: orderMenuItemId,
    instructions: instructions
  });
  post("/api/authTable/changeOrderInstructions", data, function (data) {
    if (data === "success") {
      $("#omi-confirm-" + orderMenuItemId).remove();
      $("#omi-instructions-input-" + orderMenuItemId).remove();
      $("#omi-edit-" + orderMenuItemId).show();
      $("#omi-instructions-"
          + orderMenuItemId).append("<span id='omi-instructions-"
          + orderMenuItemId + "-text'>" + instructions + "</span>")
    }
  });
}

function confirmOrder() {
  const orderId = sessionStorage.getItem("orderId");
  const dataToSend = JSON.stringify({
    orderId: orderId,
    newOrderStatus: "READY_TO_CONFIRM"
  });
  post("/api/authTable/changeOrderStatus", dataToSend, function (data) {
    if (data === "success") {
      window.location.replace("/customer/basket.html");
    }
  });
}

function callWaiterToTable() {

  const dataToSend = JSON.stringify({newStatus: "NEEDS_HELP"});

  post("/api/authTable/changeTableStatus", dataToSend, function (data) {
    if (data === "success") {
      bootbox.alert(
          "Your waiter has been called, and will be with you shortly.");
    }
  })

}