import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


public class Dijkstra {

	private Graph graph;
	private String initialNodeLabel;
	private static HashMap<String, String> predecessors;
	private HashMap<String, Double> distances;
	private PriorityQueue<classNode> availableNodes;
	private HashSet<classNode> visitedNodes;
	private HashMap<String, Double> bandwidth;
	LinkedList<HashMap<String, String>> nodeToNodeConnectivity = new LinkedList<HashMap<String, String>>();

	//private static List<classNode> networkNodes;

	public Dijkstra(){
	}

	//public Dijkstra(Graph graph, String initialNodeLabel) throws IOException{ 
	public Dijkstra(Graph graph, String initialNodeLabel, HashMap<String, classLink> link_data) throws IOException{
		this.graph = graph;
		Set<String> nodeKeys = this.graph.nodeKeys();

		if(!nodeKeys.contains(initialNodeLabel)){
			throw new IllegalArgumentException("The graph must contain the initial node.");
		}

		this.initialNodeLabel = initialNodeLabel;
		this.predecessors = new HashMap<String, String>();
		this.bandwidth = new HashMap<String, Double>();
		this.distances = new HashMap<String, Double>();
		this.availableNodes = new PriorityQueue<classNode>(nodeKeys.size(), new Comparator<classNode>(){

			public int compare(classNode one, classNode two){
				double weightOne = Dijkstra.this.distances.get(one.getLabel());
				double weightTwo = Dijkstra.this.distances.get(two.getLabel());

				if(weightOne<weightTwo)
					return -1;
				else if (weightTwo<weightOne)
					return 1;
				return 0;
			}
		});

		this.visitedNodes = new HashSet<classNode>();

		//for each Node in the graph assume it has distance infinity denoted by Double.MAX_VALUE

		for(String key: nodeKeys){
			this.predecessors.put(key, null); 
			this.distances.put(key, Double.MAX_VALUE);
			this.bandwidth.put(key, Double.MAX_VALUE);
		}

		//the distance from the initial node to itself is 0
		this.distances.put(initialNodeLabel, 0.0);
		this.bandwidth.put(initialNodeLabel, 0.0);

		//and seed initialNode's neighbors
		classNode initialNode = this.graph.getNode(initialNodeLabel);
		ArrayList<classLink> initialNodeNeighbors = initialNode.getNeighbors();

		//-	if(classParameters.print_condition) System.out.println(initialNodeNeighbors);

		NetworkMetrics nm = new NetworkMetrics();
		int neighbors = initialNodeNeighbors.size();
		for(int i = 0; i<neighbors; i++) {
			classLink l = initialNodeNeighbors.get(i);
			classNode other = l.getNeighbor(initialNode);

			String Key = initialNode.getLabel().concat("-").concat(other.getLabel());
			classLink cl = link_data.get(Key); 

			if(cl.getLinkCapacity()!=0.0)
				;
			else{
				initialNodeNeighbors.remove(i);
				neighbors--;
				i--;
			}
		}
		//	if(classParameters.print_condition) System.out.println(initialNodeNeighbors);

		for(classLink l : initialNodeNeighbors){
			classNode other = l.getNeighbor(initialNode);
			this.predecessors.put(other.getLabel(), initialNodeLabel); 
			this.distances.put(other.getLabel(), l.getWeight());
			this.bandwidth.put(other.getLabel(), l.getAvailableBandwidth());
			this.availableNodes.add(other);
		}

		this.visitedNodes.add(initialNode);
		//	if(classParameters.print_condition) System.out.println("dk: " + this.predecessors);

		//now apply Dijkstra's algorithm to the Graph
		processGraph(link_data);
	}

