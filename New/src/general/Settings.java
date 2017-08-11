package general;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;

/**
 * The Class Settings.
 */
public class Settings {
	
	private static DataSource conlocal = null;
	private static final Logger LOGGER = new Logging().create(Settings.class.getName());
	
	//DB info
	private static final String DBuri="127.0.0.1";
	private static final String DBport="3306";
	private static final String DBname="Okapi";
	private static final String DBsettings="autoReconnect=true&useSSL=true";
	private static final String DBurl = "jdbc:mysql://"+DBuri+":"+DBport+"/"+DBname+"?"+DBsettings;
	private static final String DBuser ="Okapi";
	private static final String DBpass ="inknowOkapi!";
	
	// Logger Table
	public static final String ltable="logs";
	public static final String ltable_user="user_id";
	public static final String ltable_timestamp="logs_timestamp";
	public static final String ltable_log="logs_log";
	

	/**
	 * Connlocal.
	 *
	 * @return the connection
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static Connection connlocal() {
		try {

			if(conlocal==null)
				startconnections();

		    Future<Connection> future = conlocal.getConnectionAsync();
		    while (!future.isDone()) {
		      try {
		        Thread.sleep(100); //simulate work
		      }catch (InterruptedException x) {
		        Thread.currentThread().interrupt();
		      }
		    }
		 
		    return future.get(); //should return instantly
		  }catch(Exception e){
			  LOGGER.log(Level.SEVERE, Errors.getError(1));
			  return null;
		  }
	}
	
	public static void startconnections(){
		
        PoolProperties p = new PoolProperties();
        
        p.setMaxIdle(40);
        p.setUrl(DBurl);
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername(DBuser);
        p.setPassword(DBpass);
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
        p.setJdbcInterceptors(
          "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        conlocal = new DataSource();
        conlocal.setPoolProperties(p);
	}
}
