package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTable;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.tracealignment.clustering.AgglomerativeHierarchicalClustering;
import org.processmining.plugins.tracealignment.clustering.AgglomerativeHierarchicalClustering.ClusterCriteria;
import org.processmining.plugins.tracealignment.clustering.AgglomerativeHierarchicalClustering.ClusterType;
import org.processmining.plugins.tracealignment.msa.MSA;
import org.processmining.plugins.tracealignment.util.ActivityOverFlowException;
import org.processmining.plugins.tracealignment.util.EncodeActivitySet;
import org.processmining.plugins.tracealignment.util.EncodeTraces;
import org.processmining.plugins.tracealignment.util.EncodingNotFoundException;
import org.processmining.plugins.tracealignment.util.EquivalenceClass;
import org.processmining.plugins.tracealignment.util.EuclideanDistance;
import org.processmining.plugins.tracealignment.util.EuclideanDistance.Normalization;
import org.processmining.plugins.tracealignment.util.FeatureExtraction.FeatureType;
import org.processmining.plugins.tracealignment.util.FeatureMatrix;
import org.processmining.plugins.tracealignment.util.IndelSubstitutionMatrix;
import org.processmining.plugins.tracealignment.util.TraceSimilarity;

public class ImproveDiscoveryClusterTransformation {

	private MSA traceAlignTransformer;
	private Set<String> activitySet;
	private ImproveDiscoveryClusterData ClusterData;
	private String distanceMetric = "Euclidean",
			similarityMetric = "F-Score";
	// The encoding length of activities
	int encodingLength = 1;
	String dash = "-";
	boolean isSequenceFeatureType = true;

	/*
	 * Store the original sequence/alphabet feature sets; It is map on the
	 * feature type; K-grams would be stored as kGram e.g., 3Gram, 6Gram etc
	 */
	Map<FeatureType, Set<String>> originalSequenceFeatureSetMap;
	Map<FeatureType, Map<Set<String>, Set<String>>> originalAlphabetFeatureSetMap;

	Map<String, Integer> sequenceFeatureCountMap;
	Map<Set<String>, Integer> alphabetFeatureCountMap;

	// char-activity encoding map
	Map<String, String> activityCharMap;
	Map<String, String> charActivityMap;
	
	
	// Capture the traces that are identical and keep track of their indices. This would be required to display trace details in the Alignment visualization
	Map<String, TreeSet<Integer>> encodedTraceIdenticalIndicesMap;
	
	List<String> activityList;
	JTable activityCharMapTable;
	
	float[][] similarityMatrix;
	
	Map<String, Integer> substitutionScoreMap = null;
	Map<String, Integer> indelRightGivenLeftScoreMap = null;
	
	
	boolean deriveSubstitutionScore = true;
	boolean deriveIndelScore = true;
	boolean isSubstitutionScoreDerived = true;
	boolean isIndelScoreDerived = true;
	
	public ImproveDiscoveryClusterTransformation(ImproveDiscoveryClusterData ClusterData) {
		// TODO Auto-generated constructor stub
		this.ClusterData=ClusterData;
		MakeProcessAlign();
	}
	
	public ImproveDiscoveryClusterData GetClusterData()
	{
		return ClusterData;
	}
	
	public void SetClusterData(ImproveDiscoveryClusterData ClusterData)
	{
		this.ClusterData=ClusterData;
	}
	
