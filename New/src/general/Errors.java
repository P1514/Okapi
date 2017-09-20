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
		Errors.put(3, "ERROR(3): Saving Settings File");
		Errors.put(4, "ERROR(4): Encrypting File");
		Errors.put(5, "ERROR(5): Decrypting File");
		Errors.put(6, "ERROR(6): While Creating RSA Keys");
		Errors.put(7, "ERROR(7): While Adding new Data into Database");
		Errors.put(8, "ERROR(8): Creating JSON for HeatMap");
		Errors.put(9, "ERROR(9): Creating JSON for ResultTable");
		Errors.put(10, "ERROR(10): Creating JSON for ResultTableElement");
		Errors.put(11, "ERROR(11): Query Unsuccessful");
		Errors.put(12,"ERROR(12): Reading CSV");
		
	}
	
	public static String getError(int error_code){
		String error=Errors.get(error_code);
		return error=="" ? "Unknown Error" : error;
	}
}
