import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class models a simple, undirected graph using an
 * incidence list representation. Vertices are identified
 * uniquely by their labels, and only unique nodes are allowed.
 * At most one Link per node pair is allowed in this Graph.
 * Note that the Graph is designed to manage the Links. You should not attempt to manually add Links yourself.
 */

public class Graph {
	private HashMap<String, classNode> nodes;
	private HashMap<Integer, classLink> links;


	public Graph(){
		this.nodes = new HashMap<String, classNode>();
		this.links = new HashMap<Integer, classLink>();
	}

	/**
	 * This constructor accepts an ArrayList<Node> and populates
	 * this.nodes. If multiple Node objects have the same label,
	 * then the last Node with the given label is used.
	 * @param nodes The initial Vertices to populate this Graph
	 */

	public Graph(ArrayList<classNode> nodes){
		this.nodes = new HashMap<String, classNode>();
		this.links = new HashMap<Integer, classLink>();

		for(classNode nd: nodes){
			this.nodes.put(nd.getLabel(), nd);
		}
	}

	//********************************************************************************************************************************
	// Accepts two nodes and a weight, and adds the link ({one, two}, weight) iff no Link relating one and two exists in the Graph.
	public boolean addLink(classNode from_Node, classNode to_Node, double weight, double linkCapacity, double link_Available_Bandwidth, double link_Utilized_Bandwidth) throws Exception{
		if(from_Node.equals(to_Node)) return false; 
		else { 
			//ensures the Link is not in the Graph
			classLink l = new classLink(from_Node, to_Node, weight, linkCapacity, link_Available_Bandwidth, link_Utilized_Bandwidth);		

			if(from_Node.containsNeighbor(l)) return false;
			else {
				links.put(l.hashCode(), l);
				from_Node.addNeighbor(l);
				return true; }
		}
	}
	//********************************************************************************************************************************

	/**
	 * @param e The Link to look up
	 * @return true iff this Graph contains the Link e
	 */

	public boolean containsLink(classLink l){
		if(l.getOne() == null || l.getTwo() == null){
			return false;
		}

		return this.links.containsKey(l.hashCode());
	}


	/**
	 * This method removes the specified Link from the Graph,
	 * including as each node's incidence neighborhood.
	 * @param e The Link to remove from the Graph
	 * @return Link The Link removed from the Graph
	 */

	public classLink removeLink(classLink l){
		l.getOne().removeNeighbor(l);
		l.getTwo().removeNeighbor(l);

		return this.links.remove(l.hashCode());
	}	

	/**
	 * @param node The Node to look up
	 * @return true iff this Graph contains node
	 */

	public boolean containsNode(classNode node){
		return this.nodes.get(node.getLabel()) != null;
	}

	/**
	 * @param label The specified Node label
	 * @return Node The Node with the specified label
	 */

	public classNode getNode(String label){
		return nodes.get(label);
	}

	/**
	 * This method adds a Node to the graph. If a Node with the same label
	 * as the parameter exists in the Graph, the existing Node is overwritten
	 * only if overwriteExisting is true. If the existing Node is overwritten,
	 * the Links incident to it are all removed from the Graph.
	 * @param node
	 * @param overwriteExisting
	 * @return true iff node was added to the Graph
	 */

	public boolean addNode(classNode node, boolean overwriteExisting){
		classNode current = this.nodes.get(node.getLabel());

		if(current != null){
			if(!overwriteExisting){
				return false;
			}

			while(current.getNeighborCount() > 0){
				this.removeLink(current.getNeighbor(0));
			}
		}

		nodes.put(node.getLabel(), node);
		return true;
	}

	/**
	 * @param label The label of the Node to remove
	 * @return Node The removed Node object
	 */

	public classNode removeNode(String label){
		classNode nd = nodes.remove(label);

		while(nd.getNeighborCount() > 0){
			this.removeLink(nd.getNeighbor((0)));
		}

		return nd;
	}

	/**
	 * @return Set<String> The unique labels of the Graph's Node objects
	 */

	public Set<String> nodeKeys(){
		return this.nodes.keySet();
	}

	/**
	 * @return Set<Link> The Links of this graph
	 */

	public Set<classLink> getLinks(){
		return new HashSet<classLink>(this.links.values());
	}

}