	public LinkedList<HashMap<String,String>> predecessorsList(Graph graph, String initialNodeLabel, HashMap<String, classLink> link_data) throws IOException{

		this.graph = graph;
		Set<String> nodeKeys = this.graph.nodeKeys();

		if(!nodeKeys.contains(initialNodeLabel)){
			throw new IllegalArgumentException("The graph must contain the initial node.");
		}

		this.initialNodeLabel = initialNodeLabel;
		this.predecessors = new HashMap<String, String>(); 
		this.bandwidth = new HashMap<String, Double>();
		this.distances = new HashMap<String, Double>(); 
		this.availableNodes = new PriorityQueue<classNode>(nodeKeys.size(), new Comparator<classNode>(){

			public int compare(classNode one, classNode two){
				double weightOne = Dijkstra.this.distances.get(one.getLabel());
				double weightTwo = Dijkstra.this.distances.get(two.getLabel());
				if(weightOne<weightTwo)
					return -1;
				else if (weightTwo<weightOne)
					return 1;
				return 0;
			}
		});

		this.visitedNodes = new HashSet<classNode>();

		//for each Node in the graph assume it has distance infinity denoted by Double.MAX_VALUE

		for(String key: nodeKeys){
			this.predecessors.put(key, null); 
			this.distances.put(key, Double.MAX_VALUE);
			this.bandwidth.put(key, Double.MAX_VALUE);
		}

		//the distance from the initial node to itself is 0
		this.distances.put(initialNodeLabel, 0.0);
		this.bandwidth.put(initialNodeLabel, 0.0);

		//and seed initialNode's neighbors
		classNode initialNode = this.graph.getNode(initialNodeLabel);
		ArrayList<classLink> initialNodeNeighbors = initialNode.getNeighbors();

		NetworkMetrics nm = new NetworkMetrics();
		int neighbors = initialNodeNeighbors.size();
		for(int i = 0; i<neighbors; i++) {
			classLink l = initialNodeNeighbors.get(i);
			classNode other = l.getNeighbor(initialNode);

			String Key = initialNode.getLabel().concat("-").concat(other.getLabel());
			classLink cl = link_data.get(Key); 

			if(cl.getLinkCapacity()!=0.0)
				;
			else{
				initialNodeNeighbors.remove(i);
				neighbors--;
				i--;
			}
		}

		for(classLink l : initialNodeNeighbors){
			classNode other = l.getNeighbor(initialNode);
			this.predecessors.put(other.getLabel(), initialNodeLabel); 
			this.distances.put(other.getLabel(), l.getWeight());
			this.bandwidth.put(other.getLabel(), l.getAvailableBandwidth());
			this.availableNodes.add(other);
		}

		this.visitedNodes.add(initialNode);
		//	if(classParameters.print_condition) System.out.println("PL"+ this.predecessors);

		//now apply Dijkstra's algorithm to the Graph
		return processGraph(link_data);
	}

	/**
	 * This method applies Dijkstra's algorithm to the graph using the Node
	 * specified by initialNodeLabel as the starting point.
	 * @post The shortest a-b paths as specified by Dijkstra's algorithm and
	 *       their distances are available
	 */

	public LinkedList<HashMap<String,String>> processGraph(HashMap<String, classLink> link_data) throws IOException{

		NetworkMetrics nm = new NetworkMetrics();
		//as long as there are Links to process
		while(this.availableNodes.size() > 0){

			//pick the cheapest node
			classNode next = this.availableNodes.poll();
			double distanceToNext = this.distances.get(next.getLabel());
			double bandwidthToNext = this.bandwidth.get(next.getLabel());

			//and for each available neighbor of the chosen node
			List<classLink> nextNeighbors = next.getNeighbors();  

			int neighbors = nextNeighbors.size();
			for(int i = 0; i<neighbors; i++) {
				classLink l = nextNeighbors.get(i);
				classNode other = l.getNeighbor(next);

				String Key = next.getLabel().concat("-").concat(other.getLabel());
				classLink cl = link_data.get(Key); 

				if(cl.getLinkCapacity()!=0.0)
					;
				else{
					nextNeighbors.remove(i);
					neighbors--;
					i--;
				}
			}

			for(classLink l: nextNeighbors){
				classNode other = l.getNeighbor(next);

				if(this.visitedNodes.contains(other)){
					continue;
				}

				//we check if a shorter path exists and update to indicate a new shortest found path in the graph
				double currentWeight = this.distances.get(other.getLabel());
				double newWeight = distanceToNext + l.getWeight();
				double currentBandwidth = this.bandwidth.get(other.getLabel());
				double newBandwidth = bandwidthToNext + l.getAvailableBandwidth();

				if((newWeight < currentWeight)){
					this.predecessors.put(other.getLabel(), next.getLabel()); 
					this.distances.put(other.getLabel(), newWeight);
					this.bandwidth.put(other.getLabel(), newBandwidth);
					this.availableNodes.remove(other);				
					this.availableNodes.add(other);
				}
			}

			// finally, mark the selected node as visited so we don't revisit it
			this.visitedNodes.add(next);
		}
		nodeToNodeConnectivity.add(predecessors);

		return nodeToNodeConnectivity;
	}

