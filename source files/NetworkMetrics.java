import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Iterator;
//import src.classNode;

public class NetworkMetrics {

	public int hopCount(String destinationNode, Graph graph, classNode[] nodes, int sourceNode, HashMap<String, classLink> link_data) throws Exception{
		Dijkstra dijkstra = new Dijkstra(graph, nodes[sourceNode].getLabel(), link_data);
		List<classNode> path = dijkstra.getPathTo(destinationNode);
		if(classParameters.print_condition) System.out.println(sourceNode + " -> "+ destinationNode + ": " + path);
		int HopCount = -1;

		for (int l=0; l<path.size(); l++){
			HopCount++;
		}
		return HopCount;
	}

	public double[] getBandwidth(List<classNode> path, String sourceNode, classNode source_Node, classLink[] links, double min_ofLinkBandwidth, Graph graph, String destinationNodeLabel, HashMap<String, String> predecessors, 
			int runStep, int totalNetworkNodes, HashMap<Integer, ApplicationDataRates> applicationDataRate, HashMap<String, classLink> link_CAU) throws IOException{

		if(classParameters.print_condition) System.out.println("\n************Bandwidth Calculation Module***********");
		classLink link_info;
		String receiverNode = graph.getNode(destinationNodeLabel).getLabel(), senderNode;
		double availableBandwidth, SrcDst_BandwidthValue, sourceNodeDataRate = 0.0;
		double communicationOverheadTime = 0.0;
		double bandwidth_and_communicationOverhead[] = new double[2];
		int controlPacketSize = 8;

		// 1.	Calculate minimum Available Bandwidth along Src-Dst path
		while(!(receiverNode).equals(sourceNode)){	// when sender and receiver are same, then quit		
			senderNode = graph.getNode(predecessors.get(receiverNode)).getLabel();
			link_info = link_CAU.get(senderNode.concat("-").concat(receiverNode));

			availableBandwidth = link_info.getAvailableBandwidth();

			if(availableBandwidth <= min_ofLinkBandwidth){
				if(classParameters.print_condition) System.out.println("Available Bandwidth = " + availableBandwidth + ", Min of Link Bandwidth =  " + min_ofLinkBandwidth);
				min_ofLinkBandwidth = availableBandwidth;
			}
			communicationOverheadTime = communicationOverheadTime + (2*(controlPacketSize/(link_info.getLinkCapacity()*1000000)));
			receiverNode = senderNode;	
		}

		// 2.	Calculate minimum of "Source-Node Application data-rate" and "Src-Dst path's Minimum Available Bandwidth"
		sourceNodeDataRate = applicationDataRate.get(Integer.parseInt(sourceNode)).get_appDataRate();	
		if(min_ofLinkBandwidth > sourceNodeDataRate) 
			SrcDst_BandwidthValue = sourceNodeDataRate;
		else 										 
			SrcDst_BandwidthValue = min_ofLinkBandwidth;

		bandwidth_and_communicationOverhead[0] = SrcDst_BandwidthValue;
		bandwidth_and_communicationOverhead[1] = communicationOverheadTime;

		if(classParameters.print_condition) System.out.println("Min of Link Bandwidth along Src-Dst Path = " + min_ofLinkBandwidth);
		if(classParameters.print_condition) System.out.println("Src-Dst Path Bandwidth = " + SrcDst_BandwidthValue);
		return bandwidth_and_communicationOverhead;
	}

