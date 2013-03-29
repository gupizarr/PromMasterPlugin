package PromMasterPlugin;

import java.awt.Color;
import java.awt.Dimension;

import org.processmining.framework.plugin.Progress;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.gui.HeuristicsNetVisualization;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;

public class ImproveDiscoveryModelPanel extends HeuristicsNetVisualization{

	private Progress DiscoveryProgress;
	private ImproveDiscoveryData DiscoveryData;

	public ImproveDiscoveryModelPanel(final ProMJGraph heuristicsNetGraph,
			final HeuristicsNet net,
			final AnnotatedVisualizationSettings settings) {
		
		super(heuristicsNetGraph,net,settings);
		// TODO Auto-generated constructor stub

					    
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
					    this.setBounds(0, 0,500 ,300);
					    this.setSize(new Dimension(500,300));
					    this.setPreferredSize(new Dimension(500,320));
					    this.setBackground(Color.cyan);

	}

}