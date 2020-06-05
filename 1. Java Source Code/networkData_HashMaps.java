
public class networkData_HashMaps {
	int runStep, destinationNode, selectedSourceNode, hopCount, noof_QualityMetrics_Met; 
	double assignedBandwidth, linkIntegrity, informationUtility, QoIT_score, Accuracy, Completeness, Timeliness, Reliability, priorityScore;

	public networkData_HashMaps(int runStep, int destinationNode, int selectedSourceNode, double assignedBandwidth, int hopCount, double linkIntegrity, double informationUtility) {
		this.runStep = runStep;
		this.destinationNode = destinationNode;
		this.selectedSourceNode = selectedSourceNode;
		this.assignedBandwidth = assignedBandwidth;
		this.hopCount = hopCount;
		this.linkIntegrity = linkIntegrity;
		this.informationUtility = informationUtility;
	}
	
	public networkData_HashMaps(int runStep, int destinationNode, int selectedSourceNode, double priorityScore, int noof_QualityMetrics_Met, double QoIT_score) {
		this.runStep = runStep;
		this.destinationNode = destinationNode;
		this.selectedSourceNode = selectedSourceNode;
		this.priorityScore = priorityScore;
		this.noof_QualityMetrics_Met = noof_QualityMetrics_Met;
		this.QoIT_score = QoIT_score;
	}
	
	public networkData_HashMaps(int runStep, int destinationNode, int selectedSourceNode, double Accuracy, double Completeness, double Timeliness, double Reliability) {
		this.runStep = runStep;
		this.destinationNode = destinationNode;
		this.selectedSourceNode = selectedSourceNode;
		this.Accuracy = Accuracy;
		this.Completeness = Completeness;
		this.Timeliness = Timeliness;
		this.Reliability = Reliability;
	}
	
	// Getters
		public int get_runStep()								{ return this.runStep; }		
		public int get_destinationNode()						{ return this.destinationNode; }		
		public int get_selectedSourceNode()						{ return this.selectedSourceNode; }	
		public int get_hopCount()								{ return this.hopCount; }	
		public int get_noof_QualityMetrics_Met()				{ return this.noof_QualityMetrics_Met; }
		public double get_priorityScore()						{ return this.priorityScore; }		
		public double get_assignedBandwidth()					{ return this.assignedBandwidth; }		
		public double get_linkIntegrity()						{ return this.linkIntegrity; }
		public double get_informationUtility()					{ return this.informationUtility; }
		public double get_QoIT_score()							{ return this.QoIT_score; }
		public double get_Accuracy()							{ return this.Accuracy; }		
		public double get_Completeness()						{ return this.Completeness; }
		public double get_Timeliness()							{ return this.Timeliness; }
		public double get_Reliability()							{ return this.Reliability; }
}
