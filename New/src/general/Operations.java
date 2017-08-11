package general;

import java.util.HashMap;

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
		op.put("testing", 99); // Admin only
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
