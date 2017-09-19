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
	var request = JSON.parse(event.data.replace(/\\/g, '')); // used to remove extra brackets in diversity, check this if json seems incorrect

	// check OPs in each request
	if (request[0].Op == "top_chart") {

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

		updateCharts();
	}
};

google.charts.load('current', {
	packages : [ 'corechart', 'treemap' ]
});
google.charts.setOnLoadCallback(requestCharts);

//toggle dropdowns when radio buttons are selected, and hide them when they're not (refactor?)
$(document).ready(function() {
  $('input[type=radio][name=filter]').change(function() {
    if (this.value == 'product') {
      $('#productSelect').show();
			$('#machineSelect').hide();
			$('#shiftSelect').hide();
			$('#mouldSelect').hide();
    }
    else if (this.value == 'machine') {
			$('#productSelect').hide();
			$('#machineSelect').show();
			$('#shiftSelect').hide();
			$('#mouldSelect').hide();
    }
		else if (this.value == 'shift') {
			$('#productSelect').hide();
			$('#machineSelect').hide();
			$('#shiftSelect').show();
			$('#mouldSelect').hide();
    }
		else if (this.value == 'mould') {
			$('#productSelect').hide();
			$('#machineSelect').hide();
			$('#shiftSelect').hide();
			$('#mouldSelect').show();
    }
		else if (this.value == 'global') {
			$('#productSelect').hide();
			$('#machineSelect').hide();
			$('#shiftSelect').hide();
			$('#mouldSelect').hide();
		}
	});
});

function requestCharts() {
	console.log('Google charts loaded')
}

// called when page is loaded and when the Update button is pressed
function updateCharts() {
	var filter = $('input[name=filter]:checked').val(); // gets the name of the selected radio button
	var option = undefined;

	if (filter != 'global') {
		option = getOption(filter + 'Select'); // if the filter is not global, get the selected option from the respective dropdown
	}

	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();

	if (startDate != undefined && endDate != undefined) { // if start date is later than end date, dates are ignored and the full timespan is shown
		if (new Date(startDate).getTime() > new Date(endDate).getTime()) {
			startDate = undefined;
			endDate = undefined;
		}
	}

	var json = {
		'Op' : 'getScrapChart',
		'Product' : filter == 'product' ? option : undefined,
		'Machine' : filter == 'machine' ? option : undefined,
		'Shift' : filter == 'shift' ? option : undefined,
		'Mould' : filter == 'mould' ? option : undefined,
		'StartDate' : startDate != '' ? startDate : undefined,
		'EndDate' : endDate != '' ? endDate : undefined,
		'Granularity' : getOption('granularity'),
		'Prediction' : document.getElementById('showPrediction').checked,
		'Global' : document.getElementById('includeGlobal').checked
	};

	ws.send(JSON.stringify(json));
}

//returns the selected option from any given dropdown id
function getOption(id) {
	return $('#' + id).val();
}

//adds options to a dropdown list
function setOptions(id, options) {
	for (var i = 0; i < options.length; i++) {
		if (!options[i].hasOwnProperty('Op')) {
	    $('#' + id).append($('<option/>', {
	      value: options[i].Value,
	      text: options[i].Value
	    }));
		}
	}
}

function drawChart() {

	// Scrap rate chart (top)
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Month');
	data.addColumn('number', 'Scrap rate');

	for (var i = 0; i < values.length; i++) {
		if (values[i].hasOwnProperty("Month")) {
			var month = values[i].Month;
			var value = values[i].Value;
			data.addRow([ month, value ]);
		}
	}

	// data.addRows([ [ 'December', 0.052 ], [ 'January', 0.0722 ],
	// [ 'February', 0.0512 ], [ 'March', 0.0558 ], [ 'April', 0.0559 ],
	// [ 'May', 0.0555 ] ]);

	var options = {
		title : 'Scrap rate',
		backgroundColor : '#F5F5F5',
		curveType : 'function',
		pointSize : 5,
		colors : [ '#24292e' ]
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
			data : [ 2, 1, 1.5]
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
