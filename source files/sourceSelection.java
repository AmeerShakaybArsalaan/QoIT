/*1. For All Users: Same Quality Metric Weight and same network layer Requirements and Weights
  2. Source Hand-over allowed*/

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.IntStream;

public class sourceSelection {
	static boolean flag_updatedInfo = false;

	public static void main(String[] args) throws Exception
	{
		System.out.println("Program execution started"); 
		sourceSelection ss = new sourceSelection();
		int noofcriteria = 4, noofSources, sourceCounter, totalNetworkNodes = classParameters.totalNetworkNodes, fileNumber = 1, runStep = 0, index = -1, nodeKeys[] = new int[totalNetworkNodes];
		Integer[] srcNodeList = Arrays.stream(classParameters.sourceNodeList).boxed().toArray(Integer[]::new);
		double[]  applicationDataRatesPool = {0.3, 0.3, 0.3, 0.3, 0.3}, QoIMetricPairwiseComparisons = {1, 0.5, 0.25, 4, 2, 1, 0.5, 4, 4, 2, 1, 5, 0.25, 0.25, 0.2, 1};	// Comparison Matrix for QoI Metrics ACTR (T>C>A>R)
		Random    rand = new Random(classParameters.seedRandom);

		String qualityMetricWeight = "QualityMetricWeight.csv";
		WriteFile QualityMetricWeight = new WriteFile(qualityMetricWeight, true);
		QualityMetricWeight.clearTheFile();
		double QoICriteriaWeights[] = ss.QoICriteriaWeights(), priority_of_QualityMetrics[] = QoICriteriaWeights;
		/***********************************************************************************************************************************/
		HashMap<Integer, ApplicationDataRates>      application_DataRate        = new HashMap<Integer, ApplicationDataRates>();	
		HashMap<Integer, selectedSourceInformation> selectedSourceList_QoI      = new HashMap<Integer, selectedSourceInformation>();
		HashMap<Integer, selectedSourceInformation> selectedSourceList_QoIT     = new HashMap<Integer, selectedSourceInformation>();
		Map<Integer, LinkedHashSet<Integer>>        srcDstRouteMap_QoI          = new HashMap<Integer, LinkedHashSet<Integer>>();
		Map<Integer, LinkedHashSet<Integer>>        srcDstRouteMap_QoIT         = new HashMap<Integer, LinkedHashSet<Integer>>();
		Map<Integer, LinkedHashSet<Integer>>        srcDstRouteMap_QoI_updated  = new HashMap<Integer, LinkedHashSet<Integer>>();
		Map<Integer, LinkedHashSet<Integer>>        srcDstRouteMap_QoIT_updated = new HashMap<Integer, LinkedHashSet<Integer>>();
		/***********************************************************************************************************************************/
		// creating timer and timer-task w.r.t networkInfoUpdate
		Timer timer_networkInfoUpdate = new Timer();  
		TimerTask networkInfoUpdate   = new TimerTask() {  
			public void run() {  		
				flag_updatedInfo = true;
				srcDstRouteMap_QoI.putAll(srcDstRouteMap_QoI_updated);
				srcDstRouteMap_QoIT.putAll(srcDstRouteMap_QoIT_updated);
			};  
		};
		timer_networkInfoUpdate.scheduleAtFixedRate(networkInfoUpdate, 0, 32000);    // assuming 8 sec per iteration	
		/***********************************************************************************************************************************/	
		// 1. Assigning fix keys to all nodes from securityKeysPool = {1, 2, 3, 4}
		for(int i = 0; i < totalNetworkNodes; i++) nodeKeys[i] = (int) (rand.nextDouble()*classParameters.key) + 1;

		// 2. Assigning Source(s) Application DataRate for runStep = 1. [Current-Time, Source-Node, Application-Data-Rate e.g. (1:33:45, 1, 0.3)]
		for(int i = 0; i < classParameters.sourceNodeList.length; i++)
			application_DataRate.put(classParameters.sourceNodeList[i], new ApplicationDataRates(classParameters.sourceNodeList[i], assignRequestedDataRatestoApplication(applicationDataRatesPool)));
		/***********************************************************************************************************************************/
		// 3. Assigning (x, y) Node Positions to each node in a network. Do this for all iterations in the start to bring parallelism.
		for (int file = 1; file <= classParameters.nSteps; file++) {  // Create nodePositions .csv file: (nodeID, x-positions, y-position)			
			WriteFile node_Positions = new WriteFile(classParameters.root_path +"nodePositions".concat(Integer.toString(file)).concat(".csv"), true);
			node_Positions.clearTheFile();
			classUAV cUAV = new classUAV();
			ArrayList<NetworkNode> networkNodes = new ArrayList<NetworkNode>();

			if(file == 1) {  // Create node positions randomly
				for(int i = 0; i < totalNetworkNodes; i++){
					int itype = 1;
					if (i < classParameters.sourceNodeList.length) itype = 0; // Source
					if (i >= classParameters.totalUserNodes) itype = 2; // UAV
					double x_pos = rand.nextDouble()*classParameters.gridSize;
					double y_pos = rand.nextDouble()*classParameters.gridSize;
					double dirnRdns = 2.0*Math.PI*rand.nextDouble();
					Location loc = new Location(x_pos, y_pos);
					NetworkNode nNode = new NetworkNode(Integer.toString(i),loc, dirnRdns, itype);
					networkNodes.add(nNode);
				}
			}
			else {  // for file >= 2 introduce mobility
				String file_Path = classParameters.root_path + "nodePositions";
				String nodePositionsFile = file_Path.concat(Integer.toString(file-1)).concat(".csv");
				String row = null;
				BufferedReader br = new BufferedReader(new FileReader(nodePositionsFile));  
				//int nNode = 0;

				while((row = br.readLine()) != null){
					//nNode++;
					String[] nodePos = row.split(","); 
					String nodeName = nodePos[0];
					double x_pos = Double.parseDouble(nodePos[1]);
					double y_pos = Double.parseDouble(nodePos[2]);
					double dirnRdns = Double.parseDouble(nodePos[3]);		
					int itype = Integer.parseInt(nodePos[4]);

					if(itype==0) { 	}       // A Source node - do not change position					
					else if (itype==2) {  } // A UAV node - keep current position - changed later					
					else {	                // A User node - update position randomly
						double speed_kmh = classParameters.min_speed_kmh + (classParameters.max_speed_kmh - classParameters.min_speed_kmh) * rand.nextDouble();
						//double deltaDirnRdns = 2.0*Math.PI*rand.nextDouble();
						double speed_X = speed_kmh*Math.cos(dirnRdns);
						double speed_Y = speed_kmh*Math.sin(dirnRdns);												
						double delta_X_km = speed_X*classParameters.deltaT_hr;
						double delta_Y_km = speed_Y*classParameters.deltaT_hr;																   
						x_pos = x_pos + delta_X_km;
						y_pos = y_pos - delta_Y_km;						 
					}				

					double deltaGrid = 10.0;
					x_pos = Math.max(-deltaGrid,x_pos);
					x_pos = Math.min(classParameters.gridSize + deltaGrid,x_pos);
					y_pos = Math.max(-deltaGrid,y_pos);
					y_pos = Math.min(classParameters.gridSize + deltaGrid,y_pos);

					Location loc = new Location(x_pos , y_pos);
					NetworkNode thisNode = new NetworkNode(nodeName, loc, dirnRdns, itype);
					networkNodes.add(thisNode);					
				}
				br.close();		
			}
			// Now change the UAVs' locations based on other nodes' locations
			for(NetworkNode uavNode : networkNodes){
				if (uavNode.itype == 2) {
					Location newLoc = cUAV.getUAVLocation(uavNode, networkNodes);
					uavNode.location = newLoc;
				}
			}

			// Now print out all the node information
			for(NetworkNode thisNode : networkNodes){
				node_Positions.writeToFile(thisNode.name, thisNode.location.x , thisNode.location.y, thisNode.dirn, thisNode.itype);
			}
		}
		/***********************************************************************************************************************************/
		// Create .csv Files 
		LinkedList<Object> files = new LinkedList<Object>();
		createCSVFiles createCSVFiles = new createCSVFiles();
		files = createCSVFiles.createFiles();

		WriteFile NetworkMetricThresholds_Data              = (WriteFile) files.get(++index);
		WriteFile SrcDstPath_Data_QoI                       = (WriteFile) files.get(++index);
		WriteFile SrcDstPath_Data_QoIT                      = (WriteFile) files.get(++index);
		WriteFile computationOverhead_QoI_Data              = (WriteFile) files.get(++index);
		WriteFile computationOverhead_QoIT_Data             = (WriteFile) files.get(++index);
		WriteFile NetworkMetricValues_SelectedSource_QoI    = (WriteFile) files.get(++index);
		WriteFile NetworkMetricValues_SelectedSource_QoIT   = (WriteFile) files.get(++index);
		WriteFile QoImetricScore_SelectedSource_QoIwrtQoIT  = (WriteFile) files.get(++index);
		WriteFile QoImetricScore_SelectedSource_QoITwrtQoIT = (WriteFile) files.get(++index);
		WriteFile PNP_SelectedSource_QoIwrtQoIT_Data        = (WriteFile) files.get(++index);
		WriteFile PNP_SelectedSource_QoITwrtQoIT_Data       = (WriteFile) files.get(++index);
		WriteFile QoISelectedSource_QoIScore_Data           = (WriteFile) files.get(++index);
		WriteFile QoISelectedSource_QoITScore_Data          = (WriteFile) files.get(++index);
		WriteFile QoITSelectedSource_QoIScore_Data           = (WriteFile) files.get(++index);
		WriteFile QoITSelectedSource_QoITScore_Data          = (WriteFile) files.get(++index);
		/***********************************************************************************************************************************/
		// System current time
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");	
		String current_time  = sdf.format(cal.getTime());
		long startTime       = System.nanoTime();

		// calling Seeds generation for random selection of destination-nodes
		classParameters.destNodes_Seed();
		for(int i = 0; i < classParameters.nSteps; i++)//
			System.out.println("step " + i + ": " + classParameters.destNodes_Seed[i]);

		/***********************************************************************************************************************************/
		/*Integer destNode[] = new Integer[classParameters.userNodes];
		int seed_destNodes = classParameters.destNodes_Seed[runStep];
		// Selecting 3 destination-nodes randomly and then selecting the same dst-nodes in each iteration
		for(int networkNode = 0; networkNode < classParameters.userNodes; networkNode++) {
			int tempNetworkNode = (int) (rand.nextDouble()*seed_destNodes);
			if(tempNetworkNode == 0 || tempNetworkNode == 1 || tempNetworkNode == 2 || tempNetworkNode == 3 || tempNetworkNode == 4 || tempNetworkNode == 5 
					|| (contains(destNode, tempNetworkNode) == true) || (tempNetworkNode >= totalNetworkNodes))
				 {networkNode--; continue;}
			else destNode[networkNode] = tempNetworkNode;
		}
		
		for(int i = 0; i < destNode.length; i++) System.out.println(destNode[i]); */
		/***********************************************************************************************************************************/

		while(true){				
			runStep++;
			int numberOfLinks, NumberOfInformationSourcesPerNetworkNode[] = new int[totalNetworkNodes];
			Integer destNode[] = new Integer[totalNetworkNodes];
			int seed_destNodes = classParameters.destNodes_Seed[runStep-1]; 

			sourceSelectionSchemes sourceSelectionSchemes = new sourceSelectionSchemes();
			Graph                  graph                  = new Graph();
			Dijkstra               dijkstra               = new Dijkstra();
			NetworkMetrics         networkMetrics         = new NetworkMetrics();			
			classNode[]            nodes                  = new classNode[totalNetworkNodes];
			/***********************************************************************************************************************************/	
			LinkedList<Object> assignTraffic_QoI  = new LinkedList<Object>();                                  
			LinkedList<Object> assignTraffic_QoIT = new LinkedList<Object>(); 
			LinkedList<Object> obj                = new LinkedList<Object>();
			LinkedList<LinkedList<List<classNode>>> shortestPathsArray_forSelectedSources_toAllDestinations = new LinkedList<LinkedList<List<classNode>>>();
			LinkedList<List<classNode>> shortestPathsArray_forSelectedSources_toCurrentDestination          = new LinkedList<List<classNode>>();
			LinkedList<List<classNode>> overallShortestPathsArray          = new LinkedList<List<classNode>>();			
			LinkedList<LinkedList<HashMap<String,String>>> predecessorList = new LinkedList<LinkedList<HashMap<String,String>>>();

			HashMap<String, classLink> link_CAU_QoI  = new HashMap<String, classLink>();
			HashMap<String, classLink> link_CAU_QoIT = new HashMap<String, classLink>();
			HashMap<String, NetworkMetricThresholdValues> networkMetricThresholdValues = new HashMap<String, NetworkMetricThresholdValues>();

			HashMap<Integer, networkData_HashMaps> nmv_SelectedSource_QoI         = new HashMap<Integer, networkData_HashMaps>(); // Network Metric Values 
			HashMap<Integer, networkData_HashMaps> nmv_SelectedSource_QoIT        = new HashMap<Integer, networkData_HashMaps>();
			HashMap<Integer, networkData_HashMaps> pnp_SelectedSource_QoIwrtQoIT  = new HashMap<Integer, networkData_HashMaps>(); // Priority, Number, Percentage of Quality Metrics met w.r.t QoIT calculation
			HashMap<Integer, networkData_HashMaps> pnp_SelectedSource_QoITwrtQoIT = new HashMap<Integer, networkData_HashMaps>();
			HashMap<Integer, networkData_HashMaps> qms_SelectedSource_QoIwrtQoIT  = new HashMap<Integer, networkData_HashMaps>(); // Quality Metric Score of each Quality Metric w.r.t QoIT calculation
			HashMap<Integer, networkData_HashMaps> qms_SelectedSource_QoITwrtQoIT = new HashMap<Integer, networkData_HashMaps>();
			/***********************************************************************************************************************************/			
			obj = dijkstra.calculateShortestPath(totalNetworkNodes, runStep, application_DataRate);
			overallShortestPathsArray = (LinkedList<List<classNode>>) obj.get(0);
			nodes = (classNode[]) obj.get(1);
			graph = (Graph) obj.get(2);
			numberOfLinks = (Integer) obj.get(3);
			classLink[] links = new classLink[numberOfLinks];
			links = (classLink[]) obj.get(4);
			predecessorList = (LinkedList<LinkedList<HashMap<String,String>>>) obj.get(5);
			link_CAU_QoI = (HashMap<String, classLink>) obj.get(6);
			link_CAU_QoIT = (HashMap<String, classLink>) obj.get(7);
			int twoHopNeighbors_Count[] = (int[]) obj.get(8);
			LinkedList<LinkedList<Object>> twoHopNeighbors_List = (LinkedList<LinkedList<Object>>) obj.get(9);

			// Finding number of information-sources for all User-Nodes and storing their respective shortest paths 
			for(int dst = 0; dst < totalNetworkNodes; dst++){
				sourceCounter  = 0;
				final int dist = dst;

				// Avoiding source-nodes from becoming destination-nodes
				if(IntStream.of(classParameters.sourceNodeList).anyMatch(x -> x == dist) == true) { 
					NumberOfInformationSourcesPerNetworkNode[dst] = sourceCounter; // 0
					shortestPathsArray_forSelectedSources_toAllDestinations.add(shortestPathsArray_forSelectedSources_toCurrentDestination); // empty list i.e. []
				}
				else {
					for(int src = 0; src < classParameters.sourceNodeList.length; src++){
						if((dst != src) && (overallShortestPathsArray.get((dst*totalNetworkNodes)+src) != null)){
							shortestPathsArray_forSelectedSources_toCurrentDestination.add(overallShortestPathsArray.get((dst*totalNetworkNodes)+src));	
							sourceCounter++;
						}
					}

					NumberOfInformationSourcesPerNetworkNode[dst] = sourceCounter;
					LinkedList<List<classNode>> clone = (LinkedList<List<classNode>>) shortestPathsArray_forSelectedSources_toCurrentDestination.clone();
					shortestPathsArray_forSelectedSources_toAllDestinations.add(clone);
					shortestPathsArray_forSelectedSources_toCurrentDestination.clear();	
				}
			}
			/***********************************************************************************************************************************/	
			// Selecting destination-node randomly i.e. 8, 54, 13, 21, 39, 27 etc. 
			for(int networkNode = 0; networkNode < totalNetworkNodes; networkNode++) {
				int tempNetworkNode = (int) (rand.nextDouble()*seed_destNodes);
				if(networkNode == 0) {
					if(tempNetworkNode < totalNetworkNodes)   destNode[networkNode] = tempNetworkNode;
					else 				        			  networkNode--;						
				}			
				else{					
					if((tempNetworkNode >= totalNetworkNodes) || (contains(destNode, tempNetworkNode) == true))   { networkNode--; continue; }  // if node already exists
					else                       																	  destNode[networkNode] = tempNetworkNode;
				}
			}
			/***********************************************************************************************************************************/
			// selecting Fixed Network Metric Threshold Values for different Users
			for(int networkNode = 0; networkNode < totalNetworkNodes; networkNode++) {
			/*for(int networkNode = 0; networkNode < classParameters.userNodes; networkNode++) {*/
				if(contains(srcNodeList, destNode[networkNode]) == false)
					networkMetrics.networkMetricThresholdValues_All_Fixed(current_time, destNode[networkNode], NetworkMetricThresholds_Data, networkMetricThresholdValues, runStep);
			}
			/***********************************************************************************************************************************/
			// SOURCE SELECTION AND TRAFFIC ASSIGNMENT
			for(int networkNode = 0; networkNode < totalNetworkNodes; networkNode++) {

				HashMap<Integer, QualityMetricScores> QMS_QoIwrtQoIT_temp = new HashMap<Integer, QualityMetricScores>();
				HashMap<Integer, QualityMetricScores> QMS_QoITwrtQoIT_temp = new HashMap<Integer, QualityMetricScores>();

				if(contains(srcNodeList, destNode[networkNode]) == false) {
				/*if(networkNode < classParameters.userNodes && contains(srcNodeList, destNode[networkNode]) == false) {*/
					if(NumberOfInformationSourcesPerNetworkNode[destNode[networkNode]] != 0){
						HashMap<String,String> predecessors = null;
						noofSources = NumberOfInformationSourcesPerNetworkNode[destNode[networkNode]];			    // Number of information-sources w.r.t a network-node.
						int destinationNode = destNode[networkNode], hopCount = 0, selectedSourceNode, iteration = runStep;
						int informationSourceList[] = new int[noofSources];
						int sourceList[] = new int[noofSources]; 
						double assignedBandwidth = 0.00, linkIntegrity = 0.00, informationUtility = 0.0;
						double NetworkMetricValues[][] = new double[noofcriteria][noofSources];					    // Information-Source Network Metric Values 
						double SourceComparisonMatrixWRTNetworkMetric[][] = new double[noofSources][noofSources];	// Source Comparison Matrix w.r.t each Network Metric
						// double relationshipWeights[] = {0.1667, 0.8333, 0.25, 0.75, 0.9, 0.1, 0.875, 0.125};	    // A,C,T,R Chen defined Weights:--> T>C>A>R, in order: "A,C,T,R"					 
						double relationshipWeights[] = {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
						//                           A = PI + IU, C = BW + IU, T = BW + HC, R = PI + HC
						//double relationshipWeights[] = {0.35, 0.65, 0.3, 0.7, 0.6, 0.4, 0.7, 0.3};								

						//Available Information-Sources for a particular user-node//
						for(int l = 0; l < NumberOfInformationSourcesPerNetworkNode[destNode[networkNode]]; l++){
							int pathToSource_Size = shortestPathsArray_forSelectedSources_toAllDestinations.get(destNode[networkNode]).get(l).size()-1;
							informationSourceList[l] = shortestPathsArray_forSelectedSources_toAllDestinations.get(destNode[networkNode]).get(l).get(pathToSource_Size).getSourceLabel();
							sourceList[l] = shortestPathsArray_forSelectedSources_toAllDestinations.get(destNode[networkNode]).get(l).get(pathToSource_Size).getSourceLabel();
						}
						// over HERE: informationSourceList and sourceList both ordered w.r.t sources (0-5)

						//**************************************************************************************************************************************//
						if(classParameters.print_condition) System.out.println("\nSource Selection w.r.t QoI-scheme\n");
						//**************************************************************************************************************************************//
						// 1. QoI Scheme: Source Selection and Traffic Assignment
						// 1(a). QoI Scheme: Source Selection   // HashMap<Integer, networkData_HashMaps> qms_SelectedSource_QoIwrtQoIT
						assignTraffic_QoI = sourceSelectionSchemes.sourceSelection_QoI(noofcriteria, QoIMetricPairwiseComparisons, NetworkMetricValues, noofSources, totalNetworkNodes, informationSourceList, destinationNode, graph, nodes, links, 
								nodeKeys, SourceComparisonMatrixWRTNetworkMetric, relationshipWeights, SrcDstPath_Data_QoI, current_time, shortestPathsArray_forSelectedSources_toAllDestinations, /*runStep,*/ 1, fileNumber, link_CAU_QoI, application_DataRate, 																																							  
								networkMetricThresholdValues, computationOverhead_QoI_Data, selectedSourceList_QoI, flag_updatedInfo, srcDstRouteMap_QoI_updated, srcDstRouteMap_QoI, QMS_QoIwrtQoIT_temp, pnp_SelectedSource_QoIwrtQoIT, qms_SelectedSource_QoIwrtQoIT,
								QoICriteriaWeights, priority_of_QualityMetrics, iteration, QoISelectedSource_QoIScore_Data, QoISelectedSource_QoITScore_Data); 

						// 1(b). QoI Scheme: Traffic Assignment
						selectedSourceNode = (int) assignTraffic_QoI.get(0);
						if(contains(srcNodeList, selectedSourceNode) == true) {
							predecessors       = (HashMap<String,String>) assignTraffic_QoI.get(1);				
							assignedBandwidth  = (double)  assignTraffic_QoI.get(2);
							hopCount           = (Integer) assignTraffic_QoI.get(3);
							linkIntegrity      = (double)  assignTraffic_QoI.get(4);
							informationUtility = (double)  assignTraffic_QoI.get(5);

							boolean flag_sameAs_previousPath = false;
							LinkedHashSet<Integer> srcDstRouteNodes_updated  = srcDstRouteMap_QoI_updated.get(destinationNode), srcDstRouteNodes_previous = srcDstRouteMap_QoI.get(destinationNode); 																									
							Iterator<Integer> srcDstUpdatedRoute_Iterator = srcDstRouteNodes_updated.iterator(), srcDstPreviousRoute_Iterator = srcDstRouteNodes_previous.iterator();

							if(srcDstRouteNodes_updated.size() == srcDstRouteNodes_previous.size()) {
								for(int i = 0; i < srcDstRouteNodes_previous.size(); i++) {				
									if(srcDstUpdatedRoute_Iterator.next() == srcDstPreviousRoute_Iterator.next())  flag_sameAs_previousPath = true;
									else                                                                         { flag_sameAs_previousPath = false; break; }
								}																																										
							}

							if (flag_sameAs_previousPath == true) {                 // whether flag_updatedInfo = True/False
								if(Double.compare(assignedBandwidth, 0.00) == 0)    // if selected Src-Dst path contains a link with BW = 0.
									nmv_SelectedSource_QoI.put(destinationNode, new networkData_HashMaps(runStep, destinationNode, selectedSourceNode, assignedBandwidth, hopCount, linkIntegrity, informationUtility));
								else                                                // assign Traffic along path-links, if "selected Src-Dst path BW > 0"
									networkMetrics.addTrafficToNetwork(selectedSourceNode, graph, Integer.toString(destinationNode), predecessors, link_CAU_QoI, hopCount, linkIntegrity, informationUtility, assignedBandwidth, 
											runStep, nmv_SelectedSource_QoI);	
							}
							else nmv_SelectedSource_QoI.put(destinationNode, new networkData_HashMaps(runStep, destinationNode, selectedSourceNode, 0, 0, 0, 0));	
						}
						else if(selectedSourceNode == -1) { nmv_SelectedSource_QoI.put(destinationNode, new networkData_HashMaps(runStep, destinationNode, selectedSourceNode, 0, 0, 0, 0)); }

						/**************************************************************************************************************************************/
						if(classParameters.print_condition) System.out.println("\nSource Selection w.r.t QoIT-scheme\n");
						/**************************************************************************************************************************************/
						// From HERE onwards: informationSourceList ordered w.r.t QoI scores (ascending to descending), sourceList ordered w.r.t sources (0-5)
						// 2. QoIT Scheme: Source Selection and Traffic Assignment
						// 2(a). QoIT Scheme: Source Selection		
						assignTraffic_QoIT = sourceSelectionSchemes.sourceSelection_QoIT(noofcriteria, noofSources, totalNetworkNodes, informationSourceList, destinationNode, graph, nodes, links, nodeKeys, SrcDstPath_Data_QoIT, 
								current_time, /*runStep,*/ 1, shortestPathsArray_forSelectedSources_toAllDestinations, fileNumber, link_CAU_QoIT, application_DataRate, networkMetricThresholdValues, relationshipWeights, QoIMetricPairwiseComparisons, 
								computationOverhead_QoIT_Data, selectedSourceList_QoIT, flag_updatedInfo, srcDstRouteMap_QoIT_updated, srcDstRouteMap_QoIT, QMS_QoITwrtQoIT_temp, pnp_SelectedSource_QoITwrtQoIT, qms_SelectedSource_QoITwrtQoIT,
								QoICriteriaWeights, priority_of_QualityMetrics, iteration, QoITSelectedSource_QoIScore_Data, QoITSelectedSource_QoITScore_Data);

						// 2(b). QoIT Scheme: Traffic Assignment			
						selectedSourceNode = (int) assignTraffic_QoIT.get(0);
						if(contains(srcNodeList, selectedSourceNode) == true) { 
							predecessors       = (HashMap<String,String>) assignTraffic_QoIT.get(1);				
							assignedBandwidth  = (double)  assignTraffic_QoIT.get(2);
							hopCount           = (Integer) assignTraffic_QoIT.get(3);
							linkIntegrity      = (double)  assignTraffic_QoIT.get(4);
							informationUtility = (double)  assignTraffic_QoIT.get(5);

							boolean flag_sameAs_previousPath = false;
							LinkedHashSet<Integer> srcDstRouteNodes_updated  = srcDstRouteMap_QoIT_updated.get(destinationNode), srcDstRouteNodes_previous = srcDstRouteMap_QoIT.get(destinationNode); 																									 
							Iterator<Integer> srcDstUpdatedRoute_Iterator = srcDstRouteNodes_updated.iterator(), srcDstPreviousRoute_Iterator = srcDstRouteNodes_previous.iterator();

							if(srcDstRouteNodes_updated.size() == srcDstRouteNodes_previous.size()) {
								for(int i = 0; i < srcDstRouteNodes_previous.size(); i++) {				
									if(srcDstUpdatedRoute_Iterator.next() == srcDstPreviousRoute_Iterator.next())  flag_sameAs_previousPath = true;
									else                                                                         { flag_sameAs_previousPath = false; break; }
								}																																										 
							}

							if (flag_sameAs_previousPath == true) {                 // whether flag_updatedInfo = True/False
								if(Double.compare(assignedBandwidth, 0.00) == 0)    // if selected Src-Dst path contains a link with BW = 0.
									nmv_SelectedSource_QoIT.put(destinationNode, new networkData_HashMaps(runStep, destinationNode, selectedSourceNode, assignedBandwidth, hopCount, linkIntegrity, informationUtility));
								else                                                // assign Traffic along path-links, if "selected Src-Dst path BW > 0"
									networkMetrics.addTrafficToNetwork(selectedSourceNode, graph, Integer.toString(destinationNode), predecessors, link_CAU_QoIT, hopCount, linkIntegrity, informationUtility, assignedBandwidth, 
											runStep, nmv_SelectedSource_QoIT);	
							}
							else nmv_SelectedSource_QoIT.put(destinationNode, new networkData_HashMaps(runStep, destinationNode, selectedSourceNode, 0, 0, 0, 0));
						}											 
						else if(selectedSourceNode == -1) { nmv_SelectedSource_QoIT.put(destinationNode, new networkData_HashMaps(runStep, destinationNode, selectedSourceNode, 0, 0, 0, 0)); }	   
					}	 

					// -2: No Available Source; -1: Source doesn't meet User's NMTV	
					else {
						int nodeType = -2; // User/Relay Node  // -1 = Source/Relay Node
						nmv_SelectedSource_QoI.put(destNode[networkNode], new networkData_HashMaps(runStep, destNode[networkNode], nodeType, 0, 0, 0, 0));
						nmv_SelectedSource_QoIT.put(destNode[networkNode], new networkData_HashMaps(runStep, destNode[networkNode], nodeType, 0, 0, 0, 0));
						pnp_SelectedSource_QoIwrtQoIT.put(destNode[networkNode], new networkData_HashMaps(runStep, destNode[networkNode], nodeType, 0, 0, 0));
						pnp_SelectedSource_QoITwrtQoIT.put(destNode[networkNode], new networkData_HashMaps(runStep, destNode[networkNode], nodeType, 0, 0, 0));
						qms_SelectedSource_QoIwrtQoIT.put(destNode[networkNode], new networkData_HashMaps(runStep, destNode[networkNode], nodeType, 0.0, 0.0, 0.0, 0.0));
						qms_SelectedSource_QoITwrtQoIT.put(destNode[networkNode], new networkData_HashMaps(runStep, destNode[networkNode], nodeType, 0.0, 0.0, 0.0, 0.0));
						SrcDstPath_Data_QoI.writeToFile(runStep, Integer.toString(destNode[networkNode]), nodeType);
						SrcDstPath_Data_QoIT.writeToFile(runStep, Integer.toString(destNode[networkNode]), nodeType);
					}
				}
			} 

			if(runStep < classParameters.nSteps) {	// 30															   
				fileNumber++;
				flag_updatedInfo = true;
				assignTraffic_QoI.clear(); assignTraffic_QoIT.clear();

				createCSVFiles.storeOutput_CSVFiles(nmv_SelectedSource_QoI, NetworkMetricValues_SelectedSource_QoI, nmv_SelectedSource_QoIT, NetworkMetricValues_SelectedSource_QoIT, pnp_SelectedSource_QoIwrtQoIT, PNP_SelectedSource_QoIwrtQoIT_Data, 
						pnp_SelectedSource_QoITwrtQoIT, PNP_SelectedSource_QoITwrtQoIT_Data, qms_SelectedSource_QoIwrtQoIT, QoImetricScore_SelectedSource_QoIwrtQoIT, qms_SelectedSource_QoITwrtQoIT, QoImetricScore_SelectedSource_QoITwrtQoIT);

				link_CAU_QoI.clear(); link_CAU_QoIT.clear();
				System.out.println("Iteration " + runStep + " completed."); 
				continue;
			}
			else { 
				createCSVFiles.storeOutput_CSVFiles(nmv_SelectedSource_QoI, NetworkMetricValues_SelectedSource_QoI, nmv_SelectedSource_QoIT, NetworkMetricValues_SelectedSource_QoIT, pnp_SelectedSource_QoIwrtQoIT, PNP_SelectedSource_QoIwrtQoIT_Data, 
						pnp_SelectedSource_QoITwrtQoIT, PNP_SelectedSource_QoITwrtQoIT_Data, qms_SelectedSource_QoIwrtQoIT, QoImetricScore_SelectedSource_QoIwrtQoIT, qms_SelectedSource_QoITwrtQoIT, QoImetricScore_SelectedSource_QoITwrtQoIT);

				System.out.println("Iteration " + runStep + " completed.");

				long endTime = System.nanoTime();
				long elapsedTime  = (endTime - startTime);
				double seconds = (double)elapsedTime / 1000000000.0;
				double minutes = (double) seconds / 60.0;																	 
				PrintWriter out = new PrintWriter("elapsedTime.txt");
				out.println(minutes);
				out.close();
				System.out.println("Elapsed Time = " + minutes + " min");
				System.exit(0);
			}
		}
	}

	// Checking if Network-Scenario has Changed?
	public static boolean networkScenario(int runStep) {
		boolean condition = true;
		if(runStep == 1)			                    condition = true;
		else if(runStep <= classParameters.nSteps)		condition = false; //30
		return condition;
	}
 
	// Varying Quality Metric Weights
	/*	public double[] QoICriteriaWeights(int destinatioNode, WriteFile QualityMetricWeight) throws IOException{
		//                    		    A,   C,   T,   R
		double QoICriteriaWeights[] = new double[4]; 
		double min = 0.0, max = 1.0;
		Random rand = new Random();
		QoICriteriaWeights[0] = rand.nextDouble() * (max - min) + min;
		max = 1.0 - QoICriteriaWeights[0] ;
		QoICriteriaWeights[1] = Math.random() * (max - min) + min;
		max = 1.0 - QoICriteriaWeights[0] - QoICriteriaWeights[1];
		QoICriteriaWeights[2] = Math.random() * (max - min) + min;
		QoICriteriaWeights[3] = 1.0 - QoICriteriaWeights[0] - QoICriteriaWeights[1] - QoICriteriaWeights[2];

		System.out.println(QoICriteriaWeights[0] + ", " + QoICriteriaWeights[1] + ", " + QoICriteriaWeights[2] + ", " + QoICriteriaWeights[3]);
		System.out.println(QoICriteriaWeights[0] + QoICriteriaWeights[1] + QoICriteriaWeights[2] + QoICriteriaWeights[3]); 
		QualityMetricWeight.writeToFile(destinatioNode, QoICriteriaWeights[0], QoICriteriaWeights[1], QoICriteriaWeights[2], QoICriteriaWeights[3]);

		return QoICriteriaWeights;	
	}*/


	// Hard coded Quality Metric weights. 
	public double[] QoICriteriaWeights(){
		//                    		    A,   C,   T,   R
		double QoICriteriaWeights[] = {0.4, 0.2, 0.1, 0.3};  //Vector Matrix of QoI Criteria Weights
		return QoICriteriaWeights;	
	}


	// Part(A): QoI Criteria Weights Calculation
	/*	public double[] QoICriteriaWeights(double[] QoIMetricPairwiseComparisons, int n){

		ahpFormulation ahp = new ahpFormulation();
		double comparisonMatrix[][] = ahp.comparisonMatrix(QoIMetricPairwiseComparisons, n); 									//Comparison Matrix
		double columnAddComparisonMatrix[] = ahp.columnAddComparisonMatrix(comparisonMatrix, n);								//Adding-up columns of Comparison Matrix
		double normalizedComparisonMatrix[][] = ahp.normalizedComparisonMatrix(comparisonMatrix,columnAddComparisonMatrix, n);  //Normalized Comparison Matrix
		double weightsOfComparisonMatrix[] = ahp.weightsOfComparisonMatrix(normalizedComparisonMatrix, n); 						//Weights of Comparison Matrix		
		double weightedSumMatrix[] = ahp.weightedSumMatrix(comparisonMatrix, weightsOfComparisonMatrix, n); 					//Weighted Sum Matrix
		double consistencyVectorMatrix[] = ahp.consistencyVectorMatrix(weightedSumMatrix, weightsOfComparisonMatrix, n); 		//Consistency Vector
		double lembda = ahp.lembda(consistencyVectorMatrix, n); 																//Eigen Value
		double consistencyIndex = ahp.consistencyIndex(lembda, n); 																//Consistency Index
		double randomIndex = ahp.randomIndex(n);
		double consistencyRatio = ahp.consistencyRatio(consistencyIndex, randomIndex); 													//Consistency Ratio
		boolean consistencyCheck = ahp.consistencyCheck(consistencyRatio);														//Checking Consistency of Comparison Matrix

		/*	if(consistencyCheck == true){
			if(classParameters.print_condition) System.out.println("Comparison Matric is consistent.\n");
		}
		else{
			if(classParameters.print_condition) System.out.println("Comparison Matric is NOT consistent. Recalculate values of comparison matrix.\n");
		}*
		return weightsOfComparisonMatrix;	//Vector Matrix of QoI Criteria Weights
	}*/

	// Calculation of Network Criteria Weights w.r.t all Information-Sources
	public static double[] NetworkCriteriaWeightsWRTSources(double[][] SourceComparisonMatrixWRTNetworkMetric, int source){

		ahpFormulation ahp = new ahpFormulation();
		double columnAddComparisonMatrix[] = ahp.columnAddComparisonMatrix(SourceComparisonMatrixWRTNetworkMetric, source);									//Adding-up columns of Comparison Matrix
		double normalizedComparisonMatrix[][] = ahp.normalizedComparisonMatrix(SourceComparisonMatrixWRTNetworkMetric,columnAddComparisonMatrix, source); 	//Normalized Comparison Matrix
		double weightsOfComparisonMatrix[] = ahp.weightsOfComparisonMatrix(normalizedComparisonMatrix, source); 											//Weights of Comparison Matrix
		double weightedSumMatrix[] = ahp.weightedSumMatrix(SourceComparisonMatrixWRTNetworkMetric, weightsOfComparisonMatrix, source); 						//Weighted Sum Matrix
		double consistencyVectorMatrix[] = ahp.consistencyVectorMatrix(weightedSumMatrix, weightsOfComparisonMatrix, source); 								//Consistency Vector
		double lembda = ahp.lembda(consistencyVectorMatrix, source); 																						//Eigen Value
		double consistencyIndex = ahp.consistencyIndex(lembda, source); 																					//Consistency Index
		double randomIndex = ahp.randomIndex(source);
		double consistencyRatio = ahp.consistencyRatio(consistencyIndex, randomIndex);																			//Consistency Ratio
		boolean consistencyCheck = ahp.consistencyCheck(consistencyRatio); 																					//Checking Consistency of Comparison Matrix

		return weightsOfComparisonMatrix;	//Individual Network Criteria Values w.r.t all Information-Sources
	}

	// Part(B): Selecting optimal QoI Information-Source
	public LinkedList<Object> QoIBasedSourceSelection(double[][] networkMetricNormalized, double[][] NetworkMetricValues, double[][] SourceComparisonMatrixWRTNetworkMetric, double[] relationshipWeights, double[] QoICriteriaWeights, 
			int source, int n, int[] informationSourceList, int destinationNode, WriteFile SrcDstPath_Data_QoI, Graph graph, classNode[] nodes, LinkedList<List<classNode>> shortestPathsArray_forSelectedSources, String current_time,
			HashMap<String, classLink> link_data, HashMap<Integer, selectedSourceInformation> selectedSourceList_QoI, int runStep, Map<Integer, LinkedHashSet<Integer>> 
			srcDstRouteMap_QoI_updated, Map<Integer, LinkedHashSet<Integer>> srcDstRouteMap_QoI, boolean flag_updatedInfo, WriteFile OverallQoIScore_Data) throws IOException{

		ahpFormulation ahp = new ahpFormulation();
		int selectedSource = 0;
		double AlternativePriorityVector[] = new double[source];						//Vector Matrix consisting Individual Network Criteria Weights w.r.t all Information-Sources
		double SourceVsNetworkMetricWeights[][] = new double[source][n];				//Combined Matrix of Individual Alternative Priority Vectors
		double SourceVsNetworkMetricRelationshipWeights[][] = new double[source][n];	//Matrix achieved by (relationship weights)*(SourceVsNetworkMetricWeights)
		double SourceQoIScores[] = new double[source];									//Vector Matrix containing QoI Score of each Information-Source
		LinkedList<Object> allSrcsQoIScore_andSelectedSource = new LinkedList<Object>();

		// Calculating Alternative Priority Vectors w.r.t each Network Metric
		for(int i=0; i<n; i++)
		{
			SourceComparisonMatrixWRTNetworkMetric = ahp.sourceComparisonMatrixWRTNetworkMetric(networkMetricNormalized, SourceComparisonMatrixWRTNetworkMetric, source, i);//Source Comparison Matrix w.r.t Network Metric
			AlternativePriorityVector = NetworkCriteriaWeightsWRTSources(SourceComparisonMatrixWRTNetworkMetric, source); //Returns Weights of Comparison Matrix
			for(int j=0; j<source; j++)
				SourceVsNetworkMetricWeights[j][i] = AlternativePriorityVector[j];	
		}

		// Calculating Source Vs Network Metric Relationship Weights
		SourceVsNetworkMetricRelationshipWeights = ahp.sourceVsNetworkMetricRelationshipWeights(SourceVsNetworkMetricRelationshipWeights, SourceVsNetworkMetricWeights, relationshipWeights, source, n);

		// Calculating Relative Source QoI Scores
		SourceQoIScores = ahp.sourceQoIScores(SourceVsNetworkMetricRelationshipWeights, QoICriteriaWeights, SourceQoIScores, source, n, informationSourceList, destinationNode);

		// Best QoI Source Selection
		try {
			selectedSource = ahp.selectedQoISource(NetworkMetricValues, SourceQoIScores, selectedSource, source, informationSourceList, destinationNode, SrcDstPath_Data_QoI, graph, nodes, 
					shortestPathsArray_forSelectedSources, current_time, link_data, selectedSourceList_QoI, runStep, srcDstRouteMap_QoI_updated, srcDstRouteMap_QoI, flag_updatedInfo, OverallQoIScore_Data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ahp.sourceQoIScoreDecsendingOrder(SourceQoIScores, informationSourceList, current_time, destinationNode);

		allSrcsQoIScore_andSelectedSource.add(selectedSource);
		allSrcsQoIScore_andSelectedSource.add(SourceQoIScores);
		allSrcsQoIScore_andSelectedSource.add(informationSourceList);
		return allSrcsQoIScore_andSelectedSource; // Selected Information Source
	}

	// Part(B): Selecting optimal QoI Information-Source
	public double[] QoIscore_ofSelectedSourcewrtQoITScheme(double[][] networkMetricNormalized, double[][] SourceComparisonMatrixWRTNetworkMetric, double[] relationshipWeights, double[] QoICriteriaWeights, int source, int n,
			int[] informationSourceList, int destination, String current_time) throws IOException{

		ahpFormulation ahp = new ahpFormulation();
		double AlternativePriorityVector[] = new double[source];						//Vector Matrix consisting Individual Network Criteria Weights w.r.t all Information-Sources
		double SourceVsNetworkMetricWeights[][] = new double[source][n];				//Combined Matrix of Individual Alternative Priority Vectors
		double SourceVsNetworkMetricRelationshipWeights[][] = new double[source][n];	//Matrix achieved by (relationship weights)*(SourceVsNetworkMetricWeights)
		double SourceQoIScores[] = new double[source];									//Vector Matrix containing QoI Score of each Information-Source

		// Calculating Alternative Priority Vectors w.r.t each Network Metric
		for(int i = 0; i < n; i++)
		{
			SourceComparisonMatrixWRTNetworkMetric = ahp.sourceComparisonMatrixWRTNetworkMetric(networkMetricNormalized, SourceComparisonMatrixWRTNetworkMetric, source, i);//Source Comparison Matrix w.r.t Network Metric
			AlternativePriorityVector = NetworkCriteriaWeightsWRTSources(SourceComparisonMatrixWRTNetworkMetric, source); //Returns Weights of Comparison Matrix
			for(int j=0; j<source; j++)
				SourceVsNetworkMetricWeights[j][i] = AlternativePriorityVector[j];	
		}

		// Calculating Source Vs Network Metric Relationship Weights
		SourceVsNetworkMetricRelationshipWeights = ahp.sourceVsNetworkMetricRelationshipWeights(SourceVsNetworkMetricRelationshipWeights, SourceVsNetworkMetricWeights, relationshipWeights, source, n);

		// Calculating Relative Source QoI Scores
		SourceQoIScores = ahp.sourceQoIScores(SourceVsNetworkMetricRelationshipWeights, QoICriteriaWeights, SourceQoIScores, source, n, informationSourceList, destination);
		
/*		for(int i = 0; i < informationSourceList.length; i++) {
			System.out.println(informationSourceList[i] + ", " + SourceQoIScores[i]);
		}*/
		
		return SourceQoIScores; // Selected Information Source
	}

	// Assigning values to QoIMetricPairwiseComparisons Matrix
	public static double[] QoIMetricPairwiseComparisons(double[] QoIMetricPairwiseComparisons, int n) {
		Scanner scanner = new Scanner(System.in);

		if(classParameters.print_condition) System.out.print("Enter the Comparison Matrix Values: \n");
		for (int i=0; i<QoIMetricPairwiseComparisons.length; i++)
		{
			QoIMetricPairwiseComparisons[i] = scanner.nextDouble();
		}
		scanner.close();
		return QoIMetricPairwiseComparisons;
	}

	public static int currentNetworkLinks(LinkedList<List<classNode>> overallShortestPathsArray, int totalNetworkNodes,
			String current_time, classNode[] nodes, Graph graph, int[] sourceNodeList, HashMap<String, classLink> link_data) throws Exception{
		int hopRange = 0;
		int hopCount = 0;
		NetworkMetrics networkMetrics = new NetworkMetrics();

		for(int destinationNode = 0; destinationNode < totalNetworkNodes; destinationNode++){			
			for(int sourceNode = 0; sourceNode < sourceNodeList.length; sourceNode++){

				final int dest = destinationNode;

				if((IntStream.of(sourceNodeList).anyMatch(x -> x == dest))==true)
					break;				
				else if(overallShortestPathsArray.get((destinationNode*totalNetworkNodes)+sourceNode)!= null){
					if(classParameters.print_condition) System.out.println("Network Path from " + destinationNode + "-" + sourceNodeList[sourceNode] + ": " + overallShortestPathsArray.get((destinationNode*totalNetworkNodes)+sourceNode));	 

					// to calculate: hopRange = hopCount of longest src-dst path in network
					hopCount = networkMetrics.hopCount(Integer.toString(destinationNode), graph, nodes, sourceNodeList[sourceNode], link_data);
					if(hopCount > hopRange)
						hopRange = hopCount;
				}
			}				
		}
		return hopRange;
	}

	// Data-Rate of each Source-Node Application
	public static double assignRequestedDataRatestoApplication(double[] applicationDataRatesPool){
		Random rand = new Random();
		double applicationRequestedDataRate = applicationDataRatesPool[rand.nextInt(applicationDataRatesPool.length)];
		return applicationRequestedDataRate;
	}

	public static boolean contains(Integer[] arr, int item) {
		List<Integer> list = Arrays.asList(arr);
		return list.contains(item);
	}
}
