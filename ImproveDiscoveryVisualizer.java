package PromMasterPlugin;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.ViewSpecificAttributeMap;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.gui.HeuristicsNetVisualizer;

/*heuristic reference*/


@Plugin(name = "Show ImproveDiscover",
        parameterLabels = { "Person" },
        returnLabels = { "Person Viewer" },
        returnTypes = { JComponent.class },
        userAccessible = false)
@Visualizer
public class ImproveDiscoveryVisualizer  extends HeuristicsNetVisualizer
{
	
	//Fuzzy parameters
	
	
  @PluginVariant(requiredParameterLabels = { 0 })
  public  JComponent visualize(final PluginContext context,
                                     final ImproveDiscoveryData DiscoveryData) {
	  ProMJGraph jgraph = createJGraph(DiscoveryData.getHeuristicsNetGraph(),new ViewSpecificAttributeMap(), context.getProgress());
	  
	  return new ImproveDiscoveryPanel(jgraph,
			  						   DiscoveryData.getHeuristicNet(),
			  						   DiscoveryData.getHMinerAVSettings());
	 
  }
  
  

}