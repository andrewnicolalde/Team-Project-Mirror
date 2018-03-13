google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var data = google.visualization.arrayToDataTable([
        ['Time', 'Stock', 'Meat', 'Veg'],
        ['8:00',  500,      400,   300],
        ['12:00',  500,      460,  500],
        ['15:00',  420,       300, 100],
        ['18:00',  400,      120,  200],
        ['21:00',  340,       100,  160],
        ['24:00',  200,       50,  63]
    ]);


    var options = {
        title: 'Stock',
        curveType: 'function',
        legend: { position: 'bottom' }
    };

    var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

    chart.draw(data, options);
}

