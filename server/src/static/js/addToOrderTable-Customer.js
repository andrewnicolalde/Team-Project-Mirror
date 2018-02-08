 $(document).ready(function(){
        $(".add-row").click(function()) {
            var name = $("#item").val();
            var email = $("#price").val();
            var markup = "<tr><td><input type='checkbox' name='record'></td><td>" + name + "</td><td>" + email + "</td></tr>";
            $("table tbody").append(markup);
        });
