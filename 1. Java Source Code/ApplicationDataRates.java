	
// Stores/Accesses: Current-Time, Source-Node, Application-Data-Rate e.g. (1:33:45, 1, 0.8)
public class ApplicationDataRates {

		private int source_Node;
		private double applicationDataRate;
		
		public ApplicationDataRates (int source_Node, double applicationDataRate) {
			this.source_Node = source_Node;
			this.applicationDataRate = applicationDataRate;
		}
		
		public double get_appDataRate(){
			return this.applicationDataRate;
		}
}
