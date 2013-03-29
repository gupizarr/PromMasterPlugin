package PromMasterPlugin;

import java.util.ArrayList;

import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.heuristics.HeuristicsNetGraph;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.FlexibleHeuristicsMiner;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;

public class ImproveDiscoveryData {
  private Name name;
  private int age;
  private ArrayList<String> Originators= new ArrayList<String>();
  private ArrayList<String> Prueba= new ArrayList<String>();
  private FlexibleHeuristicsMiner HeuristicModel;
  private AnnotatedVisualizationSettings HNetSettings;
  private HeuristicsNetGraph HNetGraph;
  private HeuristicsNet HNet;
	
  
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
  
  public void setHeuristicsNetGraph(HeuristicsNetGraph HNG)
  {
	  this.HNetGraph=HNG;
  }
    
  
  public ImproveDiscoveryData() {
    age = 0;
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
 
  public Name getName() {
    return name;
  }
 
  public void setName(Name name) {
    this.name = name;
  }
 
  public int getAge() {
    return age;
  }
 
  public void setAge(int age) {
    this.age = age;
  }
  
  public void setOrigintators(ArrayList<String> Originators)
  {
	  this.Originators=Originators;
  }
  
  public ArrayList<String> GetOriginators()
  {
	  return Originators;
  }
}
