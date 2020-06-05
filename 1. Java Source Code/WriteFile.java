import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.*;

public class WriteFile {

	private String path;
	private boolean append_to_file = false;
	
	public void writeToFile(int destination, int source, int priority, int number, double percentage) throws IOException{
		FileWriter write = new FileWriter(path, append_to_file);
		PrintWriter print_line = new PrintWriter(write);	
		print_line.printf("%d", destination);
		print_line.printf(",");
		print_line.printf("%d", source);
		print_line.printf(",");
		print_line.printf("%d", priority);
		print_line.printf(",");
		print_line.printf("%d", number);
		print_line.printf(",");
		print_line.printf("%f", percentage);
		print_line.printf("\n");
		
		print_line.flush();
		print_line.close();
	}
	
	public void writeToFile(int runStep, int destination, int source, double OverallQoIScore) throws IOException{
		FileWriter write = new FileWriter(path, append_to_file);
		PrintWriter print_line = new PrintWriter(write);
		print_line.printf("%d", runStep);
		print_line.printf(",");
		print_line.printf("%d", destination);
		print_line.printf(",");
		print_line.printf("%d", source);
		print_line.printf(",");
		print_line.printf("%f", OverallQoIScore);
		print_line.printf("\n");
		
		print_line.flush();
		print_line.close();
	}
	
	public WriteFile(String path_file) {
		path = path_file;
	}
	
	public WriteFile(String path_file, boolean append_value) {
		path = path_file;
		append_to_file = append_value;
	}
	
	public void clearTheFile() throws IOException{
		FileWriter write = new FileWriter(path, false);
		PrintWriter print_line = new PrintWriter(write, false);
		print_line.flush();
		print_line.close();
		write.close();
	}
	
public void writeToFile(int destinationNode, double A, double C, double T, double R) throws IOException{
		FileWriter write = new FileWriter(path, append_to_file);
		PrintWriter print_line = new PrintWriter(write);
		print_line.printf("%d", destinationNode);
		print_line.printf(",");
		print_line.printf("%f", A);
		print_line.printf(",");
		print_line.printf("%f", C);
		print_line.printf(",");
		print_line.printf("%f", T);
		print_line.printf(",");
		print_line.printf("%f", R);
		print_line.printf("\n");
		
		print_line.flush();
		print_line.close();
	}
	
public void writeToFile(String time, String destination, double destinationX, double destinationY) throws IOException{
		FileWriter write = new FileWriter(path, append_to_file);
		PrintWriter print_line = new PrintWriter(write);
		print_line.printf("%s", time);
		print_line.printf(",");
		print_line.printf("%f", 0.0);
		print_line.printf(",");
		print_line.printf("%d", 0);
		print_line.printf(",");
		print_line.printf("%f", 0.0);
		print_line.printf(",");
		print_line.printf("%f", 0.0);
		print_line.printf(",");
		print_line.printf("%s", destination);
		print_line.printf(",");
		print_line.printf("%f", destinationX);
		print_line.printf(",");
		print_line.printf("%f", destinationY);
		print_line.printf(",");
		print_line.printf("%s", -2);
		print_line.printf(",");
		print_line.printf("%f", 0.0);
		print_line.printf(",");
		print_line.printf("%f", 0.0);
		print_line.printf("\n");
		
		print_line.flush();
		print_line.close();
	}

public void writeToFile(String time, double networkMetric, String destination, double destinationX, double destinationY, String source, double sourceX, double sourceY) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	print_line.printf("%s", time);
	print_line.printf(",");
	print_line.printf("%.15f", networkMetric);
	print_line.printf(",");
	print_line.printf("%s", destination);
	print_line.printf(",");
	print_line.printf("%f", destinationX);
	print_line.printf(",");
	print_line.printf("%f", destinationY);
	print_line.printf(",");
	print_line.printf("%s", source);
	print_line.printf(",");
	print_line.printf("%f", sourceX);
	print_line.printf(",");
	print_line.printf("%f", sourceY);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

// new
public void writeToFile(int runStep, String node, int hopCount, int pathLength) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	if((pathLength == 0) && (hopCount == 0 || hopCount == -1))
	{
		print_line.printf("%d", runStep);
		print_line.printf(",");
		print_line.printf("%s", node);
		print_line.printf(",");
		print_line.printf(Integer.toString(hopCount-1));
		print_line.printf("\n");
	}
	else if (pathLength == 0) {
		print_line.printf("%d", runStep);
		print_line.printf(",");
		print_line.printf("%s", node);
		print_line.printf(",");
	}
	else if(pathLength < hopCount) {
	print_line.printf("%s", node);
	print_line.printf(",");
	}
	else if (pathLength == hopCount) {
		print_line.printf("%s", node);
		print_line.printf("\n");
	}
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String current_time, double nodeX, double nodeY, String node) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%s", node);
	print_line.printf(",");
	print_line.printf("%f", nodeX);
	print_line.printf(",");
	print_line.printf("%f", nodeY);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

