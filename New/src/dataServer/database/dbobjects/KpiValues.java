package dataServer.database.dbobjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import java.util.logging.Level;

import file_manager.fileLoader;
import general.Errors;
import general.Logging;
import general.Settings;

public class KpiValues extends KpiDataObject {
	private static Logger LOGGER = new Logging().create(KpiValues.class.getName(),false);
	
	public Timestamp timestamp = null;
	public int kpiId = 0;
	public int machineId = 0;
	public double value = 0;
	public String designation = null;
	public boolean goodPart = true;
	public int productId = 0;
	public int mouldId = 0;
	public int shiftId=0;
	public int granularityId=0;

	public KpiValues() {
		super("kpi_values");
		super.columnsNames.addAll(Arrays.asList("timestamp", "kpi_id", "machine_id", "value", "designation",
				"good_part", "product_id", "mould_id"));
	}

	public Object getColumnValue(String column) {
		Object columnObj = null;
		switch (column) {
		case "id":
			columnObj = super.id;
			break;
		case "timestamp":
			columnObj = "'" + timestamp.toString() + "'";
			break;
		case "kpi_id":
			columnObj = kpiId;
			break;
		case "machine_id":
			columnObj = machineId;
			break;
		case "value":
			columnObj = value;
			break;
		case "designation":
			columnObj = designation;
			break;
		case "good_part":
			columnObj = goodPart;
			break;
		case "product_id":
			columnObj = productId;
			break;
		case "mould_id":
			columnObj = mouldId;
			break;
		case "shift_id":
			columnObj=shiftId;
		case "granularity_id":
			columnObj=granularityId;
		default:
			break;
		}
		return columnObj;
	}

	@Override
	public boolean loadContents(String[] contents) {
		try {
		timestamp = getTimestampValue(contents[0]);
		kpiId = Integer.parseInt(contents[1]);
		machineId = Integer.parseInt(contents[2]); 
		value = Double.parseDouble(contents[3]);
		designation = "'"+contents[4]+"'";
		goodPart = Boolean.parseBoolean(contents[5]);
		productId = Integer.parseInt(contents[6]);
		mouldId = Integer.parseInt(contents[7]);
		shiftId= Integer.parseInt(contents[8]);
		}catch (NumberFormatException | ParseException e) {
			LOGGER.log(Level.SEVERE, Errors.getError(12));
			return false;
		}
		return true;
	}
	
	public PreparedStatement getInsertQuery(PreparedStatement ps) throws SQLException {
		
		int arg=1;
		ps.setTimestamp(arg++, this.timestamp);
		ps.setInt(arg++, this.kpiId);
		ps.setInt(arg++,this.machineId);
		ps.setDouble(arg++, this.value);
		ps.setInt(arg++,this.productId);
		ps.setInt(arg++,this.mouldId);
		ps.setInt(arg++,this.shiftId);
		ps.setInt(arg++,this.granularityId);
		return ps;
		
	}

	private Timestamp getTimestampValue(String timestampValue) throws ParseException {
		Timestamp result = null;
		DateFormat sourceFormat, targetFormat;
		Date date;
		sourceFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		date = sourceFormat.parse(timestampValue);

		String s = targetFormat.format(date);

		result = Timestamp.valueOf(s);

		return result;
	}

	@Override
	public Object toJSonObject() {
		// TODO Auto-generated method stub
		return null;
	}

}