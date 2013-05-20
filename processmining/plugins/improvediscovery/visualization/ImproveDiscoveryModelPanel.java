package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeListener;

import org.processmining.framework.plugin.Progress;
import org.processmining.framework.util.Cleanable;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryData;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.gui.HeuristicsNetVisualization;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;

public class ImproveDiscoveryModelPanel extends HeuristicsNetVisualization implements Cleanable,
ChangeListener, ActionListener {

	private Progress DiscoveryProgress;
	private ImproveDiscoveryData DiscoveryData;
	public ProMJGraphPanel graphPanel;
	
	public ImproveDiscoveryModelPanel(final ProMJGraph heuristicsNetGraph,
			final HeuristicsNet net,
			final AnnotatedVisualizationSettings settings) {
		
		super(heuristicsNetGraph,net,settings);
		
		//graphPanel.add(this.graph);
		// TODO Auto-generated constructor stub

		                this.setOpaque(false);
					    this.remove(0);
					    this.remove(0);
					    this.remove(0);
					    this.remove(0);
					    this.remove(0);
					    this.remove(0);
					    this.remove(0);
					    this.remove(0);
					    this.remove(0);
					    this.remove(0);
					    
						//Cambiar tamaño a diagrama
					    this.getComponent(1).setSize(new Dimension(980,290));
						//LPanel.repaint();	
					    this.setAutoscrolls(false);
					    this.setBounds(0, 0,700 ,300);
					    this.setSize(new Dimension(750,300));
					    this.setPreferredSize(new Dimension(750,320));
					    this.setBackground(Color.orange);
					
					    

	}
	
	public ProMJGraph getGraph()
	{
		return graph;
	}
	
	public void setGraph( ProMJGraph Pgraph)
	{
		this.graph=Pgraph;
	}
	public void RemoveActivity()
	{
	
	   
	}
     
}