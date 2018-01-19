$(document).ready(function () {
  // Send a request to get the tables
  $.get("/api/tables", function(data) {
    //Callback function

    // Parse the json into objects
    var response = JSON.parse(data);

    // Loop through response and append items to a list (Or do what ever you like to them
    for (i = 0; i < response.length; i++) {
      console.log(i);
      $("#tables-list").append("<li class='list-group-item'>" + response[i].number + ": " + response[i].status + "</li>");
    }
  });
});