	public double allotedBandwidth(String sourceNode, double min_ofLinkBandwidth, Graph graph, String destinationNode, HashMap<String, String> predecessors, 
			HashMap<Integer, ApplicationDataRates> applicationDataRate, HashMap<String, classLink> link_CAU, HashMap<String, NetworkMetricThresholdValues> networkMetricThresholdValues) throws IOException{

		if(classParameters.print_condition) System.out.println("\n************Bandwidth Calculation Module***********");
		classLink link_info;
		String receiverNode = graph.getNode(destinationNode).getLabel(), senderNode;
		double availableBandwidth, SrcDst_BandwidthValue, sourceNodeDataRate = 0.0, communicationOverheadTime = 0.0, bandwidth_and_communicationOverhead[] = new double[2], assigned_bandwidth = 0.0;

		// 1.	Calculate minimum Available Bandwidth along Src-Dst path
		while(!(receiverNode).equals(sourceNode)){	// when sender and receiver are same, then quit		
			senderNode = graph.getNode(predecessors.get(receiverNode)).getLabel();
			link_info = link_CAU.get(senderNode.concat("-").concat(receiverNode));

			availableBandwidth = link_info.getAvailableBandwidth();

			if(availableBandwidth <= min_ofLinkBandwidth){
				if(classParameters.print_condition) System.out.println("Available Bandwidth = " + availableBandwidth + ", Min of Link Bandwidth =  " + min_ofLinkBandwidth);
				min_ofLinkBandwidth = availableBandwidth;
			}
			receiverNode = senderNode;	
		}

		// 2.	Calculate minimum of "Source-Node Application data-rate" and "Src-Dst path's Minimum Available Bandwidth"
		sourceNodeDataRate = applicationDataRate.get(Integer.parseInt(sourceNode)).get_appDataRate();	
		if(min_ofLinkBandwidth > sourceNodeDataRate) 
			SrcDst_BandwidthValue = sourceNodeDataRate;
		else 										 
			SrcDst_BandwidthValue = min_ofLinkBandwidth;

		// 3.	Assigned Bandwidth = Minimum of "SrcDst_BandwidthValue" and "Bandwidth Threshold"
		NetworkMetricThresholdValues nmtv = networkMetricThresholdValues.get(destinationNode);
		if((Double.compare(SrcDst_BandwidthValue, nmtv.get_bandwidth()) > 0)) 
			assigned_bandwidth = nmtv.get_bandwidth();
		else 										 
			assigned_bandwidth = SrcDst_BandwidthValue;

		return assigned_bandwidth;
	}

	public double getLinkIntegrity(int[] nodeKeys, String sourceNodeLabel, double Link_Integrity, Graph graph, classNode destinationNode,
			HashMap<String, String> predecessors){

		boolean condition = true;
		Link_Integrity = nodeKeys[Integer.parseInt(destinationNode.getLabel())];
		classNode successor = destinationNode;
		classNode predecessor = graph.getNode(predecessors.get(successor.getLabel()));

		if(sourceNodeLabel.equals(destinationNode.getLabel()))
			Link_Integrity = 0;

		while(condition == true){					
			int nodeId = Integer.parseInt(predecessor.getLabel());

			if(nodeKeys[nodeId] <= Link_Integrity) 
				Link_Integrity = nodeKeys[nodeId];

			successor = predecessor;
			predecessor = graph.getNode(predecessors.get(successor.getLabel()));		
			if(predecessor==null)
				condition = false;
		}	

		return Link_Integrity;		
	}

	// To keep source IU values fixed throughout all iterations
	public double getInformationUtility(double Information_Utility, String sourceNode) {

		if(sourceNode.equals("0"))
			Information_Utility = 5.5; //0.6;
		else if (sourceNode.equals("1"))
			Information_Utility = 9.4; //0.92
		else if (sourceNode.equals("2"))
			Information_Utility = 7.2; //0.84
		else if (sourceNode.equals("3"))
			Information_Utility = 4.8; //0.55
		else if (sourceNode.equals("4"))
			Information_Utility = 3.9; //0.75
		else if (sourceNode.equals("5"))
			Information_Utility = 5.1; //0.68

		return Information_Utility;

	}