	public void  MakeProcessAlign()
	{

        encodedTraceIdenticalIndicesMap = new HashMap<String, TreeSet<Integer>>();
    	/*
    	 * Store the original sequence/alphabet feature sets; It is map on the
    	 * feature type; K-grams would be stored as kGram e.g., 3Gram, 6Gram etc
    	 */
        originalSequenceFeatureSetMap = new HashMap<FeatureType, Set<String>>();
    	originalAlphabetFeatureSetMap = new HashMap<FeatureType, Map<Set<String>, Set<String>>>();

		encodeLog();
		 Filtered();
		 
		AgglomerativeHierarchicalClustering ahc;
		
		EquivalenceClass equivalenceClass = new EquivalenceClass();
		ClusterData.SetFeatureMatrix(new FeatureMatrix(encodingLength, ClusterData.GetUniqueEncodedTraceList(), equivalenceClass
				.getAlphabetEquivalenceClassMap(encodingLength, ClusterData.GetFilteredFeatureSet())).getFeatureMatrix());
		
	    similarityMatrix = new TraceSimilarity(encodingLength).getFScore(ClusterData.GetFeatureMatrix());
		computeDistanceSimilarity();
	    //Distance by default	if (isDistanceSelected) 

		ahc = new AgglomerativeHierarchicalClustering(ClusterData.GetDistanceMatrix(), ClusterType.DISTANCE,
					ClusterCriteria.MIN_VARIANCE);

		IndelSubstitutionMatrix indelSubstitutionMatrix = new IndelSubstitutionMatrix(encodingLength,ClusterData.GetEncodeTraceList());
		
		//Valores para substitucionScoreMap y indeRightGivenLeftScoreMap
		
		if ((substitutionScoreMap == null) || (!isSubstitutionScoreDerived && deriveSubstitutionScore)) {
			substitutionScoreMap = indelSubstitutionMatrix.getSubstitutionScoreMap();
			isSubstitutionScoreDerived = true;
		}
		
		if ((indelRightGivenLeftScoreMap == null) || (!isIndelScoreDerived && deriveIndelScore)) {
			indelRightGivenLeftScoreMap = indelSubstitutionMatrix.getIndelRightGivenLeftScoreMap();
			isIndelScoreDerived = true;
		}
		
		traceAlignTransformer=  new MSA(encodingLength, //calculado en encodeLog
										ClusterData.GetUniqueEncodedTraceList(), //calculado en encodeLog
										ahc.getItemsJoined(), //calculado aqui en el constructor
										substitutionScoreMap, //calculado aqui en el constructor
										indelRightGivenLeftScoreMap, //calculado aqui en el constructor
										ClusterData.GetIncrementLikeSubstitutionScore(), //viene dado y debo averiguar para que sirve aumentanrlo 
										ClusterData.getScaleFactor(), // viene dato idem anterior
										ClusterData.GetGapPenalty(), // viene dado 
										1.2f);
		
		CalculateResult(traceAlignTransformer.getAlignments(ClusterData.GetNumberOfCluster()));
	}
	
	private int GetEstimateNumberOfClusters()
	{
		int cases=ClusterData.GetNumberOfCase();
		if(Math.round(cases/3)>0)
			return 3;
		else if(Math.round(cases/2)>0)
			return 2;
		else
			return -1;
	}
	
    private void computeDistanceSimilarity() 
    {	
    	
      			ClusterData.SetPreviousDistanceMetric(distanceMetric);
				ClusterData.SetDistanceMatrix(
						new EuclideanDistance(encodingLength, ClusterData.GetUniqueEncodedTraceList(),
		    	ClusterData.GetFilteredFeatureSet(),Normalization.NONE).getDistanceMatrix());
	}
	

    private void CalculateResult(List<String[]>clusterAlignmentList)
	{
		ClusterData.SetActivityClusters(new ArrayList<List<List<String>>>());
        
		String actividad;
		for(int j=0;j<clusterAlignmentList.size();j++)
		{     
			List<List<String>> Clusters= new ArrayList<List<String>>();
			 ClusterData.GetActivityClusters().add(Clusters);
		
			for(int c=0;c<clusterAlignmentList.get(j).length;c++)
			{
				
	         
				String act=clusterAlignmentList.get(j)[c];
        		List<String> ActivityList= new ArrayList<String>();
				Clusters.add(ActivityList);
                while(!act.equals(""))
				{
					
					actividad=act.substring(0,1);
					act=act.substring(1);
				    if(!actividad.equals("-"))
				    {
				    ActivityList.add(actividad);
				    }
				}
 	
			}
		}

	}
	
