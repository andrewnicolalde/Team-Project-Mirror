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
      for (let i = 0; i < menuItems.length; i++) {
        const item = menuItems[i];
        $("#menu").append("<tr id='mi-" + item.id + "'>\n"
            + "<td>" + item.name + "</td>\n"
            + "<td>" + item.description + "</td>\n"
            + "<td>" + item.category + "</td>"
            + "<td>£" + parseFloat(item.price).toFixed(2) + "</td>\n"
            + "<td>" + item.calories + "kCal</td>\n"
            + "<td>" + getDietaryRequirements(item) + "</td>\n"
            + "<td>" + getIngredientsList(item.ingredients) + "</td>\n"
            + "<td>" + item.picture_src + "</td>\n"
            + "<td><i id='edit-" + item.id + "' class=\"fas fa-edit fa-lg edit\" onclick='wizardEdit(" + item.id + ");'></i></td>\n"
            + "</tr>");
      }
    });
  });

  get("/api/authStaff/getCategories", function (data) {
    categories = JSON.parse(data);
  });
});

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

  // Start the wizard
  wizardBasic();
}

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

  // Start the wizard.
  wizardBasic();
}

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

function precheckIngredient(ingredient) {
  for (let i = 0; i < mi.ingredients.length; i++) {
    if (ingredient.id === mi.ingredients[i]) {
      return " checked";
    }
  }
  return "";
}

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
    mi.addNow = true;
    console.log(mi);
    post("/api/authStaff/createMenuItem", JSON.stringify(mi), function(data) {
      console.log(data);
    });

  } else {
    // Edit mode.

  }

  // Hide the modal
  wizard.modal("hide");
}

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

