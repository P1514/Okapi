package general;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	public String resolve() throws JSONException {
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
		case 1:
			dbc = new DBController();
			obj = new JSONObject();
			obj.put(OP, "total_units");
			
			result = new JSONArray();
			result.put(obj);
			
			obj = new JSONObject();
			obj.put("Value", dbc.FM_getTotalUnits());
			
			result.put(obj);
			
			return result.toString();
		case 2:
			dbc = new DBController();
			obj = new JSONObject();
			obj.put(OP, "scrap_rate");
			
			result = new JSONArray();
			result.put(obj);
			
			obj = new JSONObject();
			obj.put("Value", dbc.FM_getScrapRate());
			
			result.put(obj);
			
			return result.toString();
		case 3: 
			dbc = new DBController();
			obj = new JSONObject();
			obj.put(OP, "oee");
			
			result = new JSONArray();
			result.put(obj);
			
			obj = new JSONObject();
			obj.put("Value", dbc.FM_getOEE());
			
			result.put(obj);
			
			return result.toString();
		case 4: 
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
		case 5:
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
		case 6: 
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
		case 7:
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
		case 8:
			break;
		case 9:
			break;
		case 10:
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
