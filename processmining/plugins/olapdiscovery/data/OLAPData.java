package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data;

import java.util.ArrayList;

import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.models.graphbased.directed.socialnetwork.SocialNetwork;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.heuristics.HeuristicsNetGraph;
import org.processmining.models.heuristics.elements.Activity;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.FlexibleHeuristicsMiner;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;

import cern.colt.matrix.DoubleMatrix2D;

/**
 * The data repository of General View Transformations (Logs, Filters and others)
*/
public class OLAPData {


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
  private XLog workingLog;
  private XLogInfo OriginallogInfo;
  public String[] Resources;
  private boolean isEmpty=false;
  
  public OLAPData(XLog log) {
	

	this.log=log;
	transformLog = (XLog) log.clone();
	workingLog= (XLog) log.clone();
	  

    this.OriginallogInfo = XLogInfoFactory.createLogInfo(this.log);
    
	XEventClasses resources_events = OriginallogInfo.getResourceClasses();
	Resources = new String[resources_events.getClasses().size()];

	for (int k = 0; k < resources_events.getClasses().size(); k++) {
	Resources[k] = resources_events.getByIndex(k).toString();
	}
	

  }
  
  public XLog GetOriginalLog()
  {
	  return log;
  }

  
  public void ResetToOriginalLog()
  {
	  System.out.print(" \n SIZE:"+log.size());
		transformLog = (XLog) log.clone();
		workingLog= (XLog) log.clone();
		
  }
  

  public void SetTransformLogFromWorkingLog()
  {
	  transformLog=(XLog) workingLog.clone();
	  
  }
  
  
  public XLog GetWorkingLog()
  {
	  return workingLog;
  } 
  
  
  public void SetHNetSettings(AnnotatedVisualizationSettings Settings)
  {
	  HNetSettings=Settings;
  }
  
  public AnnotatedVisualizationSettings GetHNetSettings()
  {
	  return HNetSettings;
  }
  


 
  public XLog GetCurrentLog()
  {
	  return this.workingLog;
  }
  
  public XLog GetBaseLog()
  {
	  return this.transformLog;
  }
  
  public void SetCurrentLog(XLog AuxLog)
  {
	  this.workingLog= (XLog) AuxLog.clone();
  }
  
  public void RemoveTrace(XTrace trace)
  {
	 transformLog.remove(trace);
  }

  public void setBaseLog(XLog Aux)
  {
	  this.transformLog= (XLog) Aux.clone();

  }
  public void RemoveActivity(XEvent event_remove)
  {
		for (XTrace trace : this.log) {
				trace.remove(event_remove);		
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
  
  public XLog GetOriginalWorkingLog()
  {
	  return workingLog;
  }
  
  public void ReturnToBaseLog()
  {
	  workingLog = (XLog) this.transformLog.clone();

  }
  
  public void SaveCurrentLogTransformation()
  {
	  workingLog = (XLog) transformLog.clone();
  }
  
  public void IsEmpty(boolean empty)
  {
	  this.isEmpty=empty;
  }
  
  public boolean GetIsEmpty()
  {
	  return this.isEmpty;
  }
  

}
