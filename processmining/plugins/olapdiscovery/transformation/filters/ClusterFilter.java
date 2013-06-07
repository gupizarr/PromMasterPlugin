package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.filters;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XAttributeMapLazyImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPClusterTransformation;

/**
 *  Methods to filter log in a Cluster context
*/
public class ClusterFilter {

	  private ArrayList<XTrace> Traces;
	  private List<List<String>> AllFilterTraces;
	  private int currentCluster=-1;
      private OLAPClusterTransformation ClusterTransformation;
      private ArrayList<String> RemovedClusters;
      private ArrayList<List<String>> RemovedParticularCases;

	  public ClusterFilter(OLAPClusterTransformation ClusterTransformation ,ArrayList<XTrace> Trace) {
		// TODO Auto-generated constructor stub
		this.ClusterTransformation=ClusterTransformation;		
		this.RemovedClusters= new ArrayList<String>();
		this.RemovedParticularCases= new ArrayList<List<String>>();
		this.Traces= Trace;


	}
	
	 public void  SearchAndRemoveTrace(XTrace trace)
	 {
		    boolean removeTrace=false; 
			for(int c=0;c<RemovedParticularCases.size();c++)
			{  	//Comienza el trace
					 if(IsTheSameTrace(trace,RemovedParticularCases.get(c)))
					 {
						 removeTrace=true;
					 }					 		    			  	 			    		
			}
	  	 
			if(!removeTrace && !Traces.contains(trace))
			{
			 Traces.add(trace);
			}
	  }
	 
	  public void GroupClusters()
	   {
		  AllFilterTraces= new ArrayList<List<String>>();
		  Traces = new ArrayList<XTrace>();
        int count=0;
        for(int j=0;j<this.RemovedClusters.size();j++)
		  {
          count=Integer.parseInt(RemovedClusters.get(j));
     	   AllFilterTraces.addAll(this.ClusterTransformation.GetClusterData().GetActivityClusters().get(count));
		  }
	   }

	 
	   public void SearchTrace(XTrace trace)
	   { 
    	    boolean removeTrace=false; 

			for(int c=0;c<AllFilterTraces.size();c++)
			{  	//Comienza el trace
					 if(IsTheSameTrace(trace,AllFilterTraces.get(c)))
					 {
						 removeTrace=true;
					 }					 		    			  	 			    		
			}
	  	 
			if(!removeTrace && !Traces.contains(trace))
			{ 
			 Traces.add(trace);
			}
	  }
	   
	  public ArrayList<XTrace> GetResult()
	  {
		  return Traces;
	  }
	   
	   
	   public void RestoreSubCluster(int cluster,int  trace_ini, int trace_last)
	   {
	  		
	  		for(int j=trace_ini;j<trace_last;j++)
	  		{
			List<String> RemovedCase=this.ClusterTransformation.GetClusterData().GetActivityClusters().get(cluster).get(j);
		    if(RemovedParticularCases.contains(RemovedCase))
		    {
		    	RemovedParticularCases.remove(RemovedCase);
		    }
	  		}
		   
	    }
	      
	   public void RestoreCase(int cluster,int  caseIndex)
	   { 		
	  
			List<String> RemovedCase=this.ClusterTransformation.GetClusterData().GetActivityClusters().get(cluster).get(caseIndex);
		    if(RemovedParticularCases.contains(RemovedCase))
		    {
		    	RemovedParticularCases.remove(RemovedCase);
		    }
	  			   
		    

	    }
	      
	  	public void ClearTraceList()
		{
	        RemovedParticularCases.clear();
			//this.GetData().ReturnToBaseLog();

		}
	  	
		public void RemoveTraceAndFilter(int caseIndex,int clusterIndex)
		{
			if(currentCluster!=clusterIndex)
			{
				currentCluster=clusterIndex;
				ClearTraceList();
			}
			
			List<String> RemovedCase=this.ClusterTransformation.GetClusterData().GetActivityClusters().get(clusterIndex).get(caseIndex);
	    	
		    if(!RemovedParticularCases.contains(RemovedCase) && caseIndex!=-1)
		    {
	         RemovedParticularCases.add(RemovedCase);
		    }
			 Traces = new ArrayList<XTrace>();

		}
		
	    public void RemoveTracesFromSubCluster(int cluster, int trace_ini,int trace_last)
	    {
			if(currentCluster!=cluster)
			{
				currentCluster=cluster;
				ClearTraceList();
			}
			
			for(int j=trace_ini;j<trace_last;j++)
			{
			List<String> RemovedCase=this.ClusterTransformation.GetClusterData().GetActivityClusters().get(cluster).get(j);
	    	
		    if(!RemovedParticularCases.contains(RemovedCase) && j!=-1)
		    {
	         RemovedParticularCases.add(RemovedCase);
		    }
		    
			}
	    }
	    
		public void RemoveClusterAndFilter(String cluster)
		{
		    if(!RemovedClusters.contains(cluster) && !cluster.equals(""))
		    {
			RemovedClusters.add(cluster);
		    }
		  
		}
		
		
		public void ClearRemovedClusters()
		{
			RemovedClusters.clear();
		//	Data.ReturnToBaseLog();

		}
		
		public void RestoreCluster(String cluster)
		{
				
		    if(RemovedClusters.contains(cluster))
		    {
		    	RemovedClusters.remove(cluster);
		    }
		   
		}
		
		public boolean IsTheSameTrace(XTrace trace, List<String> secuence)
		{
			  int activity_count;
			  int current_event;
			  XAttributeMap attributeMap;
			  String alignName;
			  String char_activity;
			  if(trace.size()==secuence.size())
			  {
	    		 activity_count=0;
			     current_event=0;
			     for (XEvent event : trace) 
				 {	
						attributeMap = event.getAttributes();
						alignName=attributeMap.get("concept:name").toString() + "-"
								+ attributeMap.get("lifecycle:transition").toString();
					    char_activity=ClusterTransformation.GetActivityCharMap().get(alignName);
					    if(secuence.get(current_event).equals(char_activity))
					    {
					    	activity_count++;
					    }
					    current_event++;
				 }
			     if(activity_count==current_event)
			     {
			    	 return true;
			     }
			  }
					
			return false;
		}	   
		
		public void Reset()
		{
			  RemovedClusters.clear();
		      RemovedParticularCases.clear();
			
		}
		
		  public ArrayList<XTrace> FilterCaseHeuristicModel(String cluster,String variant , XLog log)
		  {
	              List<String> secuence=this.ClusterTransformation.GetClusterData().GetActivityClusters().get(Integer.parseInt(cluster)).get(Integer.parseInt(variant));
	              ArrayList<XTrace> SelectedTraces= new ArrayList<XTrace>();
	                for(int u=0; u<log.size(); u++)
	 			    {	
		 		    	     XTrace trace = log.get(u);   
				 		     if(IsTheSameTrace(trace,secuence))
		 					 {
				 		    	 SelectedTraces.add(trace);
				 		    	 
		 					 }					 		    			  	 			    		
		 			}
				  	 
		
			  
		          XLog emptyList= new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class));
		          emptyList.addAll(SelectedTraces);
		          
		          return SelectedTraces;
		
		  }

}
