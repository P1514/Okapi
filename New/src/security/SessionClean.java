package security;

import java.util.TimerTask;

// Handles sessions timeouts
public class SessionClean extends TimerTask {

	private String id;
	public SessionClean(String _id){
		id=_id;
	}
    @Override
    public void run() {
        //TODO Data.deleteSession(id);
        this.cancel();
    }
}