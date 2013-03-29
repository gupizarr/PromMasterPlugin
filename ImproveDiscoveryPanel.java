package PromMasterPlugin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import org.processmining.framework.plugin.Progress;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;

public class ImproveDiscoveryPanel extends JComponent  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImproveDiscoveryData DiscoveryData;
	private  ImproveDiscoveryModelPanel ModelPanel;
	private Progress DiscoveryProgress;
	private ImproveDiscoveryParametersPanel ParametersPanel;
	
	
	public ImproveDiscoveryPanel(final ProMJGraph jgraph,
			final HeuristicsNet net,
			final AnnotatedVisualizationSettings settings) {
		
	
         ModelPanel= new ImproveDiscoveryModelPanel(jgraph,net,settings);
         ParametersPanel= new ImproveDiscoveryParametersPanel();
         this.add(ModelPanel,BorderLayout.NORTH);

         this.add(ParametersPanel);
					//this.add(MainPanel);
					//final_view.add(BuiltheuristicPanel(1));
         this.setSize(new Dimension(1200,700));
		 //this.setBounds(0, 0, 1200,700);
		 this.setAutoscrolls(false);
		 this.setBackground(Color.BLUE);
		// TODO Auto-generated constructor stub
	}
	



	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {

		
		

	}
	

	 
	  



}
