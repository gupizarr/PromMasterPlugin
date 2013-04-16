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
import org.processmining.models.graphbased.directed.socialnetwork.SocialNetwork;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.heuristics.HeuristicsNetGraph;
import org.processmining.models.heuristics.elements.Activity;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.FlexibleHeuristicsMiner;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.HeuristicsMiner;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationGenerator;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;

import cern.colt.matrix.DoubleMatrix2D;

public class ImproveDiscoveryData {


  private ArrayList<String> Originators= new ArrayList<String>();
  private ArrayList<String> Prueba= new ArrayList<String>();
  private FlexibleHeuristicsMiner HeuristicModel;
  private AnnotatedVisualizationSettings HNetSettings;
  private HeuristicsNetGraph HNetGraph;
  private HeuristicsNet HNet;
  //Social
  private DoubleMatrix2D matrix;	
  protected ArrayList<String> originatorList;
  private SocialNetwork  social_n;
  private XLog log;
  private XLog transformLog;
  private XLogInfo OriginallogInfo;
  public String[] Resources;
  private ArrayList<String> Events;
  private PluginContext context;
  
  private ArrayList<String> RemovedResources;
  private ArrayList<String> RemovedClusters;
  
  private ArrayList<String> Activities;
  private List<List<List<List<String>>>> Clusters;
  private ArrayList<XTrace> Traces;
  private boolean fixLog=false;
  private ImproveDiscoveryTransformation transformationTraceAlign;
  
  public ImproveDiscoveryData(XLog log, final PluginContext context) {
	
	this.transformationTraceAlign = new ImproveDiscoveryTransformation(log);
    this.Clusters= this.transformationTraceAlign.GetProcessAlig();
	  Traces= new ArrayList<XTrace>();
	//Activities= new ArrayList<String,String>();
	RemovedResources= new ArrayList<String>();
    this.log=log;
    transformLog = (XLog) log.clone();
    this.OriginallogInfo = XLogInfoFactory.createLogInfo(this.log);
    
	XEventClasses resources_events = OriginallogInfo.getResourceClasses();
	Resources = new String[resources_events.getClasses().size()];

	for (int k = 0; k < resources_events.getClasses().size(); k++) {
	Resources[k] = resources_events.getByIndex(k).toString();
	}

  }
  
  public ImproveDiscoveryTransformation getTraceAlignTransformation()
  {
	  return this.transformationTraceAlign;
  }
  
  public boolean GetFixCase()
  {
	  return fixLog;
  }
  public void setContext(PluginContext context)
  {
   this.context=context;
  }
  
  public void FixLog()
  {
	  fixLog=false;
	  int count=0;
	  for(int j=0;j<this.transformLog.size();j++)
	  {
		  if(transformLog.get(j).size()==1)
		  {
			  count++;
		  }
	  }
	  if(this.transformLog.size()==count)
	  {
		  for(int j=0;j<this.transformLog.size();j++)
		  {
			  XEvent event= new XEventImpl();
			  transformLog.get(j).add(event);
		  }
		  fixLog=true;
		  
		  
	  }
  }

