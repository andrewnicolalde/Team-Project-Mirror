/**
 * This function is responsible for adding menu items to the current order
 * of the currently active table.
 * @param menuItemId the ID of the menu item to be added to the order
 * @param description Any requirements such as cooking preferences to be sent
 *                    to the kitchen with the order.
 */
// TODO: Add a description attribute here so that additional preparation info can be provided.
function addToOrder(menuItemId) {

  // Find active table
  var allTables = document.getElementById("tables-list").children;
  var activeTable;
  for (i = 0; i < allTables.length; i++) {
    if (allTables[i].classList.contains("active")) {
      activeTable = allTables[i];
    }
  }

  // TODO: Remove this and add an actual description box in the UI
  var requirements = "This is a test descripion"

  // Create name-value pairs for HTTP post request, see
  // https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
  var nameValuePairs = JSON.stringify({
    tableNumber: activeTable.getAttribute('data-tablenum'),
    menuItemId: menuItemId,
    requirements: requirements
  });

  // Handle possible responses
  post("/api/authStaff/addToOrder", nameValuePairs, function (status) {
    loadOrder();
    if (status === "") {
      // Refresh current order table to show new change
      console.log("Add item to order failed");
      console.log(status);
    }
  });
}