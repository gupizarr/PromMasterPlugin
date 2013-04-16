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
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
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

public class ImproveDiscoveryTransformation {
    
	private List<List<List<List<String>>>> ActivityClusters= new ArrayList<List<List<List<String>>>>();
	private Set<String> activitySet;
	private MSA traceAlignTransformer;
	private XLog mainLog;
	String fileSeparator = System.getProperty("file.separator");
	String outputDir = System.getProperty("java.io.tmpdir")+fileSeparator+"ProM"+fileSeparator+"TraceAlignment";
	String delim = "@";
	UIPluginContext context;
	XLog log;
	List<XTrace> traceList = new ArrayList<XTrace>();
	boolean isLogEncoded = false;
	Set<FeatureType> previousFeatureExtractionTypeSet = new HashSet<FeatureType>();
	Set<FeatureType> featureExtractionTypeSet = new HashSet<FeatureType>();

	int prevMinFrequencyCountThreshold = 0, minFrequencyCountThreshold;
	float prevMinInstanceCountPercentageThreshold = 0f, minInstanceCountPercentageThreshold;

	// The encoding length of activities
	int encodingLength = 1;
	String dash = "-";

	boolean hasOutliers = false;
	/*
	 * Store the original sequence/alphabet feature sets; It is map on the
	 * feature type; K-grams would be stored as kGram e.g., 3Gram, 6Gram etc
	 */
	Map<FeatureType, Set<String>> originalSequenceFeatureSetMap = new HashMap<FeatureType, Set<String>>();
	Map<FeatureType, Map<Set<String>, Set<String>>> originalAlphabetFeatureSetMap = new HashMap<FeatureType, Map<Set<String>, Set<String>>>();

	Map<String, Integer> sequenceFeatureCountMap;
	Map<Set<String>, Integer> alphabetFeatureCountMap;

	Set<String> unionFeatureSet;
	Set<String> filteredFeatureSet;

	Set<String> filteredSequenceFeatureSet;
	Set<Set<String>> filteredAlphabetFeatureSet;

	boolean isSequenceFeatureType = true;

	int[][] featureMatrix;
	float[][] distanceMatrix;
	float[][] similarityMatrix;

	String previousDistanceMetric = "", previousSimilarityMetric = "";
	String distanceMetric = "Euclidean", similarityMetric = "F-Score";
	boolean isPreviousDistanceSelected = true;
	boolean isDistanceSelected = true;

	// Store the size of the k-gram; The default value is 3
	int kGramSize = 3;

	boolean deriveSubstitutionScore = true;
	boolean deriveIndelScore = true;
	boolean isSubstitutionScoreDerived = true;
	boolean isIndelScoreDerived = true;

	Map<String, Integer> substitutionScoreMap = null;
	Map<String, Integer> indelRightGivenLeftScoreMap = null;

	int prevNoClusters = -1;
	private int noClusters = 3;

	int incrementLikeSubstitutionScore = 3; // default value for incrementing like substitution score
	int gapPenalty = Integer.MIN_VALUE; // if set to this, it will be automatically assigned in alignment
	float scaleFactor = 0.1f; // default value

	int prevIncrementLikeSubstitutionScore = -1;
	int prevGapPenalty = -1;
	float prevScaleFactor = -1;

	// List of encoded traces
	List<String> encodedTraceList;

	// List of unique encoded traces
	List<String> uniqueEncodedTraceList;

	// Capture the traces that are identical and keep track of their indices. This would be required to display trace details in the Alignment visualization
	Map<String, TreeSet<Integer>> encodedTraceIdenticalIndicesMap = new HashMap<String, TreeSet<Integer>>();

	// char-activity encoding map
	Map<String, String> activityCharMap;
	Map<String, String> charActivityMap;

	List<String> activityList;
	JTable activityCharMapTable;
	// activity color map

	int MaxPriority = 10;
	int numColumnSorts;

	public ImproveDiscoveryTransformation(XLog log)  {
		// TODO Auto-generated constructor stub
		this.mainLog=log;      
	}
	