// new
public void writeToFile(String node_Id, double node_X, double node_Y, double dirn, int itype) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", node_Id);
	print_line.printf(",");
	print_line.printf("%f", node_X);
	print_line.printf(",");
	print_line.printf("%f", node_Y);
	print_line.printf(",");
	print_line.printf("%f", dirn);
	print_line.printf(",");
	print_line.printf("%d", itype);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}																													

public void writeToFile(String current_time, String node, double nodeX, double nodeY, double networkMetric) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%f", networkMetric);
	print_line.printf(",");
	print_line.printf("%d", 0);
	print_line.printf(",");
	print_line.printf("%f", 0.0);
	print_line.printf(",");
	print_line.printf("%f", 0.0);
	print_line.printf(",");
	print_line.printf("%s", node);
	print_line.printf(",");
	print_line.printf("%f", nodeX);
	print_line.printf(",");
	print_line.printf("%f", nodeY);
	print_line.printf(",");
	print_line.printf("%s", -2);
	print_line.printf(",");
	print_line.printf("%f", 0.0);
	print_line.printf(",");
	print_line.printf("%f", 0.0);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String current_time, String destination, double destinationX, 
		double destinationY, String source, double sourceX, double sourceY) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%s", destination);
	print_line.printf(",");
	print_line.printf("%f", destinationX);
	print_line.printf(",");
	print_line.printf("%f", destinationY);
	print_line.printf(",");
	print_line.printf("%s", source);
	print_line.printf(",");
	print_line.printf("%f", sourceX);
	print_line.printf(",");
	print_line.printf("%f", sourceY);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(double linkCapacity, double link_Available_Bandwidth, double link_Utilized_Bandwidth, 
		String predecessorNode, String successorNode) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", predecessorNode);
	print_line.printf(",");
	print_line.printf("%s", successorNode);
	print_line.printf(",");
	print_line.printf("%f", linkCapacity);
	print_line.printf(",");
	print_line.printf("%f", link_Available_Bandwidth);
	print_line.printf(",");
	print_line.printf("%f", link_Utilized_Bandwidth);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String current_time, int i, double nodeRequestedDataRate) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%d", i);
	print_line.printf(",");
	print_line.printf("%f", nodeRequestedDataRate);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String current_time, int destination, double[] sourceQoIScores, int[] sourceList) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%d", destination);
	print_line.printf(",");
	
	for(int i=0; i<sourceList.length; i++) {
		print_line.printf("%f", sourceQoIScores[i]);
		print_line.printf(",");
		print_line.printf("%d", sourceList[i]);
		print_line.printf(",");
	}
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String current_time, int destination, double selectedQoISourceScore, int selectedQoISource) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%d", destination);
	print_line.printf(",");
	print_line.printf("%f", selectedQoISourceScore);
	print_line.printf(",");
	print_line.printf("%d", selectedQoISource);
	print_line.printf(",");
	
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String current_time, int destination, int selectedQoISource, int assignedSource) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%d", destination);
	print_line.printf(",");
	print_line.printf("%d", selectedQoISource);
	print_line.printf(",");
	print_line.printf("%d", assignedSource);
	print_line.printf(",");
	
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String nodeSender, String nodeReceiver) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", nodeSender);
	print_line.printf(",");
	print_line.printf("%s", nodeReceiver);
	print_line.printf(",");
	
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

