package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XAttributeMapLazyImpl;
import org.deckfour.xes.model.impl.XEventImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.heuristics.HeuristicsNetGraph;
import org.processmining.models.heuristics.elements.Activity;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.HeuristicsMiner;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationGenerator;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data.ClusterData;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data.OLAPData;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data.TimeData;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.filters.ClusterFilter;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.filters.SocialFilter;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.filters.TimeFilter;

/**
 * Methods to get final result of filters and update model data view 
*/
 public class OLAPTransformation {
    
	private ArrayList<XTrace> Traces;
	private OLAPData Data;
	
	private OLAPClusterTransformation ClusterTransformation;
	private OLAPSocialTransformation SocialTransformation;
	private OLAPTimeTransformation TimeTransformation;
	
	private ClusterFilter ClusterFilter;
	private SocialFilter SocialFilter;
	private TimeFilter PerformanceFilter;

	private PluginContext context;
    private boolean fixLog=false;
 
   
    private XLog SocialBackUpUnit;
  
    protected StatisticsCalculator Calculator;


	public OLAPTransformation(OLAPData Data, final PluginContext context)  {
		// TODO Auto-generated constructor stub
		Calculator=new StatisticsCalculator();
		this.context=context;
		this.Data=Data;
		
		this.TimeTransformation= new OLAPTimeTransformation(new TimeData(Data.GetCurrentLog()));
		this.ClusterTransformation= new OLAPClusterTransformation(new ClusterData(Data.GetCurrentLog(),3));
		this.SocialTransformation= new OLAPSocialTransformation(Data);
		
		SocialTransformation.WTCalculation();
		Traces= new ArrayList<XTrace>();
		
		
		//Filters
		ClusterFilter= new ClusterFilter(ClusterTransformation,Traces) ;
		SocialFilter = new SocialFilter(SocialTransformation,Traces);
		PerformanceFilter =new TimeFilter();
	
	   
	}
	
	public OLAPTimeTransformation GetTimeTransformation()
	{
		return TimeTransformation;
	}
	

	public TimeFilter GetPerformanceFilter()
	{
		return PerformanceFilter;
	}
	
	public SocialFilter GetSocialFilter()
	{
		return SocialFilter;
	}
	
	public ClusterFilter GetClusterFilter()
	{
		return ClusterFilter;
	}
	
	public void SetXLogBackUpUnit()
	{
		SocialBackUpUnit= (XLog) this.Data.GetBaseLog().clone();
	}
	
	public OLAPSocialTransformation GetSocialTransformation()
	{
	return 	SocialTransformation;
	}
	
	public void SetSocialTransformation(OLAPSocialTransformation ST)
	{
	SocialTransformation=ST;
	}
	
	public OLAPData GetData()
	{
		return Data;
	}
	
	public OLAPClusterTransformation GetClusterTransformation()
	{
		return ClusterTransformation;	
	}
	
	public void SetClusterTransformation(OLAPClusterTransformation ClusterTransformation)
	{
		this.ClusterTransformation=ClusterTransformation;
	}
		
	public void SetNumberOfClusters(int newNumber)
	{
		ClusterTransformation.GetClusterData().SetNumberOfClusters(newNumber);
	}
	
	
    public void UpdateForClusters()
	{
		  
		  this.Data.SetCurrentLog(new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class)));
		  this.Data.GetCurrentLog().addAll(GetClusterFilter().GetResult());
		  

		  
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
		  ClusterFilter.GetResult().clear();

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
		  ClusterFilter.Reset();
		  SocialFilter.Reset();

		  Data.ResetToOriginalLog();
		  GenerateHeuristicModel();
	}
	 
	  
	public void UpdateGraphWithPerformanceChanges()
	{
		
		  	  if(Data.GetCurrentLog().size()==0)
		  	  {	
			  XTrace trace= new XTraceImpl(new XAttributeMapImpl());
			  XEvent event= new XEventImpl();
			  trace.add(event);
			  Data.GetCurrentLog().add(trace);
		  	  }
		  	  else if(!Data.GetCurrentLog().isEmpty() && Data.GetCurrentLog().get(0).size()==1)
			  {
			  FixLog();
			  }
			  
			  XLogInfo Info=XLogInfoFactory.createLogInfo(this.Data.GetCurrentLog());
			  HeuristicsMiner fhm = new HeuristicsMiner(context, Data.GetCurrentLog(),Info ); 
			  this.Data.setHeuristicNet(fhm.mine()); 
			  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
			  this.Data.setAnnotatedVisualizationSettings(new AnnotatedVisualizationSettings());
		      this.Data.GetHNetSettings().setShowingUnconnectedTasks(true);
			  this.Data.setHeuristicsNetGraph(generator.generate(this.Data.getHeuristicNet(),this.Data.GetHNetSettings()));
		  	
	  }
	  
	public void ShowSocialData()
	{
		  ArrayList<String> resourceslist= new ArrayList<String>();
		  for(int j=0;j<Data.GetCurrentLog().size();j++)
		  {
			  for(int k=0;k<Data.GetCurrentLog().get(j).size();k++)
			  {
				  XEvent event=Data.GetCurrentLog().get(j).get(k);
				  String resource=XLogInfoFactory.createLogInfo(Data.GetBaseLog()).getResourceClasses().getClassOf(event).getId();
				  if(!resourceslist.contains(resource))
					  resourceslist.add(resource);
			  }
		  }
	
	  }
	  
	public void UpdateGraphWithSocialChanges(String type)
	{
		     Data.SetCurrentLog(new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class)));
			 Data.GetCurrentLog().addAll(SocialFilter.GetResult());
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
			  }
		  
			  if(Data.GetCurrentLog().size()>0)
			  {
				  GenerateHeuristicModelSocial();	  
			  }
			  else
			  {
			  ArrayList<Activity> act= new ArrayList<Activity>();
		      Iterator<Activity> activities = this.Data.getHeuristicsNetGraph().getActivities().iterator();
		      while(activities.hasNext()) {
		         act.add(activities.next());
		      }

			  }

			  
			  if(type.equals("Working Together"))
			  this.SocialTransformation.SetWorkingTogetherToShow(this.GetData().GetWorkingLog());
			  else if(type.equals("Hanover of Work"))
			  this.SocialTransformation.SetHandoverWTaskToShow(this.GetData().GetWorkingLog());
			  else
			  this.SocialTransformation.SetSimilarTaskToShow(this.GetData().GetWorkingLog());

			  SocialFilter.GetResult().clear();
				 
		  }
		 
			
	  
	  public void GenerateHeuristicModelSocial()
	  {
		  XLogInfo Info=XLogInfoFactory.createLogInfo(this.Data.GetCurrentLog());
		  HeuristicsMiner fhm = new HeuristicsMiner(context, Data.GetCurrentLog(),Info ); 
		  this.Data.setHeuristicNet(fhm.mine()); 
		  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
		  this.Data.setAnnotatedVisualizationSettings(new AnnotatedVisualizationSettings());
	      this.Data.GetHNetSettings().setShowingUnconnectedTasks(true);

		  this.Data.setHeuristicsNetGraph(generator.generate(this.Data.getHeuristicNet(),this.Data.GetHNetSettings()));

		    
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
	  
	  public HeuristicsNetGraph FindVariantHeuristicModel(String cluster,String variant)
	  {
		      ArrayList<XTrace> SelectedTraces= ClusterFilter.FilterCaseHeuristicModel(cluster,variant , Data.GetBaseLog());
			  	 
		  
	          XLog emptyList= new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class));
	          emptyList.addAll(SelectedTraces);
	          
	          XLogInfo Info=XLogInfoFactory.createLogInfo(emptyList);
			  HeuristicsMiner fhm = new HeuristicsMiner(context, emptyList,Info ); 
			  HeuristicsNet net=fhm.mine(); 
			  AnnotatedVisualizationSettings settings= new AnnotatedVisualizationSettings();
			  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();		     
			  settings.setShowingUnconnectedTasks(true);

			  
			  HeuristicsNetGraph graph= generator.generate(net,settings);
		  
		      return graph;
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
		  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
		  this.Data.setAnnotatedVisualizationSettings(new AnnotatedVisualizationSettings());
	      this.Data.GetHNetSettings().setShowingUnconnectedTasks(true);

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
	  
	
	
	  public void RestoreBaseXLog()
	  {
		this.Data.setBaseLog(this.SocialBackUpUnit);  
	  }
	  	 

	  public void UpdateClusterCasesAfterOthersDimensiones()
	  {
		  boolean removeTrace=false;
          Map<String,List<String>> Remove = new HashMap<String,List<String>>();
          
          for(int j=0;j<this.ClusterTransformation.GetClusterData().GetActivityClusters().size();j++)
		  {
		 	
          	  for(int c=0;c<this.ClusterTransformation.GetClusterData().GetActivityClusters().get(j).size();c++)
			  { 	       
		 		    for (int u=0; u<this.Data.GetBaseLog().size(); u++)
	 			    {	
		 		    	     XTrace trace = this.Data.GetBaseLog().get(u);   
				 		     removeTrace=false; 
				 		     if(ClusterFilter.IsTheSameTrace(trace,ClusterTransformation.GetClusterData().GetActivityClusters().get(j).get(c)))
		 					 {
		 						 removeTrace=true;
		 					 }					 		    			  	 			    		
		 			}
 			  	 
		 			if(!removeTrace)
		 			{
		 				Remove.put(""+j,ClusterTransformation.GetClusterData().GetActivityClusters().get(j).get(c));
		 			}
 			  }
		  }
      	    String key;
      	    List<String> value;	  
          	for(Entry<String, List<String>> entry : Remove.entrySet()) {
          	    key = entry.getKey();
          	    value = entry.getValue();
  		        this.ClusterTransformation.GetClusterData().GetActivityClusters().get(Integer.parseInt(key)).removeAll(value);
          	}
          
      
         
       	}
	  
	
		public ArrayList<String> GetActivities(String Resource)
		{
			ArrayList<String> Activities = new ArrayList<String>();
			String resource;
			String eventName;
			for(int j=0;j<Data.GetBaseLog().size();j++)
			{
			    if(Data.GetBaseLog().get(j).size()>0)
		  	    {
		  	    	//por cada evento
				 			for (XEvent event : Data.GetBaseLog().get(j)) 
							{		
			        	            resource=XLogInfoFactory.createLogInfo(Data.GetBaseLog()).getResourceClasses().getClassOf(event).getId();
			        	            if(resource.equals(Resource))
			        	            {
			        	            eventName=XLogInfoFactory.createLogInfo(Data.GetBaseLog()).getEventClasses().getClassOf(event).getId();
			        	            if(!Activities.contains(eventName))
			        	            	Activities.add(eventName);
			        	            }
							}
		  	    }
			}
			return Activities;
		}
		
		public void SetSocialGraph(String Type, double threshold)
		{
			if(Type.equals("Working Together"))
			GetSocialTransformation().RecalculateSocialRelations("WT",threshold);
			else if(Type.equals("Similar Task"))
			GetSocialTransformation().RecalculateSocialRelations("ST",threshold);
			else
			GetSocialTransformation().RecalculateSocialRelations("HW",threshold);
			
		}
 }


