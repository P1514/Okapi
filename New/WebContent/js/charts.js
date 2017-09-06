var ws;


if (window.location.href.indexOf('https://') != -1) {
	ws = new WebSocket('wss://' + window.location.hostname + ':' + window.location.port + '/Okapi/server');
} else {
	ws = new WebSocket('ws://' + window.location.hostname + ':' + window.location.port + '/Okapi/server');
}


ws.onopen = function() {
	var json = {
			'Op' : 'testing'
	};
	
	ws.send(JSON.stringify(json));
	console.log('Socket is open');
};

ws.onmessage = function(event) {
	var request = JSON.parse(event.data.replace(/\\/g,'')); //used to remove extra brackets in diversity, check this if json seems incorrect
	
	//check OPs in each request
};



google.charts.load('current', {
	packages : [ 'corechart', 'treemap' ]
});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {

	// Scrap rate chart (top)
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Month');
	data.addColumn('number', 'Scrap rate');
	data.addRows([ [ 'December', 0.052 ], [ 'January', 0.0722 ],
			[ 'February', 0.0512 ], [ 'March', 0.0558 ], [ 'April', 0.0559 ],
			[ 'May', 0.0555 ] ]);

	var options = {
		title : 'Scrap rate',
		vAxis : {
			format : '#%'
		},
		backgroundColor : '#F5F5F5',
		curveType : 'function',
		pointSize : 5,
		colors : [ '#24292e' ]
	}

	var chart = new google.visualization.LineChart(document
			.getElementById('lineChart'));
	chart.draw(data, options);

	// Heatmap (bottom)

	var data = {
		labels : [ 'Shift 1', 'Shift 2', 'Shift 3' ],
		datasets : [ {
			label : 'Product A',
			data : [ 3, 4, 1 ]
		}, {
			label : 'Product B',
			data : [ 1, 7, 5 ]
		}, {
			label : 'Product C',
			data : [ 2, 1, 1 ]
		}, {
			label : 'Product D',
			data : [ 3, 5, 1 ]
		}, {
			label : 'Product E',
			data : [ 2, 0, 1 ]
		}, {
			label : 'Product F',
			data : [ 2, 1, 1 ]
		}, {
			label : 'Product G',
			data : [ 2, 3, 1 ]
		} ]
	};



	options = {
		colors : [ '#ffffe0', '#ffd59b', '#ffa474'],
		stroke: true,
		strokePerc: 0.01,
		roundedRadius: 0.0,
		paddingScale: 0,
		colorHighlightMultiplier: 0.80,
		highlightStrokeColor: "rgb(192,192,192)",
		labelScale: 0.35
	}
	var ctx = document.getElementById('heatmap').getContext('2d');
	var newChart = new Chart(ctx).HeatMap(data, options);

}

/*
 * $(function () { $('#startDate').datetimepicker();
 * $('#endDate').datetimepicker({ useCurrent: false });
 * $("#startDate").on("dp.change", function (e) {
 * $('#endDate').data("DateTimePicker").minDate(e.date); });
 * $("#endDate").on("dp.change", function (e) {
 * $('#startDate').data("DateTimePicker").maxDate(e.date); }); });
 */