// new
public void writeToFile(int runStep, String destination, int nodeType) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", runStep);
	print_line.printf(",");
	print_line.printf("%s", destination);
	print_line.printf(",");
	print_line.printf("%d", nodeType);
	print_line.printf(",");
	
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String current_time, String nodeSender, String nodeReceiver, int count, double SSTDMA_NodeDataRate, int Node_ForwardingCount) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%s", nodeSender);
	print_line.printf(",");
	print_line.printf("%s", nodeReceiver);
	print_line.printf(",");
	print_line.printf("%d", count);
	print_line.printf(",");
	print_line.printf("%f", SSTDMA_NodeDataRate);
	print_line.printf(",");
	print_line.printf("%d", Node_ForwardingCount);
	print_line.printf(",");
	
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

// To write to file "PathsUtilizingBandwidth"
public void writeToFile(String current_time, String sourceNode, String sourceX, String sourceY, String destinationNode, String destinationX, String destinationY, double linkCapacity, double availableBandwidth, double utilizedBandwidth) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%s", destinationNode);
	print_line.printf(",");
	print_line.printf("%s", destinationX);
	print_line.printf(",");
	print_line.printf("%s", destinationY);
	print_line.printf(",");	
	print_line.printf("%s", sourceNode);
	print_line.printf(",");	
	print_line.printf("%s", sourceX);
	print_line.printf(",");	
	print_line.printf("%s", sourceY);
	print_line.printf(",");
	print_line.printf("%f", linkCapacity);
	print_line.printf(",");
	print_line.printf("%f", availableBandwidth);
	print_line.printf(",");
	print_line.printf("%f", utilizedBandwidth);
	print_line.printf(",");
	
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

//LinkCapacity File
public void writeToFile(String current_time, String fromNode, String toNode, double linkCapacity, double availableBandwidth, double utilizedBandwidth) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%s", fromNode);
	print_line.printf(",");
	print_line.printf("%s", toNode);
	print_line.printf(",");	
	print_line.printf("%f", linkCapacity);
	print_line.printf(",");
	print_line.printf("%f", availableBandwidth);
	print_line.printf(",");
	print_line.printf("%f", utilizedBandwidth);
	print_line.printf(",");
	
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(double seconds) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%f", seconds);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String current_time, int destinationNode, int selectedSource) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", current_time);
	print_line.printf(",");
	print_line.printf("%d", destinationNode);
	print_line.printf(",");
	print_line.printf("%d", selectedSource);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(int destinationNode, int communicationOverhead) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", destinationNode);
	print_line.printf(",");
	print_line.printf("%d", communicationOverhead);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

// new
public void writeToFile(int runStep, int destinationNode, int sourceNode) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", runStep);
	print_line.printf(",");
	print_line.printf("%d", destinationNode);
	print_line.printf(",");
	print_line.printf("%d", sourceNode);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(int source) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", source);
	print_line.printf(",");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(double QoImetricScore, int j) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	if(j!=3) {
	print_line.printf("%f", QoImetricScore);
	print_line.printf(",");
	}
	else {
	print_line.printf("%f", QoImetricScore);
	print_line.printf("\n");
	}
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(int destination, String source, String A, String C, String T, String R) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", destination);
	print_line.printf(",");
	print_line.printf("%s", source);
	print_line.printf(",");
	print_line.printf("%s", A);
	print_line.printf(",");
	print_line.printf("%s", C);
	print_line.printf(",");
	print_line.printf("%s", T);
	print_line.printf(",");
	print_line.printf("%s", R);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String neighbor) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	print_line.printf("%s", neighbor);
	print_line.printf(",");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile() throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);	
	print_line.printf("\n");	
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(String node1, String node2, Double RandomFading) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", node1);
	print_line.printf(",");
	print_line.printf("%s", node2);
	print_line.printf(",");
	print_line.printf("%f", RandomFading);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

// delete below code
public void writeToFile(int n, double d, double l, String node1, String node2) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%s", node1);
	print_line.printf(",");
	print_line.printf("%s", node2);
	print_line.printf(",");
	print_line.printf("%f", l);
	print_line.printf(",");
	print_line.printf("%f", d);
	print_line.printf(",");
	print_line.printf("%d", n);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

// new
public void writeToFile(int runStep, int destination, int source, double assignedBandwidth, int hopCount, double linkIntegrity, double informationUtility, int hashMap_record) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", runStep);
	print_line.printf(",");
	print_line.printf("%d", destination);
	print_line.printf(",");
	print_line.printf("%d", source);
	print_line.printf(",");
	print_line.printf("%f", assignedBandwidth);
	print_line.printf(",");
	print_line.printf("%d", hopCount);
	print_line.printf(",");
	print_line.printf("%f", linkIntegrity);
	print_line.printf(",");
	print_line.printf("%f", informationUtility);
	print_line.printf("\n");
	
