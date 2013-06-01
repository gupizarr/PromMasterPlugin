package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeListener;

import org.processmining.framework.util.Cleanable;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.gui.HeuristicsNetVisualization;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;

public class OLAPDiscoveryModelPanel extends HeuristicsNetVisualization implements Cleanable,
ChangeListener, ActionListener {


	public ProMJGraphPanel graphPanel;
	public ProMJGraph heuristicsNetGraph;
	public OLAPDiscoveryModelPanel(ProMJGraph heuristicsNetGraph,
			final HeuristicsNet net,
			final AnnotatedVisualizationSettings settings) {
		
		
		super(heuristicsNetGraph,net,settings);
		this.heuristicsNetGraph=heuristicsNetGraph;
		//graphPanel.add(this.graph);
		// TODO Auto-generated constructor stub

		//8 componetes
		                this.setOpaque(false);
					    //this.remove(0);
					    //this.remove(0);
					    this.remove(2);
					    this.remove(2);
					    this.remove(2);
					    this.remove(2);
					    this.remove(2);
					    this.remove(2);
					    this.remove(2);
					    this.remove(2);
					    
						//Cambiar tamaño a diagrama
					    this.getComponent(1).setSize(new Dimension(1020,300));
						//LPanel.repaint();	
					    this.setAutoscrolls(false);
					    this.setBounds(0, 0,1020 ,300);
					    this.setSize(new Dimension(1020,300));
					    this.setPreferredSize(new Dimension(1020,320));
	}
	
	public void RevalidateAll()
	{
		
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
	public void RepaintChildrens()
	{
		for(int j=0; j<this.getComponents().length;j++)
		{
			this.getComponent(j).repaint();
		}
	}
     
}