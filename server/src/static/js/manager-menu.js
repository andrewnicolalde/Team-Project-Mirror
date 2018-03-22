let mi = {
  id: null,
  addNow: "",
  name: "",
  description: "",
  category: "",
  price: "",
  calories: "",
  isGlutenFree: false,
  isVegetarian: false,
  isVegan: false,
  ingredients: [],
  pictureSrc: ""
};

let categories = [];
let ingredients = [];
let menuItems = [];

$(document).ready(function() {
  get("/api/authStaff/getIngredients", function(data) {
    ingredients = JSON.parse(data);

    get("/api/authStaff/getMenu", function(data) {
      menuItems = JSON.parse(data);
      reloadMenuItems(menuItems);
    });
  });

  get("/api/authStaff/getCategories", function (data) {
    categories = JSON.parse(data);
  });
});

/**
 * Adds a new menu item to the table
 * @param item The menu item to add
 */
function addMenuItem(item) {
  $("#menu").append("<tr id='mi-" + item.id + "'>\n"
      + "<td>" + item.name + "</td>\n"
      + "<td>" + item.description + "</td>\n"
      + "<td>" + item.category + "</td>"
      + "<td>£" + parseFloat(item.price).toFixed(2) + "</td>\n"
      + "<td>" + item.calories + "kCal</td>\n"
      + "<td>" + getDietaryRequirements(item) + "</td>\n"
      + "<td>" + getIngredientsList(item.ingredients) + "</td>\n"
      + "<td>" + item.picture_src + "</td>\n"
      + "<td>" + getActions(item) + "</td>\n"
      + "</tr>");
}

/**
 * Gets the action buttons to display to the user
 * @param item The menu item the actions perfrom on.
 * @returns The HTML representing the action buttons.
 */
function getActions(item) {
  if (item.partOfFranchise) {
    return "<i id='edit-" + item.id + "' class=\"fas fa-edit fa-lg edit\" onclick='wizardEdit(" + item.id + ");'></i><i class=\"fa fa-times fa-lg remove\" onclick='unassign(" + item.id + ");'></i>";
  } else {
    return "<i id='edit-" + item.id + "' class=\"fas fa-edit fa-lg edit\" onclick='wizardEdit(" + item.id + ");'></i><i class=\"fas fa-plus fa-lg confirm\" onclick='addToFranchise(" + item.id + ");'></i>";

  }
}

/**
 * Finds the menu item with the given id, or returns null if it does not exist.
 * @param id The id to search for.
 * @returns The menu item, or null if it is not found.
 */
function getMenuItem(id) {
  for (let i = 0; i < menuItems.length; i++) {
    if (menuItems[i].id === id) {
      return menuItems[i];
    }
  }
  return null;
}

/**
 * Assigns a menu item to a franchise
 * @param id The id of the menu item to assign
 */
function addToFranchise(id) {
  const item = getMenuItem(id);
  post("/api/authStaff/assignMenuItem", String(id), function(data) {
    if(data === "success") {
      item.partOfFranchise = true;
      reloadMenuItems();
    }
  })
}

/**
 * Unassigns a menu item from a franchise.
 * @param id The id of the item to unassign.
 */
function unassign(id) {
  const item = getMenuItem(id);
 post("/api/authStaff/unassignMenuItem", String(id), function(data) {
   if (data === "success") {
     item.partOfFranchise = false;
     reloadMenuItems();
   }
 });
}

/**
 * Clears what is currently displayed, and displays all the menuItems currently loaded.
 */
function reloadMenuItems() {
  $("#menu").empty();
  for (let i = 0; i < menuItems.length; i++) {
    const item = menuItems[i];
    if ($("#see-all-menuitems")[0].checked) {
      // Showing all menu items
      addMenuItem(item);
    } else {
      // Only show item if it is part of the franchise
      if (item.partOfFranchise) {
        addMenuItem(item);
      }
    }
  }
}

/**
 * Returns a human readable string representing the ingredients in the menu item.
 * @param ingredientIds An array of ids of the ingredients in the item.
 * @returns A human readable string of ingredients.
 */
function getIngredientsList(ingredientIds) {
  let displayString = "";
  for (let i = 0; i < ingredientIds.length; i++) {
    let ingredientName = "";
    for (let j = 0; j < ingredients.length; j++) {
      if (ingredients[j].id === ingredientIds[i]) {
        ingredientName = ingredients[j].ingredientName;
        break;
      }
    }
    displayString = displayString + ingredientName + ", ";
  }
  return displayString.slice(0, -2);
}