	// Calculating minimum data-rate along src-dst path links and comparing with src's appl data-rate
	public double getPathDataRate(List<classNode> path, String sourceNode, classNode source_node, classLink[] links, double min_ofLinkDataRate, Graph graph, String destinationNodeLabel, HashMap<String, String> predecessors, 
			int runStep, int totalNetworkNodes, int fileNumber, HashMap<Integer, ApplicationDataRates> applicationDataRate, HashMap<String, LinkStatistics> linkStatistics) throws IOException{

		double linkDataRate = 0, sourceNodeApplicationDataRate = 0, assignedBandwidth, Node_SSTDMA;
		int Node_ForwardingCount;
		LinkStatistics link_states;
		String receiverNode = graph.getNode(destinationNodeLabel).getLabel(), senderNode;

		// 1.	Calculate Path DataRate
		while(!(receiverNode).equals(sourceNode)){	// when sender and receiver are same, then quit		
			senderNode = graph.getNode(predecessors.get(receiverNode)).getLabel();
			link_states = linkStatistics.get(senderNode.concat("-").concat(receiverNode));

			Node_SSTDMA = link_states.get_linkCapacity();
			Node_ForwardingCount =  link_states.get_nodeForwardingCount();
			linkDataRate = Node_SSTDMA/Node_ForwardingCount;

			if(linkDataRate <= min_ofLinkDataRate){
				if(classParameters.print_condition) System.out.println("Link Data-Rate = " + linkDataRate + ", Min of Link Data-Rate =  " + min_ofLinkDataRate);
				min_ofLinkDataRate = linkDataRate;
			}
			receiverNode = senderNode;
		}

		// 2.	Read source-node application data rate
		int Key = Integer.parseInt(sourceNode);
		sourceNodeApplicationDataRate = applicationDataRate.get(Key).get_appDataRate();		

		// Compare 1 and 2
		if(min_ofLinkDataRate > sourceNodeApplicationDataRate) 
			assignedBandwidth = sourceNodeApplicationDataRate;
		else
			assignedBandwidth = min_ofLinkDataRate;

		if(classParameters.print_condition) System.out.println("Min of Link data-rate along Path: " + min_ofLinkDataRate);
		if(classParameters.print_condition) System.out.println("Source-Node: " + sourceNode + " Application data-rate is = " + sourceNodeApplicationDataRate);

		return assignedBandwidth;		
	}

	/*	public double getOriginalLinkDataRate(double distance_km, double freq_Mhz, String from_Node, String to_Node) throws IOException{

		HashMap<String, RandomFading> randomFading = new HashMap<String, RandomFading>();
		Random rand = new Random();
		double Beta = 4.0, mean_dB = 0.0, sd_dB = 1.0, RandomFading_dB = rand.nextGaussian()*sd_dB + mean_dB, thisRate = 0.0;
		double[] rates = {0.0, 7.2, 14.4, 21.7, 28.9, 43.3, 57.8, 65.0, 72.2};
		double[] snrThreshold = {0.0, 2.0, 5.0, 9.0, 11.0, 15.0, 18.0, 20.0, 25.0}; 

		if (RandomFading_dB < 0.0) 
			RandomFading_dB = 0.0; // Do not make fading improve signal

		double PathLoss_db = - 32.45 - Beta * 10 * Math.log10(freq_Mhz * distance_km) - RandomFading_dB;  
		double Radio_Pwr_dBm = 20.0;
		double rcdPower_dBm = Radio_Pwr_dBm + PathLoss_db;
		double noise_cuttoff = -180.0;
		double sinr_dB = rcdPower_dBm - noise_cuttoff;	 

		for (int i = 0; i < snrThreshold.length; i++) {
			if (sinr_dB > snrThreshold[i]) 	thisRate = rates[i]; 
			else 							break;
		}	  

		String Key = from_Node.concat("-").concat(to_Node);
		randomFading.put(Key, new RandomFading(from_Node, to_Node, RandomFading_dB)); // Storing: From-Node, To-Node, Random-Fading-Value via HashMap		
		return (thisRate);
	}*/

	public double getOriginalLinkDataRate(double distance_km, double freq_Mhz, String from_Node, String to_Node) throws IOException {

		double Beta = 4.0, thisRate = 0.0; 
		double[] rates = {0.0, 7.2, 14.4, 21.7, 28.9, 43.3, 57.8, 65.0, 72.2};
		double[] snrThreshold = {0.0, 2.0, 5.0, 9.0, 11.0, 15.0, 18.0, 20.0, 25.0}; 
		double PathLoss_db = - 32.45 - Beta * 10 * Math.log10(freq_Mhz * distance_km); 
		double Radio_Pwr_dBm = 20.0;
		double rcdPower_dBm = Radio_Pwr_dBm + PathLoss_db;
		double noise_cuttoff = -180.0;
		double sinr_dB = rcdPower_dBm - noise_cuttoff;	 

		for (int i = 0; i < snrThreshold.length; i++) {
			if (sinr_dB > snrThreshold[i]) 	thisRate = rates[i]; 
			else 							break;
		}	  

		return (thisRate);
	}