	public List<List<List<List<String>>>>  GetProcessAlig()
	{
		encodeLog();
		
		 Filtered();
		 
		AgglomerativeHierarchicalClustering ahc;
		EquivalenceClass equivalenceClass = new EquivalenceClass();
		featureMatrix = new FeatureMatrix(encodingLength, uniqueEncodedTraceList, equivalenceClass
				.getAlphabetEquivalenceClassMap(encodingLength, filteredFeatureSet)).getFeatureMatrix();
		similarityMatrix = new TraceSimilarity(encodingLength).getFScore(featureMatrix);
		computeDistanceSimilarity();
	    //Distance by default	if (isDistanceSelected) 
			ahc = new AgglomerativeHierarchicalClustering(distanceMatrix, ClusterType.DISTANCE,
					ClusterCriteria.MIN_VARIANCE);

		IndelSubstitutionMatrix indelSubstitutionMatrix = new IndelSubstitutionMatrix(encodingLength, encodedTraceList);
		
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
										uniqueEncodedTraceList, //calculado en encodeLog
										ahc.getItemsJoined(), //calculado aqui en el constructor
										substitutionScoreMap, //calculado aqui en el constructor
										indelRightGivenLeftScoreMap, //calculado aqui en el constructor
										incrementLikeSubstitutionScore, //viene dado y debo averiguar para que sirve aumentanrlo 
										scaleFactor, // viene dato idem anterior
										gapPenalty , // viene dado 
										1.2f);
		return  GetResult(traceAlignTransformer.getAlignments(noClusters));
	}
	
    private void computeDistanceSimilarity() {
		
	           //By Default isSequenceFeatureType
				previousDistanceMetric = distanceMetric;
				
					distanceMatrix = new EuclideanDistance(encodingLength, uniqueEncodedTraceList, filteredFeatureSet,
							Normalization.NONE).getDistanceMatrix();
				
				
	/*
					
					//By default  F-score
					previousSimilarityMetric = similarityMetric;
					if (isSequenceFeatureType) {
						featureMatrix = (new FeatureMatrix(encodingLength, uniqueEncodedTraceList, filteredFeatureSet))
								.getFeatureMatrix();
					} else {
						EquivalenceClass equivalenceClass = new EquivalenceClass();
						featureMatrix = (new FeatureMatrix(encodingLength, uniqueEncodedTraceList, equivalenceClass
								.getAlphabetEquivalenceClassMap(encodingLength, filteredFeatureSet))).getFeatureMatrix();
					}

					similarityMatrix = new TraceSimilarity(encodingLength).getFScore(featureMatrix);
		*/	


	}
	
	public List<List<List<List<String>>>>  GetClusters()
	{
		return ActivityClusters;
	}
	private List<List<List<List<String>>>>   GetResult(List<String[]>clusterAlignmentList)
	{
		ActivityClusters= new ArrayList<List<List<List<String>>>>();
          String actividad;
		for(int j=0;j<clusterAlignmentList.size();j++)
		{     
			  List<List<List<String>>> Clusters= new ArrayList<List<List<String>>>();
			  ActivityClusters.add(Clusters);
			for(int c=0;c<clusterAlignmentList.get(j).length;c++)
			{
				List<List<String>> Traces= new ArrayList<List<String>>();
	            Clusters.add(Traces);
				String act=clusterAlignmentList.get(j)[c];
        		List<String> ActivityList= new ArrayList<String>();
				Traces.add(ActivityList);
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

		return ActivityClusters;



		
	}
	public void Filtered()
	{
		if (unionFeatureSet == null) {
			unionFeatureSet = new HashSet<String>();
		}
		
		if (filteredFeatureSet == null) {
			filteredFeatureSet = new HashSet<String>();
		}
		
		filteredFeatureSet.clear();

		if (featureExtractionTypeSet.size() > 0) {
			if (isSequenceFeatureType) {
				Set<String> unionSequenceFeatureSet = new HashSet<String>();
				for (FeatureType featureType : originalSequenceFeatureSetMap.keySet()) {
					unionSequenceFeatureSet.addAll(originalSequenceFeatureSetMap.get(featureType));
				}
				unionFeatureSet.addAll(unionSequenceFeatureSet);
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
					unionFeatureSet.addAll(unionAlphabetFeatureSetMap.get(alphabet));
				}
			}
		}

		filteredFeatureSet.addAll(unionFeatureSet);
	}
	
	  private void encodeLog() {

			int  traceIndex,totalNoEvents = 0;
			 activitySet = new HashSet<String>();
			XAttributeMap attributeMap;

			for (XTrace trace : mainLog) {
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
			
				
				EncodeTraces encodeTraces = new EncodeTraces(activityCharMap, this.mainLog);
				
				encodedTraceList = encodeTraces.getCharStreamList();

				uniqueEncodedTraceList = new ArrayList<String>();
				traceIndex = 0;
				TreeSet<Integer> encodedTraceIdenticalIndicesSet;
				for (String encodedTrace : encodedTraceList) {
					if (encodedTraceIdenticalIndicesMap.containsKey(encodedTrace)) {
						encodedTraceIdenticalIndicesSet = encodedTraceIdenticalIndicesMap.get(encodedTrace);
					} else {
						encodedTraceIdenticalIndicesSet = new TreeSet<Integer>();
						uniqueEncodedTraceList.add(encodedTrace);
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
