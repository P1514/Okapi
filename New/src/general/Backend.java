package general;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import charts.*;

/**
 * Class that processes operations and returns the output, if it exists, in a stringified JSON format.
 * @author Uninova
 *
 */
public class Backend {
	
	private static final String OP = "Op";
	private int op;
	private JSONObject msg;
	
	public Backend(int op, JSONObject msg) {
		this.op = op;
		this.msg = msg;
	}

	public String resolve() throws JSONException, ParseException {
		JSONArray result = new JSONArray();
		JSONObject obj = new JSONObject();
		DBController dbc = new DBController();
		switch (op) {

		case 99:
			//testing if server receives/sends messages correctly
			// it does - this can be deleted
			obj = new JSONObject();
			obj.put(OP, "top_chart");
			
			result = new JSONArray();
			result.put(obj);
			
			String[] months = { "January" , "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
			
			for (String month : months) {
				obj = new JSONObject();
				obj.put("Month", month);
				obj.put("Value", (Math.random() * 100));
				
				result.put(obj);
			}
			
			break;
		case 1: // getTotal
			dbc = new DBController();
			obj = new JSONObject();
			obj.put(OP, "total_units");
			
			result = new JSONArray();
			result.put(obj);
			
			obj = new JSONObject();
			obj.put("Value", dbc.FM_getTotalUnits());
			
			result.put(obj);
			
			return result.toString();
		case 2: // getScrapRate
			dbc = new DBController();
			obj = new JSONObject();
			obj.put(OP, "scrap_rate");
			
			result = new JSONArray();
			result.put(obj);
			
			obj = new JSONObject();
			obj.put("Value", dbc.FM_getScrapRate());
			
			result.put(obj);
			
			return result.toString();
		case 3: //getOEE
			dbc = new DBController();
			obj = new JSONObject();
			obj.put(OP, "oee");
			
			result = new JSONArray();
			result.put(obj);
			
			obj = new JSONObject();
			obj.put("Value", dbc.FM_getOEE());
			
			result.put(obj);
			
			return result.toString();
		case 4: // getProducts
			dbc = new DBController();
			obj = new JSONObject();
			obj.put(OP, "products");
			
			result = new JSONArray();
			result.put(obj);
			
			List<String> products = dbc.FM_getProducts();
			for (String s : products) {
				obj = new JSONObject();
				obj.put("Value", s);
				result.put(obj);
			}
			
			return result.toString();
		case 5: // getMachines
			dbc = new DBController();
			obj = new JSONObject();
			obj.put(OP, "machines");
			
			result = new JSONArray();
			result.put(obj);
			
			List<String> machines = dbc.FM_getMachines();
			for (String s : machines) {
				obj = new JSONObject();
				obj.put("Value", s);
				result.put(obj);
			}
			
			return result.toString();
		case 6: // getShifts
			dbc = new DBController();
			obj = new JSONObject();
			obj.put(OP, "shifts");
			
			result = new JSONArray();
			result.put(obj);
			
			List<String> shifts = dbc.FM_getShifts();
			for (String s : shifts) {
				obj = new JSONObject();
				obj.put("Value", s);
				result.put(obj);
			}
			
			return result.toString();
		case 7: // getMoulds
			dbc = new DBController();
			obj = new JSONObject();
			obj.put(OP, "moulds");
			
			result = new JSONArray();
			result.put(obj);
			
			List<String> moulds = dbc.FM_getMoulds();
			for (String s : moulds) {
				obj = new JSONObject();
				obj.put("Value", s);
				result.put(obj);
			}
			
			return result.toString();
		case 8: // getScrapChart
			int type = Settings.TYPE_GLOBAL;
			String filter = null;
			Date startDate = null;
			Date endDate = null;
			
			if (msg.has("Product")) {
				type = Settings.TYPE_PRODUCT;
				filter = msg.getString("Product");
			} else if (msg.has("Machine")) {
				type = Settings.TYPE_MACHINE;
				filter = msg.getString("Machine");
			} else if (msg.has("Shift")) {
				type = Settings.TYPE_SHIFT;
				filter = msg.getString("Shift");
			} else if (msg.has("Mould")) {
				type = Settings.TYPE_MOULD;
				filter = msg.getString("Mould");
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if (msg.has("StartDate")) {
				startDate = sdf.parse(msg.getString("StartDate"));
			}
			
			if (msg.has("EndDate")) {
				endDate = sdf.parse(msg.getString("EndDate"));
			}
			
			ScrapChart chart = new ScrapChart(type, filter, startDate, startDate, msg.getBoolean("Prediction"), msg.getBoolean("Global"));
			return chart.getChart();
		case 9: // getHeatmap
			break;
		case 10: // getPrediction
			break;
		default:
			obj = new JSONObject();
			obj.put(OP, "Error");
			obj.put("Message", "Operation not found.");
			
			result = new JSONArray();
			result.put(obj);
		}

		return result.toString();
	}
}