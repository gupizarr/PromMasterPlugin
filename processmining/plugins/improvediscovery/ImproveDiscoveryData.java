package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.ArrayList;
import java.util.Iterator;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
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
  private ArrayList<String> Activities;
  private ArrayList<XTrace> Traces;
  
  public ImproveDiscoveryData(XLog log, final PluginContext context) {
	
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
  
  public void setContext(PluginContext context)
  {
   this.context=context;
  }
  
public void QueTareasCorresponden()
  {
  for(int u=0; u< this.transformLog.size(); u++)
  {  
	  
	  for( int j=0 ; j<this.transformLog.get(u).size();j++)
	  {	
	  XEventClass c = this.OriginallogInfo.getEventClasses().getClassOf(this.transformLog.get(u).get(j));
	  System.out.print("\n"+ c.getId());
	  }
  }
  }
  
  public void queEventosTieneElLogInfo(XLogInfo info)
  {
	  for( int j=0 ; j<info.getEventClasses().size();j++)
	  {
		  System.out.print("\n"+ info.getEventClasses().getByIndex(j).getId());
	  }
 }
  
  
  public void ShowLogAttributes()
  {
	  XLogInfo Info=XLogInfoFactory.createLogInfo(this.transformLog);
	  
	  //this.transformLog.setAttributes(arg0);
	  Iterator<XAttribute> b=this.transformLog.getAttributes().values().iterator();
	  //Iterator<XAttribute> a=this.transformLog.getGlobalEventAttributes().iterator();



  }
  public void UpdateGraph()
  {
	  transformLog.clear();
	  transformLog.addAll(Traces);
	
	  for(int j=0; j< transformLog.getClassifiers().size();j++)
	  {
		  
		  System.out.print("\n "+ transformLog.getClassifiers().get(j).toString());	
	  }

	  XLogInfo Info=XLogInfoFactory.createLogInfo(this.transformLog);
	  //ShowLogAttributes();  
	  System.out.print("\n Quedaron trazas : "+this.transformLog.size());
	  HeuristicsMiner fhm = new HeuristicsMiner(context, transformLog,Info ); 
	  this.HNet= fhm.mine();
	 
	  this.HNetSettings.setShowingUnconnectedTasks(true);
	  //queEventosTieneElLogInfo(Info);

	  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
	  this.HNetSettings=new AnnotatedVisualizationSettings();
	
	  this.HNetGraph= generator.generate(HNet, this.HNetSettings); 
	
		
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
		        	     resource= this.OriginallogInfo.getResourceClasses().getClassOf(event).getId();
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

         System.out.print("\n Se borraron:"+elementos_borrados+"eventos y quedan "+Traces.size()+" de "+this.transformLog.size()+  " Trazas");
   	

    
    		 // System.out.print("No hay roles y el numero de trazas es: "+this.transformLog.size());
    	  
		  //MostrarFiltrados();
			
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
