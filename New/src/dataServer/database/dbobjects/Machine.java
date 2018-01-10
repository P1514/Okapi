package dataServer.database.dbobjects;

import java.util.Arrays;

public class Machine extends KpiDataObject {
	public String name;
	
	public Machine() {
		super("machine");
		super.columnsNames.addAll(Arrays.asList("name"));
	}

	@Override
	public Object getColumnValue(String column) {
		Object columnObj = null;
		switch (column) {
			case "id": 	 columnObj = super.id; 
						 break;
			case "name": columnObj = name;
						 break;
			default: 	 break;
		}
		return columnObj;
	}

	@Override
	public Object toJSonObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean loadContents(String[] contents) {
		// TODO Auto-generated method stub
		return false;
	}
}