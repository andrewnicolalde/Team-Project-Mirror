google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var data = google.visualization.arrayToDataTable([
        ['Time',  'Meat', 'Veg'],
        ['8:00',   400,   300],
        ['12:00',  460,  500],
        ['15:00',  300, 100],
        ['18:00',  120,  200],
        ['21:00',  100,  160],
        ['24:00',  50,  63]
    ]);


    var options = {
        title: 'Main Ingredients',
        curveType: 'function',
        legend: { position: 'bottom' }
    };

    var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

    chart.draw(data, options);
}

