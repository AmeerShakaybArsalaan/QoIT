
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ahpFormulation {
	int k = 0, n = 4;
	double eigenValue = 0, CI, RI, CR, CR_Threshold = 0.1;
	boolean consistency;

	//double RI[] = {0,0,0.58,0.9,1.12,1.24,1.32,1.41,1.45,1.49,1.52,1.54,1.56,1.58,1.59,1.59,1.60,1.61,1.62,1.63,1.64,1.64,1.65,1.65,1.66,1.66,
	//1.67,1.67,1.67,1.68,1.68,1.68,1.68,1.69,1.69,1.69,1.69,1.70,1.70};

	double[][] Comparison_Matrix; 					//Comparison Matrix
	double[] Total_TACR;							//Adding-up each column of Comparison Matrix
	double[][] Normalized_Comparison_Matrix;		//Normalized Comparison Matrix
	double[] Weights_Comparison_Matrix; 			//Weights of Comparison Matrix
	double[] Weighted_Sum_Matrix;					//Weighted Sum Matrix
	double[] Consistency_Vector_Matrix;				//(Weighted Sum Matrix).1/(Weights of Comparison Matrix)

	// Constructing Comparison Matrix
	public double[][] comparisonMatrix(double[] pairwiseComparisons, int n){
		Comparison_Matrix = new double [n][n];

		for (int i=0; i<n; i++)
			for (int j=0; j<n; j++)
			{
				Comparison_Matrix[i][j]= pairwiseComparisons[k];
				k++;	
			}

		return Comparison_Matrix;
	}

	// Adding-up each column of Comparison Matrix
	public double[] columnAddComparisonMatrix(double[][] comparisonMatrix, int n){
		Total_TACR = new double [n]; 

		for (int i=0; i<n; i++)
			for (int j=0; j<n; j++)
			{
				Total_TACR[i] += comparisonMatrix[j][i];
			}

		return Total_TACR;
	}

	// Normalizing Comparison Matrix
	public double[][] normalizedComparisonMatrix(double[][] comparisonMatrix, double[] columnAddComparisonMatrix, int n){
		Normalized_Comparison_Matrix = new double [n][n]; 	

		for (int i=0; i<n; i++)
			for (int j=0; j<n; j++)
			{
				Normalized_Comparison_Matrix[j][i] = comparisonMatrix[j][i]/columnAddComparisonMatrix[i];	
			}

		return Normalized_Comparison_Matrix;
	}

	// Averaging rows of Normalized Comparison Matrix to get Weights of Comparison Matrix
	public double[] weightsOfComparisonMatrix(double[][] normalizedComparisonMatrix, int n){
		Weights_Comparison_Matrix = new double [n];

		for (int i=0; i<n; i++)
		{
			for (int j=0; j<n; j++)
			{
				Weights_Comparison_Matrix[i] += normalizedComparisonMatrix[i][j];	
			}
			Weights_Comparison_Matrix[i] = Weights_Comparison_Matrix[i]/n;
		}

		/*	if(classParameters.print_condition) System.out.println("\nWeights of Comparison Matrix:");		
		for (int i=0;i<n;i++)
			if(classParameters.print_condition) System.out.println(Weights_Comparison_Matrix[i] + " ");
		if(classParameters.print_condition) System.out.println();*/
		return Weights_Comparison_Matrix;
	}

	// Checking Consistency of Comparison Matrix

	// Calculating Weighted Sum Matrix 
	public double[] weightedSumMatrix(double[][] comparisonMatrix, double[] weightsOfComparisonMatrix, int n){
		Weighted_Sum_Matrix = new double [n];

		for (int i=0; i<n; i++)
		{
			for (int j=0; j<n; j++)
			{
				Weighted_Sum_Matrix[i] += comparisonMatrix[i][j]*weightsOfComparisonMatrix[j];	
			}
		}

		return Weighted_Sum_Matrix;
	}

	// Calculating Consistency Vector
	public double[] consistencyVectorMatrix(double[] weightedSumMatrix, double[] weightsOfComparisonMatrix, int n){
		Consistency_Vector_Matrix = new double [n];
		for (int i=0; i<n; i++)
		{
			Consistency_Vector_Matrix[i] = weightedSumMatrix[i]/weightsOfComparisonMatrix[i];	
		}
		return Consistency_Vector_Matrix;
	}

	//Calculating Eigen Value i.e. lembda
	public double lembda(double[] consistencyVectorMatrix, int n){
		for (int i=0; i<n; i++)
		{
			eigenValue += consistencyVectorMatrix[i];
			if(i == n-1)
				eigenValue = eigenValue/n;
		}	
		return eigenValue;
	}	

	// Consistency Index
	public double consistencyIndex(double lembda, int n){
		CI = (lembda-n)/(n-1);
		//if(classParameters.print_condition) System.out.println("CI: " + CI);
		return CI;
	}

	// Random Index
	public double randomIndex(int n){
		if(n==1 || n==2) {
			RI = 0.0;
			//if(classParameters.print_condition) System.out.println("Random Index for " + n + " = " + RI);
			return RI;
		}

		else {
			double lembdaMax = (2.7699*n)-4.3513;		
			RI = (lembdaMax-n)/(n-1);
			//if(classParameters.print_condition) System.out.println("Random Index for " + n + " = " + RI);
			return RI;
		}
	}

	// Consistency Ratio
	public double consistencyRatio(double consistencyIndex, double randomIndex){
		CR = consistencyIndex/randomIndex;
		//if(classParameters.print_condition) System.out.println("Consistency Ratio = "+CR);
		return CR;
	}

	// Consistency Check
	public boolean consistencyCheck(double consistencyRatio){
		if(consistencyRatio<CR_Threshold){
			consistency = true;
			//if(classParameters.print_condition) System.out.println("Consistency = "+consistency);
			return consistency;
		}
		else{
			consistency = false;
			//if(classParameters.print_condition) System.out.println(consistency);
			return consistency;
		}
	}

	// Network_Metric/Range to get relative values
	//	public double[][] networkMetricNormalization(double[][] NetworkMetricValues, double[] NetworkMetricRange, int source, int n){

	public double[][] networkMetricNormalization(double[][] NetworkMetricValues, int noofsources, int noofcriteria, Graph graph, classNode[] nodes, HashMap<String, classLink> link_data, classLink[] links, int runStep, int totalNetworkNodes, 
			HashMap<Integer, ApplicationDataRates> applicationDataRate, int destinationNode, int[] informationSourceList) throws Exception{

		if(classParameters.print_condition) System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		double Range_Bandwidth = 0.0, Path_Bandwidth = 0.0, bandwidth_and_communicationOverhead[];
		int Range_HopCount = 0, Path_HopCount = 0;
		NetworkMetrics networkMetrics = new NetworkMetrics();

		for(int i = 0; i < noofsources; i++){
			Dijkstra dijkstra = new Dijkstra(graph, nodes[informationSourceList[i]].getLabel(), link_data);
			HashMap<String,String> predecessors =  dijkstra.predecessorsList(graph, nodes[informationSourceList[i]].getLabel(), link_data).get(0);

			Path_HopCount = networkMetrics.hopCount(Integer.toString(destinationNode), graph, nodes, informationSourceList[i], link_data);
			bandwidth_and_communicationOverhead = networkMetrics.getBandwidth(dijkstra.getPathTo(Integer.toString(destinationNode)), Integer.toString(informationSourceList[i]), nodes[informationSourceList[i]], links, 
					Double.MAX_VALUE, graph, nodes[destinationNode].getLabel(), predecessors, runStep, totalNetworkNodes, applicationDataRate, link_data);
			Path_Bandwidth = bandwidth_and_communicationOverhead[0];

			// Range-Bandwidth = max(Path-Bandwidth of Src-Dst Path) w.r.t all Sources for a given Destination
			if(Range_Bandwidth <= Path_Bandwidth)
				Range_Bandwidth = Path_Bandwidth;

			// Range-HopCount = max(Path-HopCount of Src-Dst Path) w.r.t all Sources for a given Destination
			if(Range_HopCount <= Path_HopCount)
				Range_HopCount = Path_HopCount;	
		}

		double NetworkMetricRange[] = {Range_HopCount, 10.0, Range_Bandwidth, 4.0};
		for(int i = 0; i < noofsources; i++){
			for(int j = 0; j < noofcriteria; j++){
				if(j == 0)
					NetworkMetricValues[j][i] = 1-(NetworkMetricValues[j][i]/NetworkMetricRange[j]);
				else if (j == 2 && NetworkMetricRange[2] == 0.0)
					NetworkMetricValues[j][i] = NetworkMetricValues[j][i];
				else
					NetworkMetricValues[j][i] = NetworkMetricValues[j][i]/NetworkMetricRange[j];
			}
		}

		return NetworkMetricValues;
	}

	// Using Network_Metric/Range to get Source Comparison Matrix w.r.t each Network Metric
	public double[][] sourceComparisonMatrixWRTNetworkMetric(double[][] networkMetricNormalized,double[][] SourceComparisonMatrixWRTNetworkMetric, int source, int l){
		ahpFormulation ahp = new ahpFormulation();

		for(int i=0; i<source; i++){
			for(int j=0; j<source; j++){
				if(i == j)
					SourceComparisonMatrixWRTNetworkMetric[i][j] = 1;
				else
				{
					switch (l)
					{
					// Calculating Comparison Matrix of Sources w.r.t "HopCount"
					case 0:
					{
						double result = (networkMetricNormalized[l][i]-networkMetricNormalized[l][j]);
						SourceComparisonMatrixWRTNetworkMetric[i][j] = ahp.sourceScoringRanges(result);
						break;
					}

					// Calculating Comparison Matrix of Sources w.r.t "InformationUtility"
					case 1:
					{
						double result = (networkMetricNormalized[l][i]-networkMetricNormalized[l][j]);
						SourceComparisonMatrixWRTNetworkMetric[i][j] = ahp.sourceScoringRanges(result);
						break;
					}

					// Calculating Comparison Matrix of Sources w.r.t "DataRate"
					case 2:
					{
						double result = (networkMetricNormalized[l][i]-networkMetricNormalized[l][j]);
						SourceComparisonMatrixWRTNetworkMetric[i][j] = ahp.sourceScoringRanges(result);
						break;
					}
					// Calculating Comparison Matrix of Sources w.r.t "LinkIntegrity"
					case 3:
					{
						double result = (networkMetricNormalized[l][i]-networkMetricNormalized[l][j]);
						SourceComparisonMatrixWRTNetworkMetric[i][j] = ahp.sourceScoringRanges(result);
						break;
					}
					}
				}
			}
		}

		return SourceComparisonMatrixWRTNetworkMetric;
	}

	// Source scoring w.r.t Chan's defined ranges i.e. delta(s)
	public double sourceScoringRanges(double result){
		double value = 0;
		if(result > 0.7)									value = 9.0;
		else if((result > 0.5) && (result <= 0.7))			value  = 7.0;
		else if((result > 0.3) && (result <= 0.5))			value  = 5.0;
		else if((result > 0.1) && (result <= 0.3))			value  = 3.0;
		else if((result > (-0.1)) && (result <= 0.1))		value  = 1.0;
		else if((result > (-0.3)) && (result <= (-0.1)))	value  = 0.3334;
		else if((result > (-0.5)) && (result <= (-0.3)))	value  = 0.2;
		else if((result > (-0.7)) && (result <= (-0.5)))	value  = 0.1428;
		else if(result <= (-0.7))							value  = 0.1111;
		return value;
	}

	// Calculating Source Vs Network Metric Relationship Weights
	public double[][] sourceVsNetworkMetricRelationshipWeights(double[][] SourceVsNetworkMetricRelationshipWeights, double[][] SourceVsNetworkMetricWeights, double[] relationshipWeights, int source, int n){
		for (int i=0; i<source; i++){	
			for (int j=0; j<n; j++){
				if (j == 0)		SourceVsNetworkMetricRelationshipWeights[i][j] = ((SourceVsNetworkMetricWeights[i][3] * relationshipWeights[(j*2)]) + (SourceVsNetworkMetricWeights[i][1] * relationshipWeights[((j*2)+1)]));  // A
				else if (j == 1)SourceVsNetworkMetricRelationshipWeights[i][j] = ((SourceVsNetworkMetricWeights[i][2] * relationshipWeights[(j*2)]) + (SourceVsNetworkMetricWeights[i][1] * relationshipWeights[((j*2)+1)]));  // C
				else if (j == 2)SourceVsNetworkMetricRelationshipWeights[i][j] = ((SourceVsNetworkMetricWeights[i][2] * relationshipWeights[(j*2)]) + (SourceVsNetworkMetricWeights[i][0] * relationshipWeights[((j*2)+1)]));  // T
				else if (j == 3)SourceVsNetworkMetricRelationshipWeights[i][j] = ((SourceVsNetworkMetricWeights[i][3] * relationshipWeights[(j*2)]) + (SourceVsNetworkMetricWeights[i][0] * relationshipWeights[((j*2)+1)]));  // R
			}
		}
		return SourceVsNetworkMetricRelationshipWeights;
	}

	// Calculating Relative Source QoI Scores
	public double[] sourceQoIScores(double[][] SourceVsNetworkMetricRelationshipWeights, double[] QoICriteriaWeights, double[] SourceQoIScores, int source, 
			int n, int[] informationSourceList, int destination) throws IOException {
		for (int i=0; i<source; i++){	
			for (int j=0; j<n; j++){
				SourceQoIScores[i] += (SourceVsNetworkMetricRelationshipWeights[i][j] * QoICriteriaWeights[j]);	
			}
		}
		return SourceQoIScores;
	}

	// Information-Source Network Metric Values Calculation
	public double[][] sourceNetworkMetricValues(double[][] NetworkMetricValues, int noofcriteria, int noofsources, int totalNetworkNodes, int[] informationSourceList, int destinationNode, Graph graph, 
			classNode[] nodes, classLink[] links, int[] nodeKeys, int runStep, int fileNumber, HashMap<String, classLink> link_CAU_QoI, HashMap<Integer, ApplicationDataRates> applicationDataRate) throws Exception{

		int SD_hopCount = 0;
		double  Information_Utility = 0.0, Link_Integrity = Double.MAX_VALUE, Bandwidth = Double.MAX_VALUE, bandwidth_and_communicationOverhead[] = null;
		NetworkMetrics networkMetrics = new NetworkMetrics();

		for(int i = 0; i < noofsources; i++){
			Dijkstra dijkstra = new Dijkstra(graph, nodes[informationSourceList[i]].getLabel(), link_CAU_QoI);
			HashMap<String,String> predecessors =  dijkstra.predecessorsList(graph, nodes[informationSourceList[i]].getLabel(), link_CAU_QoI).get(0);
			String sourceNode = Integer.toString(informationSourceList[i]);

			for(int j = 0; j < noofcriteria; j++) {
				switch (j)
				{			
				case 0:	{SD_hopCount = networkMetrics.hopCount(Integer.toString(destinationNode), graph, nodes, informationSourceList[i], link_CAU_QoI);
				NetworkMetricValues[j][i] = SD_hopCount;
				break;}				
				case 1: {Information_Utility = networkMetrics.getInformationUtility(Information_Utility, sourceNode);
				NetworkMetricValues[j][i] = Information_Utility;
				break;}
				case 2:	{bandwidth_and_communicationOverhead = networkMetrics.getBandwidth(dijkstra.getPathTo(Integer.toString(destinationNode)), Integer.toString(informationSourceList[i]), nodes[informationSourceList[i]], 
						links, Double.MAX_VALUE, graph, nodes[destinationNode].getLabel(), predecessors,  runStep, totalNetworkNodes, applicationDataRate, link_CAU_QoI);
				NetworkMetricValues[j][i] = bandwidth_and_communicationOverhead[0];
				break;}
				case 3:	{Link_Integrity = networkMetrics.getLinkIntegrity(nodeKeys, Integer.toString(informationSourceList[i]), Link_Integrity, graph, nodes[destinationNode], predecessors);
				NetworkMetricValues[j][i] = Link_Integrity;
				break;}	
				}
			}
			Bandwidth=Double.MAX_VALUE;
		}

		return NetworkMetricValues;
	}

	public double pathBandwidth(double[][] NetworkMetricValues, int n, int source, int totalNetworkNodes, int[] informationSourceList, int destinationNode, 
			Graph graph, classNode[] nodes, classLink[] links, int selected_QoI_Source, double[] nodeRequestedDataRate, int currentLinksCount,
			String FileName, int runStep, String nodeDataRates, int fileNumber, HashMap<String, classLink> link_data
			, HashMap<Integer, ApplicationDataRates> applicationDataRate) throws Exception{

		double Bandwidth = Double.MAX_VALUE, bandwidth_and_communicationOverhead[];
		NetworkMetrics networkMetrics = new NetworkMetrics();

		for(int i = 0; i < source; i++){
			String sourceNode = Integer.toString(informationSourceList[i]);

			if(informationSourceList[i] == selected_QoI_Source) {
				Dijkstra dijkstra = new Dijkstra(graph, nodes[informationSourceList[i]].getLabel(), link_data);
				HashMap<String,String> predecessors =  dijkstra.predecessorsList(graph, nodes[informationSourceList[i]].getLabel(), link_data).get(0);

				bandwidth_and_communicationOverhead = networkMetrics.getBandwidth(dijkstra.getPathTo(Integer.toString(destinationNode)), Integer.toString(informationSourceList[i]), nodes[informationSourceList[i]], links, Bandwidth, 
						graph, nodes[destinationNode].getLabel(), predecessors, runStep, totalNetworkNodes, applicationDataRate, link_data);
				Bandwidth = bandwidth_and_communicationOverhead[0];
			}
		}
		return Bandwidth;
	}

	public int pathHopCount(int[] informationSourceList, int noofsources, int destinationNode, Graph graph, classNode[] nodes, int selected_QoI_Source, HashMap<String, classLink> link_data) throws Exception{
		int Hop_Count=0;
		NetworkMetrics networkMetrics = new NetworkMetrics();		

		for(int i=0; i<noofsources; i++){
			int sourceNode = informationSourceList[i];
			if(informationSourceList[i] == selected_QoI_Source){
				Hop_Count = networkMetrics.hopCount(Integer.toString(destinationNode), graph, nodes, sourceNode, link_data);
				if(classParameters.print_condition) System.out.println("Hop Count:	" + Hop_Count);}
		}
		return Hop_Count;
	}

	public double pathLinkIntegrity(int[] nodeKeys, int noofsources, int[] informationSourceList, int destinationNode, Graph graph, classNode[] nodes, int selectedSource, HashMap<String, classLink> link_data) throws Exception{

		double Link_Integrity = Double.MAX_VALUE;
		NetworkMetrics networkMetrics = new NetworkMetrics();	

		for(int i=0; i<noofsources; i++){			

			if(informationSourceList[i] == selectedSource) {
				Dijkstra dijkstra = new Dijkstra(graph, nodes[informationSourceList[i]].getLabel(), link_data);
				HashMap<String,String> predecessors =  dijkstra.predecessorsList(graph, nodes[informationSourceList[i]].getLabel(), link_data).get(0);

				Link_Integrity = networkMetrics.getLinkIntegrity(nodeKeys, Integer.toString(informationSourceList[i]), Link_Integrity, graph, 
						nodes[destinationNode], predecessors);
				if(classParameters.print_condition) System.out.println("Link Integrity: " + Link_Integrity);
				break;
			}
		}
		return Link_Integrity;
	}

	public void sourceQoIScoreDecsendingOrder(double[] SourceQoIScores, int[] informationSourceList, String current_time, int destination) throws IOException{

		double tempQoIscore = 0;
		int tempSourceNode;

		for(int i = 0; i < SourceQoIScores.length; i++){
			for(int j = 1; j < (SourceQoIScores.length-i); j++){
				if(SourceQoIScores[j-1] < SourceQoIScores[j]){
					//swap the elements!
					tempQoIscore = SourceQoIScores[j-1];
					SourceQoIScores[j-1] = SourceQoIScores[j];
					SourceQoIScores[j] = tempQoIscore;

					tempSourceNode = informationSourceList[j-1];
					informationSourceList[j-1] = informationSourceList[j];
					informationSourceList[j] = tempSourceNode;
				}				
			}
		}

		for(int i=0; i < SourceQoIScores.length; i++){
			if(classParameters.print_condition) System.out.println(SourceQoIScores[i] + " , " + informationSourceList[i]);
		}
	}

	// Calculating Individual Quality Metric Scores w.r.t all Sources for Thresholds-based-QoI-Scheme 
	public double[][] qualityMetricScores_QoIT(double[][] qualityMetricValues, double[] QoICriteriaWeights, int noofsources, int noofcriteria, 
			int[] informationSourceList, int destinationNode, double[][] qualityMetricScores, HashMap<Integer, QualityMetricScores> QMS_temp) throws IOException {
		//QMS_temp represents: QMS_(QoI/QoIT/TE)wrtQoIT_temp
		for (int i = 0; i < noofsources; i++){	 
			for (int j = 0; j < noofcriteria; j++){	
				qualityMetricScores[i][j] = qualityMetricValues[i][j] * QoICriteriaWeights[j];
			}

			QMS_temp.put(informationSourceList[i], new QualityMetricScores(informationSourceList[i], qualityMetricScores[i][0], qualityMetricScores[i][1], qualityMetricScores[i][2], qualityMetricScores[i][3]));
		}
		return qualityMetricScores;
	}

	/*************************************************************************************************************************************************************************/
	/********************* QoI Source Selection ***********************/
	/*************************************************************************************************************************************************************************/
	public int selectedQoISource(double[][] NetworkMetricValues, double[] SourceQoIScores, int selectedSource, int source, int[] informationSourceList, int destinationNode, 
			WriteFile SrcDstPath_Data_QoI, Graph graph, classNode[] nodes, LinkedList<List<classNode>> shortestPathsArray_forSelectedSources, String current_time, 
			HashMap<String, classLink> link_data, HashMap<Integer, selectedSourceInformation> selectedSourceList_QoI, int runStep, Map<Integer, LinkedHashSet<Integer>> 
			srcDstRouteMap_QoI_updated, Map<Integer, LinkedHashSet<Integer>> srcDstRouteMap_QoI, boolean flag_updatedInfo, WriteFile OverallQoIScore_Data) throws Exception{	

		double max = 0;
		boolean destination_Exists = false, flag_sameAs_previousPath = false;
		int index = 0, initialSource = -1;
		Integer[] infoSrcList = Arrays.stream(informationSourceList).boxed().toArray(Integer[]::new);
		Random random = new Random();
		NetworkMetrics networkMetrics = new NetworkMetrics();
		sourceSelection ss = new sourceSelection();
		LinkedHashSet<Integer> srcDstRouteNodes = new LinkedHashSet<Integer>();

		if(runStep == 1) {
			for(int i = 0; i < source; i++) {		
				if(SourceQoIScores[i] > max)                                            { max = SourceQoIScores[i]; index = i; selectedSource = informationSourceList[i]; }
				else if((SourceQoIScores[i] == max) && (random.nextBoolean() == true))  { max = SourceQoIScores[i]; index = i; selectedSource = informationSourceList[i]; }
			}			
			selectedSourceList_QoI.put(destinationNode, new selectedSourceInformation(destinationNode, selectedSource));
			OverallQoIScore_Data.writeToFile(runStep, destinationNode, selectedSource, max);
		}
		else {  
			Object keyset[] = selectedSourceList_QoI.keySet().toArray();
			for(int j = 0; j < keyset.length; j++) {
				selectedSourceInformation ssi = selectedSourceList_QoI.get((Integer) keyset[j]);
				if(destinationNode == ssi.get_destinationNode()) { destination_Exists = true; initialSource = ssi.get_sourceNode(); break; }	
			}

			if (destination_Exists == true) {  	// no source hand-over: no matter the network information is updated or not
				if(ss.contains(infoSrcList, initialSource) == true) { // if the current infoSrcList contains the original Source selected in iteration 1, select its index in informationSourceList 
					for(int i = 0; i < source; i++) { if(informationSourceList[i] == initialSource) { selectedSource = initialSource; index = i; break; } }
				} 
				else selectedSource = -1; 		// if original/initial selected source isn't available in infoSrcList
			}
			else { 								// if destination doesn't already exist in "selectedSourceList_QoI"
				if(flag_updatedInfo == true) {
					for(int i = 0; i < source; i++) {		
						if(SourceQoIScores[i] > max)                                            { max = SourceQoIScores[i]; index = i; selectedSource = informationSourceList[i]; }
						else if((SourceQoIScores[i] == max) && (random.nextBoolean() == true))  { max = SourceQoIScores[i]; index = i; selectedSource = informationSourceList[i]; }
					}			
					selectedSourceList_QoI.put(destinationNode, new selectedSourceInformation(destinationNode, selectedSource));
				}
				else selectedSource = -1; 		// if flag_updatedInfo == false
			}
		}

		/*************************************************************************************************************************************************************************/	
		// New implementation
		if(selectedSource != -1) {
			List<classNode> path = shortestPathsArray_forSelectedSources.get(index);
			int hopCount = networkMetrics.hopCount(Integer.toString(destinationNode), graph, nodes, selectedSource, link_data);

			//populating srcDstRouteNodes with src-dst path nodes
			for(int pathLength = 0; pathLength <= hopCount; pathLength++) srcDstRouteNodes.add(Integer.parseInt(path.get(pathLength).getLabel()));

			// Storing selected src-dst route into "srcDstRouteMap_QoI_updated" (for each iteration) and "srcDstRouteMap_QoI" (only when n/w info is propagated)
			srcDstRouteMap_QoI_updated.put(destinationNode, srcDstRouteNodes);
			if(flag_updatedInfo == true) srcDstRouteMap_QoI.put(destinationNode, srcDstRouteNodes);

			// To decide what to write to the "SrcDstPath_Data_QoI" file i.e. either -1 or the path (if )
			LinkedHashSet<Integer> srcDstRouteNodes_updated  = srcDstRouteMap_QoI_updated.get(destinationNode), srcDstRouteNodes_previous = srcDstRouteMap_QoI.get(destinationNode);
			Iterator<Integer> srcDstUpdatedRoute_Iterator = srcDstRouteNodes_updated.iterator(), srcDstPreviousRoute_Iterator = srcDstRouteNodes_previous.iterator();
			if(srcDstRouteNodes_updated.size() == srcDstRouteNodes_previous.size()) {
				for(int i = 0; i < srcDstRouteNodes_previous.size(); i++) {				
					if(srcDstUpdatedRoute_Iterator.next() == srcDstPreviousRoute_Iterator.next())  flag_sameAs_previousPath = true;
					else                                                                         { flag_sameAs_previousPath = false; break; }
				}
			}

			if(flag_sameAs_previousPath == true) { 
				for(int pathLength = 0; pathLength <= hopCount; pathLength++) 
					SrcDstPath_Data_QoI.writeToFile(runStep, path.get(pathLength).getLabel(), hopCount, pathLength);
			}
			else SrcDstPath_Data_QoI.writeToFile(runStep, destinationNode, -3);
		}

		return selectedSource;
		/*************************************************************************************************************************************************************************/
	}

	// Identifying Sources which satisfy all Quality Metric Thresholds
	public int data_selectedSource_QoIT(double[][] qualityMetricScores, double[] QoICriteriaWeights, int noofsources, int[] sourceOrdered_sourceList, int destinationNode, 
			double[] PriorityScore, int[] Noof_QualityMetrics_Met, double[] SourceQoITScore, HashMap<Integer, selectedSourceInformation> selectedSourceList_QoIT, int runStep, 
			boolean flag_updatedInfo) throws IOException {
		
		// array of only maximum Priority elements, array of number of metrics met for maximum Priority elements, array of sources of maximum Priority elements
		int selectedSource = -1, sourceCounter = 0, noofMetricsMet_maxPriorityScore[] = null, infSrcList_maxPriorityScore[] = null;
		double sourceQoITScore_maxPriorityScore[] = null, maxPriorityScore[] = null, maxPriority = 0.0;

		// A. 1
		// Counting No. of Sources satisfying all Quality Metric Thresholds 
		for (int i = 0; i < noofsources; i++){		
			if((qualityMetricScores[i][0] == QoICriteriaWeights[0]) && (qualityMetricScores[i][1] == QoICriteriaWeights[1]) && (qualityMetricScores[i][2] == QoICriteriaWeights[2]) && (qualityMetricScores[i][3] == QoICriteriaWeights[3])) 
				sourceCounter++;				
		}

		// Identifying Sources which satisfy all Quality Metric Thresholds
		int thresholdSatisfyingSources [] = new int[sourceCounter], k = 0;
		for (int i = 0; i < noofsources; i++){		
			if((qualityMetricScores[i][0] == QoICriteriaWeights[0]) && (qualityMetricScores[i][1] == QoICriteriaWeights[1]) && (qualityMetricScores[i][2] == QoICriteriaWeights[2]) && (qualityMetricScores[i][3] == QoICriteriaWeights[3])) {
				thresholdSatisfyingSources[k] = sourceOrdered_sourceList[i];
				k++;
			} }

		// A. 2
		// PriorityScore ={9,6,7,7,9,5}; Noof_QualityMetrics_Met = {3,3,3,2,3,2}; infSrcList ={0,1,2,3,4,5}
		// Finding Maximum of Array: PriorityScore i.e. {9, 6, 7, 7, 9, 5} = 9
		if(thresholdSatisfyingSources.length == 0) {
			maxPriority = PriorityScore[0]; int  count = 0, a = 0;
			for (int i = 1; i < PriorityScore.length; i++) 	{ if(Double.compare(maxPriority,PriorityScore[i]) < 0) maxPriority = PriorityScore[i]; } 

			// Find count of maximum of Array: PriorityScore i.e. {9, 6, 7, 7, 9, 5} = 2(9)
			if(Double.compare(maxPriority, 0.0) > 0) {
				for(int i = 0; i < PriorityScore.length; i++) { if(Double.compare(maxPriority,PriorityScore[i]) == 0) count++; }
				maxPriorityScore = new double[count];     
				noofMetricsMet_maxPriorityScore = new int[count]; 
				infSrcList_maxPriorityScore = new int[count]; 
				sourceQoITScore_maxPriorityScore = new double[count];

				// populating arrayof_maxPriorityMet, arrayof_NoofMetricsMet, infSrcList_maxPriorityScore, arrayof_maxPriorityMet_Percentage
				// in accordance to maxPriority
				for(int i = 0; i < PriorityScore.length; i++) {
					if(Double.compare(maxPriority,PriorityScore[i]) == 0) {
						maxPriorityScore[a] = PriorityScore[i];				// set array to 9, 9
						noofMetricsMet_maxPriorityScore[a] = Noof_QualityMetrics_Met[i];     // set array to 3, 3
						infSrcList_maxPriorityScore[a] = sourceOrdered_sourceList[i];  // set array to 0, 4
						sourceQoITScore_maxPriorityScore[a] = SourceQoITScore[i];
						a++;
		} } } }

		selectedSource = selectedSource_QoIT(runStep, thresholdSatisfyingSources, SourceQoITScore, selectedSource, maxPriority, maxPriorityScore, infSrcList_maxPriorityScore, 
				noofMetricsMet_maxPriorityScore, sourceQoITScore_maxPriorityScore, destinationNode, noofsources, sourceOrdered_sourceList, selectedSourceList_QoIT, flag_updatedInfo);

		return selectedSource;
	}

	/*************************************************************************************************************************************************************************/
	/********************* QoIT Source Selection ***********************/
	/*************************************************************************************************************************************************************************/
	// QoIT-based Source Selection: w.r.t user's Network/Quality Metric Threshold Values
	public int selectedSource_QoIT(int runStep, int[] thresholdSatisfyingSources, double[] SourceQoITScore, int selectedSource, double maxPriority, double[] maxPriorityScore, 
			int[] infSrcList_maxPriorityScore, int[] noofMetricsMet_maxPriorityScore, double[] sourceQoITScore_maxPriorityScore, int destinationNode, int noofsources, 
			int[] sourceOrdered_sourceList, HashMap<Integer, selectedSourceInformation> selectedSourceList_QoIT, boolean flag_updatedInfo) throws IOException{

		int initialSource = -1, min_NoofMetricsMet; boolean destination_Exists = false; Random random = new Random(); double maxQoITScore;
		sourceSelection ss = new sourceSelection(); Integer[] infoSrcList = Arrays.stream(sourceOrdered_sourceList).boxed().toArray(Integer[]::new);

		// Selecting QoIT Source
		if (runStep == 1) {
			if(thresholdSatisfyingSources.length >= 1)	{		 										// if number of thresholdSatisfyingSources >= 1
				selectedSource = thresholdSatisfyingSources[0];
				for(int i = 1; i < thresholdSatisfyingSources.length; i++) { if (random.nextBoolean() == true) selectedSource = thresholdSatisfyingSources[i]; }
			}
			else if (Double.compare(maxPriority,0.0) > 0){
				if(maxPriorityScore.length == 1) selectedSource = infSrcList_maxPriorityScore[0];  		// if only one source having maximum Priority Score
				else {                         															// if multiple sources having maximum Priority Score
					min_NoofMetricsMet = noofMetricsMet_maxPriorityScore[0]; 
					maxQoITScore = sourceQoITScore_maxPriorityScore[0]; 
					selectedSource = infSrcList_maxPriorityScore[0];

					for (int i = 1; i < noofMetricsMet_maxPriorityScore.length; i++) {
						if (min_NoofMetricsMet > noofMetricsMet_maxPriorityScore[i])  					// 3 (4,2,1) > 2 (4,3) , min_NoofMetricsMet = 2
							{ min_NoofMetricsMet = noofMetricsMet_maxPriorityScore[i]; maxQoITScore = sourceQoITScore_maxPriorityScore[i]; selectedSource = infSrcList_maxPriorityScore[i];	}
						else if (min_NoofMetricsMet == noofMetricsMet_maxPriorityScore[i]) {		    // 2 (4,3) == 2 (4,3)	
							if(Double.compare(maxQoITScore, sourceQoITScore_maxPriorityScore[i]) < 0) 
							  	{ maxQoITScore = sourceQoITScore_maxPriorityScore[i]; selectedSource = infSrcList_maxPriorityScore[i]; }
							else if ((Double.compare(maxQoITScore, sourceQoITScore_maxPriorityScore[i]) == 0) && (random.nextBoolean() == true)) 
								selectedSource = infSrcList_maxPriorityScore[i];	
			} } } }
			else if (Double.compare(maxPriority,0.0) == 0) { 
				maxQoITScore = SourceQoITScore[0]; selectedSource = sourceOrdered_sourceList[0];
				for (int i = 1; i < SourceQoITScore.length; i++) {
					if(maxQoITScore < SourceQoITScore[i]) 		{ maxQoITScore = SourceQoITScore[i]; selectedSource = sourceOrdered_sourceList[i]; }
					else if (maxQoITScore == SourceQoITScore[i] && random.nextBoolean() == true)   { selectedSource = sourceOrdered_sourceList[i]; }
			} }
			else  selectedSource = -1; 
			selectedSourceList_QoIT.put(destinationNode, new selectedSourceInformation(destinationNode, selectedSource));
		}
		else {	
			Object keyset[] = selectedSourceList_QoIT.keySet().toArray();
			for(int j = 0; j < keyset.length; j++) {
				selectedSourceInformation ssi = selectedSourceList_QoIT.get((Integer) keyset[j]);
				if(destinationNode == ssi.get_destinationNode()) { destination_Exists = true; initialSource = ssi.get_sourceNode(); break; }	
			}

			if (destination_Exists == true) { 	// no source hand-over: no matter the network information is updated or not
				if(ss.contains(infoSrcList, initialSource) == true) { // if the current infoSrcList contains the original Source selected in iteration 1, select that source 
					for(int i = 0; i < noofsources; i++) { if(sourceOrdered_sourceList[i] == initialSource) { selectedSource = initialSource; break; } }
				} 
				else selectedSource = -1; 		// if original/initial selected source isn't available in infoSrcList
			}
			else { 								// if destination doesn't already exist in "selectedSourceList_QoI"
				if(flag_updatedInfo == true) {
					selectedSource = selectedSource_QoIT(1, thresholdSatisfyingSources, SourceQoITScore, selectedSource, maxPriority, maxPriorityScore, infSrcList_maxPriorityScore, 
							noofMetricsMet_maxPriorityScore, sourceQoITScore_maxPriorityScore, destinationNode, noofsources, sourceOrdered_sourceList, selectedSourceList_QoIT, flag_updatedInfo);	
				}
				else selectedSource = -1; 		// if flag_updatedInfo == false
			}			
		}
		return selectedSource;
	}
}