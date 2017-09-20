package file_manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import dataServer.database.dbobjects.Kpi;
import dataServer.database.dbobjects.KpiAggType;
import dataServer.database.dbobjects.KpiDataObject;
import dataServer.database.dbobjects.KpiFormula;
import dataServer.database.dbobjects.KpiTarget;
import dataServer.database.dbobjects.KpiValues;
import dataServer.database.dbobjects.Machine;
import dataServer.database.dbobjects.Mould;
import dataServer.database.dbobjects.Product;
import dataServer.database.dbobjects.Sensor;
import dataServer.database.enums.TableValueType;
import general.DBController;
import general.Logging;
import general.Server;
import general.Settings;
import general.Startup;

public class fileLoader {
	private static Logger LOGGER = new Logging().create(fileLoader.class.getName(),false);
	
	private HashMap <String,Integer> auxiliarMachineIds = new HashMap<String,Integer>();
	private HashMap <String,Integer> auxiliarProductIds = new HashMap<String,Integer>();
	private HashMap <String,Integer> auxiliarKpiIds 	= new HashMap<String,Integer>();
	
	private int bufferSize = 10000;
	private int totalLines = 0;
	private int fileLines = 0;
	
	private int clioLowerCaseCount = 0;
	private int clioUpperCaseCount = 0;
	private int opelCorsa2kOr2 = 0;
	public fileLoader()
	{

		
		}
	public void readCSVFile(String csvFile, TableValueType table){
		File csvFileRootPath = new File("");
		String csvFullPath = csvFileRootPath.getAbsolutePath() + csvFile;
		
		BufferedReader bufReader = null;
		String dataLine = "";
		String valueSeparator = ",";
		Integer readCount = 0;		
		ArrayList<KpiDataObject> buffer = new ArrayList<KpiDataObject>();
		
	}
	
	private KpiDataObject processLine(String[] lineValues, TableValueType table){
//		KpiValues kpiValue = new KpiValues();
		
		KpiDataObject kpiDO = null;
		switch (table) {
		case KPI_VALUES: kpiDO = new KpiValues();
			break;
		case PRODUCT: kpiDO = new Product();
			break;
		case MOULD: kpiDO = new Mould();
			break;
		case MACHINE:  kpiDO = new Machine();
			break;
		case KPI: kpiDO = new Kpi();
			break;
		case SENSOR: kpiDO = new Sensor();
			break;
		case SHIFT: kpiDO = new Sensor();
			break;
		case KPI_TARGET: kpiDO = new KpiTarget();
			break;
		case KPI_AGG_TYE:kpiDO = new KpiAggType();
			break;
		case KPI_FORMULA:kpiDO = new KpiFormula();
			break;
		default:break;
		
		}
		kpiDO.loadContents(lineValues);
		
		return kpiDO;
	}

	private Timestamp getTimestampValue(String timestampValue) {
		Timestamp result = null;
		DateFormat sourceFormat, targetFormat ;
		Date date;
		sourceFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			date = sourceFormat.parse(timestampValue);
			
			String s = targetFormat.format(date);

			result = Timestamp.valueOf(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private int getNameForeignKeyId(String valueName, String tableName, String foreignKey, HashMap<String, Integer> auxiliarIds){
		Integer foreignKeyId  = auxiliarIds.get(valueName);
		
		if (foreignKeyId == null) {
			foreignKeyId = new DBController().getForeignKeyId(tableName, foreignKey, valueName);
			if ( (foreignKeyId == 0) || (foreignKeyId == null) ) {
				LOGGER.log(Level.INFO,"id for <"+valueName+"> = <"+foreignKeyId+">");
			}
			auxiliarIds.put(valueName, foreignKeyId);
		}
		
		
		return foreignKeyId;
	}

	private int getNameId(String valueName, String tableName, HashMap<String, Integer> auxiliarIds){
		// TODO: Get id of <valueName> from <tableName> if exists.
		// 1st time ask DB and save answer, following times gets saved answer
		Integer id = auxiliarIds.get(valueName);
		
		if (id == null) {
			id = new DBController().getNameId(tableName, valueName);
			auxiliarIds.put(valueName, id);
		}
		
		return id;
	}
	
	private String getPartDesignation(boolean goodPart){
		// TODO: Get id of <valueName> from <tableName> if exists.
		// 1st time ask DB and save answer, following times gets saved answer
		return goodPart?"Good parts":"Scrapped parts";
	}
	
	
//	public boolean executeQuery(ArrayList<String> query){
//		boolean success = true;
//		dAO.insertBatchData(query);
//		return success;
//	}
	
	public static void main(String[] args) {		
		fileLoader fl = new fileLoader();
		String dataPath = "\\dataFiles";
		String[] csvFiles = {"\\ProaSense_dataGen_1410-events.csv",
							 "\\ProaSense_dataGen_1411-events.csv",
							 "\\ProaSense_dataGen_1412-events.csv",
							 "\\ProaSense_dataGen_1501-events.csv",
							 "\\ProaSense_dataGen_1502-events.csv",
							 "\\ProaSense_dataGen_1503-events.csv",
							 "\\ProaSense_dataGen_1504-events.csv",
							 "\\ProaSense_dataGen_1505-events.csv"};
		
		for (String csvFile : csvFiles) {
			LOGGER.log(Level.INFO,("Starting uploading file: "+csvFile));
			fl.readCSVFile(dataPath+csvFile, TableValueType.KPI_VALUES);
			LOGGER.log(Level.INFO,("Finished uploading file: "+csvFile));
			LOGGER.log(Level.INFO,("<<------------------------->>"+csvFile));
		}
		
//		String csvFileExtra = "\\ProaSense-HELLA-machines.csv";
//		LOGGER.log(LEVEL.INFO,("Starting uploading file: "+csvFileExtra, logFileName);
//		fl.readCSVFile(dataPath+csvFileExtra, TableValueType.MACHINE);
//		LOGGER.log(LEVEL.INFO,("Finished uploading file: "+csvFileExtra, logFileName);
//		LOGGER.log(LEVEL.INFO,("<<------------------------->>"+csvFileExtra, logFileName);

//		String csvFileExtra = "\\ProaSense-HELLA-products.csv";
//		LOGGER.log(LEVEL.INFO,("Starting uploading file: "+csvFileExtra, logFileName);
//		fl.readCSVFile(dataPath+csvFileExtra, TableValueType.PRODUCT);
//		LOGGER.log(LEVEL.INFO,("Finished uploading file: "+csvFileExtra, logFileName);
//		LOGGER.log(LEVEL.INFO,("<<------------------------->>"+csvFileExtra, logFileName);
		
//		String csvFileExtra = "\\ProaSense-HELLA-moulds.csv";
//		LOGGER.log(LEVEL.INFO,("Starting uploading file: "+csvFileExtra, logFileName);
//		fl.readCSVFile(dataPath+csvFileExtra, TableValueType.MOULD);
//		LOGGER.log(LEVEL.INFO,("Finished uploading file: "+csvFileExtra, logFileName);
//		LOGGER.log(LEVEL.INFO,("<<------------------------->>"+csvFileExtra, logFileName);
		
		
	}
	
	public static void LogtoDB(){
		LOGGER = new Logging().create(Server.class.getName(),true); 
	}

}
