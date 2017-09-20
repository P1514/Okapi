package general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private static Logger LOGGER = new Logging().create(DBController.class.getName(),false);
	
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
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/"+sql+"/");
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
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/"+sql+"/");
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
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/"+sql+"/");
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
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/"+sql+"/");
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
				LOGGER.log(Level.SEVERE, Errors.getError(11) + "/"+sql+"/");
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
		String sql = "SELECT \"ID\" FROM \"" + tableName.toUpperCase() + "\" WHERE NAME='?'";
		PreparedStatement query = null;
		try (Connection cnlocal = Settings.connlocal()) {
			query = cnlocal.prepareStatement(sql);
			ResultSet queryResult = query.executeQuery();
			for (; queryResult.next();) {
				id = (int) queryResult.getObject(1);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, Errors.getError(11) + "/"+sql+"/");
			return null;
		}
		return id;
	}

	public Integer getForeignKeyId(String tableName, String foreignKeyName, String valueName) {
		Integer id=0;
		
		//TODO replace foreignKeyName with list of tables to prevent injection
		String query = "SELECT \""+foreignKeyName.toUpperCase()+"\" FROM \""+tableName.toUpperCase()+"\" WHERE NAME='"+valueName+"';";

		
		
	    try (Connection cnlocal = Settings.connlocal()){
	    	ResultSet queryResult = cnlocal.createStatement().executeQuery(query);
			for (; queryResult.next(); ) {
				id = (Integer)queryResult.getObject(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,"getForeignKeyId exception: "+e );
			e.printStackTrace();
		}		
		
		return id;
	}
}
