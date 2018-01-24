/**
 * This script is responsible for retrieving and displaying the tables
 * (i.e. Table 1) in the Tables column in waiter-ui.html
 */
$(document).ready(function () {
  // Send a request to get the tables
  get("/api/auth/tables", function(data) {
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
          + "onclick=\"loadOrder(this.getAttribute('data-tablenum'))\" >"
          + "<span class='waiter-ui-span-bold'>Table "
          + response[i].number + ":</span> " + response[i].status + "</li>");
    }
  });
});