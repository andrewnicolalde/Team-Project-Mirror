/**
 * This function returns the selected table.
 * @returns The web element representing the current table
 */
function getActiveTable() {
    var allTables = document.getElementById("tables-list").children;
    var activeTable;
    for (i = 0; i < allTables.length; i++) {
        if (allTables[i].classList.contains("active")) {
            activeTable = allTables[i];
        }
    }
    return activeTable;
}

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
    var activeTable = getActiveTable();

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
    post("/api/authStaff/addToOrder", nameValuePairs, function(status){
        loadOrder(activeTable.getAttribute('data-tablenum'));
        if(status === ""){
            // Refresh current order table to show new change
            console.log("Add item to order failed");
            console.log(status);
        }
    });
}

/**
 * This script is responsible for retrieving and displaying the contents of each
 * Table's current order. It also clears the Current Order column of any existing
 * entries before adding the selected Table's entries to the Current Order column.
 *
 * @param tableNumber The number of the table who's current order is to be displayed
 *                    in the Current Order column.
 */
function loadOrder(tableNumber) {
    var tableNumberToSend = JSON.stringify({tableNumber: tableNumber});
    post("/api/authStaff/getOrder", tableNumberToSend, function (data) {

        // Parse JSON
        var response = JSON.parse(data);

        // Remove any existing elements in the current order list
        var currentOrderElement = document.getElementById("current-order");
        while (currentOrderElement.firstChild) {
            currentOrderElement.removeChild(currentOrderElement.firstChild);
        }

        // Add each list item
        for (i = 0; i < response.length; i++) {
            $("#current-order").append("<li class='list-group-item list-group-item-action'"
                + "id= \"order-item-" + i + "\">"
                + "<span class='waiter-ui-span-bold'>"
                + response[i].name + ": </span> "
                + response[i].price + "</li>");
            // Show dietary information
            if (response[i].is_gluten_free) { // Gluten Free
                $("#order-item-" + i).append(" <img src="
                    + "'../images/gluten-free.svg'alt='Gluten Free'>");
            }
            if (response[i].is_vegetarian) { // Vegetarian
                $("#order-item-" + i).append(" <img src="
                    + "'../images/vegetarian-mark.svg'alt='Vegetarian'>");
            }
            if (response[i].is_vegan) {
                $("#order-item-" + i).append(" <img src="
                    + "'../images/vegan-mark.svg'alt='Vegan'>");
            }
        }
    });
}

/**
 * This function is responsible for retrieving and displaying the menu
 * item elements in the Menu column in waiter-ui.html.
 *
 * Note: This script should be used only for the waiter's UI. This is because this script
 * only displays the name of each menu item & its price, along with dietary info.
 * Obviously a customer would want to see more than that.
 */
function loadMenu() {
    // Send get request to server for menu JSON
    get("/api/authStaff/menu", function (data) {
        // Parse JSON
        var response = JSON.parse(data);

        // Add items to menu list
        for (i = 0; i < response.length; i++) {
            $("#menu-list").append("<li class='list-group-item list-group-item-action'"
                + "id= \"menu-item-" + i + "\""
                + "data-menuItemNum='" + response[i].id + "'"
                + "onclick='addToOrder(this.getAttribute(\"data-menuItemNum\"))'>"
                + "<span class='waiter-ui-span-bold'>"
                + response[i].name + ": </span> " + response[i].price + "</li>");
            // Show dietary information
            if (response[i].is_gluten_free) { // Gluten Free
                $("#menu-item-" + i).append(
                    " <img src='../images/gluten-free.svg' alt='Gluten Free'>");
            }
            if (response[i].is_vegetarian) { // Vegetarian
                $("#menu-item-" + i).append(
                    " <img src='../images/vegetarian-mark.svg' alt='Vegetarian'>");
            }
            if (response[i].is_vegan) {
                $("#menu-item-" + i).append(
                    " <img src='../images/vegan-mark.svg' alt='Vegan'>");
            }
        }
    });
}

/**
 * This function is responsible for retrieving and displaying the tables
 * (i.e. Table 1) in the Tables column in waiter-ui.html
 */
function loadTables() {
    get("/api/authStaff/tables", function (data) {
        //Callback function

        // Parse the json into objects
        var response = JSON.parse(data);

        /*
        *  Loop through response and append items to a list
        *  Or do whatever you'd like with them.
        */
        for (i = 0; i < response.length; i++) {
            $("#tables-list").append("<li data-tablenum='" + response[i].number +
                "' id='table-" + response[i].number
                + "' class='list-group-item list-group-item-action' "
                + "onclick=\"setCurrentTable(event); loadOrder(this.getAttribute('data-tablenum'));\">"
                + "<span class='waiter-ui-span-bold'>Table "
                + response[i].number + ":</span> " + response[i].status + "</li>");
        }
    });
}

/**
 * This script is responsible for setting whichever table is selected,
 * making it active.
 *
 * @param event The event which was triggered by the clicked element
 */
function setCurrentTable(event) {
    // Reset all Tables to non-active
    var allTables = document.getElementById("tables-list").children;
    for (i = 0; i < allTables.length; i++) {
        allTables[i].className = "list-group-item list-group-item-action";
    }

    // Set active element
    var activeTable = document.getElementById(event.currentTarget.id);
    activeTable.className += " active";
}


function changeOrderStatus(orderStatus) {
    var activeTable = getActiveTable();
    post("/api/authStaff/changeOrderStatus",
        JSON.stringify({
            tableNumber: activeTable.getAttribute('data-tablenum'),
            newOrderStatus: orderStatus
        }),
        loadTables
    );
}

// Loads the menu and tables when the page loads.
$(document).ready(function () {
    loadMenu();
    loadTables();
});