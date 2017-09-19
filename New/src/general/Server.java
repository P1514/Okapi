
package general;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
// TODO: Auto-generated Javadoc
/**
 * The Class Server.
 */
@ServerEndpoint("/server")
public class Server {
	private Session session;
	private static Logger LOGGER = new Logging().create(Server.class.getName(),false);

	/** The Assyncronous variable. */
	Async as;

	/**
	 * Open.
	 *
	 * @param session
	 *            the session
	 */
	@OnOpen
	public void open(Session session) {
		as = session.getAsyncRemote();
		this.session = session;
	}

	/**
	 * Received message.
	 *
	 * @param session
	 *            the session
	 * @param msg
	 *            the msg
	 * @param last
	 *            the last
	 * @throws JSONException 
	 * @throws ParseException 
	 */
	@OnMessage
	public void receivedMessage(Session session, String msg, boolean last) throws JSONException, ParseException {
		LOGGER.log(Level.INFO, "\r\nIN: " + msg);
		
		JSONObject resolve = new JSONObject(msg);
		handleMessage(resolve);
		
	}

	/**
	 * On close.
	 *
	 * @param session
	 *            the session
	 */
	@OnClose
	public void onClose(Session session) {
		try {
			session.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Send message.
	 *
	 * @param msg
	 *            the msg
	 */
	public void sendMessage(String msg) {
		LOGGER.log(Level.INFO, "\r\nOUT: " + msg);
		as.sendText(msg);
		return;
	}

	/**
	 * On error.
	 *
	 * @param session
	 *            the session
	 * @param thr
	 *            the thr
	 */
	@OnError
	public void onError(Session session, Throwable thr) {
		try {
			session.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void LogtoDB(){
		LOGGER = new Logging().create(Server.class.getName(),true); 
	}
	
	private void handleMessage(JSONObject msg) throws JSONException, ParseException {
		JSONArray result = new JSONArray();
		JSONObject obj = new JSONObject();
		Backend backend;
		Operations operations;
		
		if (session.isOpen()) {
			operations = new Operations();
			int op = operations.getOP(msg.getString("Op"));
			backend = new Backend(op, msg);
			
			sendMessage(backend.resolve());
		}
	}
}
