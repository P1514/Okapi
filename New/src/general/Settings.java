package general;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import javafx.event.Event;
import security.XMLEncryption;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import org.w3c.dom.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The Class Settings.
 */
public class Settings {

	private static DataSource conlocal = null;
	private static Logger LOGGER = new Logging().create(Settings.class.getName(), false);
	// TODO Change to XML read and write on first run
	// See using RSA-2048 for XML file protection
	// Tips Use "License Key" as encryption key
	// NEVER save it on SOFTWARE ask it each time program boots.
	// DB info
	private static Boolean firstrun = true;
	public static final String directory = System.getProperty("user.home") + File.separator + "Okapi" + File.separator;

	/*
	 * private static final String DBuri="127.0.0.1"; private static final
	 * String DBport="3306"; private static final String DBname="Okapi"; private
	 * static final String DBsettings="autoReconnect=true&useSSL=true"; private
	 * static final String DBurl =
	 * "jdbc:mysql://"+DBuri+":"+DBport+"/"+DBname+"?"+DBsettings; private
	 * static final String DBuser ="Okapi"; private static final String DBpass
	 * ="inknowOkapi!";
	 */

	// Logger Table
	public static final String ltable = "logs";
	public static final String ltable_id = "logs_id";
	public static final String ltable_user = "user_id";
	public static final String ltable_timestamp = "timestamp";
	public static final String ltable_log = "message";

	/**
	 * Connlocal.
	 *
	 * @return the connection
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */

	public static Connection connlocal() {
		try {
			Future<Connection> future = conlocal.getConnectionAsync();
			while (!future.isDone()) {
				try {
					Thread.sleep(100); // simulate work
				} catch (InterruptedException x) {
					Thread.currentThread().interrupt();
				}
			}

			return future.get(); // should return instantly
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Errors.getError(1) + "/n" + e.toString());
			return null;
		}
	}

	public static class params {
		String DBurl = "";
		String DBuser = "";
		String DBpass = "";
	}

	public static void startconnections() {

		PoolProperties p = new PoolProperties();
		new XMLEncryption();
		params info = readXML();
		do {
			if (info == null) {
				SetSettings.main(null);
				info = readXML();
			}
			p.setMaxIdle(40);
			p.setUrl(info.DBurl);
			p.setDriverClassName("com.mysql.jdbc.Driver");
			p.setUsername(info.DBuser);
			p.setPassword(info.DBpass);
			p.setJmxEnabled(true);
			p.setTestWhileIdle(false);
			p.setTestOnBorrow(true);
			p.setFairQueue(true);
			p.setValidationQuery("SELECT 1");
			p.setTestOnReturn(false);
			p.setValidationInterval(30000);
			p.setTimeBetweenEvictionRunsMillis(30000);
			p.setMaxActive(80);
			p.setInitialSize(10);
			p.setMaxWait(10000);
			p.setRemoveAbandonedTimeout(60);
			p.setMinEvictableIdleTimeMillis(30000);
			p.setMinIdle(30);
			p.setLogAbandoned(true);
			p.setRemoveAbandoned(true);
			p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
					+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
			conlocal = new DataSource();
			conlocal.setPoolProperties(p);
			info = null;
			try (Connection con = conlocal.getConnection()) {
				break;
			} catch (SQLException error) {
				LOGGER.log(Level.SEVERE, Errors.getError(1));
			}

		} while (true);
	}

	public static void writeXML(String DBuri, String DBport, String DBname, String DBsettings, String DBuser,
			String DBpass) {
		if (!firstrun)
			return;
		Document dom;
		Element e = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String file = directory + "Settings.xml";
		
		try {
			new File(Settings.directory).mkdirs();
			new File(file).createNewFile();
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();

			Element rootEle = dom.createElement("Okapi");

			e = dom.createElement("URI");
			e.appendChild(dom.createTextNode(DBuri));
			rootEle.appendChild(e);

			e = dom.createElement("Port");
			e.appendChild(dom.createTextNode(DBport));
			rootEle.appendChild(e);

			e = dom.createElement("Name");
			e.appendChild(dom.createTextNode(DBname));
			rootEle.appendChild(e);

			e = dom.createElement("Settings");
			e.appendChild(dom.createTextNode(DBsettings));
			rootEle.appendChild(e);

			e = dom.createElement("User");
			e.appendChild(dom.createTextNode(DBuser));
			rootEle.appendChild(e);

			e = dom.createElement("Password");
			e.appendChild(dom.createTextNode(DBpass));
			rootEle.appendChild(e);

			dom.appendChild(rootEle);

			Transformer tr = TransformerFactory.newInstance().newTransformer();
			tr.setOutputProperty(OutputKeys.INDENT, "yes");
			tr.setOutputProperty(OutputKeys.METHOD, "xml");
			tr.setOutputProperty(OutputKeys.ENCODING, "UTF-16");
			// tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "Settings.dtd");
			tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "6");

			tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(file)));
			XMLEncryption.encrypt(new File(file), new File(file));
		} catch (Exception e1) {
			LOGGER.log(Level.SEVERE, Errors.getError(1) + "\n");
			e1.printStackTrace();
		}
	}
	
	protected static params readXML() {

		String file = directory + "Settings.xml";
		String DBuri = "";
		String DBname = "";
		String DBsettings = "";
		String DBport = "";
		params info = new params();
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			if(!(new File(file).exists())) throw new Exception();
			byte[] filearray = null;
			filearray=XMLEncryption.decrypt(new File(file));
			ByteInputStream bis = new ByteInputStream(filearray, filearray.length);
			dom = db.parse(bis);
			Element doc = dom.getDocumentElement();
			DBuri = getTextValue(DBuri, doc, "URI");
			DBport = getTextValue(DBport, doc, "Port");
			DBname = getTextValue(DBname, doc, "Name");
			DBsettings = getTextValue(DBsettings, doc, "Settings");
			info.DBurl = "jdbc:mysql://" + DBuri + ":" + DBport + "/" + DBname + "?" + DBsettings;
			info.DBuser = getTextValue(info.DBuser, doc, "User");
			info.DBpass = getTextValue(info.DBpass, doc, "Password");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Errors.getError(2) + "\n" + e.toString());
			return null;
		}
		return info;

	}

	private static String getTextValue(String def, Element doc, String tag) {

		String value = def;
		NodeList nl;
		nl = doc.getElementsByTagName(tag);
		if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
			value = nl.item(0).getFirstChild().getNodeValue();
		}

		return value;
	}

	public static void LogtoDB() {
		LOGGER = new Logging().create(Settings.class.getName(), true);
	}
}
