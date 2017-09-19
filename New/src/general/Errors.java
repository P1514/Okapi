package general;

import java.util.HashMap;

/**
 * Class that stores error messages.
 * @author Uninova
 *
 */
public class Errors {
	
	private static HashMap<Integer, String> Errors;
	static {
		Errors = new HashMap<>();
		
		Errors.put(1, "ERROR(1): Failed to Connect to Database");
		Errors.put(2, "ERROR(2): Missing Settings File");
		Errors.put(3, "ERROR(3): Error Saving Settings File");
		Errors.put(4, "ERROR(4): Error Encrypting File");
		Errors.put(5, "ERROR(5): Error Decrypting File");
		Errors.put(6, "ERROR(6): Error While Creating RSA Keys");
		Errors.put(7, "ERROR(7): Error While Adding new Data into Database");
		Errors.put(8, "ERROR(8): Error Creating JSON for HeatMap");
		Errors.put(9, "ERROR(9): Error Creating JSON for ResultTable");
		Errors.put(10, "ERROR(10): Error Creating JSON for ResultTableElement");
		
	}
	
	public static String getError(int error_code){
		String error=Errors.get(error_code);
		return error=="" ? "Unknown Error" : error;
	}
}
