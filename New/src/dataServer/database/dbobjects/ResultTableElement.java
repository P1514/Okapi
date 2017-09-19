package dataServer.database.dbobjects;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import dataServer.database.enums.TableValueType;
import general.Errors;
import general.Logging;

public class ResultTableElement extends KpiDataObject {
	
	public String[] columnValues;
	
	TableValueType tableValueType; 
	private static Logger LOGGER = new Logging().create(ResultTableElement.class.getName(), false);
	public ResultTableElement(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}
	
	public ResultTableElement(TableValueType tVT, Integer colN) {
		this("");
		tableValueType = tVT;
		columnValues = new String[colN];
	}

	@Override
	public void loadContents(String[] contents) {
		columnValues = contents;
	}

	@Override
	public Object getColumnValue(String column) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object toJSonObject(){
		JSONObject jsonObject = new JSONObject();
		
		String temp = "[";
		for (int i = 0; i<columnValues.length;i++) {
			temp += "\""+columnValues[i]+"\",";
		}
		
		temp = temp.substring(0, temp.length()-1);

		temp +="]";
		try {
			jsonObject = new JSONObject(temp);
		} catch (JSONException e) {
			LOGGER.log(Level.WARNING, Errors.getError(10));
		}
		return jsonObject;
	}
	
	public Object toJSonObject(Integer column){
		Object jsonObject = new JSONObject();
		
		String temp = "";
		
		temp += "\""+columnValues[column-1]+"\"";
			
		
		try {
			jsonObject = new JSONObject(temp);
		} catch (JSONException e) {
			LOGGER.log(Level.WARNING, Errors.getError(10));
		}
		return jsonObject;
	}
	
	


}
