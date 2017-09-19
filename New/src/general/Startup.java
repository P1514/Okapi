package general;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import file_manager.fileLoader;



/**
 * The Class Startup runs every time the server boots up.
 */
@WebListener
public class Startup implements ServletContextListener {
	
	private static Logger LOGGER = new Logging().create(Startup.class.getName(),false);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// Start Up Sequence
		Settings.startconnections();
		changeLogstoDB();
		LOGGER.log(Level.INFO,"Starting Up!");
		
		

	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		
		LOGGER.log(Level.INFO,"Shutting down!");
	}

	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	
	public void changeLogstoDB(){
		Settings.LogtoDB();
		LogtoDB();
		Server.LogtoDB();
		fileLoader.LogtoDB();
	}
	
	public static void LogtoDB(){
		LOGGER = new Logging().create(Startup.class.getName(),true); 
	}
}