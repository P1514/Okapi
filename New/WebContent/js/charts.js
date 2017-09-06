google.charts.load('current', {packages: ['corechart','treemap']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
  var data = new google.visualization.DataTable();
      data.addColumn('string', 'Month');
      data.addColumn('number', 'Scrap rate');
      data.addRows([
        ['December', 0.052],
        ['January', 0.0722],
        ['February', 0.0512],
        ['March', 0.0558],
        ['April', 0.0559],
        ['May', 0.0555]
      ]);

      var options = {
        title: 'Scrap rate',
        vAxis: {format:'#%'},
        backgroundColor: '#F5F5F5',
        curveType: 'function',
        pointSize: 5,
        colors: ['#24292e']
      }
      // Instantiate and draw the chart.
      var chart = new google.visualization.LineChart(document.getElementById('lineChart'));
      chart.draw(data, options);
}

/*
$(function () {
  $('#startDate').datetimepicker();
  $('#endDate').datetimepicker({
    useCurrent: false
  });
  $("#startDate").on("dp.change", function (e) {
    $('#endDate').data("DateTimePicker").minDate(e.date);
  });
  $("#endDate").on("dp.change", function (e) {
      $('#startDate').data("DateTimePicker").maxDate(e.date);
  });
});
*/
