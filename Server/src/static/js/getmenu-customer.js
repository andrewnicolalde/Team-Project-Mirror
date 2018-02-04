$(document).ready(function() {

  get("/api/authStaff/menu", function(data){
    // Parse JSON
    var response = JSON.parse(data);

    function drawTable() {
        for (var i = 0; i < response.length; i++) {
            drawRow(response[i]);
        }
    }

    function drawRow(rowData) {
        var row = $("<tr />")
        $("#menu-table").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
        row.append($("<td>" + rowData.name + "</td>"));
        row.append($("<td>" + rowData.price + "</td>"));
    }



    for(i = 0; i < response.length; i++) {
        $("#menu-table").append("<tr>" +
            "<th scope=\"row\">" + (i + 1) + "</th>" +
            "<td>" + response[i].menuItems.name + "</td>" +
            "<td>" + response[i].menuItems.price + "</td>" +
            "</tr>");
    }
  }
}