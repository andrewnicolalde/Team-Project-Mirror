let mi = {
  name: "",
  description: "",
  category: "",
  price: "",
  calories: "",
  isGlutenFree: false,
  isVegetarian: false,
  isVegan: false
};

let categories = [];

$(document).ready(function() {
  get("/api/authStaff/getMenu", function(data) {
    menuItems = JSON.parse(data);
    for (let i = 0; i < menuItems.length; i++) {
      const item = menuItems[i];
      $("#menu").append("<tr><td>" + item.name + "</td>\n"
          + "<td>" + item.description + "</td>\n"
          + "<td>" + item.category + "</td>"
          + "<td>£" + parseFloat(item.price).toFixed(2) + "</td>\n"
          + "<td>" + item.calories + "kCal</td>\n"
          + "<td>" + getDietaryRequirements(item) + "</td>\n"
          + "<td>" + item.ingredients + "</td>\n"
          + "<td>" + item.picture_src + "</td>\n"
          + "<td></td></tr>");
    }
  });

  get("/api/authStaff/getCategories", function (data) {
    categoriesObjects = JSON.parse(data);
    for (let i = 0; i < categoriesObjects.length; i++) {
      categories.push(categoriesObjects[i].name);
    }
  });
});

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

function wizardStart() {
  const wizardBody = $("#wizard-body");
  wizardBody.empty();
  wizardBody.append("<label for='w-name'>Name:</label>\n"
      + "<input type='text' class='form-control' name='w-name' id='w-name' value='" + mi.name + "'>"
      + "<label for='w-description'>Description:</label>\n"
      + "<input type='text' class='form-control' name='w-description' id='w-description' value='" + mi.description + "'>\n"
      + "<label for='w-category'>Category:</label><br>\n"
      + "<select id='w-category' name='w-category'>\n"
      + "</select><br>\n"
      + "<label for='w-price'>Price:</label>\n"
      + "<input type='text' class='form-control' name='w-price' id='w-price' value='" + mi.price + "'>");

  const categorySelect = $("#w-category");
  for (let i = 0; i < categories.length; i++) {
    const category = categories[i];
    if (category === mi.category) {
      categorySelect.append("<option value='" + category + "' selected>" + category + "</option>");
    } else {
      categorySelect.append("<option value='" + category + "'>" + category + "</option>");
    }
  }

  $("#wizard-next-btn").click(wizardDietInfo);
  $("#wizard").modal("show");
}

function wizardDietInfo() {
  const wizardBody = $("#wizard-body");

  // Save the previous info and clear the modal
  mi.name = $("#w-name").val();
  mi.description = $("#w-description").val();
  mi.category = $("#w-category").val();
  mi.price = $("#w-price");
  wizardBody.empty();

  // Load the new contents
  wizardBody.append("<label for='w-calories'>Calories:</label>\n"
      + "<input type='text' class='form-control' name='w-calories' id='w-calories' value='" + mi.calories + "'>\n"
      + "<input type='checkbox' name='w-isglutenfree' id='w-isglutenfree'> <img src=\"../images/gluten-free.svg\">\n"
      + "<input type='checkbox' name='w-isvegetarian' id='w-isvegetarian'> <img src=\"../images/vegetarian-mark.svg\">\n"
      + "<input type='checkbox' name='w-isvegan' id='w-isvegan'> <img src=\"../images/vegan-mark.svg\">\n");

  // Make sure the modal is showing (can't see why it wouldn't but just to make sure)
  $("#wizard").modal("show");
}

