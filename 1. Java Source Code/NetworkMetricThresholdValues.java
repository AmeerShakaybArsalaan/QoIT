
public class NetworkMetricThresholdValues {

	String time;
	double bandwidth, informationUtility, linkIntegrity;
	int destinationNode, hopCount;
	
	public NetworkMetricThresholdValues (String currentTime, int destinationNode, double bandwidth, int hopCount, double linkIntegrity, double informationUtility){
			this.time = currentTime;								
			this.destinationNode = destinationNode;								
			this.bandwidth = bandwidth;								
			this.hopCount = hopCount;							
			this.linkIntegrity = linkIntegrity;					
			this.informationUtility = informationUtility;		
		}
	
	public NetworkMetricThresholdValues (int destinationNode, double bandwidth, int hopCount, double linkIntegrity, double informationUtility){							
		this.destinationNode = destinationNode;								
		this.bandwidth = bandwidth;								
		this.hopCount = hopCount;							
		this.linkIntegrity = linkIntegrity;					
		this.informationUtility = informationUtility;		
	}
	
	// Getters
	public String get_currentTime(){
		return this.time;
	}
	
	public int get_destinationNode(){
		return this.destinationNode;
	}
	
	public int get_hopCount(){
		return this.hopCount;
	}
	
	public double get_linkIntegrity(){
		return this.linkIntegrity;
	}
	
	public double get_bandwidth(){
		return this.bandwidth;
	}

	public double get_informationUtility(){
		return this.informationUtility;
	}
	
	public int set_hopCount(){
		hopCount = this.hopCount + 1;
		return hopCount;
	}
	
	public double set_linkIntegrity(){
		linkIntegrity = this.linkIntegrity + 0.5;
		return linkIntegrity;
	}
	
	public double set_bandwidth(){
		bandwidth = this.bandwidth + 0.001;
		return bandwidth;
	}

	public double set_informationUtility(){
		informationUtility = this.informationUtility + 1.0;
		return informationUtility;
	}

}
