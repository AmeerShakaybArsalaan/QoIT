import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.*;

public class createCSVFiles {

	public createCSVFiles(){}

	public LinkedList<Object> createFiles () throws Exception{
		LinkedList<Object> obj = new LinkedList<Object>();

		 // Network Metric Threshold Values w.r.t All Users
		String networkMetric_ThresholdValues = "NetworkMetric_ThresholdValues.csv";
		WriteFile NetworkMetricThresholdValues = new WriteFile(networkMetric_ThresholdValues, true);
		NetworkMetricThresholdValues.clearTheFile();
 
		// Selected Src-Dst Paths w.r.t QoI/QoIT/TE
		String srcDstPath_QoI = "SrcDstPath_QoI.csv";
		WriteFile SrcDstPath_QoI = new WriteFile(srcDstPath_QoI, true);
		SrcDstPath_QoI.clearTheFile();
		String srcDstPath_QoIT = "SrcDstPath_QoIT.csv";
		WriteFile SrcDstPath_QoIT = new WriteFile(srcDstPath_QoIT, true);
		SrcDstPath_QoIT.clearTheFile();

		// Computation Overhead for QoI, QoIT, TE
		String timeConsumedQoI = "ComputationOverhead_QoI.csv";
		WriteFile timeConsumedQoI_Data = new WriteFile(timeConsumedQoI, true);
		timeConsumedQoI_Data.clearTheFile();
		String timeConsumedQoIT = "ComputationOverhead_QoIT.csv";
		WriteFile timeConsumedQoIT_Data = new WriteFile(timeConsumedQoIT, true);
		timeConsumedQoIT_Data.clearTheFile();

		// Network Metric Values for SELECTED Source of All Users w.r.t QoI/QoIT/TE
		String networkMetricValues_QoISelectedSource = "NetworkMetricValues_SelectedSource_QoI.csv"; 
		WriteFile NetworkMetricValues_QoISelectedSource = new WriteFile(networkMetricValues_QoISelectedSource, true);
		NetworkMetricValues_QoISelectedSource.clearTheFile(); 
		String networkMetricValues_QoITSelectedSource = "NetworkMetricValues_SelectedSource_QoIT.csv"; 
		WriteFile NetworkMetricValues_QoITSelectedSource = new WriteFile(networkMetricValues_QoITSelectedSource, true);
		NetworkMetricValues_QoITSelectedSource.clearTheFile();

		// Individual Quality Metric Score of SELECTED Source for All Users considering (QoI/QoIT/TE) w.r.t QoIT
		String QoImetricScore_QoIwrtQoIT = "QoImetricScore_SelectedSource_QoIwrtQoIT.csv";
		WriteFile QoImetricScore_SelectedSource_QoIwrtQoIT = new WriteFile(QoImetricScore_QoIwrtQoIT, true);
		QoImetricScore_SelectedSource_QoIwrtQoIT.clearTheFile();
		String QoImetricScore_newnonQoIScheme = "QoImetricScore_SelectedSource_QoITwrtQoIT.csv";
		WriteFile QoImetricScore_SelectedSource_QoITwrtQoIT = new WriteFile(QoImetricScore_newnonQoIScheme, true);
		QoImetricScore_SelectedSource_QoITwrtQoIT.clearTheFile();

		// "Priorities, Number, Percentage" of Metrics met w.r.t SELECTED Source for All Users considering (QoI/QoIT/TE) w.r.t QoIT
		String PNP_QoIwrtQoIT = "PNP_SelectedSources_QoIwrtQoIT.csv";
		WriteFile PNP_SelectedSources_QoIwrtQoIT_Data = new WriteFile(PNP_QoIwrtQoIT, true);
		PNP_SelectedSources_QoIwrtQoIT_Data.clearTheFile();
		String PNP_QoITwrtQoIT = "PNP_SelectedSources_QoITwrtQoIT.csv";
		WriteFile PNP_SelectedSources_QoITwrtQoIT_Data = new WriteFile(PNP_QoITwrtQoIT, true);
		PNP_SelectedSources_QoITwrtQoIT_Data.clearTheFile();
		
		// QoI and QoIT score for a selected source under QoI Scheme
		String QoISelectedSource_QoIScore = "QoISelectedSource_QoIScore.csv";
		WriteFile QoISelectedSource_QoIScore_Data = new WriteFile(QoISelectedSource_QoIScore, true);
		QoISelectedSource_QoIScore_Data.clearTheFile();
		String QoISelectedSource_QoITScore = "QoISelectedSource_QoITScore.csv";
		WriteFile QoISelectedSource_QoITScore_Data = new WriteFile(QoISelectedSource_QoITScore, true);
		QoISelectedSource_QoITScore_Data.clearTheFile();
		// QoI and QoIT score for a selected source under QoI Scheme
		String QoITSelectedSource_QoIScore = "QoITSelectedSource_QoIScore.csv";
		WriteFile QoITSelectedSource_QoIScore_Data = new WriteFile(QoITSelectedSource_QoIScore, true);
		QoITSelectedSource_QoIScore_Data.clearTheFile();
		String QoITSelectedSource_QoITScore = "QoITSelectedSource_QoITScore.csv";
		WriteFile QoITSelectedSource_QoITScore_Data = new WriteFile(QoITSelectedSource_QoITScore, true);
		QoITSelectedSource_QoITScore_Data.clearTheFile();
		
		/******************************************************************************************************************************/
		obj.add(NetworkMetricThresholdValues);
		obj.add(SrcDstPath_QoI);
		obj.add(SrcDstPath_QoIT);
		obj.add(timeConsumedQoI_Data);
		obj.add(timeConsumedQoIT_Data);
		obj.add(NetworkMetricValues_QoISelectedSource);
		obj.add(NetworkMetricValues_QoITSelectedSource);
		obj.add(QoImetricScore_SelectedSource_QoIwrtQoIT);
		obj.add(QoImetricScore_SelectedSource_QoITwrtQoIT);	
		obj.add(PNP_SelectedSources_QoIwrtQoIT_Data);
		obj.add(PNP_SelectedSources_QoITwrtQoIT_Data);
		obj.add(QoISelectedSource_QoIScore_Data);
		obj.add(QoISelectedSource_QoITScore_Data);
		obj.add(QoITSelectedSource_QoIScore_Data);
		obj.add(QoITSelectedSource_QoITScore_Data);

		return obj;
	}