//	if(hashMap_record == 40) {
		print_line.flush();
		print_line.close();
//	}
}

// new
public void writeToFile(int runStep, int destination, int source, double priorityScore, int noof_QualityMetrics_Met, double percentageof_QualityMetrics_Met, int hashMap_record) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", runStep);
	print_line.printf(",");
	print_line.printf("%d", destination);
	print_line.printf(",");
	print_line.printf("%d", source);
	print_line.printf(",");
	print_line.printf("%f", priorityScore);
	print_line.printf(",");
	print_line.printf("%d", noof_QualityMetrics_Met);
	print_line.printf(",");
	print_line.printf("%f", percentageof_QualityMetrics_Met);
	print_line.printf("\n");
	
//	if(hashMap_record == 40) {
		print_line.flush();
		print_line.close();
//	}
}

// new writeToFile(int, int, int, double, int, double, double, int)
public void writeToFile(int runStep, int destination, int source, double accuracy, double completeness, double timeliness, double reliability, int hashMap_record) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", runStep);
	print_line.printf(",");
	print_line.printf("%d", destination);
	print_line.printf(",");
	print_line.printf("%d", source);
	print_line.printf(",");
	print_line.printf("%f", accuracy);
	print_line.printf(",");
	print_line.printf("%f", completeness);
	print_line.printf(",");
	print_line.printf("%f", timeliness);
	print_line.printf(",");
	print_line.printf("%f", reliability);
	print_line.printf("\n");
	
//	if(hashMap_record == 40) {
		print_line.flush();
		print_line.close();
//	}
}

public void writeToFile(int destination, double bandwidth, int hopCount, int linkIntegrity, double informationUtility) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", destination);
	print_line.printf(",");
	print_line.printf("%f", bandwidth);
	print_line.printf(",");
	print_line.printf("%d", hopCount);
	print_line.printf(",");
	print_line.printf("%d", linkIntegrity);
	print_line.printf(",");
	print_line.printf("%f", informationUtility);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(int destination, double bandwidth, int hopCount, double linkIntegrity, double informationUtility) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", destination);
	print_line.printf(",");
	print_line.printf("%f", bandwidth);
	print_line.printf(",");
	print_line.printf("%d", hopCount);
	print_line.printf(",");
	print_line.printf("%f", linkIntegrity);
	print_line.printf(",");
	print_line.printf("%f", informationUtility);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(int destination, int source, double assigneBandwidth, int hopCount, double linkIntegrity, double informationUtility) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", destination);
	print_line.printf(",");
	print_line.printf("%d", source);
	print_line.printf(",");
	print_line.printf("%f", assigneBandwidth);
	print_line.printf(",");
	print_line.printf("%d", hopCount);
	print_line.printf(",");
	print_line.printf("%f", linkIntegrity);
	print_line.printf(",");
	print_line.printf("%f", informationUtility);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(int destination, int source, double A, double C, double T, double R) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", destination);
	print_line.printf(",");
	print_line.printf("%d", source);
	print_line.printf(",");
	print_line.printf("%f", A);
	print_line.printf(",");
	print_line.printf("%f", C);
	print_line.printf(",");
	print_line.printf("%f", T);
	print_line.printf(",");
	print_line.printf("%f", R);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}

public void writeToFile(int destination, int source, double assigneBandwidth, int hopCount, double linkIntegrity, double informationUtility, 
		int srcExists_flag) throws IOException{
	FileWriter write = new FileWriter(path, append_to_file);
	PrintWriter print_line = new PrintWriter(write);
	
	print_line.printf("%d", destination);
	print_line.printf(",");
	print_line.printf("%d", source);
	print_line.printf(",");
	print_line.printf("%f", assigneBandwidth);
	print_line.printf(",");
	print_line.printf("%d", hopCount);
	print_line.printf(",");
	print_line.printf("%f", linkIntegrity);
	print_line.printf(",");
	print_line.printf("%f", informationUtility);
	print_line.printf(",");
	print_line.printf("%d", srcExists_flag);
	print_line.printf("\n");
	
	print_line.flush();
	print_line.close();
}
}