	/*	public double getOriginalLinkDataRate(double distance_km, double freq_Mhz, String from_Node, String to_Node) throws IOException {

		double Beta = 4.0, thisRate = 0.0; 
		double[] rates = {0.0, 7.2, 14.4, 21.7, 28.9, 43.3, 57.8, 65.0, 72.2};
		double[] snrThreshold = {0.0, 2.0, 5.0, 9.0, 11.0, 15.0, 18.0, 20.0, 25.0}; 
		double PathLoss_db = - 32.45 - Beta * 10 * Math.log10(freq_Mhz * distance_km); 
	-->	double Radio_Pwr_dBm = 30.0; // updated value
		double rcdPower_dBm = Radio_Pwr_dBm + PathLoss_db;
	-->	double noise_cuttoff = -170.0; // updated value
		double sinr_dB = rcdPower_dBm - noise_cuttoff;	 

		for (int i = 0; i < snrThreshold.length; i++) {
			if (sinr_dB > snrThreshold[i]) 	thisRate = rates[i]; 
			else 							break;
		}	  

		return (thisRate);
	}*/

	public void addTrafficToNetwork(int selected_Source, Graph graph, String destinationNode, HashMap<String, String> predecessors, HashMap<String, classLink> link_CAU, int hop_Count, double linkIntegrity, 
			double informationUtility, double assignedBandwidth, int runStep, HashMap<Integer, networkData_HashMaps> nmv_SelectedSource) throws Exception{		
		int destNode = Integer.parseInt(destinationNode);
		/************* Storing Network Metric Values w.r.t QoI/QoIT/TE selected source *************/
		nmv_SelectedSource.put(destNode, new networkData_HashMaps(runStep, destNode, selected_Source, assignedBandwidth, hop_Count, linkIntegrity, informationUtility));

		// Re-evaluate Links Available Bandwidth and Utilized Bandwidth along src-dst route
		/************* link_CAU -->> HashMap: currentTime, fromNode, toNode, linkCapacity, availableBandwidth, utilizedBandwidth *************/
		String receiverNode = graph.getNode(destinationNode).getLabel(), senderNode;
		while(!(receiverNode).equals(Integer.toString(selected_Source))){	// when sender and receiver are same, then quit	
			senderNode = graph.getNode(predecessors.get(receiverNode)).getLabel();
			double ratioofLinkBandwidth_UsedbySrcDstPair = assignedBandwidth / link_CAU.get(senderNode.concat("-").concat(receiverNode)).getAvailableBandwidth();

			Object KeySet[] = link_CAU.keySet().toArray();
			for(int i = 0; i < KeySet.length; i++) {
				classLink ld = link_CAU.get((String) KeySet[i]);
				StringTokenizer st = new StringTokenizer((String) KeySet[i], "-");  

				if(senderNode.equals(st.nextToken())) {
					double reducedLinkBandwidth = ld.getAvailableBandwidth() * ratioofLinkBandwidth_UsedbySrcDstPair;
					ld.setAvailableBandwidth(reducedLinkBandwidth);
					ld.setUtilizedBandwidth(reducedLinkBandwidth);
				}
			}
			receiverNode = senderNode;	
		}
	}

	// Packet Loss Rate for a "selected src-dst" pair
	public double packetLossRate (double assignedBandwidth, String selectedSource, classNode node, classLink[] links, Graph graph, String destinationNodeLabel, HashMap<String, String> predecessors){

		double packetLossPercentage;
		double link_PacketLoss_Probability, packetLossProbability;
		int number_ofPacketsLost = 0;
		classNode successor = graph.getNode(destinationNodeLabel);

		// We Suppose that Packet Length = 1MB. 
		int[] number_ofTotalPackets = new int[(int) assignedBandwidth];
		if(classParameters.print_condition) System.out.println("number_ofTotalPackets: " + number_ofTotalPackets.length);

		// Populate Packet-array with 1 (meaning each packet is believed to be received at destination)
		for(int i=0; i<number_ofTotalPackets.length; i++) {
			number_ofTotalPackets[i] = 1;
		}

		while(!(successor.getLabel()).equals(selectedSource)){
			classNode predecessor = graph.getNode(predecessors.get(successor.getLabel()));
			link_PacketLoss_Probability = Math.random();		// Link's Packet Loss Probability

			for(classLink l: links){
				if((predecessor!=null) && (l.getOne()==predecessor && l.getTwo()==successor)){

					for(int i=0; i<number_ofTotalPackets.length; i++) {
						packetLossProbability = Math.random();		// chance of occurrence of packet loss

						if((packetLossProbability <= link_PacketLoss_Probability) && (number_ofTotalPackets[i] == 1)) {
							number_ofTotalPackets[i] = 0;
							number_ofPacketsLost++;
							//if(classParameters.print_condition) System.out.println("link_PacketLoss_Probability: " + link_PacketLoss_Probability + ", packetLossProbability: " + packetLossProbability);								
						}
					}

					successor = predecessor;
					break;
				}
			}
		}
		if(classParameters.print_condition) System.out.println("number_ofPacketsLost: " + number_ofPacketsLost);
		packetLossPercentage = (double) number_ofPacketsLost/ (double) number_ofTotalPackets.length;

		if(classParameters.print_condition) System.out.println("Source Node: " + selectedSource + " Packet-Loss is = " + packetLossPercentage);

		return packetLossPercentage;	

	}

