google.charts.load('current', {'packages':['bar']});
google.charts.setOnLoadCallback(drawStuff);

function fillChartStock() {
    get("/api/authStaff/getStock", function (data) {
        const stocks = JSON.parse(data);

        var stocknum = [['Ingredient', 'Stock count']];
        for(var i = 0; i < stocks.length; i++){
            const stock = stocks[i];
            stocknum.push([stock.ingredient, stock.stockCount]);
        }
    });
}

function drawStuff() {
    var data = new google.visualization.arrayToDataTable([
        ['Ingredient', 'Stock count'],
        ["Apple", 44],
        ["Carrots", 22],
        ["Cheese", 10],
    ]);

    var options = {
        width: 800,
        height: 600,
        legend: { position: 'none' },
        chart: {
            title: 'Ingredient',
            subtitle: 'Stock Count' },
        axes: {
            x: {
                0: { side: 'top', label: ''} // Top x-axis.
            }
        },
        bar: { groupWidth: "90%" }
    };

    var chart = new google.charts.Bar(document.getElementById('chart'));
    // Convert the Classic options to Material options.
    chart.draw(data, google.charts.Bar.convertOptions(options));
};