  public void UpdateGraph()
  {
	  this.transformLog= new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class));
	  this.transformLog.addAll(Traces);
	  
	  if(!transformLog.isEmpty() && transformLog.get(0).size()==1)
	  {
	  FixLog();
	  }
	  
	  if(transformLog.size()==0)
	  {	
		  XTrace trace= new XTraceImpl(new XAttributeMapImpl());
		  XEvent event= new XEventImpl();
		  trace.add(event);
		  transformLog.add(trace);
	  }
	  
	  if(transformLog.size()>0)
	  {
	  XLogInfo Info=XLogInfoFactory.createLogInfo(this.transformLog);
	  System.out.print("\n Quedaron trazas : "+this.transformLog.size());
	  HeuristicsMiner fhm = new HeuristicsMiner(context, transformLog,Info ); 
	  this.HNet= fhm.mine(); 

      this.HNetSettings.setShowingUnconnectedTasks(true);
	  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
	  this.HNetSettings=new AnnotatedVisualizationSettings();
	  this.HNetGraph= generator.generate(HNet, this.HNetSettings);
	  		  
	  }
	  else
	  {
		  ArrayList<Activity> act= new ArrayList<Activity>();
	      Iterator<Activity> activities = this.HNetGraph.getActivities().iterator();
	      while(activities.hasNext()) {
	         act.add(activities.next());
	      }

		  this.HNetGraph.getActivities().removeAll(act);
	  }
		
  }
  public void RemoveTrace(XTrace trace)
  {
	 transformLog.remove(trace);
  }

  public void RemoveActivity(XEvent event_remove)
  {
		for (XTrace trace : this.log) {
				trace.remove(event_remove);		
		}
  }
  public void AddSocialGroup(String resource)
  {
     transformLog = (XLog) log.clone();
     RemovedResources.remove(resource);
	 SocialFilter("");
  }
  
  public void ClusterFilter(String cluster)
  {
	  Traces.clear();
	  String alignName; 
	  int count=0;
	  int current_event=0;
	  ArrayList<XTrace> Traces= new ArrayList<XTrace>();
		XAttributeMap attributeMap;

	    if(!RemovedClusters.contains(cluster))
	    {
		RemovedClusters.add(cluster);
	    }
	    for(int j=0;j<this.RemovedClusters.size();j++)
	    {
	    	//Comienza el Cluster seleccionado
	    	count=Integer.parseInt(RemovedClusters.get(j));
	    	for(int c=0;c<this.Clusters.get(count).size();c++)
	    	{
	    	//Comienza el trace	
	    		for(int s=0;s<this.Clusters.get(count).get(c).size();s++)
	    		{  
	    			 count=0;
	    			//Comienzan las actividades
	    			  for (int u=0; u<this.transformLog.size(); u++)
	    			  {
	    	      
	    	        	  XTrace trace = this.transformLog.get(u); 
	    	            	
	    	        	  
	    	        	    //remove= new XEvent[trace.size()];
	    	           	    if(trace.size()>0)
	    	        	    {
	    	           	    		current_event=0;
	    							for (XEvent event : trace) 
	    							{	
	    								attributeMap = event.getAttributes();
	    								alignName=attributeMap.get("concept:name").toString() + "-"
	    										+ attributeMap.get("lifecycle:transition").toString();
	    								if(current_event==u && !alignName.equals(this.Clusters.get(count).get(c).get(s)))
	    								{	    									
    									count++;
	    								}
	    								current_event++;
	    							}

	    	        	    }
							if(count==trace.size())
							{
								Traces.add(trace);
							}
	    			  }
	    			 
	    			  this.transformLog.removeAll(Traces);
	    		}
	    	}
	    }
	      UpdateGraph();
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
        XTrace[] removeTrace= new XTrace[this.transformLog.size()];
        String resource;
        int elementos_borrados=0;
        int remove_trace_count=0;
     
    	for(int j=0;j<RemovedResources.size();j++)
 		{
          for (int u=0; u<this.transformLog.size(); u++)
		  {
      
        	  XTrace trace = this.transformLog.get(u); 
            	
        	    count=0;
        	    remove= new XEvent[trace.size()];
           	    if(trace.size()>0)
        	    {
           	 
						for (XEvent event : trace) 
						{		
		        	     resource=XLogInfoFactory.createLogInfo(this.transformLog).getResourceClasses().getClassOf(event).getId();
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

						elementos_borrados+=count;
						count=0;
        	    }

		  
		  }
         
  
 		}
    	 for(int s=0; s<transformLog.size();s++)
         {
       	  boolean existe=false;
         for(int c=0; c<removeTrace.length;c++)
         {
       	  if(removeTrace[c]==transformLog.get(s))
       	  existe=true;
         }
         if(!existe)
       	  Traces.add(transformLog.get(s));
         }

         System.out.print("\n Se borraron:"+elementos_borrados+"  eventos y quedan "+Traces.size()+" de "+this.transformLog.size()+  " Trazas");
 		
          UpdateGraph();
	}
  
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
			  
	          for (int u=0; u<this.transformLog.size(); u++)
			  {
	        	  XTrace trace = this.transformLog.get(u); 
	              
	        	
			
	      	        System.out.print("\n Nueva iteración");
	
	
				for (XEvent event : trace) 
				{			
		   
			  	     String resource= this.OriginallogInfo.getResourceClasses().getClassOf(event).getId();
			
			      	        System.out.print("\n ejecutor aún no filtrado: "+ resource);
				}
				}
	   }
  
  }

  

  public void SetSocialNetwork(SocialNetwork sn)
  {
	  this.social_n=sn;
  }
  
  
  
  public SocialNetwork getSocialNetwork()
  {
	  return social_n;
  }
  public void SetOriginatorList(ArrayList<String> originatorList)
  {
	  this.originatorList=originatorList;
  }
  public void SetDoubleMatrix2D(DoubleMatrix2D matrix)
  {
	  this.matrix=matrix;
  }
  
  public DoubleMatrix2D getDoubleMatrix2D()
  {
	  return matrix;
  }
  
  public HeuristicsNet getHeuristicNet()
  {
	return HNet;  
  }
  
  public void setHeuristicNet(HeuristicsNet HNet)
  {
	  this.HNet=HNet;
  }
  public AnnotatedVisualizationSettings getHMinerAVSettings()
  {
	  return HNetSettings;
  }
  
  public void setAnnotatedVisualizationSettings(AnnotatedVisualizationSettings AVS)
  {
	  this.HNetSettings=AVS;
  }
  
  public HeuristicsNetGraph getHeuristicsNetGraph()
  {
	  return HNetGraph;
  }
  
  public void setHeuristicsNetGraph(HeuristicsNetGraph graph)
  {
	  this.HNetGraph=graph;
  }
    

  
  public void SetFHModel(FlexibleHeuristicsMiner HeuristicModel)
  {
	  this.HeuristicModel=HeuristicModel;
  }
  
  public FlexibleHeuristicsMiner getFHMiner()
  {
	  return HeuristicModel;
  }
  
  public ArrayList<String> getPrueba()
  {
	  return Prueba;
  }
 


  
  public void setOrigintators(ArrayList<String> Originators)
  {
	  this.Originators=Originators;
  }
  
  public ArrayList<String> GetOriginators()
  {
	  return Originators;
  }
  
  public void RemoveActivity(Activity activity)
  {
	 HNetGraph.removeActivity(activity);

  }
  

}
