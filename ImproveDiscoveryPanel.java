package PromMasterPlugin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.Progress;
import org.processmining.models.heuristics.HeuristicsNetGraph;
import org.processmining.models.heuristics.elements.Activity;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationGenerator;
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
	private JPanel MainContainer;
	private ProMJGraph jgraph;
	private PluginContext context;
	private ProMJGraphPanel comparator_panel;


	public ImproveDiscoveryPanel(final ProMJGraph jgraph,
			final ImproveDiscoveryData DiscoveryData,final PluginContext context) {
		 this.context=context;
		 this.jgraph=jgraph;
		 this.DiscoveryData=DiscoveryData;
		
		 this.MainContainer = new JPanel();
		 this.MainContainer.setSize(new Dimension(1350,615));
		 this.MainContainer.setBounds(0, 0, 1350,615);	 
	     this.MainContainer.setBackground(new Color(60, 60, 60));
	     this.MainContainer.repaint();
	     
	     ModelPanel= new ImproveDiscoveryModelPanel(jgraph,this.DiscoveryData.getHeuristicNet(),this.DiscoveryData.getHMinerAVSettings());
         ParametersPanel= new ImproveDiscoveryParametersPanel();
         this.add(ModelPanel);
         this.add(ParametersPanel);
         this.add(this.MainContainer);
         JButton j= new JButton("Hola");
         
         
         
         j.addActionListener(new ActionListener() {
        	  public void actionPerformed(ActionEvent evt) {
        		  redrawGraph();
        	  }
        	});   
            
            j.setSize(new Dimension(50,30));
		    j.setBounds(300, 300, 50, 30);
		    j.setBackground(Color.BLUE);
		    
		    this.MainContainer.add(j);
		// TODO Auto-generated constructor stub
	}
	

	public void refresh()
	{
		java.util.Iterator<Activity> i1= DiscoveryData.getHeuristicsNetGraph().getActivities().iterator();
	    Activity a= i1.next();
	    DiscoveryData.getHeuristicsNetGraph().removeActivity(a);
	    
	    
		AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
	    AnnotatedVisualizationSettings settings = new AnnotatedVisualizationSettings();
		HeuristicsNetGraph graph = generator.generate(DiscoveryData.getHeuristicNet(), settings); 
	    
		DiscoveryData.setAnnotatedVisualizationSettings(settings);
		DiscoveryData.setHeuristicsNetGraph(graph);

	}

	public void redrawGraph() {

	
		java.util.Iterator<Activity> i1= DiscoveryData.getHeuristicsNetGraph().getActivities().iterator();
	    Activity a= i1.next();
	    DiscoveryData.RemoveActivity(a);
	    DiscoveryData.getHeuristicsNetGraph().removeActivity(a);
		// DiscoveryData.setHeuristicsNetGraph( new  HeuristicsNetGraph(null, name, autoscrolls));
		if (comparator_panel!= null) {
			this.remove(comparator_panel);
			comparator_panel = null;
			this.revalidate();
		}
		if (comparator_panel == null) {
			//			System.out.println("Creating new graph panel");
			comparator_panel=ProMJGraphVisualizer.instance().visualizeGraph(context, DiscoveryData.getHeuristicsNetGraph());
			//			System.out.println("Replacing graph panel");
			comparator_panel.setAutoscrolls(false);
			comparator_panel.setBounds(0, 300,500 ,300);
			comparator_panel.setSize(new Dimension(500,300));
			comparator_panel.setPreferredSize(new Dimension(500,320));
			comparator_panel.setBackground(Color.orange);
		
			this.add(comparator_panel, BorderLayout.CENTER);
			this.revalidate();

		}


	}


	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		
	    
		 this.setBackground( new Color(60, 60, 60));
		

	}
	

	 
	  



}
