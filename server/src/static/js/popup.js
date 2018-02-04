$(document).ready(function(){
    $('.myBtn').click(function(){
        $("#myModal").modal();
    });
});


$(document).ready(function(){
    $('#addbutton').click(function(){
        $("#menu-table").clone().appendTo('#table-body');
    });
});

var row = document.getElementById("#row1move");

function putBack(){
  var tbl = document.getElementById("#table-body");
  tbl.appendChild(row);
}