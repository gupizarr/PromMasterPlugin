package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.plugins.tracealignment.util.FeatureExtraction.FeatureType;

public class ImproveDiscoveryClusterData {
    
	private List<List<List<String>>> ActivityClusters= new ArrayList<List<List<String>>>();
	private XLog mainLog;
	String fileSeparator = System.getProperty("file.separator");
	String outputDir = System.getProperty("java.io.tmpdir")+fileSeparator+"ProM"+fileSeparator+"TraceAlignment";
	String delim = "@";
	UIPluginContext context;
	XLog log;
	List<XTrace> traceList = new ArrayList<XTrace>();
	boolean isLogEncoded = false;
	
	private Set<FeatureType> previousFeatureExtractionTypeSet = new HashSet<FeatureType>();
	private Set<FeatureType> featureExtractionTypeSet = new HashSet<FeatureType>();

	int prevMinFrequencyCountThreshold = 0, minFrequencyCountThreshold;
	float prevMinInstanceCountPercentageThreshold = 0f, minInstanceCountPercentageThreshold;



	boolean hasOutliers = false;

	Set<String> unionFeatureSet;
	Set<String> filteredFeatureSet;

	Set<String> filteredSequenceFeatureSet;
	Set<Set<String>> filteredAlphabetFeatureSet;

	int[][] featureMatrix;
	float[][] distanceMatrix;


	String previousDistanceMetric = "", previousSimilarityMetric = "";
	boolean isPreviousDistanceSelected = true;
	boolean isDistanceSelected = true;

	// Store the size of the k-gram; The default value is 3
	int kGramSize = 3;





	int prevNoClusters = -1;
	private int noClusters;

	private int incrementLikeSubstitutionScore = 3; // default value for incrementing like substitution score
	private int gapPenalty = Integer.MIN_VALUE; // if set to this, it will be automatically assigned in alignment
	private float scaleFactor = 0.1f; // default value

	int prevIncrementLikeSubstitutionScore = -1;
	int prevGapPenalty = -1;
	float prevScaleFactor = -1;

	// List of encoded traces
	List<String> encodedTraceList;

	// List of unique encoded traces
	List<String> uniqueEncodedTraceList;





	// activity color map

	int MaxPriority = 10;
	int numColumnSorts;

	public ImproveDiscoveryClusterData(XLog log, int noClusters)  {
		// TODO Auto-generated constructor stub
		this.noClusters=noClusters;
		this.mainLog=log;      
	}
	
	public int GetNumberOfCase()
	{
		List<String> uniqueCase= new ArrayList<String>();
		
		for(int j=0;j<encodedTraceList.size();j++)
		{
			if(!uniqueCase.contains(encodedTraceList.get(j)))
			{
				uniqueCase.add(encodedTraceList.get(j));
			}
		}
		
		int numberOfClusterCase= uniqueCase.size()/2;
		if(numberOfClusterCase%2!=0)
		{
			numberOfClusterCase=numberOfClusterCase-1;
		}
		return numberOfClusterCase;
	}
	
	public void SetNumberOfClusters(int newNumber )
	{
		noClusters=	newNumber;
	}
	
	public void SetFeatureMatrix(int [][] featureMatrix)
	{
		this.featureMatrix=featureMatrix;
	}
	
	public void SetPreviousDistanceMetric(String newPreviousDistanceMetric)
	{
		previousDistanceMetric= newPreviousDistanceMetric;
	}
	
	public void SetDistanceMatrix( float[][] newDMatrix)
	{
		distanceMatrix=newDMatrix;
	}
	
	public float[][] GetDistanceMatrix()
	{
		return distanceMatrix;
	}
	
	public List<String> GetUniqueEncodedTraceList()
	{
		return uniqueEncodedTraceList;
	}
	
	public void SetUniqueEncodedTraceList( ArrayList<String> List)
	{
		uniqueEncodedTraceList=List;
	}
	
	public Set<String> GetFilteredFeatureSet()
	{
		return filteredFeatureSet;
	}
	
	public void SetFilteredFeatureSet(Set<String> filteredFeatured)
	{
		filteredFeatureSet=filteredFeatured;
	}

	public List<List<List<String>>>  GetActivityClusters()
	{
		return ActivityClusters;
	}
	
	public void SetActivityClusters(List<List<List<String>>> ActList)
	{
		ActivityClusters=ActList;
	}
	
	public void SetUnionFeatureSet(Set<String> newUnionFeatureSet)
	{
		unionFeatureSet= newUnionFeatureSet;
	}
	
	public Set<String> GetUnionFeatureSet()
	{
		return unionFeatureSet;
	}
	
	public 	Set<FeatureType> GetPreviousFeatureExtractionTypeSet()
	{
		return previousFeatureExtractionTypeSet;
	}
	
	public  Set<FeatureType> GetFeatureExtractionTypeSet()
	{
		return featureExtractionTypeSet;
	}
	
	public XLog GetLog()
	{
		return mainLog;
	}
	
	public void SetEncodedTraceList(List<String> EncodeTraceList)
	{
		encodedTraceList=EncodeTraceList;
	}
	
	public List<String> GetEncodeTraceList()
	{
		return encodedTraceList;
	}
	
	public int[][] GetFeatureMatrix()
	{
		return  featureMatrix;
	}
	
	public void setIncrementLikeSubstitutionScore(int num)
	{
		incrementLikeSubstitutionScore=num;
		
	}
	
	public int GetIncrementLikeSubstitutionScore()
	{
		return incrementLikeSubstitutionScore;
	}
	
	public int GetGapPenalty()
	{
		return gapPenalty;
	}
	
	public void SetGapPenalty(int gapP)
	{
		gapPenalty=gapP;
	}
	
	public void SetScaleFactor(float factor)
	{
		 scaleFactor=factor;
	}
	
	public float getScaleFactor()
	{
		return scaleFactor;
	}
	
	
	
	public int GetNumberOfCluster()
	{
		return noClusters;
	}
	
	public int GetNumberOfCaseOnCluster(int count)
	{
	 return	ActivityClusters.get(count).size();
	}

}
