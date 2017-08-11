package general;

import java.util.HashMap;

public class Errors {
	
	private static HashMap<Long, String> Errors;
	static {
		long i=1;
		Errors = new HashMap<>();
		
		Errors.put(i, "ERROR("+i+"): Failed to Connect to Database");
		i++;
	}
	
	public static String getError(long error_code){
		String error=Errors.get(error_code);
		return error=="" ? "Unknown Error" : error;
	}
}
