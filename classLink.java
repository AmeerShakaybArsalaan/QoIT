/**
 * This class models an undirected Link in the Graph implementation. An Link contains two nodes and a weight. If no weight is
 * specified, the default is a weight of 1. This is so traversing links is assumed to be of greater distance or cost than staying at the given node.
 * This class also deviates from the expectations of the Comparable interface in that a return value of 0 does not indicate that this.equals(other). The
 * equals() method only compares the nodes, while the compareTo() method compares the link weights. This provides more efficient implementation for
 * checking uniqueness of links, as well as the fact that two links of equal weight should be considered equitably in a path-finding or spanning tree algorithm.
 */

public class classLink implements Comparable<classLink> {

	private classNode one, two;
	private double weight, bandwidth;
	double linkCapacity, originalLinkCapacity, link_Utilized_Bandwidth, link_Available_Bandwidth; 
	int fromNode, toNode, forwardingNode, runStep;

	public classLink(int runStep, int from_Node, int to_Node, double originalLinkCapacity) {
		this.runStep  = runStep;
		this.fromNode = from_Node;
		this.toNode   = to_Node;
		this.originalLinkCapacity = originalLinkCapacity;
	}

	public classLink(classNode one, classNode two, double weight) {
		this.one = (one.getLabel().compareTo(two.getLabel()) <= 0) ? one : two;
		this.two = (this.one == one) ? two : one;
		this.weight = weight;
	}
	
	// this one function is used
	public classLink(classNode from_Node, classNode to_Node, double weight_of_link, double linkCapacity, double link_Available_Bandwidth, double link_Utilized_Bandwidth) throws Exception {
		setLinkCapacity(linkCapacity);
		this.bandwidth = getLinkCapacity();		// For the first-time, bandwidth = total link capacity.
		
		this.one  = from_Node;
		this.two  = to_Node;	
		this.weight = weight_of_link;
		this.linkCapacity = linkCapacity;
		this.link_Available_Bandwidth = link_Available_Bandwidth;
		this.link_Utilized_Bandwidth  = link_Utilized_Bandwidth;
	}
	
	// get Methods
	public classNode getOne() { return this.one; }
	public classNode getTwo() { return this.two; }
	public int get_runStep()  { return this.runStep; }
	public int getFromNode()  { return this.fromNode; }
	public int getToNode()    { return this.toNode; }
	public double getWeight() { return this.weight; }
	public double getLinkCapacity()         { return this.linkCapacity; }
	public double getOriginalLinkCapacity() { return this.originalLinkCapacity; }  // 7.2, 14.4, 21.7 etc
	public double getUtilizedBandwidth()    { return this.link_Utilized_Bandwidth; }
	public double getAvailableBandwidth()   { return this.link_Available_Bandwidth; }
	
	// set Methods
	public void setLinkCapacity(double linkCapacity)             { this.linkCapacity = linkCapacity; }
	public void setUtilizedBandwidth(double utilizedBandwidth)   { this.link_Utilized_Bandwidth += utilizedBandwidth; }
	public void setAvailableBandwidth(double availableBandwidth) { this.link_Available_Bandwidth -= availableBandwidth; }	

   // The neighbor of current along this Link
	public classNode getNeighbor(classNode current){
		if(!(current.equals(one) || current.equals(two))) { return null; }
		return (current.equals(one)) ? two : one;
	}

	/**
	 * Note that the compareTo() method deviates from the specifications in the Comparable interface. A return value of 0 does not indicate that this.equals(other).
	 * The equals() method checks the classNode end-points, while the compareTo() is used to compare Link weights
	 * @param other The Link to compare against this
	 * @return int this.weight - other.weight
	 */

	public int compareTo(classLink other){
		if(this.weight<other.weight) 		return -1;
		else if (other.weight<this.weight) 	return 1;
		return 0;
	}

	// A String representation of this Link
	public String toString() { return "({" + one + ", " + two + "}, " + weight + ", " + bandwidth + ", " + link_Available_Bandwidth + ", " + link_Utilized_Bandwidth + ")"; }

	// The hash code for this Link
	public int hashCode() { return (one.getLabel() + two.getLabel()).hashCode(); }

	/**
	 * @param other The Object to compare against this
	 * @return true iff other is an Link with the same Vertices as this
	 */

	public boolean equals(Object other) { 
		if(!(other instanceof classLink)) return false;

		classLink l = (classLink)other;
		return l.one.equals(this.one) && l.two.equals(this.two);
	}  
}