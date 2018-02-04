$(document).ready(function() {

  get("/api/authStaff/menu", function(data){
    // Parse JSON
    var response = JSON.parse(data);

    for(i = 0; i < response.length; i++) {
        $("#menu-table").append("<tr>" +
            "<th scope=\"row\">" + (i + 1) + "</th>" +
            "<td>" + response[i].menuItems.name + "</td>" +
            "<td>" + response[i].menuItems.price + "</td>" +
            "</tr>");
    }
  }
}