package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.heuristics.elements.Activity;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryTransformation;
import org.processmining.plugins.socialnetwork.analysis.SocialNetworkAnalysisUI;


public class ImproveDiscoveryPanel extends JComponent  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImproveDiscoveryData DiscoveryData;
	private  ImproveDiscoveryModelPanel ModelPanel;
	private ImproveDiscoveryParametersPanel ParametersPanel;
	private JPanel MainContainer;
	private PluginContext context;
	private ProMJGraphPanel comparator_panel;
	private SocialNetworkAnalysisUI SNPanel;
	private ImproveDiscoveryTransformation transformationTraceAlign;
     

	public ImproveDiscoveryPanel(final ProMJGraph jgraph,
			final ImproveDiscoveryData DiscoveryData,final PluginContext context) {
		 
		 // tracealign
		 //SOCIAL 
		 this.SNPanel=new SocialNetworkAnalysisUI(context,DiscoveryData.getSocialNetwork());
		 
		 this.context=context;
		 this.DiscoveryData=DiscoveryData;
		
		 this.MainContainer = new JPanel();
		 this.MainContainer.setSize(new Dimension(1350,615));
		 this.MainContainer.setBounds(0, 0, 1350,615);	 
	     //this.MainContainer.setBackground(new Color(60, 60, 60));
	     this.MainContainer.repaint();
	     
	     ModelPanel= new ImproveDiscoveryModelPanel(jgraph,this.DiscoveryData.getHeuristicNet(),this.DiscoveryData.getHMinerAVSettings()); 
	     ParametersPanel= new ImproveDiscoveryParametersPanel(DiscoveryData);
         this.add(ModelPanel);
         this.add(ParametersPanel);
         this.SNPanel.setSize(new Dimension(400,400));
         this.SNPanel.setBounds(200, 0, 800,500);
         this.SNPanel.setBackground(Color.CYAN);
         this.add(this.SNPanel);
         
	     this.add(this.MainContainer);

         
         AddCheckBoxesEvent();
         //AddCheckBoxesClusterEvents();

		// TODO Auto-generated constructor stub
	}
	
	public void UpdateDiagram()
	{
/*
		this.context = context;
		this.bpmn = bpmn;
		this.proMJGraphPanel = ProMJGraphVisualizer.instance().visualizeGraphWithoutRememberingLayout(bpmn);
		this.viewSpecificMap = proMJGraphPanel.getGraph().getViewSpecificAttributes();
       
		String message = "";
		ConnectionManager conn = context.getConnectionManager();
		try {
			message = "BPMNAnalysisConnection";
			BPMNAnalysisConnection BPMNConnection = conn
					.getFirstConnection(BPMNAnalysisConnection.class, context, bpmn);

			BPMNAnalysisDataList BPMNAnalysisList = (BPMNAnalysisDataList) BPMNConnection
					.getObjectWithRole(BPMNAnalysisConnection.BPMNAnalysisList);

			this.flex = (Flex) BPMNConnection.getObjectWithRole(BPMNAnalysisConnection.FLEX);

			message = "FlexCodecConnection";
			FlexCodecConnection codecConn = conn.getFirstConnection(FlexCodecConnection.class, context, flex);
			this.codec = (FlexCodec) codecConn.getObjectWithRole(FlexCodecConnection.FLEXCODEC);

			firstBPMNAnalysisData = BPMNAnalysisList.get(0);
			this.selectedBPMNAnalysisData = firstBPMNAnalysisData;
			fillFlexBPMNNodeEdgeMappings(bpmn, flex
			initVisualization(BPMNAnalysisList, proMJGraphPanel);
			updatePanelsAndColorModel();
		} catch (ConnectionCannotBeObtained e) {
			e.printStackTrace();
			System.out.println("Exception occured while obtaining " + message);
		}

	}
*/
	}
    
	public void AddCheckBoxesClusterEvents()
	{
		for(int u=0;u<ParametersPanel.ClusteredgesConcurrencyActiveBox.length;u++)
    	{
    		
    		this.ParametersPanel.ClusteredgesConcurrencyActiveBox[u].addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			;
			}

			public void mouseEntered(MouseEvent e) {
			    }

			public void mouseExited(MouseEvent e) {
				}

			public void mousePressed(MouseEvent e) {
				
				JCheckBox check=(JCheckBox) e.getComponent();
				redrawGraphWithClusters(check.getName(),check.isSelected());
		
			}
			public void mouseReleased(MouseEvent e) {
				}
		});
    	}
	}
	
    public void AddCheckBoxesEvent()
    {
    	for(int u=0;u<ParametersPanel.edgesConcurrencyActiveBox.length;u++)
    	{
    		
    		this.ParametersPanel.edgesConcurrencyActiveBox[u].addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			;
			}

			public void mouseEntered(MouseEvent e) {
			    }

			public void mouseExited(MouseEvent e) {
				}

			public void mousePressed(MouseEvent e) {
				JCheckBox check=(JCheckBox) e.getComponent();
				if(check.isSelected())
					redrawGraph(check.getName(),true);
				else
					redrawGraph(check.getName(),false);

			}
			public void mouseReleased(MouseEvent e) {
				}
		});
		}
    }
    
	public void redrawGraphWithClusters(String number,boolean remove)
	{
		DiscoveryData.setContext(context);
		
		if(remove)
		{
             DiscoveryData.ClusterFilter(number);
 	    }
		else
		{
			//DiscoveryData.AddSocialGroup(id);
		}
		
		if (comparator_panel!= null) {
			this.remove(comparator_panel);
			comparator_panel = null;
			this.revalidate();

		} 
		if (comparator_panel == null) {
				
		    if(DiscoveryData.GetFixCase())
		    {
				Iterator<Activity> iterador= this.DiscoveryData.getHeuristicsNetGraph().getActivities().iterator();
				
				Activity ac= iterador.next();
				this.DiscoveryData.getHeuristicsNetGraph().removeActivity(ac);  
		
			}
			    comparator_panel=ProMJGraphVisualizer.instance().visualizeGraphWithoutRememberingLayout(DiscoveryData.getHeuristicsNetGraph());
			    

			    
			    comparator_panel.setAutoscrolls(false);
				comparator_panel.setBounds(0, 250,950 ,350);
				comparator_panel.setSize(new Dimension(950,350));
				comparator_panel.setPreferredSize(new Dimension(950,350));
				comparator_panel.setBackground(Color.orange);
			    
				this.add(comparator_panel);
				this.revalidate();
		
		        
		}
	}
	public void redrawGraph(String id,boolean remove) {

		DiscoveryData.setContext(context);
	
		if(remove)
		{
             DiscoveryData.SocialFilter(id);
 	    }
		else
		{
			DiscoveryData.AddSocialGroup(id);
		}
		
		if (comparator_panel!= null) {
			this.remove(comparator_panel);
			comparator_panel = null;
			this.revalidate();

		} 
		if (comparator_panel == null) {
				
		    if(DiscoveryData.GetFixCase())
		    {
				Iterator<Activity> iterador= this.DiscoveryData.getHeuristicsNetGraph().getActivities().iterator();
				
				Activity ac= iterador.next();
				this.DiscoveryData.getHeuristicsNetGraph().removeActivity(ac);  
		
			}
			    comparator_panel=ProMJGraphVisualizer.instance().visualizeGraphWithoutRememberingLayout(DiscoveryData.getHeuristicsNetGraph());
			    

			    
			    comparator_panel.setAutoscrolls(false);
				comparator_panel.setBounds(0, 250,950 ,350);
				comparator_panel.setSize(new Dimension(950,350));
				comparator_panel.setPreferredSize(new Dimension(950,350));
				comparator_panel.setBackground(Color.orange);
			    
				this.add(comparator_panel);
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
