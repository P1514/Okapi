package general;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Logging {

	private static Connection cnlocal;

	public Logger create(String classname, boolean toDB) {
		Logger LOGGER = Logger.getLogger(classname);
		if (toDB) {
			DBHandler dh;

			dh = new DBHandler(1, System.currentTimeMillis());
			LOGGER.addHandler(dh);

			SimpleFormatter formatter = new SimpleFormatter();
			dh.setFormatter(formatter);
		}
		LOGGER.info(classname + "Logger initialized\n");

		return LOGGER;
	}

	private static void dbconnect() {
		try {
			cnlocal = Settings.connlocal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