	public int nullBandwidth(String sourceNode, double min_ofLinkBandwidth, Graph graph, String destinationNodeLabel, HashMap<String, String> predecessors, 
			HashMap<String, classLink> link_data) throws IOException{

		double availableBandwidth;
		classLink link_info;
		String receiverNode = graph.getNode(destinationNodeLabel).getLabel(), senderNode;

		if(sourceNode.equals(destinationNodeLabel)){
			min_ofLinkBandwidth = 0;
		}

		while(!(receiverNode).equals(sourceNode)){	// when sender and receiver are same, then quit	
			senderNode = graph.getNode(predecessors.get(receiverNode)).getLabel();
			link_info = link_data.get(senderNode.concat("-").concat(receiverNode));
			availableBandwidth = link_info.getAvailableBandwidth();

			if(availableBandwidth <= min_ofLinkBandwidth){
				if(classParameters.print_condition) System.out.println("Available Bandwidth = " + availableBandwidth + ", Min of Link Bandwidth =  " + min_ofLinkBandwidth);
				min_ofLinkBandwidth = availableBandwidth;
			}
			receiverNode = senderNode;	
		}

		if(Double.compare(min_ofLinkBandwidth, 0.00) == 0)
			return -1;
		else
			return (Integer.parseInt(sourceNode));
	}

	public double CommunicationOverheadTime(String current_time, int source, int totalNetworkNodes, int[] informationSourceList, int destinationNode, Graph graph, classNode[] nodes, 
			classLink[] links, int runStep, HashMap<String, classLink> link_data, HashMap<Integer, ApplicationDataRates> applicationDataRate) throws Exception{

		double Bandwidth = Double.MAX_VALUE, bandwidth_and_communicationOverhead[];
		double communicationOverheadTime = 0.0;

		for(int i = 0; i < source; i++){
			Dijkstra dijkstra = new Dijkstra(graph, nodes[informationSourceList[i]].getLabel(), link_data);
			HashMap<String,String> predecessors =  dijkstra.predecessorsList(graph, nodes[informationSourceList[i]].getLabel(), link_data).get(0);

			bandwidth_and_communicationOverhead = getBandwidth(dijkstra.getPathTo(Integer.toString(destinationNode)), Integer.toString(informationSourceList[i]), nodes[informationSourceList[i]], 
					links, Bandwidth, graph, nodes[destinationNode].getLabel(), predecessors, runStep, totalNetworkNodes, applicationDataRate, link_data);

			communicationOverheadTime = communicationOverheadTime + (4*bandwidth_and_communicationOverhead[1]);
			Bandwidth = Double.MAX_VALUE;
		}

		communicationOverheadTime = communicationOverheadTime*1000000000;
		return communicationOverheadTime;
	}

