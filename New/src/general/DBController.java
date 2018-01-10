package general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import file_manager.fileLoader;
import dataServer.database.dbobjects.KpiDataObject;

import general.Logging;

/**
 * Class that serves as a controller between the application and the database.
 * Every DB access is done through this controller.
 * 
 * @author Fábio Mano
 *
 */
public class DBController {

	private static Logger LOGGER = new Logging().create(DBController.class.getName(), false);

	public DBController() {

	}

	/**
	 * Gets the total units from the database TODO get info from db
	 * 
	 * @return the total units value
	 */
	public int FM_getTotalUnits() {
		return 0;
	}

	/**
	 * Gets the scrap rate from the database TODO get info from db
	 * 
	 * @return the scrap rate value
	 */
	public double FM_getScrapRate() {
		return 0.0;
	}

	/**
	 * Gets the OEE from the database TODO get info from db
	 * 
	 * @return the OEE value
	 */
	public double FM_getOEE() {
		return 0.0;
	}

	/**
	 * Gets a list of products from the database
	 * 
	 * @return a list with all products
	 */
	public List<String> FM_getProducts() {
		List<String> result = new ArrayList<String>();

		String sql = "SELECT " + Settings.ptable_name + " FROM " + Settings.ptable;

		PreparedStatement query = null;

		try (Connection cnlocal = Settings.connlocal()) {
			query = cnlocal.prepareStatement(sql);

			try (ResultSet rs = query.executeQuery()) {
				while (rs.next()) {
					result.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/" + sql + "/");
			return null;
		}
		return result;
	}

	public List<String> FM_getMachines() {
		List<String> result = new ArrayList<String>();

		String sql = "SELECT " + Settings.mtable_name + " FROM " + Settings.mtable;

		PreparedStatement query = null;

		try (Connection cnlocal = Settings.connlocal()) {
			query = cnlocal.prepareStatement(sql);

			try (ResultSet rs = query.executeQuery()) {
				while (rs.next()) {
					result.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/" + sql + "/");
			return null;
		}
		return result;
	}

	public List<String> FM_getMoulds() {
		List<String> result = new ArrayList<String>();

		String sql = "SELECT " + Settings.mldtable_name + " FROM " + Settings.mldtable;

		PreparedStatement query = null;

		try (Connection cnlocal = Settings.connlocal()) {
			query = cnlocal.prepareStatement(sql);

			try (ResultSet rs = query.executeQuery()) {
				while (rs.next()) {
					result.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/" + sql + "/");
			return null;
		}

		return result;
	}

	public List<String> FM_getShifts() {
		List<String> result = new ArrayList<String>();

		String sql = "SELECT " + Settings.stable_name + " FROM " + Settings.stable;

		PreparedStatement query = null;

		try (Connection cnlocal = Settings.connlocal()) {
			query = cnlocal.prepareStatement(sql);

			try (ResultSet rs = query.executeQuery()) {
				while (rs.next()) {
					result.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/" + sql + "/");
			return null;
		}
		return result;
	}

	public JSONArray getScrapChart(String product, String machine, String shift, String mould, Date startDate,
			Date endDate, String granularity) throws JSONException {

		JSONArray result = new JSONArray();
		JSONObject obj = new JSONObject();
		
		obj.put("Op", "scrapChart");
		result.put(obj);
		String query = "SELECT KPI_TIMESTMP,SUM(KPI_VALUE)/COUNT(KPI_VALUE) FROM kpi_values WHERE ";
		
		boolean prod = false;
		boolean mach = false;
		boolean shf = false;
		boolean mld = false;
		boolean str = false;
		boolean end = false;
		
		if (!product.contains("All products")) {
			query += "PRODUCT_ID = ? AND ";
			prod = true;
		}
		
		if (!machine.contains("All machines")) {
			query += "MACHINE_ID = ? AND ";
			mach = true;
		}
		
		if (!shift.contains("All shifts")) {
			query += "SHIFT_ID = ? AND ";
			shf = true;
		}
		
		if (!mould.contains("All moulds")) {
			query += "MOULD_ID = ? AND ";
			mld = true;
		}
		
		if (startDate != null) {
			query += "KPI_TIMESTMP >= ? AND ";
			str = true;
		}
		
		if (endDate != null) {
			query += "KPI_TIMESTMP <= ? AND ";
			end = true;
		}
		
		query += "GRANULARITY_ID = ? AND KPI_ID = " + Settings.SCRAP_RATE + " GROUP BY KPI_TIMESTMP;";
		
		PreparedStatement ps = null;
		try (Connection cnlocal = Settings.connlocal()) {
			ps = cnlocal.prepareStatement(query);
			int param = 1;
			if (prod == true) {
				ps.setInt(param++, getNameId("product", product));
			}
			
			if (mach == true) {
				ps.setInt(param++, getNameId("machine", machine));
			}
			
			if (shf == true) {
				ps.setInt(param++, getNameId("shift", shift));
			}
			
			if (mld == true) {
				ps.setInt(param++, getNameId("mould", mould));
			}
			
			if (str == true) {
				ps.setTimestamp(param++, new Timestamp(startDate.getTime()));
			}
			
			if (end == true) {
				ps.setTimestamp(param++, new Timestamp(endDate.getTime()));
			}
			
			ps.setInt(param++, getNameId("granularity", granularity));
			System.out.println(ps.toString());
			ResultSet queryResult = ps.executeQuery();
			for (; queryResult.next();) {
				JSONObject val = new JSONObject();
				val.put("Date", queryResult.getDate(1));
				val.put("Time", queryResult.getTime(1));
				val.put("Value", queryResult.getDouble(2));
				result.put(val);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/" + query + "/");
			return null;
		}
		return result;
	}

	/**
	 * Stores a log in the database
	 * 
	 * @param log
	 *            - a string containing the message to be stored
	 * @param userId
	 *            - the id of the user who triggered the log
	 * @param timestamp
	 *            - timestamp of the log
	 * @return true if the log is stored, false otherwise
	 */
	public boolean publishLog(String log, int userId, Timestamp timestamp) {
		if (log != null) {
			String sql = "INSERT INTO " + Settings.ltable + "(" + Settings.ltable_user + "," + Settings.ltable_timestamp
					+ "," + Settings.ltable_log + ") VALUES (?,?,?)";

			PreparedStatement insert = null;

			try (Connection cnlocal = Settings.connlocal()) {
				insert = cnlocal.prepareStatement(sql);
				insert.setInt(1, userId);
				insert.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				insert.setString(3, log);
				insert.executeUpdate();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, Errors.getError(11) + "/" + sql + "/");
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public Integer getNameId(String tableName, String valueName) {
		int id = 0;

		// TODO change tableName for specific table, prevent injection
		String sql = "SELECT ID FROM " + tableName.toUpperCase() + " WHERE NAME LIKE \"%" + valueName + "%\";";
		PreparedStatement query = null;
		try (Connection cnlocal = Settings.connlocal()) {
			query = cnlocal.prepareStatement(sql);
			ResultSet queryResult = query.executeQuery();
			for (; queryResult.next();) {
				id = (int) queryResult.getObject(1);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/" + sql + "/");
			return null;
		}
		return id;
	}

	public Integer getForeignKeyId(String tableName, String foreignKeyName, String valueName) {
		Integer id = 0;

		// TODO replace foreignKeyName with list of tables to prevent injection
		String query = "SELECT \"" + foreignKeyName.toUpperCase() + "\" FROM \"" + tableName.toUpperCase()
				+ "\" WHERE NAME='" + valueName + "';";

		try (Connection cnlocal = Settings.connlocal()) {
			ResultSet queryResult = cnlocal.createStatement().executeQuery(query);
			for (; queryResult.next();) {
				id = (Integer) queryResult.getObject(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "getForeignKeyId exception: " + e);
			e.printStackTrace();
		}

		return id;
	}
}
