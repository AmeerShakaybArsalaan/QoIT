
public class QualityMetricScores {
	String time;
	int sourceNode, destinationNode; 
	double A, C, T, R;
	
	public QualityMetricScores (String currentTime, int sourceNode, int destinationNode, 
			double A, double C, double T, double R){
			this.time = currentTime;									// current time w.r.t iteration
			this.sourceNode = sourceNode;									// o>------
			this.destinationNode = destinationNode;										// ------>o
			this.A = A;							// Original Link Capacity / Neighbor Count
			this.C = C;					// fromNode-toNode occurance
			this.T = T;		// fromNode occurance
			this.R = R;
		}
	
	public QualityMetricScores (int sourceNode, double A, double C, double T, double R){
			this.sourceNode = sourceNode;									// o>------										// ------>o
			this.A = A;							// Original Link Capacity / Neighbor Count
			this.C = C;					// fromNode-toNode occurance
			this.T = T;		// fromNode occurance
			this.R = R;
		}
	
	// Getters
	public String get_currentTime(){
		return this.time;
	}
	
	public int get_sourceNode(){
		return this.sourceNode;
	}
	
	public int get_destinationNode(){
		return this.destinationNode;
	}
	
	public double get_Accuracy(){
		return this.A;
	}
	public double get_Completeness(){
		return this.C;
	}
	public double get_Timeliness(){
		return this.T;
	}
	public double get_Reliability(){
		return this.R;
	}
	
}
