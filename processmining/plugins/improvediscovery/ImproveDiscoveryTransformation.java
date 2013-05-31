package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XAttributeMapLazyImpl;
import org.deckfour.xes.model.impl.XEventImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.heuristics.elements.Activity;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.HeuristicsMiner;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationGenerator;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;

 public class ImproveDiscoveryTransformation {
    
	
	private ImproveDiscoveryData Data;
	private ImproveDiscoveryClusterTransformation ClusterTransformation;
	private ImproveDiscoverySocialTransformation SocialTransformation;
	private ArrayList<String> RemovedClusters;
	private ArrayList<List<String>> RemovedParticularCases;
	private List<XTrace> Traces;
	private PluginContext context;
    private boolean fixLog=false;
    private ArrayList<String> RemovedResources;  
    private int currentCluster=-1;
    private XLog SocialBackUpUnit;

	public ImproveDiscoveryTransformation(ImproveDiscoveryData Data, final PluginContext context)  {
		// TODO Auto-generated constructor stub
		this.RemovedClusters= new ArrayList<String>();
		this.RemovedParticularCases= new ArrayList<List<String>>();
		this.context=context;
		this.Data=Data;
		this.ClusterTransformation= new ImproveDiscoveryClusterTransformation(new ImproveDiscoveryClusterData(Data.GetCurrentLog(),3));
		this.SocialTransformation= new ImproveDiscoverySocialTransformation(Data);
		SocialTransformation.WTCalculation();
		Traces= new ArrayList<XTrace>();
		RemovedResources= new ArrayList<String>();
	   
	}
	
	public void SetXLogBackUpUnit()
	{
		SocialBackUpUnit= (XLog) this.Data.GetBaseLog().clone();
	}
	public ImproveDiscoverySocialTransformation GetSocialTransformation()
	{
	return 	SocialTransformation;
	}
	
	public void SetSocialTransformation(ImproveDiscoverySocialTransformation ST)
	{
	SocialTransformation=ST;
	}
	
	public ImproveDiscoveryData GetData()
	{
		return Data;
	}
	
	public ImproveDiscoveryClusterTransformation GetClusterTransformation()
	{
		return ClusterTransformation;	
	}
	
	public void SetClusterTransformation(ImproveDiscoveryClusterTransformation ClusterTransformation)
	{
		this.ClusterTransformation=ClusterTransformation;
	}
	
	public void PerformanceFilter(double minTimeValue, double maxTimeValue)
	{
		 ArrayList<XTrace> KeptXTrace= new ArrayList<XTrace>();
 	    
		 
		 System.out.print("\n min:"+minTimeValue);
		 System.out.print("\n max:"+maxTimeValue);

			 for(int i=0; i<Data.GetBaseLog().size(); i++)
		     {
				    if((Data.GetPerformanceData().getFinalTimes()[i])>=minTimeValue && (Data.GetPerformanceData().getFinalTimes()[i])<=maxTimeValue)
				    {    		 
		       		 KeptXTrace.add(Data.GetBaseLog().get(i));
				    }
		    	 
		     }
		 

		 Data.SetCurrentLog( new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class)));
	     Data.GetCurrentLog().addAll(KeptXTrace);
	     Data.ResetPerformanceData();
         UpdateGraphWithPerformanceChanges();

	}
	
	public void SetNumberOfClusters(int newNumber)
	{
		ClusterTransformation.GetClusterData().SetNumberOfClusters(newNumber);
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
				    char_activity=ClusterTransformation.activityCharMap.get(alignName);
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
	
	public void ClearRemovedClusters()
	{
		RemovedClusters.clear();
		Data.ReturnToBaseLog();

	}
	
	public void RestoreCluster(String cluster)
	{
		this.GetData().ReturnToBaseLog();
			
	    if(RemovedClusters.contains(cluster))
	    {
	    	RemovedClusters.remove(cluster);
	    }
	   
	    ClusterFilter();
	    
	}
    
	public void RemoveClusterAndFilter(String cluster)
	{
	    if(!RemovedClusters.contains(cluster) && !cluster.equals(""))
	    {
		RemovedClusters.add(cluster);
	    }
	    ClusterFilter();
	}
	
	public void ClusterFilter()
	{
		
		
		    System.out.print("\n Log size in cluster filter:"+Data.GetCurrentLog().size());		    
		    RemoveTracesFromClusters();  
		    System.out.print("\n Log size after cluster filter:"+Traces.size());
		    UpdateForClusters();
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
	    TracesFilter(cluster);
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
	    TracesFilter(clusterIndex);
	}
	
	
	public void ClearTraceList()
	{
        RemovedParticularCases.clear();
		this.GetData().ReturnToBaseLog();

	}
	
	public void TracesFilter(int clusterIndex)
	{
		
		
		   // System.out.print("\n Log size in cluster filter:"+Data.GetCurrentLog().size());		    
		    RemoveTracesFromTraces(clusterIndex);  
		   // System.out.print("\n Log size after cluster filter:"+Traces.size());
		    UpdateForClusters();
	}
	
	public void RemoveTracesFromTraces(int clusterIndex)
	{
	
		 Traces = new ArrayList<XTrace>();
		 boolean removeTrace=false;
             
      	 		
		      for (int u=0; u<this.Data.GetCurrentLog().size(); u++)
			  {
		 	       XTrace trace = this.Data.GetCurrentLog().get(u);   
		 		   removeTrace=false; 
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
	
      	  
	}
	
	public void RemoveTracesFromClusters()
	{
		  Traces = new ArrayList<XTrace>();
		  boolean removeTrace=false;
          int count=0;
		  List<List<String>> AllFilterTraces= new ArrayList<List<String>>();
       	  if(RemovedClusters.size()!=this.ClusterTransformation.GetClusterData().GetActivityClusters().size())
       	  { 
          for(int j=0;j<this.RemovedClusters.size();j++)
		  {
        	 count=Integer.parseInt(RemovedClusters.get(j));
        	 AllFilterTraces.addAll(this.ClusterTransformation.GetClusterData().GetActivityClusters().get(count));
		  }
		 			
		      for (int u=0; u<this.Data.GetBaseLog().size(); u++)
 			  {
		 	       XTrace trace = this.Data.GetBaseLog().get(u);   
		 		   removeTrace=false; 
              	 
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
	
       	  }
	}
	 
	public void UpdateForClusters()
	  {
	
		  this.Data.SetCurrentLog(new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class)));
		  this.Data.GetCurrentLog().addAll(Traces);
	
		  
		  if(!Data.GetCurrentLog().isEmpty() && Data.GetCurrentLog().get(0).size()==1)
		  {
		  FixLog();
		  }
		  
		  if(Data.GetCurrentLog().size()==0)
		  {	
			  XTrace trace= new XTraceImpl(new XAttributeMapImpl());
			  XEvent event= new XEventImpl();
			  trace.add(event);
			  Data.GetCurrentLog().add(trace);
			  this.Data.IsEmpty(true);
		  }
		  BuiltHeuristic();
	  }
	  
	  public void BuiltHeuristic()
	  {
		  if(Data.GetCurrentLog().size()>0)
		  {
		  XLogInfo Info=XLogInfoFactory.createLogInfo(this.Data.GetCurrentLog());
		  HeuristicsMiner fhm = new HeuristicsMiner(context, Data.GetCurrentLog(),Info ); 
		  this.Data.setHeuristicNet(fhm.mine()); 
	      this.Data.GetHNetSettings().setShowingUnconnectedTasks(true);
		  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
		  this.Data.SetHNetSettings(new AnnotatedVisualizationSettings());
		  this.Data.setHeuristicsNetGraph(generator.generate(this.Data.getHeuristicNet(),this.Data.GetHNetSettings()));
		  		  
		  }
		  else
		  {
			  ArrayList<Activity> act= new ArrayList<Activity>();
		      Iterator<Activity> activities = this.Data.getHeuristicsNetGraph().getActivities().iterator();
		      
		      while(activities.hasNext()) {
		         act.add(activities.next());
		      }

			  this.Data.getHeuristicsNetGraph().getActivities().removeAll(act);
		
		  }
		  
		  
		  
	  }
	
	  public void ResetLog()
	  {
		  RemovedClusters.clear();
	      RemovedParticularCases.clear();
		  RemovedResources.clear();
		  Data.ResetToOriginalLog();
		  GenerateHeuristicModel();
	  }
	  public void UpdateGraphWithPerformanceChanges()
	  {
		
		  	  if(Data.GetCurrentLog().size()==0)
		  	  {	
			  System.out.print("\n refill");

			  XTrace trace= new XTraceImpl(new XAttributeMapImpl());
			  XEvent event= new XEventImpl();
			  trace.add(event);
			  Data.GetCurrentLog().add(trace);
		  	  }
		  	  else if(!Data.GetCurrentLog().isEmpty() && Data.GetCurrentLog().get(0).size()==1)
			  {
				  System.out.print("\n fixlog");
			  FixLog();
			  }
			  
			  System.out.print("\n current size: "+Data.GetCurrentLog().size());
			  XLogInfo Info=XLogInfoFactory.createLogInfo(this.Data.GetCurrentLog());
			  HeuristicsMiner fhm = new HeuristicsMiner(context, Data.GetCurrentLog(),Info ); 
			  this.Data.setHeuristicNet(fhm.mine()); 
		      this.Data.GetHNetSettings().setShowingUnconnectedTasks(true);
			  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
			  this.Data.setAnnotatedVisualizationSettings(new AnnotatedVisualizationSettings());
			  this.Data.setHeuristicsNetGraph(generator.generate(this.Data.getHeuristicNet(),this.Data.GetHNetSettings()));
		  	
	  }
	  public void ShowLog()
	  {
		      int num=0;
              for (int u=0; u<this.Data.GetCurrentLog().size(); u++)
			  {	        	  
	        	    // XTrace trace = (XTrace)this.Data.GetBaseLog().get(u).clone(); 	
	        	    XTrace trace = this.Data.GetCurrentLog().get(u); 	
	        	    
	        			
			      				
			        	            num+= trace.size();
			        	            
			      			
	        	    
			  }
              System.out.print("\n number of events: "+num);
	  }
	  public void UpdateGraphWithSocialChanges()
	  {
		
		      
		   

			 ShowLog();
			 
			  if(!Data.GetCurrentLog().isEmpty() && Data.GetCurrentLog().get(0).size()==1)
			  {
			  System.out.print("\n fixed");
			  FixLog();
			  }
			  
			  if(Data.GetCurrentLog().size()==0)
			  {	
				  System.out.print("\n refill");

				  XTrace trace= new XTraceImpl(new XAttributeMapImpl());
				  XEvent event= new XEventImpl();
				  trace.add(event);
				  Data.GetCurrentLog().add(trace);
			  }
		  
			  if(Data.GetCurrentLog().size()>0)
			  {
				  GenerateHeuristicModel();	  
			  }
			  else
			  {
			  ArrayList<Activity> act= new ArrayList<Activity>();
		      Iterator<Activity> activities = this.Data.getHeuristicsNetGraph().getActivities().iterator();
		      while(activities.hasNext()) {
		         act.add(activities.next());
		      }

			  	//this.Data.getHeuristicsNetGraph().getActivities().removeAll(act);
			  }
			  
			  //update socialview
			  this.SocialTransformation.SetWorkingTogetherToShow();

		  }
		 
			
	  
	  
	  
	  public void GenerateHeuristicModel()
	  {
		  
		  XLogInfo Info=XLogInfoFactory.createLogInfo(this.Data.GetCurrentLog());
		  HeuristicsMiner fhm = new HeuristicsMiner(context, Data.GetCurrentLog(),Info ); 
		  this.Data.setHeuristicNet(fhm.mine()); 
	      this.Data.GetHNetSettings().setShowingUnconnectedTasks(true);
		  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
		  this.Data.setAnnotatedVisualizationSettings(new AnnotatedVisualizationSettings());
		  this.Data.setHeuristicsNetGraph(generator.generate(this.Data.getHeuristicNet(),this.Data.GetHNetSettings()));
	  
	  }
	  
	  public void GenerateHeuristicModel(boolean remove)
	  {
		  XLogInfo Info;
		  HeuristicsMiner fhm;
		  if(!remove)
		  {
		   Info=XLogInfoFactory.createLogInfo(this.Data.GetCurrentLog());
		   fhm = new HeuristicsMiner(context, Data.GetCurrentLog(),Info ); 
		  }
		  else
		  {
			  //carga el log con exactamente los cambios que debe restructurar
			  Info=XLogInfoFactory.createLogInfo(this.Data.GetCurrentLog());
			  fhm = new HeuristicsMiner(context, Data.GetCurrentLog(),Info ); 			  
		  }
		  this.Data.setHeuristicNet(fhm.mine()); 
	      this.Data.GetHNetSettings().setShowingUnconnectedTasks(true);
		  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
		  this.Data.setAnnotatedVisualizationSettings(new AnnotatedVisualizationSettings());
		  this.Data.setHeuristicsNetGraph(generator.generate(this.Data.getHeuristicNet(),this.Data.GetHNetSettings()));
	  }
	  
	  public void FixLog()
	  {
		  fixLog=false;
		  int count=0;
		  for(int j=0;j<this.Data.GetCurrentLog().size();j++)
		  {
			  if(Data.GetCurrentLog().get(j).size()==1)
			  {
				  count++;
			  }
		  }
		  if(this.Data.GetCurrentLog().size()==count)
		  {
			  for(int j=0;j<this.Data.GetCurrentLog().size();j++)
			  {
				  XEvent event= new XEventImpl();
				  Data.GetCurrentLog().get(j).add(event);
			  }
			  fixLog=true;
			  
			  
		  }
	  }
	  
	  public boolean GetFixCase()
	  {
		  return fixLog;
	  }
	  
	  public void SocialFilter(String Rols)
	  {
		    if(!Rols.equals(""))
		    Rols= this.SocialTransformation.TranslateNode(Integer.parseInt(Rols));
		    
		    Traces.clear();

		    if(!RemovedResources.contains(Rols))
		    {
			RemovedResources.add(Rols);
		    }
		 
		    SocialFilterRemove();
	  }
	  
	
	  public void RestoreBaseXLog()
	  {
		System.out.print("\n backup log size:"+ this.SocialBackUpUnit.size());
		this.Data.setBaseLog(this.SocialBackUpUnit);  
	  }
	  
	  public void SocialFilterRemove()
	  {
		    
	        String resource;
	        ShowLog();
			System.out.print("\n Log size before:"+ this.GetData().GetBaseLog().size());
            Traces.clear();

			//por cada persona
	       System.out.print("\n personas a remover:"+RemovedResources);
	          for (int u=0; u<this.Data.GetBaseLog().size(); u++)
			  {	        	  

	        	    XTrace trace= new XTraceImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class));
	        	    Traces.add(trace);
	        	    //si es que tiene eventos
	        	    if(this.Data.GetBaseLog().get(u).size()>0)
	        	    {
	        	    	//por cada evento
				 			for (XEvent event : this.Data.GetBaseLog().get(u)) 
							{		
			        	            resource=XLogInfoFactory.createLogInfo(this.Data.GetBaseLog()).getResourceClasses().getClassOf(event).getId();
					     		    //si el evento es ejecutado por la persona que se quiere remover
			        	            if(!RemovedResources.contains(resource))
					     			{
			        	            	trace.add(event);	     				          
					     			}  			        	     
							}
			   	    }
	        	    if(trace.size()==0)
	        	    	Traces.remove(trace);
			  } 
	 		
	         
		      System.out.print("\n se mantienen: "+Traces.size());

		      this.Data.SetCurrentLog(new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class)));
			  this.Data.GetCurrentLog().addAll(Traces);
			  System.out.print("\n Log size after:"+ this.GetData().GetCurrentLog().size());
			//		  RestoreBaseXLog();
	  
	         UpdateGraphWithSocialChanges();
	  }
	  
	  
      public void RestoreSubCluster(int cluster,int  trace_ini, int trace_last)
      {
  		this.GetData().ReturnToBaseLog();
  		
  		for(int j=trace_ini;j<trace_last;j++)
  		{
		List<String> RemovedCase=this.ClusterTransformation.GetClusterData().GetActivityClusters().get(cluster).get(j);
	    if(RemovedParticularCases.contains(RemovedCase))
	    {
	    	RemovedParticularCases.remove(RemovedCase);
	    }
  		}
	   
	    TracesFilter(cluster);

      }
      
      public void RestoreCase(int cluster,int  caseIndex)
      {
  		this.GetData().ReturnToBaseLog();
  		
  
		List<String> RemovedCase=this.ClusterTransformation.GetClusterData().GetActivityClusters().get(cluster).get(caseIndex);
	    if(RemovedParticularCases.contains(RemovedCase))
	    {
	    	RemovedParticularCases.remove(RemovedCase);
	    }
  			   
	    TracesFilter(cluster);

      }

	  public void AddSocialResource(String resource)
	  {
	     //Data.SetCurrentLog((XLog) Data.GetOriginalWorkingLog().clone());
	     String person= this.SocialTransformation.TranslateNode(Integer.parseInt(resource));
	     RemovedResources.remove(person);
	     SocialFilterRemove();
	  }
	  
	  public void setContext(PluginContext context)
	  {
	   this.context=context;
	  }
	  
	  public void GroupFilter(String GroupName)
	  {
			ArrayList<Integer> Team;
			
		  System.out.print("\n Group Filter");
		  if(GroupName.equals("-1"))
    		 Team=this.SocialTransformation.GetGroupsOneTwo();
		  else
			 Team=this.SocialTransformation.GetTeamsWT().get(Integer.parseInt(GroupName));

			 for(int j=0; j<Team.size();j++)
			 {
				String person= this.SocialTransformation.TranslateNode(Team.get(j));
				if(!RemovedResources.contains(person))
				{
				RemovedResources.add(person);
				}			    
			
			 }
			  SocialFilterRemove();

	  }
	  
	  public void RestoreGroup(String GroupName)
	  {
		 
		  		ArrayList<Integer> Team;
			
		  if(GroupName.equals("-1"))
    		 Team=this.SocialTransformation.GetGroupsOneTwo();
		  else
			 Team=this.SocialTransformation.GetTeamsWT().get(Integer.parseInt(GroupName));
			 
			 for(int j=0; j<Team.size();j++)
			 {
				String person= this.SocialTransformation.TranslateNode(Team.get(j));
				if(RemovedResources.contains(person))
				{
					RemovedResources.remove(person);	
				}	
				
			 }
			 
			  Traces.clear();
			  SocialFilterRemove();
	  }
	  
	  public void GetCurrentResourceList()
	  {
	    XLogInfo  summary = XLogInfoFactory.createLogInfo(this.GetData().GetCurrentLog());

		XEventClasses originators = summary.getResourceClasses();
	
 	  }	

	  

}
