
public class selectedSourceInformation {
	private int source_Node, destination_Node, updatedSourceInfo;
	
	public selectedSourceInformation (int destination_Node, int source_Node) { //, int updatedSourceInfo) {
		this.destination_Node = destination_Node;
		this.source_Node = source_Node;
	//	this.updatedSourceInfo = updatedSourceInfo;
	}
	
	// Getters
	public int get_destinationNode()  		{ return this.destination_Node; }	
	public int get_sourceNode()       		{ return this.source_Node; }	
	public int get_updatedSourceInfo()      { return this.updatedSourceInfo; }
}