/**
 * Retuns the html of the dietary requirements for a menu item
 * @param menuItem The menu item the dietary requirements are of.
 * @returns A string of HTML holding the images to represent the dietary requirements.
 */
function getDietaryRequirements(menuItem) {
  let htmlString = "";
  if (menuItem.is_gluten_free) {
    htmlString = htmlString + "<img src=\"../images/gluten-free.svg\">";
  }
  if (menuItem.is_vegetarian) {
    htmlString = htmlString + "<img src=\"../images/vegetarian-mark.svg\">";
  }
  if (menuItem.is_vegan) {
    htmlString = htmlString + "<img src=\"../images/vegan-mark.svg\">";
  }
  return htmlString;
}

/**
 * Starts the wizard in edit mode, loading an existing menu item into the fields.
 * @param id The id of the menu item to load.
 */
function wizardEdit(id) {
  for (let i = 0; i < menuItems.length; i++) {
    if (menuItems[i].id === id) {
      mi = {
        id: menuItems[i].id,
        addNow: null,
        name: menuItems[i].name,
        description: menuItems[i].description,
        category: menuItems[i].description,
        price: menuItems[i].price,
        calories: menuItems[i].calories,
        isGlutenFree: menuItems[i].isGlutenFree,
        isVegetarian: menuItems[i].isVegetarian,
        isVegan: menuItems[i].isVegan,
        ingredients: menuItems[i].ingredients,
        pictureSrc: menuItems[i].pictureSrc
      }
    }
  }

  // Remove "Add to franchise" checkbox if it is already there
  $("#addtofranchise").remove();

  // Set the mode to edit
  $("#wizard").attr("data-mode", "edit");

  // Set the title
  $("#wizard-title").text("Edit a Menu Item");

  // Start the wizard
  wizardBasic();
}

/**
 * Starts the wizard in start mode, making sure the fields are all in their default state.
 */
function wizardStart() {
  // Reset the current values stored.
  mi = {
    id: null,
    addNow: "",
    name: "",
    description: "",
    category: "",
    price: "",
    calories: "",
    isGlutenFree: false,
    isVegetarian: false,
    isVegan: false,
    ingredients: [],
    pictureSrc: ""
  };


  // Remove "Add to franchise" checkbox if it is already there
  $("#addtofranchise").remove();
  // Add the "Add to franchise" checkbox
  $("#wizard-footer").prepend("<label id='addtofranchise'><input type=\"checkbox\" id=\"w-addtofranchise\" checked>  Add this item to the franchise?</label>");

  // Set the mode to add
  $("#wizard").attr("data-mode", "add");

  // Set the title
  $("#wizard-title").text("Create a New Menu Item");

  // Start the wizard.
  wizardBasic();
}

/**
 * Shows the basic information display of the wizard
 */
function wizardBasic() {
  const wizardBody = $("#wizard-body");
  wizardBody.empty();
  wizardBody.append("<label for='w-name'>Name:</label>\n"
      + "<input type='text' class='form-control' name='w-name' id='w-name' value='" + mi.name + "'>"
      + "<label for='w-description'>Description:</label>\n"
      + "<input type='text' class='form-control' name='w-description' id='w-description' value='" + mi.description + "'>\n"
      + "<label for='w-category'>Category:</label><br>\n"
      + "<select id='w-category' name='w-category' class='form-control'></select>\n"
      + "<label for='w-price'>Price:</label>\n"
      + "<input type='text' class='form-control' name='w-price' id='w-price' value='" + mi.price + "'>");

  const categorySelect = $("#w-category");
  for (let i = 0; i < categories.length; i++) {
    const category = categories[i];
    if (category.categoryId === mi.category) {
      categorySelect.append("<option value='" + category.categoryId + "' selected>" + category.name + "</option>");
    } else {
      categorySelect.append("<option value='" + category.categoryId + "'>" + category.name + "</option>");
    }
  }

  $("#wizard-next-btn").attr("onclick", "wizardDietInfo()");
  $("#wizard").modal("show");
}

/**
 * Shows the diet info display of the wizard. Also saves the values of the basic info wizard display.
 */