    public void Filtered()
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	{
		if (ClusterData.GetUnionFeatureSet() == null) {
			ClusterData.SetUnionFeatureSet(new HashSet<String>());
		}
		
		if (ClusterData.GetFilteredFeatureSet() == null) {
			ClusterData.SetFilteredFeatureSet(new HashSet<String>());
		}
		
		ClusterData.GetFilteredFeatureSet().clear();

		if (ClusterData.GetFeatureExtractionTypeSet().size() > 0) {
			if (isSequenceFeatureType) {
				Set<String> unionSequenceFeatureSet = new HashSet<String>();
				for (FeatureType featureType : originalSequenceFeatureSetMap.keySet()) {
					unionSequenceFeatureSet.addAll(originalSequenceFeatureSetMap.get(featureType));
				}
				ClusterData.GetUnionFeatureSet().addAll(unionSequenceFeatureSet);
			} else {
				Map<Set<String>, Set<String>> unionAlphabetFeatureSetMap = new HashMap<Set<String>, Set<String>>();
				Map<Set<String>, Set<String>> alphabetFeatureSetMap;
				Set<String> alphabetEquivalenceClassFeatureSet;
				for (FeatureType featureType : originalAlphabetFeatureSetMap.keySet()) {
					alphabetFeatureSetMap = originalAlphabetFeatureSetMap.get(featureType);
					for (Set<String> alphabet : alphabetFeatureSetMap.keySet()) {
						if (unionAlphabetFeatureSetMap.containsKey(alphabet)) {
							alphabetEquivalenceClassFeatureSet = unionAlphabetFeatureSetMap.get(alphabet);
						} else {
							alphabetEquivalenceClassFeatureSet = new TreeSet<String>();
						}
						alphabetEquivalenceClassFeatureSet.addAll(alphabetFeatureSetMap.get(alphabet));
						unionAlphabetFeatureSetMap.put(alphabet, alphabetEquivalenceClassFeatureSet);
					}
				}

				for (Set<String> alphabet : unionAlphabetFeatureSetMap.keySet()) {
					ClusterData.GetUnionFeatureSet().addAll(unionAlphabetFeatureSetMap.get(alphabet));
				}
			}
		}

		ClusterData.GetFilteredFeatureSet().addAll(ClusterData.GetUnionFeatureSet());
	}
	
	private void encodeLog() {

			int  traceIndex,totalNoEvents = 0;
			 activitySet = new HashSet<String>();
			XAttributeMap attributeMap;
         
			for (XTrace trace : ClusterData.GetLog()) {
				totalNoEvents += trace.size();
				for (XEvent event : trace) {
					attributeMap = event.getAttributes();
					activitySet.add(attributeMap.get("concept:name").toString() + "-"
							+ attributeMap.get("lifecycle:transition").toString());
				}
			}
    
			try {
				EncodeActivitySet encodeActivitySet = new EncodeActivitySet(activitySet);
				encodingLength = encodeActivitySet.getEncodingLength();

				for (int i = 1; i < encodingLength; i++) {
					dash += "-";
				}
				
				activityCharMap = encodeActivitySet.getActivityCharMap();
				charActivityMap = encodeActivitySet.getCharActivityMap();
			
				
				EncodeTraces encodeTraces = new EncodeTraces(activityCharMap, ClusterData.GetLog());
				
	
		
				ClusterData.SetEncodedTraceList(encodeTraces.getCharStreamList());
                ClusterData.SetUniqueEncodedTraceList(new ArrayList<String>());
				
				traceIndex = 0;
				TreeSet<Integer> encodedTraceIdenticalIndicesSet;
				

				for (String encodedTrace : ClusterData.GetEncodeTraceList()) {
				
					
					if (encodedTraceIdenticalIndicesMap.containsKey(encodedTrace)) {
						encodedTraceIdenticalIndicesSet = encodedTraceIdenticalIndicesMap.get(encodedTrace);
					} else {
						encodedTraceIdenticalIndicesSet = new TreeSet<Integer>();
						ClusterData.GetUniqueEncodedTraceList().add(encodedTrace);
					}
					encodedTraceIdenticalIndicesSet.add(traceIndex);
					encodedTraceIdenticalIndicesMap.put(encodedTrace, encodedTraceIdenticalIndicesSet);

					traceIndex++;
				}
				

				
				

				String[] columnNames = { "Activity Name", "Char Encoding" };
				String[][] activityCharValues = new String[activityCharMap.size()][2];
				int index = 0;
				for (String activity : activityCharMap.keySet()) {
					activityCharValues[index][0] = activity;
					activityCharValues[index++][1] = activityCharMap.get(activity);
				}
				activityCharMapTable = new JTable(activityCharValues, columnNames);
				activityCharMapTable.setAutoCreateRowSorter(true);

			}
			
			catch (ActivityOverFlowException e) {
				
			} catch (EncodingNotFoundException e) {
				
			}
		
		}


	
	

}
