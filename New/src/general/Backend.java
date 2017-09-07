package general;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Backend {
	
	private static final String OP = "Op";
	private int op;
	private JSONObject msg;
	
	public Backend(int op, JSONObject msg) {
		this.op = op;
		this.msg = msg;
	}

	public String resolve() throws JSONException {
		JSONArray result;
		JSONObject obj;
		
		switch (op) {

		case 99:
			//testing if server receives/sends messages correctly
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
