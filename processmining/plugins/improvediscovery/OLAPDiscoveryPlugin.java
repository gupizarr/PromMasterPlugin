package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.ArrayList;
import java.util.HashMap;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
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
import org.processmining.plugins.socialnetwork.miner.gui.PanelWorkingTogether;
import org.processmining.plugins.socialnetwork.miner.miningoperation.BasicOperation;
import org.processmining.plugins.socialnetwork.miner.miningoperation.OperationFactory;
import org.processmining.plugins.socialnetwork.miner.miningoperation.UtilOperation;

import cern.colt.matrix.DoubleMatrix2D;
/*Social Network reference */





@Plugin(name = "ImproveDiscovery",
        parameterLabels = { "Log", "Mother", "Procreation Configuration" },
        returnLabels = { "Model View" },
        returnTypes = { OLAPTransformation.class })
public class OLAPDiscoveryPlugin {
  
	public static XLogInfo logInfo;
	protected HashMap<String, Integer> keys= new HashMap<String, Integer>();
	private BasicOperation baseOprtation = null;
	@UITopiaVariant(affiliation = "PUC",
                  author = "Gustavo Pizarro",
                  email = "gupizarr@uc.cl",
                  uiLabel = UITopiaVariant.USEPLUGIN)
  @PluginVariant(requiredParameterLabels = {0,2})
  public  OLAPTransformation create(final PluginContext context, final OLAPDiscoveryConfiguration config,XLog log) {
    
    OLAPData DiscoveryData = new OLAPData(log);
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
	
	  //Social
		
	    PanelWorkingTogether input = new PanelWorkingTogether();
	  	int indexType = SNMinerOptions.WORKING_TOGETHER + input.getWorkingTogetherSetting();
	  	baseOprtation = OperationFactory.getOperation(indexType, log);
		DiscoveryData.SetDoubleMatrix2D(baseOprtation.calculation());
		DiscoveryData.SetOriginatorList(baseOprtation.getOriginatorList());
		DiscoveryData.SetSocialNetwork(UtilOperation.generateSN(baseOprtation.calculation(), baseOprtation.getOriginatorList()));
		
	OLAPTransformation IDTransformation= new OLAPTransformation(DiscoveryData,context);
		
    return IDTransformation;
    
    
  }
 
  @UITopiaVariant(affiliation = "PUC",
                  author = "Gustavo Pizarro",
                  email = "gupizarr@uc.cl",
                  uiLabel = UITopiaVariant.USEPLUGIN)
  @PluginVariant(requiredParameterLabels = { 0 })
  public  OLAPTransformation create(final UIPluginContext context, XLog log) 
  {
    OLAPDiscoveryConfiguration config = new OLAPDiscoveryConfiguration("Config");
    
    return create(context,  config,log);
  }
  
  
  public static OLAPDiscoveryConfiguration populate(final UIPluginContext context,final OLAPDiscoveryConfiguration config) 
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
		     		//String eventKey = logInfo.getEventClasses().getClassOf(event).getId();
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

  
			
}
 

