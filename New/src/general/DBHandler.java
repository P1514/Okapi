package general;

import java.sql.Timestamp;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Handler to log output directly to the database.
 *
 */
public class DBHandler extends Handler {

	private int user_id;
	private Timestamp timestamp;
	private DBController dbc;
	
	public DBHandler(int user_id, long timestamp) {
		this.user_id = user_id;
		this.timestamp = new Timestamp(timestamp);
		this.dbc = new DBController();
	}

	/**
	 * Stores a record in the database
	 */
	@Override
	public void publish(LogRecord record) {
		if (!isLoggable(record)) {
			return;
		}
		String msg;
		try {
			msg = getFormatter().format(record);
		} catch (Exception ex) {
			reportError(null, ex, ErrorManager.FORMAT_FAILURE);
			return;
		}
		
		dbc.publishLog(msg, user_id, timestamp);
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void close() throws SecurityException {
		// TODO Auto-generated method stub
		
	}
}
