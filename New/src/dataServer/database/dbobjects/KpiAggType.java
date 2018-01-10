package dataServer.database.dbobjects;

import java.util.Arrays;

import dataServer.database.enums.Aggregation;

public class KpiAggType extends KpiDataObject {
	public Aggregation aggregation;
	
	public KpiAggType() {
		super("kpi_agg_type");
		super.columnsNames.addAll(Arrays.asList("aggregation"));
	}


	@Override
	public Object getColumnValue(String column) {
		Object columnObj = null;
		switch (column) {
			case "id": 			columnObj = super.id; 
								break;
			case "aggregation": columnObj = aggregation;
							  	break;
			default: 			break;
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