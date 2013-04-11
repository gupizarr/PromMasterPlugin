package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.models.graphbased.directed.socialnetwork.SocialNetwork;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.heuristics.HeuristicsNetGraph;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.HeuristicsMiner;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationGenerator;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;
import org.processmining.plugins.socialnetwork.miner.SNMinerOptions;
import org.processmining.plugins.socialnetwork.miner.miningoperation.BasicOperation;
import org.processmining.plugins.socialnetwork.miner.miningoperation.OperationFactory;
import org.processmining.plugins.socialnetwork.miner.miningoperation.UtilOperation;

import cern.colt.matrix.DoubleMatrix2D;
/*Social Network reference */





@Plugin(name = "ImproveDiscovery",
        parameterLabels = { "Log", "Mother", "Procreation Configuration" },
        returnLabels = { "Model View" },
        returnTypes = { ImproveDiscoveryData.class })
public class ImproveDiscoveryPlugin {
  
	public static XLogInfo logInfo;
	protected HashMap<String, Integer> keys= new HashMap<String, Integer>();
	private BasicOperation baseOprtation = null;
	@UITopiaVariant(affiliation = "PUC",
                  author = "Gustavo Pizarro",
                  email = "gupizarr@uc.cl",
                  uiLabel = UITopiaVariant.USEPLUGIN)
  @PluginVariant(requiredParameterLabels = {0,2})
  public  ImproveDiscoveryData create(final PluginContext context, final ImproveDiscoveryConfiguration config,XLog log) {
    
    ImproveDiscoveryData DiscoveryData = new ImproveDiscoveryData(log ,context);
    DiscoveryData.setOrigintators(SearchComponents(log));
    HeuristicsMiner fhm = new HeuristicsMiner(context, log, logInfo);
    HeuristicsNet HNet= fhm.mine();
    
	AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
    AnnotatedVisualizationSettings settings = new AnnotatedVisualizationSettings();
    settings.setShowingUnconnectedTasks(true);
	HeuristicsNetGraph graph = generator.generate(HNet, settings); 
	
	DiscoveryData.setAnnotatedVisualizationSettings(settings);
	DiscoveryData.setHeuristicsNetGraph(graph);
	DiscoveryData.setHeuristicNet(HNet);
	
	  /*Social
		
	    PanelSimilarTask input = new PanelSimilarTask();
	  	int indexType = SNMinerOptions.SIMILAR_TASK + input.getSimilarTaskSetting();
	  	baseOprtation = OperationFactory.getOperation(indexType, log);
		DiscoveryData.SetDoubleMatrix2D(baseOprtation.calculation());
		DiscoveryData.SetOriginatorList(baseOprtation.getOriginatorList());
		DiscoveryData.SetSocialNetwork(UtilOperation.generateSN(baseOprtation.calculation(), baseOprtation.getOriginatorList()));
		
	  */
		
    return DiscoveryData;
    
    
  }
 
  @UITopiaVariant(affiliation = "PUC",
                  author = "Gustavo Pizarro",
                  email = "gupizarr@uc.cl",
                  uiLabel = UITopiaVariant.USEPLUGIN)
  @PluginVariant(requiredParameterLabels = { 0 })
  public  ImproveDiscoveryData create(final UIPluginContext context, XLog log) 
  {
    ImproveDiscoveryConfiguration config = new ImproveDiscoveryConfiguration("Config");
    
    return create(context,  config,log);
  }
  
  
  public static ImproveDiscoveryConfiguration populate(final UIPluginContext context,final ImproveDiscoveryConfiguration config) 
  {
	  
	ProMPropertiesPanel panel = new ProMPropertiesPanel("Configure Procreation");
	ProMTextField name = panel.addTextField("Name", config.getName());
	final InteractionResult interactionResult = context.showConfiguration("Setup Procreation", panel);
	if (interactionResult == InteractionResult.FINISHED || 	interactionResult == InteractionResult.CONTINUE ||
	interactionResult == InteractionResult.NEXT) 
	{
	config.setName(name.getText());
	return config;
	}
	return null;
  }


  public  ArrayList<String> SearchComponents(XLog log)
  {
	  
		logInfo = XLogInfoFactory.createLogInfo(log);
		ArrayList<String> components= new ArrayList<String>();

		  for (XTrace trace : log) 
		  {
				for (XEvent event : trace) 
				{			
		     		String eventKey = logInfo.getEventClasses().getClassOf(event).getId();
		     		String resource=logInfo.getResourceClasses().getClassOf(event).getId();

		     		if(components.indexOf(resource)==-1)
		     		components.add(resource);			     		
				}
		  }

          return components;

  }
  
  
  public SocialNetwork getSN(XLog log) 
  {
		int indexType = SNMinerOptions.SIMILAR_TASK + +SNMinerOptions.CORRELATION_COEFFICIENT;
		BasicOperation baseOprtation = OperationFactory.getOperation(indexType, log);
		DoubleMatrix2D matrix = baseOprtation.calculation();
		SocialNetwork sn = UtilOperation.generateSN(matrix, baseOprtation.getOriginatorList());
		return sn;
  }

  private static ArrayList<String> getUserActivityTripleList(XLog log)
  {
			XLogInfo summary = XLogInfoFactory.createLogInfo(log);
			ArrayList<String> originatorList = new ArrayList<String>();
			XEventClasses originators = summary.getResourceClasses();
			Map<String, SocialNetwork> activityBasedHoWSocialNetworkList = new HashMap<String, SocialNetwork>();
			for (int k = 0; k < originators.getClasses().size(); k++) {
			originatorList.add(originators.getByIndex(k).toString());
			}
			
			return originatorList;
  }
			
}
 

