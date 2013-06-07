package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.ViewSpecificAttributeMap;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.gui.HeuristicsNetVisualizer;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTransformation;


/**
 *  Visualizer.
 *  @author Gustavo Pizarro
*/

@Plugin(name = "Show OLAPDiscovery",
        parameterLabels = { "OLAP" },
        returnLabels = { "Model Viewer" },
        returnTypes = { JComponent.class },
        userAccessible = false)
@Visualizer
public class OLAPDiscoveryVisualizer  extends HeuristicsNetVisualizer
{

	
	
  @PluginVariant(requiredParameterLabels = { 0 })
  public  JComponent visualize(final PluginContext context,
                                     final OLAPTransformation DiscoveryTransformation) {
	  ProMJGraph jgraph = createJGraph(DiscoveryTransformation.GetData().getHeuristicsNetGraph(),
			  							new ViewSpecificAttributeMap(), context.getProgress());
	  
	  return new OLAPDiscoveryPanel(jgraph,DiscoveryTransformation,context);
	 
  }
  
  

}