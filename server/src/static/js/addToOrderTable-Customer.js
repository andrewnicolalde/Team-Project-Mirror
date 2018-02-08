$(document).ready(function(){
        $(".add-row").click(function(){
            var name = $(".name").val();
            var price = $(".price").val();
            var markup = "<tr><td></td><td>" + name + "</td><td>" + price + "</td></tr>";
            $("table.cart tbody").append(markup);
        });
    $("td").click(function () {
          $(".name").clone().appendTo(".cart");
        });

        // Find and add selected table rows to cart table
        $(".add-order").click(function(){
            $("table tbody").find('input[name="record"]').each(function(){
            	if($(this).is(":checked")){
                    $(".name").clone().appendTo(".cart");
                }
            });
        });
    });