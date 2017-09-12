package general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that serves as a controller between the application and the database. Every DB access is done through this controller.
 * @author Fábio Mano
 *
 */
public class DBController {

	private Connection cnlocal;
	
	public DBController() {
		
	}
	
	private boolean dbconnect() {
		try {
			cnlocal = Settings.connlocal();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public void close() throws SecurityException {
		try {
			cnlocal.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the total units from the database
	 * TODO get info from db
	 * @return the total units value
	 */
	public int FM_getTotalUnits() {
		return 0;
	}
	
	/**
	 * Gets the scrap rate from the database
	 * TODO get info from db
	 * @return the scrap rate value
	 */
	public double FM_getScrapRate() {
		return 0.0;
	}
	
	/**
	 * Gets the OEE from the database
	 * TODO get info from db
	 * @return the OEE value
	 */
	public double FM_getOEE() {
		return 0.0;
	}
	
	/**
	 * Gets a list of products from the database
	 * @return a list with all products
	 */
	public List<String> FM_getProducts() {
		List<String> result = new ArrayList<String>();
		try {
			dbconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String sql = "SELECT " + Settings.ptable_name + " FROM " + Settings.ptable;
		
		PreparedStatement query = null;
		
		try {
			query = cnlocal.prepareStatement(sql);
			
			try (ResultSet rs = query.executeQuery()) {
				while (rs.next()) {
					result.add(rs.getString(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return result;
	}
	
	
	public List<String> FM_getMachines() {
		List<String> result = new ArrayList<String>();
		try {
			dbconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String sql = "SELECT " + Settings.mtable_name + " FROM " + Settings.mtable;
		
		PreparedStatement query = null;
		
		try {
			query = cnlocal.prepareStatement(sql);
			
			try (ResultSet rs = query.executeQuery()) {
				while (rs.next()) {
					result.add(rs.getString(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return result;
	}
	
	public List<String> FM_getMoulds() {
		List<String> result = new ArrayList<String>();
		try {
			dbconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String sql = "SELECT " + Settings.mldtable_name + " FROM " + Settings.mldtable;
		
		PreparedStatement query = null;
		
		try {
			query = cnlocal.prepareStatement(sql);
			
			try (ResultSet rs = query.executeQuery()) {
				while (rs.next()) {
					result.add(rs.getString(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return result;
	}

	public List<String> FM_getShifts() {
		List<String> result = new ArrayList<String>();
		try {
			dbconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String sql = "SELECT " + Settings.stable_name + " FROM " + Settings.stable;
		
		PreparedStatement query = null;
		
		try {
			query = cnlocal.prepareStatement(sql);
			
			try (ResultSet rs = query.executeQuery()) {
				while (rs.next()) {
					result.add(rs.getString(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return result;
	}
	
	/**
	 * Stores a log in the database
	 * @param log - a string containing the message to be stored
	 * @param userId - the id of the user who triggered the log
	 * @param timestamp - timestamp of the log
	 * @return true if the log is stored, false otherwise
	 */
	public boolean publishLog(String log, int userId, Timestamp timestamp) {
		if (log != null) {
			if (!dbconnect()) {
				return false;
			}
			
			String sql = "INSERT INTO " + Settings.ltable + "(" + Settings.ltable_user + "," + Settings.ltable_timestamp
					+ "," + Settings.ltable_log + ") VALUES (?,?,?)";
			
			PreparedStatement insert = null; 
			
			try {
				insert = cnlocal.prepareStatement(sql);
				insert.setInt(1, userId);
				insert.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				insert.setString(3, log);
				insert.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (insert != null) {
						insert.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (cnlocal != null) {
						close();
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
			close();
			return true;
		} else {
			close();
			return false;
		}
	}
}
