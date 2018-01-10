var ws; // websocket
var values; // chart data

if (window.location.href.indexOf('https://') != -1) {
	ws = new WebSocket('wss://' + window.location.hostname + ':'
			+ window.location.port + '/Okapi/server');
} else {
	ws = new WebSocket('ws://' + window.location.hostname + ':'
			+ window.location.port + '/Okapi/server');
}

ws.onopen = function() {
	var json = {
		'Op' : 'getProducts'
	};

	ws.send(JSON.stringify(json));
};

ws.onmessage = function(event) {
	var request = JSON.parse(event.data.replace(/\\/g, '')); // used to
	// remove extra
	// brackets in
	// diversity,
	// check this if
	// json seems
	// incorrect

	// check OPs in each request
	if (request[0].Op == "scrap") {

		values = request;
		drawChart();
	}

	if (request[0].Op == "products") {
		setOptions('productSelect', request);

		var json = {
			'Op' : 'getMachines'
		};
		ws.send(JSON.stringify(json));
	}

	if (request[0].Op == "machines") {
		setOptions('machineSelect', request);

		var json = {
			'Op' : 'getMoulds'
		};
		ws.send(JSON.stringify(json));
	}

	if (request[0].Op == "moulds") {
		setOptions('mouldSelect', request);

		var json = {
			'Op' : 'getShifts'
		};
		ws.send(JSON.stringify(json));
	}

	if (request[0].Op == "shifts") {
		setOptions('shiftSelect', request);

		requestChart();
	}
	
	if (request[0].Op == "scrapChart") {
		drawChart(request);
	}
};

google.charts.load('current', {
	packages : [ 'corechart', 'treemap' ]
});
google.charts.setOnLoadCallback(requestCharts);

function requestCharts() {
	console.log('Google charts loaded')
}

function setOptions(id, options) {
	for (var i = 0; i < options.length; i++) {
		if (!options[i].hasOwnProperty('Op')) {
			$('#' + id).append($('<option/>', {
				value : options[i].Value,
				text : options[i].Value
			}));
		}
	}
}

function drawChart(values) {

	// Scrap rate chart (top)
	var data = new google.visualization.DataTable();
	data.addColumn('date', 'Date');
	data.addColumn('number', 'Scrap rate');

	for (var i = 0; i < values.length; i++) {
		if (values[i].hasOwnProperty("Value")) {
			var strDate = values[i].Date.split('-');
			var strTime = values[i].Time.split(':');
//			var date = new Date(parseInt(strDate[0]), parseInt(strDate[1]), parseInt(strDate[3]), parseInt(strTime[0]), parseInt(strTime[1]), parseInt(strTime[2]), 0);
			var date = new Date(values[i].Date + ' ' + values[i].Time);
			var value = values[i].Value;
			data.addRow([ date, value ]);
		}
	}

	// data.addRows([ [ 'December', 0.052 ], [ 'January', 0.0722 ],
	// [ 'February', 0.0512 ], [ 'March', 0.0558 ], [ 'April', 0.0559 ],
	// [ 'May', 0.0555 ] ]);

	var options = {
		title : 'Scrap rate',
		backgroundColor : '#F5F5F5',
		pointSize : 5,
		colors : [ '#24292e' ],
		explorer: {
            axis: 'horizontal',
            keepInBounds: true,
            maxZoomIn: 4.0,
            maxZoomOut: 1.0
          },
		pointsVisible: false
	};

	var chart = new google.visualization.LineChart(document
			.getElementById('lineChart'));
	chart.draw(data, options);

	// Heatmap (bottom)

	data = {
		labels : [ 'Shift 1', 'Shift 2', 'Shift 3' ],
		datasets : [ {
			label : 'Product A',
			data : [ 3.5, 4.2, 1 ]
		}, {
			label : 'Product B',
			data : [ 1.5, 7, 5 ]
		}, {
			label : 'Product C',
			data : [ 2, 1, 1.5 ]
		}, {
			label : 'Product D',
			data : [ 3, 5.5, 1 ]
		}, {
			label : 'Product E',
			data : [ 0.5, 0, 1.5 ]
		}, {
			label : 'Product F',
			data : [ 2, 1.5, 1 ]
		}, {
			label : 'Product G',
			data : [ 2, 3, 1 ]
		} ]
	};

	options = {
		colors : [ '#ffffe0', '#ffd59b', '#ffa474' ],
		stroke : true,
		strokePerc : 0.01,
		roundedRadius : 0.0,
		paddingScale : 0,
		colorHighlightMultiplier : 0.80,
		highlightStrokeColor : "rgb(192,192,192)",
		labelScale : 0.35
	};

	var ctx = document.getElementById('heatmap').getContext('2d');
	var newChart = new Chart(ctx).HeatMap(data, options);

}

function requestChart() {
	var json = {
		'Op' : 'getScrapChart',
		'Product' : $('#productSelect').find(":selected").text(),
		'Machine' : $('#machineSelect').find(":selected").text(),
		'Shift' : $('#shiftSelect').find(":selected").text(),
		'Mould' : $('#mouldSelect').find(":selected").text(),
		'Start' : $('#startDate').val(),
		'End' : $('#endDate').val(),
		'Granularity' : $('#granularitySelect').find(":selected").text(),
	};
	ws.send(JSON.stringify(json));
}

/*
 * $(function () { $('#startDate').datetimepicker();
 * $('#endDate').datetimepicker({ useCurrent: false });
 * $("#startDate").on("dp.change", function (e) {
 * $('#endDate').data("DateTimePicker").minDate(e.date); });
 * $("#endDate").on("dp.change", function (e) {
 * $('#startDate').data("DateTimePicker").maxDate(e.date); }); });
 */
