package general;

import java.util.HashMap;

/**
 * Class that stores operation keys and permissions.
 * @author Uninova
 *
 */
public class Operations {
	private static HashMap<Integer, Integer> permission;
	static{
		permission = new HashMap<>();
		//99- ADMIN
		permission.put(99, 99); // Developer Operation
	}
	private static HashMap<String, Integer> op;
	static {
		op = new HashMap<>();
		op.put("testing", 99); 			// Admin only
		op.put("getTotal", 1); 			//TODO
		op.put("getScrapRate", 2);		//TODO
		op.put("getOEE", 3);			//TODO
		op.put("getProducts", 4);		
		op.put("getMachines", 5);		
		op.put("getShifts", 6);			
		op.put("getMoulds", 7);			
		op.put("getScrapChart", 8);		//TODO
		op.put("getHeatmap", 9);		//TODO
		op.put("getPrediction", 10);	//TODO
	}

	public int getOP(String msg) {
		if (op.containsKey(msg))
			return op.get(msg);
		return 0;
	}
	
	public static int return_main_permission(int id){
		
		return permission.containsKey(id) ? permission.get(id) : 99;
	}
}
