package PromMasterPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.models.graphbased.directed.socialnetwork.SocialNetwork;
import org.processmining.plugins.socialnetwork.miner.SNMinerOptions;
import org.processmining.plugins.socialnetwork.miner.miningoperation.BasicOperation;
import org.processmining.plugins.socialnetwork.miner.miningoperation.OperationFactory;
import org.processmining.plugins.socialnetwork.miner.miningoperation.UtilOperation;

import cern.colt.matrix.DoubleMatrix2D;
/*Social Network reference */

@Plugin(name = "ImproveDiscover",
        parameterLabels = { "Log", "Mother", "Procreation Configuration" },
        returnLabels = { "Model View" },
        returnTypes = { Person.class })
public class ImproveDiscoveryPlugin {
  @UITopiaVariant(affiliation = "PUC",
                  author = "Gustavo Pizarro",
                  email = "gupizarr@uc.cl",
                  uiLabel = UITopiaVariant.USEPLUGIN)
  @PluginVariant(requiredParameterLabels = {0,2/* 0, 1, 2 */})
  public static Person procreate(final PluginContext context, final ImproveDiscoveryConfiguration config,XLog log) {
    
	ArrayList<String> Originators= getUserActivityTripleList(log);
    Person child = new Person();
    child.setOrigintators(Originators);
    child.setAge(0);
    child.setName(new Name(config.getName(), "hola"));
    return child;
    
    
  }
 
  @UITopiaVariant(affiliation = "PUC",
                  author = "Gustavo Pizarro",
                  email = "gupizarr@uc.cl",
                  uiLabel = UITopiaVariant.USEPLUGIN)
  @PluginVariant(requiredParameterLabels = { 0 })
  public static Person procreate(final UIPluginContext context, XLog log) {
    ImproveDiscoveryConfiguration config = new ImproveDiscoveryConfiguration("Config");
    
    return procreate(context,  config,log);
  }
  
  
  public static ImproveDiscoveryConfiguration populate(final UIPluginContext context,
          final ImproveDiscoveryConfiguration config) {
	ProMPropertiesPanel panel = new ProMPropertiesPanel("Configure Procreation");
	ProMTextField name = panel.addTextField("Name", config.getName());
	final InteractionResult interactionResult = context.showConfiguration("Setup Procreation", panel);
	if (interactionResult == InteractionResult.FINISHED ||
	interactionResult == InteractionResult.CONTINUE ||
	interactionResult == InteractionResult.NEXT) {
	config.setName(name.getText());
	return config;
	}
	return null;
}
  
   
  public SocialNetwork getSN(XLog log) {
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
			//bpmnAnalysisData.setNoOfResources(originators.getClasses().size());
			
			//Originators
			for (int k = 0; k < originators.getClasses().size(); k++) {
			originatorList.add(originators.getByIndex(k).toString());
			}
			
			return originatorList;
  }
			/*
			ArrayList<String> activityNameList = new ArrayList<String>();
			DoubleMatrix2D otMatrix = null;
			
			XEventClasses activityNameClasses = summary.getNameClasses();
			for (int k = 0; k < activityNameClasses.getClasses().size(); k++) {
			activityNameList.add(activityNameClasses.getByIndex(k).toString());
			}
			int numOriginator = originatorList.size();
			
			otMatrix = DoubleFactory2D.sparse.make(numOriginator, activityNameList.size(), 0);
			
			for (XTrace trace : log) {
			for (int k = 0; k < trace.size(); k++) {
			XExtendedEvent xEvent = XExtendedEvent.wrap(trace.get(k));
			int row = originatorList.indexOf(xEvent.getResource());
			if (row == -1) {
			continue;
			}
			int column = activityNameList.indexOf(xEvent.getName());
			otMatrix.set(row, column, otMatrix.get(row, column) + 1.0);
			}
			}
			
			ArrayList<String, String, Integer> userActivityTripleList = new ArrayList<String, String, Integer>();
			for (int i = 0; i < otMatrix.rows(); i++) {
			for (int k = 0; k < otMatrix.columns(); k++) {
			String user = originatorList.get(i);
			String activity = activityNameList.get(k);
			Integer occurence = (int) otMatrix.get(i, k);
			Triple<String, String, Integer> userActivityTriple = new Triple<String, String, Integer>(user,
			activity, occurence / 2);
			userActivityTripleList.add(userActivityTriple);
			}
			}
			// sorting according to the number of times it is conducted by users
			Collections.sort(userActivityTripleList, new Comparator<Triple<String, String, Integer>>() {
			
			@Override
			public int compare(Triple<String, String, Integer> o1, Triple<String, String, Integer> o2) {
			if (o1.getThird().intValue() < o2.getThird().intValue())
			return 1;
			else if (o1.getThird().intValue() > o2.getThird().intValue())
			return -1;
			else
			return 0;
			}
			
			});
			return userActivityTripleList;
			}
			*/
}
 

