
public class NetworkNode {
	
	String name;
	Location location;
	double dirn;
	int itype;
	
	
	NetworkNode(String n, Location loc, double d, int t) {
		location = loc;
		name = n;
		dirn = d;
		itype = t;
	}
	
	boolean isUAV() {
		return true;
	}
	
	Location getLocation() {
		return location;
	}

}
