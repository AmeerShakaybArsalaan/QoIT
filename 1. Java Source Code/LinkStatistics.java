
	// Stores/Accesses: From-Node, To-Node, Random-Fading-Values e.g. (0,1,-0.192384625723)
	public class LinkStatistics {
		
		String time, fromNode, toNode;
		double linkCapacity;
		double link_Utilized_Bandwidth;
		double link_Available_Bandwidth; 
		int linkUsage_Counter, nodeForwarding_Counter;
		
		public LinkStatistics (String currentTime, String from_Node, String to_Node, int link_Occurance, double linkCapacity, int forwardingNode_Occurance){
				this.time = currentTime;									// current time w.r.t iteration
				this.fromNode = from_Node;									// o>------
				this.toNode = to_Node;										// ------>o
				this.linkCapacity = linkCapacity;							// Original Link Capacity / Neighbor Count
				this.linkUsage_Counter = link_Occurance;					// fromNode-toNode occurance
				this.nodeForwarding_Counter = forwardingNode_Occurance;		// fromNode occurance
			}
		
		// Getters
		public String get_currentTime(){
			return this.time;
		}
		
		public String get_FromNode(){
			return this.fromNode;
		}
		
		public String get_ToNode(){
			return this.toNode;
		}
		
		public double get_linkCapacity(){
			return this.linkCapacity;
		}
		
		public int get_linkUsageCount(){
			return this.linkUsage_Counter;
		}
		
		public int get_nodeForwardingCount(){
			return this.nodeForwarding_Counter;
		}
		
		// Setters
		public void set_linkUsageCount(int linkUsageCounter){
			this.linkUsage_Counter = this.linkUsage_Counter + linkUsageCounter;
		}
		
		public void set_nodeForwardingCount(int nodeForwardingCounter){
			this.nodeForwarding_Counter = this.nodeForwarding_Counter + nodeForwardingCounter;
		}
	}