	// The shortest path (Destination-Source) specified by Dijkstra's algorithm.
	public List<classNode> getPathTo(String destinationLabel) throws Exception{
		LinkedList<classNode> path = new LinkedList<classNode>();
		path.add(graph.getNode(destinationLabel));	//adds the last Node to the end of a selected shortest path

		while(!destinationLabel.equals(this.initialNodeLabel)){
			classNode predecessor = graph.getNode(this.predecessors.get(destinationLabel)); 
			if(predecessor == null){
				path = null;
				return path;
			}
			destinationLabel = predecessor.getLabel();
			path.add(predecessor);
		}
		return path;
	}



	/**
	 * @param destinationLabel The Node to determine the distance from the initial Node
	 * @return int The distance from the initial Node to the Node specified by destinationLabel
	 */

	public double getDistanceTo(String destinationLabel){
		if(classParameters.print_condition) System.out.println("Check Distances: " + distances);
		return this.distances.get(destinationLabel);
	}

	public LinkedList<Object> calculateShortestPath(int totalNetworkNodes, int runStep, HashMap<Integer, ApplicationDataRates> applicationDataRate) throws Exception{
		HashMap<String, classLink> link_CAU_QoI  = new HashMap<String, classLink>();
		HashMap<String, classLink> link_CAU_QoIT = new HashMap<String, classLink>();
		HashMap<String, classLink> link_endNodes = new HashMap<String, classLink>();
		double link_dataRate, link_Capacity, x_pos, y_pos, weight_of_link = 0.0;
		int linkCounter = 0, nodeID;
		Graph graph = new Graph();
		classNode[] nodes = new classNode[totalNetworkNodes];
		NetworkMetrics networkmetrics = new NetworkMetrics();
		/***********************************************************************************************************************************/
		// 1. Adding Nodes to the Network/Graph
		String nodePositionsFile = classParameters.root_path + "nodePositions".concat(Integer.toString(runStep)).concat(".csv");		
		BufferedReader br = new BufferedReader(new FileReader(nodePositionsFile)); String row = null;

		while((row = br.readLine()) != null){
			String[] nodePos = row.split(","); 
			nodeID = Integer.parseInt(nodePos[0]);
			x_pos = Double.parseDouble(nodePos[1]);
			y_pos = Double.parseDouble(nodePos[2]);	

			nodes[nodeID] = new classNode(nodeID + "", x_pos, y_pos);
			graph.addNode(nodes[nodeID], true);
		}
		br.close();	
		/***********************************************************************************************************************************/
		// 2. Counting the number of links in the Network/Graph
		int numberOfLinks = 0;
		for (classNode from_Node: nodes) {
			for (classNode to_Node: nodes) {
				if(from_Node != to_Node) {
					link_dataRate = networkmetrics.getOriginalLinkDataRate(from_Node.distance(to_Node), 2400.0, from_Node.getLabel(), to_Node.getLabel());
					if (link_dataRate > 0.0) {	
						numberOfLinks++;

						// Storing: runStep, From-Node, To-Node, Original Link Data-Rate (e.g. 72.2) via HashMap
						String Key = from_Node.getLabel().concat("-").concat(to_Node.getLabel());
						link_endNodes.put(Key, new classLink(runStep, Integer.parseInt(from_Node.getLabel()), Integer.parseInt(to_Node.getLabel()), link_dataRate));
					} } } }
		/***********************************************************************************************************************************/
		// 3. Finding 1-hop and 2-hop neighbors of a node
		LinkedList<Object> twohop_Neighbors_Count_and_List = new LinkedList<Object>();
		twohop_Neighbors_Count_and_List = neighbors(totalNetworkNodes, link_endNodes);
		int twoHopNeighbors_Count[] = (int[]) twohop_Neighbors_Count_and_List.get(0);
		LinkedList<LinkedList<Object>> twoHopNeighbors_List = (LinkedList<LinkedList<Object>>) twohop_Neighbors_Count_and_List.get(1);
		
		for (classNode from_Node: nodes) { System.out.println(from_Node + ": " + twoHopNeighbors_Count[Integer.parseInt(from_Node.getLabel())]);
		}
		/***********************************************************************************************************************************/
		// 4. Establishing Links between Nodes and putting weights(delay) and bandwidths along links
		System.out.println("No. of Links: " + numberOfLinks);
		classLink[] links = new classLink[numberOfLinks];		
		for (classNode from_Node: nodes) {
			for (classNode to_Node: nodes) {

				if (from_Node != to_Node) {
					String Key = from_Node.getLabel().concat("-").concat(to_Node.getLabel());

					if(link_endNodes.containsKey(Key)) {
						classLink cl  = link_endNodes.get(Key);				
						link_dataRate = cl.getOriginalLinkCapacity();   
						link_Capacity = link_dataRate/(twoHopNeighbors_Count[Integer.parseInt(from_Node.getLabel())]+1); // neighborsCount represents 2-hop neighbors  
						double link_Utilized_Bandwidth  = 0.0;
						double link_Available_Bandwidth = link_Capacity - link_Utilized_Bandwidth;
						weight_of_link = (1/link_Capacity);			// Calculating weight of link as a function of linkCapacity					
						links[linkCounter] = new classLink(from_Node, to_Node, weight_of_link, link_Capacity, link_Available_Bandwidth, link_Utilized_Bandwidth); // Assigning linkCapacity w.r.t to All Links
						link_CAU_QoI.put(Key, new classLink(from_Node, to_Node, weight_of_link, link_Capacity, link_Available_Bandwidth,  link_Utilized_Bandwidth));
						link_CAU_QoIT.put(Key, new classLink(from_Node, to_Node, weight_of_link, link_Capacity, link_Available_Bandwidth,  link_Utilized_Bandwidth));
						linkCounter++; 
					}
				}
			}
		}
		/***********************************************************************************************************************************/
		// 5. Adding Links to Graph/Network
		for(classLink l: links){
			graph.addLink(l.getOne(), l.getTwo(), l.getWeight(), l.getAvailableBandwidth(), l.getAvailableBandwidth(), l.getUtilizedBandwidth()); 
			if(classParameters.print_condition) System.out.println(l);
		}

		LinkedList<List<classNode>> sPA = new LinkedList<List<classNode>>();
		LinkedList<List<classNode>> shortestPathsArray = new LinkedList<List<classNode>>();
		LinkedList<LinkedList<HashMap<String,String>>> predecessor_List = new LinkedList<LinkedList<HashMap<String,String>>>();
		LinkedList<HashMap<String,String>> predec_List = new LinkedList<HashMap<String,String>>();
		LinkedList<Object> obj = new LinkedList<Object>();

		// 6. 
		for(int i = 0; i < nodes.length; i++){
			Dijkstra dijkstra = new Dijkstra(graph, nodes[i].getLabel(), link_CAU_QoI);	
			predec_List = new LinkedList<HashMap<String,String>>();
			predec_List = predecessorsList(graph, nodes[i].getLabel(), link_CAU_QoI);

			for(int j = 0; j < nodes.length; j++){
				String destinationNode = Integer.toString(j);	
				sPA.add(dijkstra.getPathTo(destinationNode));	
			}
		}
		predecessor_List.add(predec_List); 

		for(int i = 0; i < nodes.length; i++){
			for(int j = 0; j < nodes.length; j++) {
				shortestPathsArray.add(sPA.get((j*nodes.length)+i));
			}
		}

		obj.add(shortestPathsArray);
		obj.add(nodes);
		obj.add(graph);
		obj.add(numberOfLinks);
		obj.add(links);
		obj.addAll(predecessor_List);
		obj.add(link_CAU_QoI);
		obj.add(link_CAU_QoIT);
		obj.add(twoHopNeighbors_Count);
		obj.add(twoHopNeighbors_List);

		return obj;
	}

