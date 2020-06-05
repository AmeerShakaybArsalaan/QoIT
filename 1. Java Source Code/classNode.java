import java.util.ArrayList;
import java.util.Set;

/**
 * This class models a node in a graph. For ease of the reader, a label for this node is required.
 * Note that the Graph object only accepts one classNode per label, so uniqueness of labels is important. This node's neighborhood
 * is described by the Links incident to it.
 */

public class classNode {

	private ArrayList<classLink> neighborhood;
	private String label;
	double x, y;

	/**
	 * @param label The unique label associated with this classNode
	 */
	
	classNode(){}
	
/*	classNode(String label) {
		this.label = label;
		this.neighborhood = new ArrayList<classLink>();
	 }*/
	
	public classNode(String label, double x, double y){
		this.label = label;
		this.x = x;
		this.y = y;
		this.neighborhood = new ArrayList<classLink>();
	}
	
	public double distance(classNode nodeTo) {
	double dx = x-nodeTo.x;
	double dy = y-nodeTo.y;
	return Math.sqrt((dx*dx)+(dy*dy));
}
	
	/**
	 * This method adds a Link to the incidence neighborhood of this graph iff the link is not already present.
	 * @param link The link to add
	 */

	public void addNeighbor(classLink link){
		if(this.neighborhood.contains(link)){
			return;
		}	
		this.neighborhood.add(link);
	}

	/**
	 * @param other The link for which to search
	 * @return true iff other is contained in this.neighborhood
	 */

	public boolean containsNeighbor(classLink other){
		return this.neighborhood.contains(other);
	}

	/**
	 * @param index The index of the Link to retrieve
	 * @return Link The Link at the specified index in this.neighborhood
	 */

	public classLink getNeighbor(int index){
		return this.neighborhood.get(index);
	}

	/**
	 * @param index The index of the link to remove from this.neighborhood
	 * @return Link The removed Link
	 */

	classLink removeNeighbor(int index){
		return this.neighborhood.remove(index);
	}

	/**
	 * @param e The Link to remove from this.neighborhood
	 */

	public void removeNeighbor(classLink l){
		this.neighborhood.remove(l);
	}

	/**
	 * @return int The number of neighbors of this classNode
	 */

	public int getNeighborCount(){
		return this.neighborhood.size();
	}

	/**
	 * @return String The label of this classNode
	 */

	public String getLabel(){	
		return this.label;
	}
	
	public int getSourceLabel(){
		int source = Integer.parseInt(this.label);
		return source;
	}

	/**
	 * @return String A String representation of this classNode
	 */

	public String toString(){
		return "Node " + label;
	}

	/**
	 * @return The hash code of this classNode's label
	 */

	public int hashCode(){
		return this.label.hashCode();
	}

	/**
	 * @param other The object to compare
	 * @return true iff other instance of classNode and the two classNode objects have the same label
	 */

	public boolean equals(Object other){
		if(!(other instanceof classNode)){
			return false;
		}

		classNode nd = (classNode)other;
		return this.label.equals(nd.label);
	}

	/**
	 * @return ArrayList<Link> A copy of this.neighborhood. Modifying the returned
	 * ArrayList will not affect the neighborhood of this classNode
	 */

	public ArrayList<classLink> getNeighbors(){
		return new ArrayList<classLink>(this.neighborhood);
	}
}