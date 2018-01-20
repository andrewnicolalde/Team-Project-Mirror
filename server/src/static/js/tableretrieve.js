$(document).ready(function () {
  // Send a request to get the tables
  $.get("/api/tables", function(data) {
    //Callback function

    // Parse the json into objects
    var response = JSON.parse(data);

    // Loop through response and append items to a list (Or do what ever you like to them
    for (i = 0; i < response.length; i++) {
      $("#tables-list").append("<li class='list-group-item'><span class='table-card-name'>Table " + response[i].number + ":</span> " + response[i].status + "</li>");
    }
  });
});