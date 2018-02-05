/**
 * This script is responsible for setting whichever table is selected,
 * making it active.
 *
 * @param event The event which was triggered by the clicked element
 */
function setCurrentTable(event){
  // Reset all Tables to non-active
  var allTables = document.getElementById("tables-list").children;
  for(i = 0; i < allTables.length; i++){
    allTables[i].className = "list-group-item list-group-item-action";
  }

  // Set active element
  var activeTable = document.getElementById(event.currentTarget.id);
  activeTable.className += " active";
}