	// Finding 1-hop and 2-hop neighbors of a node
	public static LinkedList<Object> neighbors(int totalNetworkNodes, HashMap<String, classLink> link_endNodes) throws IOException {

		int OneHopNeighbors_Count[] = new int[totalNetworkNodes], TwoHopNeighbors_Count[] = new int[totalNetworkNodes];
		LinkedList<LinkedList<Object>> oneHopNeighborList = new LinkedList<LinkedList<Object>>();
		LinkedList<LinkedList<Object>> twoHopNeighborList = new LinkedList<LinkedList<Object>>();
		LinkedList<Object> obj                			  = new LinkedList<Object>();
		for(int i = 0; i < totalNetworkNodes; i++) { OneHopNeighbors_Count[i] = 0; TwoHopNeighbors_Count[i] = 0; }

		// Populating 1-Hop Neighbors for each Node
		for(int node = 0; node < totalNetworkNodes; node++) {
			LinkedList<Object> oneHopNeighbor = new LinkedList<Object>();

			for(classLink cl : link_endNodes.values()) {
				if(cl.fromNode == node) {
					OneHopNeighbors_Count[node] = OneHopNeighbors_Count[node] + 1;
					oneHopNeighbor.add(cl.toNode);
				}
			}
			oneHopNeighborList.add(node, oneHopNeighbor);
		}

		// Populating 2-Hop Neighbors for each Node
		for(int node = 0; node < oneHopNeighborList.size(); node++) {	
			LinkedList<Object> clone = (LinkedList<Object>) oneHopNeighborList.get(node).clone();
			LinkedList<Object> twoHop_Neighbor = clone; 
			int size = oneHopNeighborList.get(node).size(), count = 0;  						

			if(size != 0) { 							
				for(int j = 0; j < size; j++) {
					int hop1_Node = (int) oneHopNeighborList.get(node).get(j);						
					for(int i = 0; i < oneHopNeighborList.get(hop1_Node).size(); i++) {  
						Object node_obj = oneHopNeighborList.get(hop1_Node).get(i);						
						if(twoHop_Neighbor.contains(node_obj) || (int) node_obj == node) ;
						else {
							twoHop_Neighbor.add(node_obj); 
							count++;
						}
					}
				}
				TwoHopNeighbors_Count[node] = OneHopNeighbors_Count[node] + count;
			} 
			twoHopNeighborList.add(node, twoHop_Neighbor); 		
		}

		obj.add(TwoHopNeighbors_Count);
		obj.add(twoHopNeighborList);
		return obj;
	}

	public static ArrayList<String> csvFile(String Line) {
		ArrayList<String> Result = new ArrayList<String>();

		if (Line != null) {
			String[] splitData = Line.split("\\s*,\\s*");
			for (int i = 0; i < splitData.length; i++) {
				if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
					Result.add(splitData[i].trim());
				}
			}
		}

		return Result;
	}
}