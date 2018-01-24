/**
 *This script retrieves the current order for a given
 *table so that it can be displayed in the basket page.
 */



//function loadOrder(tableNumber){
    document.write("An order");
    var tableNumberToSend = JSON.stringify({tableNumber: tableNumber});
    post("path", tableNumberToSend, function(data){
        //parse JSON
        var response = JSON.parse(data);
        var superelement = document.getElementById("current-order");
        //add items to order list
        while(superelement.firstChild){
            superelement.removeChild(superelement.firstChild);
        }
        for (i = 0; i < response.length; i++) {
            $("#current-order").append("<li class = 'list-group-item list-group-item-action' id= \"order-item-"+i+"\"><span class='custmomer-ui-span-bold'>"
                +response[i].name+": </span>"+response[i].price+"</li>");

        }
});
//}