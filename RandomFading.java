
// Stores/Accesses: From-Node, To-Node, Random-Fading-Values e.g. (0,1,-0.192384625723)
public class RandomFading {

	private String fromNode;
	private String toNode;
	private double randomFadingValue;

	public RandomFading() {}
	
	public RandomFading (String from_Node, String to_Node, double RandomFading_Value) {
		this.fromNode = from_Node;
		this.toNode = to_Node;
		this.randomFadingValue = RandomFading_Value;
	}

	public String get_FromNode(){
		return this.fromNode;
	}
	
	public String get_ToNode(){
		return this.toNode;
	}
	
	public double get_RandomFadingValue(){
		return this.randomFadingValue;
	}
	
}
