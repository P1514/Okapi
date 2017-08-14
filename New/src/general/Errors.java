package general;

import java.util.HashMap;

public class Errors {
	
	private static HashMap<Integer, String> Errors;
	static {
		Errors = new HashMap<>();
		
		Errors.put(1, "ERROR(1): Failed to Connect to Database");
		Errors.put(2, "ERROR(2): Missing Settings File");
		Errors.put(3, "ERROR(3): Error Saving Settings File");
		Errors.put(4, "ERROR(4): Error Encrypting File");
		Errors.put(5, "ERROR(5): Error Decrypting File");
		
	}
	
	public static String getError(int error_code){
		String error=Errors.get(error_code);
		return error=="" ? "Unknown Error" : error;
	}
}
