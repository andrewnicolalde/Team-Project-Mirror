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

function startNewMenuItem(name) {
  const wizardBody = $("#wizard-body");
  wizardBody.empty();
  wizardBody.append("<label for='w-name'>Name</label>\n"
      + "<input type='text' class='form-control' name='w-name' id='w-name' value='" + name + "'>");
  $("#wizard").modal("show");
}