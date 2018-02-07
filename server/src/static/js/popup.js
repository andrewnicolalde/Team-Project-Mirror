$(document).ready(function(){
    $('.myBtn').click(function(){
        $("#myModal").modal();
    });

function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {

    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}
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