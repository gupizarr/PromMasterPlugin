package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	private ArrayList<String> RemovedClusters;
	private List<XTrace> Traces;
	private PluginContext context;
    private boolean fixLog=false;
    private ArrayList<String> RemovedResources;  
    

	public ImproveDiscoveryTransformation(ImproveDiscoveryData Data, final PluginContext context)  {
		// TODO Auto-generated constructor stub
		this.RemovedClusters= new ArrayList<String>();
		this.context=context;
		this.Data=Data;
		this.ClusterTransformation= new ImproveDiscoveryClusterTransformation(new ImproveDiscoveryClusterData(Data.GetCurrentLog(),3));
		//ClusterTransformation= new ImproveDiscoveryClusterTransformation(Data.getTraceAlignData());
		Traces= new ArrayList<XTrace>();
		//Activities= new ArrayList<String,String>();
		RemovedResources= new ArrayList<String>();
	   
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
	
	public void PerformanceFilter(int minTimeValue, int maxTimeValue)
	{
		 ArrayList<XTrace> KeptXTrace= new ArrayList<XTrace>();
     	
		 for(int i=0; i<Data.GetCurrentLog().size(); i++)
	     {
	    	   
			    if(Data.GetPerformanceData().getFinalTimes()[i]>=minTimeValue && Data.GetPerformanceData().getFinalTimes()[i]<=maxTimeValue)
			    {    		 
	       		 KeptXTrace.add(Data.GetCurrentLog().get(i));
			    }
	
	    	 
	    	 
	     }
		 Data.SetCurrentLog( new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class)));
	     Data.GetCurrentLog().addAll(KeptXTrace);
	     Data.ResetPerformanceData();
         UpdateGraph(true);

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
		Data.ReturnToOriginalLog();

	}
	
	public void RestoreCluster(String cluster)
	{
		this.GetData().ReturnToOriginalLog();
			
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
		 			
		      for (int u=0; u<this.Data.GetCurrentLog().size(); u++)
 			  {
		 	       XTrace trace = this.Data.GetCurrentLog().get(u);   
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
			  System.out.print("\n Esta vacio");
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
	
	  public void UpdateGraph(boolean isPerformance)
	  {
		  if(!isPerformance)
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
		  }
		  
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
		  else
		  {
			  if(!Data.GetCurrentLog().isEmpty() && Data.GetCurrentLog().get(0).size()==1)
			  {
			  FixLog();
			  }
			  

			  XLogInfo Info=XLogInfoFactory.createLogInfo(this.Data.GetCurrentLog());
			  HeuristicsMiner fhm = new HeuristicsMiner(context, Data.GetCurrentLog(),Info ); 
			  this.Data.setHeuristicNet(fhm.mine()); 

		      this.Data.GetHNetSettings().setShowingUnconnectedTasks(true);
			  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
			  this.Data.setAnnotatedVisualizationSettings(new AnnotatedVisualizationSettings());
			  this.Data.setHeuristicsNetGraph(generator.generate(this.Data.getHeuristicNet(),this.Data.GetHNetSettings()));
		  }
			
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
		    Traces.clear();
		    if(!RemovedResources.contains(Rols))
		    {
			RemovedResources.add(Rols);
		    }
			int count=0;
	        XEvent[] remove;
	        XTrace[] removeTrace= new XTrace[this.Data.GetCurrentLog().size()];
	        String resource;
	
	        int remove_trace_count=0;
	     
	    	for(int j=0;j<RemovedResources.size();j++)
	 		{
	          for (int u=0; u<this.Data.GetCurrentLog().size(); u++)
			  {
	      
	        	  XTrace trace = this.Data.GetCurrentLog().get(u); 
	            	
	        	    count=0;
	        	    remove= new XEvent[trace.size()];
	           	    if(trace.size()>0)
	        	    {
	           	 
							for (XEvent event : trace) 
							{		
			        	     resource=XLogInfoFactory.createLogInfo(this.Data.GetCurrentLog()).getResourceClasses().getClassOf(event).getId();
					     			if(resource.equals(RemovedResources.get(j)))
					     			{
					     				     		remove[count]=event;
					     				     		count++;
					     				          
					     			}  		
							}
							for(int g=0;g<count;g++)
							{
								trace.remove(remove[g]);
								if(trace.size()==0)
								{
									removeTrace[remove_trace_count]=trace;
									remove_trace_count++;
								}

					
							}

						count=0;
	        	    }

			  
			  }
	         
	  
	 		}
	    	 for(int s=0; s<Data.GetCurrentLog().size();s++)
	         {
	       	  boolean existe=false;
	         for(int c=0; c<removeTrace.length;c++)
	         {
	       	  if(removeTrace[c]==Data.GetCurrentLog().get(s))
	       	  existe=true;
	         }
	         if(!existe)
	       	  Traces.add(Data.GetCurrentLog().get(s));
	         }

	         
	 		
	          UpdateGraph(false);
		}
	  
	  public void AddSocialGroup(String resource)
	  {
	     Data.SetCurrentLog((XLog) Data.GetOriginalLog().clone());
	     RemovedResources.remove(resource);
		 SocialFilter("");
	  }
	  
	  public void setContext(PluginContext context)
	  {
	   this.context=context;
	  }
	  /*
	  public void MostrarFiltrados()
	  {
	  	  if(this.RemovedResources.size()>8)
		  {
	  		  System.out.print("\n Nueve ejecución de mostrar NO filtrados:");
	  		  System.out.print("\n Ejecutor que debería estar filtrado:");
			      for(int j=0;j<RemovedResources.size();j++)
			      {
			        	 System.out.print(RemovedResources.get(j)+"\n");
			      }
				  
		          for (int u=0; u<this.Data.GetCurrentLog().size(); u++)
				  {
		        	  XTrace trace = this.Data.GetCurrentLog().get(u); 
		              
		        	
				
		      	        System.out.print("\n Nueva iteración");
		
		
					for (XEvent event : trace) 
					{			
			   
				  	     String resource= this.OriginallogInfo.getResourceClasses().getClassOf(event).getId();
				
				      	        System.out.print("\n ejecutor aún no filtrado: "+ resource);
					}
					}
		   }
	  
	  }*/

}