function wizardDietInfo() {
  const wizardBody = $("#wizard-body");

  // Save the previous info and clear the modal
  mi.name = $("#w-name").val();
  mi.description = $("#w-description").val();
  mi.category = $("#w-category").val();
  mi.price = $("#w-price").val();
  wizardBody.empty();

  // Load the new contents
  wizardBody.append("<label for='w-calories'>Calories:</label>\n"
      + "<input type='text' class='form-control' name='w-calories' id='w-calories' value='" + mi.calories + "'>\n"
      + "<img id='w-isglutenfree' src=\"../images/gluten-free.svg\" onclick='toggleRequirement(this)'>\n"
      + "<img id='w-isvegetarian' src=\"../images/vegetarian-mark.svg\" onclick='toggleRequirement(this)'>\n"
      + "<img id='w-isvegan' src=\"../images/vegan-mark.svg\" onclick='toggleRequirement(this)'>\n");
  if (!mi.isGlutenFree) {
    $("#w-isglutenfree").fadeTo("fast", .5);
  }
  if (!mi.isVegetarian) {
    $("#w-isvegetarian").fadeTo("fast", .5);
  }
  if (!mi.isVegan) {
    $("#w-isvegan").fadeTo("fast", .5);
  }

  $("#wizard-next-btn").attr("onclick", "wizardIngredients()");

  // Make sure the modal is showing (can't see why it wouldn't but just to make sure)
  $("#wizard").modal("show");
}

/**
 * Returns a string to be inserted into the ingredients checklist if they should be checked before displaying to the user.
 * @param ingredient The ingredient to check
 * @returns Either the string ' checked' if it should be prechecked, or a blank string if not.
 */
function precheckIngredient(ingredient) {
  for (let i = 0; i < mi.ingredients.length; i++) {
    if (ingredient.id === mi.ingredients[i]) {
      return " checked";
    }
  }
  return "";
}

/**
 * Displays the ingredient check list wizard display. Also saves the values from the diet info display.
 */
function wizardIngredients() {
  const wizardBody = $("#wizard-body");

  mi.calories = $("#w-calories").val();

  wizardBody.empty();

  for (let i = 0; i < ingredients.length; i++) {
    const ingredient = ingredients[i];
    wizardBody.append("<div class='checkbox'>\n"
        + "<label><input class='ingredient-check' type='checkbox' value='" + ingredient.id + "'" + precheckIngredient(ingredient) + "> " + ingredient.ingredientName + "</label>"
        + "</div>")
  }

  $("#wizard-next-btn").attr("onclick", "wizardSubmit()");

  $("#wizard").modal("show");
}

/**
 * Saves the values of the ingredients wizard display and submits it to the server.
 */
function wizardSubmit() {
  const wizard = $("#wizard");
  const wizardBody = $("#wizard-body");

  // Clear the ingredients list
  mi.ingredients = [];

  // Set the ingredients list.
  $(".ingredient-check").each(function(index) {
    const currentIngredient = $(this)[0]; // Returns an array with one element for some reason? So index out.
    if (currentIngredient.checked) {
      mi.ingredients.push(currentIngredient.value);
    }
  });

  if (wizard.attr("data-mode") === "add") {
    // Add mode

    // Check the addNow
    if ($("#w-addtofranchise")[0].checked) {
      mi.addNow = true;
    } else {
      mi.addNow = false;
    }
    console.log(mi.addNow);
    post("/api/authStaff/createMenuItem", JSON.stringify(mi), function(data) {
      console.log(data);
      location.reload();
    });

  } else {
    // Edit mode.
    post("/api/authStaff/editMenuItem", JSON.stringify(mi), function(data) {
      console.log(data);
      location.reload();
    });
  }

  // Hide the modal
  wizard.modal("hide");
}

/**
 * Handles toggling the diet requirements in the diet info wizard.
 * @param item
 */
function toggleRequirement(item) {
  if (item.id === "w-isglutenfree") {
    if (mi.isGlutenFree === "true") {
      $("#w-isglutenfree").fadeTo("fast", .5);
      mi.isGlutenFree = "false";
    } else {
      $("#w-isglutenfree").fadeTo("fast", 1);
      mi.isGlutenFree = "true";
    }
  } else if (item.id === "w-isvegetarian") {
    if (mi.isVegetarian === "true") {
      $("#w-isvegetarian").fadeTo("fast", .5);
      mi.isVegetarian = "false";
    } else {
      $("#w-isvegetarian").fadeTo("fast", 1);
      mi.isVegetarian = "true";
    }
  } else if (item.id === "w-isvegan") {
    if (mi.isVegan === "true") {
      $("#w-isvegan").fadeTo("fast", .5);
      mi.isVegan = "false";
    } else {
      $("#w-isvegan").fadeTo("fast", 1);
      mi.isVegan = "true";
    }
  }
}