	/*	public void networkMetricThresholdValues(String currentTime, int destination, WriteFile NetworkMetric_ThresholdValues, HashMap<String, NetworkMetricThresholdValues> networkMetricThresholdValues) throws IOException {
		double bandwidth, informationUtility;
		int hopCount, linkIntegrity;
		Random random = new Random();
		String Key = Integer.toString(destination);

		hopCount = 1 + random.nextInt(15);
		linkIntegrity = 1 + random.nextInt(4);
		do {
			bandwidth = random.nextDouble() * 0.1;
		} while(bandwidth < 0.08);
		informationUtility = random.nextDouble() * 10.0;

		do {
			bandwidth = random.nextDouble() * 1.0;
		} while(bandwidth < 0.8);
		informationUtility = random.nextDouble() * 1.0;

		networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(currentTime, destination, bandwidth, hopCount, linkIntegrity, informationUtility));
		NetworkMetric_ThresholdValues.writeToFile(destination, bandwidth, hopCount, linkIntegrity, informationUtility);
	}*/

	public void networkMetricThresholdValues_Fixed_BW_IU_PI_VaryingHC(String currentTime, int destination, WriteFile NetworkMetric_ThresholdValues, 
			HashMap<String, NetworkMetricThresholdValues> networkMetricThresholdValues, int runStep) throws IOException {
		double bandwidth, informationUtility;
		int hopCount, linkIntegrity;
		String Key = Integer.toString(destination);

		if(runStep == 1) {
			linkIntegrity = 1;
			bandwidth = 0.1;
			informationUtility = 5.0;
			hopCount = 1;

			networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(currentTime, destination, bandwidth, hopCount, linkIntegrity, informationUtility));
			NetworkMetric_ThresholdValues.writeToFile(destination, bandwidth, hopCount, linkIntegrity, informationUtility);
		}
		else {
			String file = classParameters.root_path + "NetworkMetric_ThresholdValues.csv";
			String csvSplitBy = ",";
			String[] readLine = null;
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			BufferedReader brCopy = new BufferedReader(new FileReader(file)); 

			while(brCopy.readLine()!=null) {
				readLine = br.readLine().split(csvSplitBy);
				Key = readLine[0];
				networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(Integer.parseInt(readLine[0]), 
						Double.parseDouble(readLine[1]), Integer.parseInt(readLine[2])+runStep-1, Integer.parseInt(readLine[3]), 
						Double.parseDouble(readLine[4])));	
			} br.close(); brCopy.close();
		}

	}

	public void networkMetricThresholdValues_Fixed_HC_IU_PI_VaryingBW(String currentTime, int destination, WriteFile NetworkMetric_ThresholdValues, 
			HashMap<String, NetworkMetricThresholdValues> networkMetricThresholdValues, int runStep) throws IOException {
		double bandwidth, informationUtility;
		int hopCount, linkIntegrity;
		String Key = Integer.toString(destination);

		if(runStep == 1) {
			/*linkIntegrity = 2;
			bandwidth = 0.1;   // 0.1 Mbps or 100 Kbps
			informationUtility = 5.0;
			hopCount = 8;*/

			linkIntegrity = 1;
			bandwidth = 0.1;   // 0.1 Mbps or 100 Kbps
			informationUtility = 5.0;
			hopCount = 15;

			networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(currentTime, destination, bandwidth, hopCount, linkIntegrity, informationUtility));
			NetworkMetric_ThresholdValues.writeToFile(destination, bandwidth, hopCount, linkIntegrity, informationUtility);
		}
		else {
			String file = classParameters.root_path + "NetworkMetric_ThresholdValues.csv";
			String csvSplitBy = ",";
			String[] readLine = null;
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			BufferedReader brCopy = new BufferedReader(new FileReader(file)); 

			while(brCopy.readLine()!=null) {
				readLine = br.readLine().split(csvSplitBy);
				Key = readLine[0];
				networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(Integer.parseInt(readLine[0]), 
						Double.parseDouble(readLine[1])+((runStep-1)*0.1), Integer.parseInt(readLine[2]), Integer.parseInt(readLine[3]), 
						Double.parseDouble(readLine[4])));	
			} br.close(); brCopy.close();
		}

	}

	public void networkMetricThresholdValues_Fixed_HC_IU_BW_VaryingPI(String currentTime, int destination, WriteFile NetworkMetric_ThresholdValues, 
			HashMap<String, NetworkMetricThresholdValues> networkMetricThresholdValues, int runStep) throws IOException {
		double bandwidth, informationUtility, linkIntegrity;
		int hopCount;
		String Key = Integer.toString(destination);

		if(runStep == 1) {
			linkIntegrity = 1;
			bandwidth = 0.1;
			informationUtility = 5.0;
			hopCount = 8;

			networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(currentTime, destination, bandwidth, hopCount, linkIntegrity, informationUtility));
			NetworkMetric_ThresholdValues.writeToFile(destination, bandwidth, hopCount, linkIntegrity, informationUtility);
		}
		else {
			String file = classParameters.root_path + "NetworkMetric_ThresholdValues.csv";
			String csvSplitBy = ",";
			String[] readLine = null;
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			BufferedReader brCopy = new BufferedReader(new FileReader(file)); 

			while(brCopy.readLine()!=null) {
				readLine = br.readLine().split(csvSplitBy);
				Key = readLine[0];
				networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(Integer.parseInt(readLine[0]), 
						Double.parseDouble(readLine[1]), Integer.parseInt(readLine[2]), runStep*1, 
						Double.parseDouble(readLine[4])));	
			} br.close(); brCopy.close();
		}

	}

	public void networkMetricThresholdValues_Fixed_HC_PI_BW_VaryingIU(String currentTime, int destination, WriteFile NetworkMetric_ThresholdValues, 
			HashMap<String, NetworkMetricThresholdValues> networkMetricThresholdValues, int runStep) throws IOException {
		double bandwidth, informationUtility;
		int hopCount, linkIntegrity;
		String Key = Integer.toString(destination);

		if(runStep == 1) {
			linkIntegrity = 2;
			bandwidth = 0.09;
			informationUtility = 1.0;
			hopCount = 8;

			networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(currentTime, destination, bandwidth, hopCount, linkIntegrity, informationUtility));
			NetworkMetric_ThresholdValues.writeToFile(destination, bandwidth, hopCount, linkIntegrity, informationUtility);
		}
		else {
			String file = classParameters.root_path + "NetworkMetric_ThresholdValues.csv";
			String csvSplitBy = ",";
			String[] readLine = null;
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			BufferedReader brCopy = new BufferedReader(new FileReader(file)); 

			while(brCopy.readLine()!=null) {
				readLine = br.readLine().split(csvSplitBy);
				Key = readLine[0];
				networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(Integer.parseInt(readLine[0]), 
						Double.parseDouble(readLine[1]), Integer.parseInt(readLine[2]), Integer.parseInt(readLine[3]), 
						Double.parseDouble(readLine[4])+runStep-1));	
			} br.close(); brCopy.close();
		}

	}

	// Same network metric thresholds for all users
		public void networkMetricThresholdValues_All_Fixed(String currentTime, int destination, WriteFile NetworkMetric_ThresholdValues, 
			HashMap<String, NetworkMetricThresholdValues> networkMetricThresholdValues, int runStep) throws IOException {
		double bandwidth, informationUtility;
		int hopCount, linkIntegrity;
		String Key = Integer.toString(destination);

		linkIntegrity = 2;
		bandwidth = 0.3;   // 0.1 Mbps or 100 Kbps
		informationUtility = 5.0;
		hopCount = 8;

		networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(currentTime, destination, bandwidth, hopCount, linkIntegrity, informationUtility));
		NetworkMetric_ThresholdValues.writeToFile(destination, bandwidth, hopCount, linkIntegrity, informationUtility);
	}

	// different network metric thresholds for all users
/*	public void networkMetricThresholdValues_All_Fixed(String currentTime, int destination, WriteFile NetworkMetric_ThresholdValues, 
			HashMap<String, NetworkMetricThresholdValues> networkMetricThresholdValues, int runStep) throws IOException {
		double bandwidth, informationUtility;
		int hopCount, linkIntegrity;
		Random rand = new Random();
		String Key = Integer.toString(destination);

		linkIntegrity = rand.nextInt(5-1) + 1;
		hopCount = rand.nextInt(11-1) + 1;
		bandwidth = rand.nextDouble() * (0.3 - 0.0) + 0.0;;   // 0.1 Mbps or 100 Kbps
		informationUtility = rand.nextDouble() * (10.0 - 3.0) + 3.0;

		networkMetricThresholdValues.put(Key, new NetworkMetricThresholdValues(currentTime, destination, bandwidth, hopCount, linkIntegrity, informationUtility));
		NetworkMetric_ThresholdValues.writeToFile(destination, bandwidth, hopCount, linkIntegrity, informationUtility);
	}*/
}