	public void storeOutput_CSVFiles( 	
			HashMap<Integer, networkData_HashMaps> nmv_SelectedSource_QoI, WriteFile NMV_SelectedSource_QoI, HashMap<Integer, networkData_HashMaps> nmv_SelectedSource_QoIT, WriteFile NMV_SelectedSource_QoIT, 
			HashMap<Integer, networkData_HashMaps> pnp_SelectedSource_QoIwrtQoIT, WriteFile PNP_SelectedSource_QoIwrtQoIT, HashMap<Integer, networkData_HashMaps> pnp_SelectedSource_QoITwrtQoIT, 
			WriteFile PNP_SelectedSource_QoITwrtQoIT, HashMap<Integer, networkData_HashMaps> qms_SelectedSource_QoIwrtQoIT, WriteFile QoImetricScore_SelectedSource_QoIwrtQoIT, 
			HashMap<Integer, networkData_HashMaps> qms_SelectedSource_QoITwrtQoIT, WriteFile QoImetricScore_SelectedSource_QoITwrtQoIT) throws IOException{

		/********************************************************************************************************************************************************************************************/
		/****** Network-Metric-Values for Selected-Sources w.r.t QoI, QoIT, TE ******/
		/********************************************************************************************************************************************************************************************/
		Object KeySet[] = nmv_SelectedSource_QoI.keySet().toArray();
		for(int i = 0; i < KeySet.length; i++) {
			networkData_HashMaps nmv = nmv_SelectedSource_QoI.get((Integer) KeySet[i]);
			NMV_SelectedSource_QoI.writeToFile(nmv.get_runStep(), nmv.get_destinationNode(), nmv.get_selectedSourceNode(), nmv.get_assignedBandwidth(), nmv.get_hopCount(), 
					nmv.get_linkIntegrity(), nmv.get_informationUtility(), i++);	i--;
		}

		KeySet = nmv_SelectedSource_QoIT.keySet().toArray();
		for(int i = 0; i < KeySet.length; i++) {
			networkData_HashMaps nmv = nmv_SelectedSource_QoIT.get((Integer) KeySet[i]);
			NMV_SelectedSource_QoIT.writeToFile(nmv.get_runStep(), nmv.get_destinationNode(), nmv.get_selectedSourceNode(), nmv.get_assignedBandwidth(), nmv.get_hopCount(), 
					nmv.get_linkIntegrity(), nmv.get_informationUtility(), i++);  i--;
		}
		/********************************************************************************************************************************************************************************************/
		/****** PNP_Data of Selected-Sources for QoI, QoIT, TE w.r.t QoIT ******/
		/********************************************************************************************************************************************************************************************/
		KeySet = pnp_SelectedSource_QoIwrtQoIT.keySet().toArray();
		for(int i = 0; i < KeySet.length; i++) {
			networkData_HashMaps pnp = pnp_SelectedSource_QoIwrtQoIT.get((Integer) KeySet[i]);
			PNP_SelectedSource_QoIwrtQoIT.writeToFile(pnp.get_runStep(), pnp.get_destinationNode(), pnp.get_selectedSourceNode(), pnp.get_priorityScore(), pnp.get_noof_QualityMetrics_Met(),
					pnp.get_QoIT_score(), i++);	i--;
		}
		
		KeySet = pnp_SelectedSource_QoITwrtQoIT.keySet().toArray();
		for(int i = 0; i < KeySet.length; i++) {
			networkData_HashMaps pnp = pnp_SelectedSource_QoITwrtQoIT.get((Integer) KeySet[i]);
			PNP_SelectedSource_QoITwrtQoIT.writeToFile(pnp.get_runStep(), pnp.get_destinationNode(), pnp.get_selectedSourceNode(), pnp.get_priorityScore(), pnp.get_noof_QualityMetrics_Met(),
					pnp.get_QoIT_score(), i++);	i--;
		}
		/*****************************************************************************************************************************************************************************/
		/****** PNP_Data of Selected-Sources for QoI, QoIT, TE w.r.t QoIT ******/
		/********************************************************************************************************************************************************************************************/		
		KeySet = qms_SelectedSource_QoIwrtQoIT.keySet().toArray();
		for(int i = 0; i < KeySet.length; i++) {
			networkData_HashMaps qms = qms_SelectedSource_QoIwrtQoIT.get((Integer) KeySet[i]);
			QoImetricScore_SelectedSource_QoIwrtQoIT.writeToFile(qms.get_runStep(), qms.get_destinationNode(), qms.get_selectedSourceNode(), qms.get_Accuracy(), qms.get_Completeness(),
					qms.get_Timeliness(), qms.get_Reliability(), i++);	i--;
		}
		
		KeySet = qms_SelectedSource_QoITwrtQoIT.keySet().toArray();
		for(int i = 0; i < KeySet.length; i++) {
			networkData_HashMaps qms = qms_SelectedSource_QoITwrtQoIT.get((Integer) KeySet[i]);
			QoImetricScore_SelectedSource_QoITwrtQoIT.writeToFile(qms.get_runStep(), qms.get_destinationNode(), qms.get_selectedSourceNode(), qms.get_Accuracy(), qms.get_Completeness(),
					qms.get_Timeliness(), qms.get_Reliability(), i++);	i--;
		}		
		/*****************************************************************************************************************************************************************************/		
	}
}
