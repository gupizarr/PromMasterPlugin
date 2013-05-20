package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.heuristics.elements.Activity;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryTransformation;
import org.processmining.plugins.socialnetwork.analysis.SocialNetworkAnalysisUI;

import com.fluxicon.slickerbox.components.SlickerButton;


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
	private ImproveDiscoveryTransformation Transformation;
     

	public ImproveDiscoveryPanel(final ProMJGraph jgraph,
			final ImproveDiscoveryTransformation Transformation,final PluginContext context) {
		 
		 // tracealign
		 //SOCIAL 
		 //this.SNPanel=new SocialNetworkAnalysisUI(context,DiscoveryData.getSocialNetwork());
		 this.Transformation=Transformation;
		 this.context=context;
		 this.DiscoveryData=Transformation.GetData();
		 
		 
		 this.MainContainer = new JPanel();
		 this.MainContainer.setSize(new Dimension(1050,615));
		 this.MainContainer.setBounds(0, 0, 1050,615);	 
	     //this.MainContainer.setBackground(new Color(60, 60, 60));
	     this.MainContainer.repaint();
	     
	     ModelPanel= new ImproveDiscoveryModelPanel(jgraph,this.DiscoveryData.getHeuristicNet(),this.DiscoveryData.getHMinerAVSettings()); 
	     ParametersPanel= new ImproveDiscoveryParametersPanel(Transformation,AddTabEvents());
 
         AddPerformanceClickAction();
         AddCheckBoxesClusterEvents();
         AddSocialCheckEvents();
         
	     this.add(ModelPanel);
	     this.add(ResourceBotton());
         this.add(ParametersPanel);
         //this.SNPanel.setSize(new Dimension(400,400));
         //this.SNPanel.setBounds(200, 0, 800,500);
         //this.SNPanel.setBackground(Color.CYAN);
         //this.add(this.SNPanel);
         
	     this.add(this.MainContainer);
	}
	
	public     		ActionListener AddTabEvents()
	{
		//import com.fluxicon.slickerbox.components.ActionBarButton;
		//import com.fluxicon.slickerbox.components.PlayButton:
    	//import com.fluxicon.slickerbox.components.SlickerButton:
			

    		ActionListener Action= new     		ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			Transformation.GetData().SaveCurrentLogTransformation();
			ResetParameters();
			}};
			
			return Action;
	}
	
	public SlickerButton ResourceBotton()
	{
		
		SlickerButton detailButton = new SlickerButton();
		detailButton.setToolTipText("click to show model detail inspector");
		detailButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		detailButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		detailButton.setMinimumSize(new Dimension(70, 70));
		detailButton.setSize(new Dimension(70,70));
		detailButton.setBounds(900, 0, 75,75);	 
		detailButton.setText("Resources");
		return detailButton;
		
	}
	public void ResetParameters()
	{
		 ParametersPanel.ResetPerformance();
	}
	public void AddPerformanceClickAction()
	{
		
		  ParametersPanel.maxTimeSlider.addMouseListener(new MouseListener(){
				public void mouseClicked(MouseEvent arg0) {					BuiltGraph();}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseReleased(MouseEvent arg0) {}
	         });
	               
	      ParametersPanel.minTimeSlider.addMouseListener(new MouseListener(){

	 			public void mouseClicked(MouseEvent arg0) {
	 				BuiltGraph();
	 			}
	 			public void mouseEntered(MouseEvent arg0) {
	 			}

	 			public void mouseExited(MouseEvent arg0) {
	 			}
	 			public void mousePressed(MouseEvent arg0) {
	 			}
	 			public void mouseReleased(MouseEvent arg0) {
	 			}
	          
	          });

	}
	
	public void LabelMouseListenerAction(JLabel label)
	{
		label.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0){
			// Inicializar los checkbox de cases
				
				int value= -1;
				String key="";
				JLabel label= (JLabel) arg0.getComponent();
				
				value= Integer.parseInt(label.getName());
	
				if(label.getText().equals("-"))
				{
						if(Transformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(value)<15)
							
						{
							
						   Map<String,JCheckBox> MapCase=ParametersPanel.mainClusterParameters.ClustersCasesCheckBoxes;
						   for(int c=0;c<Transformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(value);c++)
						   {        
							   key=value+"-"+c;
							   MapCase.get(key).addMouseListener(new MouseListener(){
								   
								public void mouseClicked(MouseEvent e) {}
								public void mouseEntered(MouseEvent e) {}
								public void mouseExited(MouseEvent e) {}
								public void mousePressed(MouseEvent e) {
									
									//Buscar el name correcto para el CASE
									JCheckBox check=(JCheckBox) e.getComponent();
									int caseIndex= Integer.parseInt(
									check.getName().substring(check.getName().indexOf("-")+1));
									
									int value=Integer.parseInt(
									check.getName().substring(0,check.getName().indexOf("-")));					          
				    				if(check.isSelected())
									{// se selecciono para borrarlo
										redrawGraphWithTraces(value,caseIndex,true);
									}
									else
									{	
							
										redrawGraphWithTraces(value,caseIndex,false);
									}
								}
								public void mouseReleased(MouseEvent e) {}
							   
							   });
						   }
						}
						else
						{
						}
				}
				else
				{
				}
			}

			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			
 		});
	
	}
	
	public void LabelMouseListenerSubCluster(JLabel label)
	{
		label.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0){
			// Inicializar los checkbox de cases
			
				int value= -1;
				String key="";
				JLabel label= (JLabel) arg0.getComponent();		
			    value=Integer.parseInt(label.getName().substring(0,label.getName().indexOf("-")));

				if(label.getText().equals("-"))
				{
									
						   Map<String,JCheckBox> MapCase=ParametersPanel.mainClusterParameters.ClustersCasesCheckBoxes;
						   for(int c=0;c<Transformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(value);c++)
						   {        
							   key=value+"-"+c;
							   MapCase.get(key).addMouseListener(new MouseListener(){
	
								public void mouseClicked(MouseEvent e) {}
								public void mouseEntered(MouseEvent e) {}
								public void mouseExited(MouseEvent e) {}
								public void mousePressed(MouseEvent e) {
									
									//Buscar el name correcto para el CASE
									JCheckBox check=(JCheckBox) e.getComponent();
									int caseIndex= Integer.parseInt(
									check.getName().substring(check.getName().indexOf("-")+1));
									
									int value=Integer.parseInt(
									check.getName().substring(0,check.getName().indexOf("-")));					          
				    				if(check.isSelected())
									{// se selecciono para borrarlo
										redrawGraphWithTraces(value,caseIndex,true);
									}
									else
									{	
							
										redrawGraphWithTraces(value,caseIndex,false);
									}
								}
								public void mouseReleased(MouseEvent e) {}
						
							   });
						   }
					}
			}

			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			
 		});
	}
	
	public void AddLabelsEvent()
	{
 		JLabel label;
		for(int j=0;j<ParametersPanel.mainClusterParameters.JLabelClusterArray.size();j++)
	 	{
	 		label=ParametersPanel.mainClusterParameters.JLabelClusterArray.get(j);
	 	    LabelMouseListenerAction(label);
	 	}
		
		for(int c=0;c<ParametersPanel.mainClusterParameters.JLabelSubClusterArray.size();c++)
		{
	 		label=ParametersPanel.mainClusterParameters.JLabelSubClusterArray.get(c);
	 		LabelMouseListenerSubCluster(label);
		}
	}
	
	
	public void SocialGroupLabelEvents()
	{
		for(int j=0; j<ParametersPanel.mainSocialParameters.JCheckBoxGroups.size();j++)
		{
			ParametersPanel.mainSocialParameters.JCheckBoxGroups.get(j).addMouseListener(new MouseListener(){

				public void mouseClicked(MouseEvent arg0) {}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					JCheckBox check=(JCheckBox) arg0.getComponent();
					if(check.isSelected())
					{// se selecciono para borrarlo
						redrawGraphWithSocialGroupSelection(true,check.getName());
					}
					else
					{	
						redrawGraphWithSocialGroupSelection(true,check.getName());
					}
				}

				public void mouseReleased(MouseEvent arg0) {}});
		}		
	

		for(int k=0;k<ParametersPanel.mainSocialParameters.JLabelGroups.size();k++)
		{
			ParametersPanel.mainSocialParameters.JLabelGroups.get(k).addMouseListener(new MouseListener(){

				public void mouseClicked(MouseEvent e) {}

				public void mouseEntered(MouseEvent e) {}
					
				public void mouseExited(MouseEvent e) {}

				public void mousePressed(MouseEvent e) {
						AddResourcesEvent();
					
						for(int j=0;j<ParametersPanel.mainSocialParameters.JLabelResources.size();j++)
						{
							ParametersPanel.mainSocialParameters.JLabelResources.get(j).addMouseListener(new MouseListener(){

								public void mouseClicked(MouseEvent e) {}
								public void mouseEntered(MouseEvent e) {}
								public void mouseExited(MouseEvent e) {}
								public void mousePressed(MouseEvent e) {
									AddResourcesEvent();
								}
								public void mouseReleased(MouseEvent e) {	
								}});
						}
				}

				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}
	}
	
	public void AddResourcesEvent()
	{
		for(int j=0; j<ParametersPanel.mainSocialParameters.JCheckBoxResources.size();j++)
		{
			ParametersPanel.mainSocialParameters.JCheckBoxResources.get(j).addMouseListener(new MouseListener(){

				public void mouseClicked(MouseEvent arg0) {}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					JCheckBox check=(JCheckBox) arg0.getComponent();
					if(check.isSelected())
					{// se selecciono para borrarlo
						redrawGraph(check.getName(),true);
					}
					else
					{	
						redrawGraph(check.getName(),false);
					}
				}

				public void mouseReleased(MouseEvent arg0) {}});
		}		
	}
	public void AddSocialCheckEvents()
	{

		SocialGroupLabelEvents();
		ParametersPanel.mainSocialParameters.filterSlider.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
			SocialGroupLabelEvents();
			}
			public void mouseReleased(MouseEvent e) {}
			
		});
		
	}
    
	public void AddSubClusterEvents()
	{
		for(int u=0;u<ParametersPanel.mainClusterParameters.SubClusteredgesConcurrencyActiveBox.size();u++)
    	{
    	
    		this.ParametersPanel.mainClusterParameters.SubClusteredgesConcurrencyActiveBox.get(u).addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {
			    }

			public void mouseExited(MouseEvent e) {
				}

			public void mousePressed(MouseEvent e) {
				
				JCheckBox check=(JCheckBox) e.getComponent();
				int cluster_number=Integer.parseInt(
						check.getName().substring(0,check.getName().indexOf("-")));					          
				int number_of_cases=Transformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(cluster_number);
				int subClusterIndex= Integer.parseInt(check.getName().substring(check.getName().indexOf("-")+1));
				int subclusters=Math.round(number_of_cases/14);
				
				if(subclusters%14==0)
					subclusters--;
				
				int casesIniToRemove=0;
				int casesLastToRemove=0;

				if(subClusterIndex==subclusters)
				{
					casesIniToRemove=number_of_cases-number_of_cases%14;
					casesLastToRemove=number_of_cases;
				}
				else
				{
				for(int j=0;j<subClusterIndex;j++)
				{
					casesIniToRemove+=14;
				}
				casesLastToRemove=casesIniToRemove+14;
				}
					
				if(check.isSelected())
				{// se selecciono para borrarlo
				    redrawGraphWithSubCluster(cluster_number, casesIniToRemove,casesLastToRemove,true);
				}
				else
				{	
				    redrawGraphWithSubCluster(cluster_number, casesIniToRemove,casesLastToRemove,false);
				}
			}
			public void mouseReleased(MouseEvent e) {
				}
		});
    	}
		
	}

	
	public void AddClusterCheckEvents()
	{
		for(int u=0;u<ParametersPanel.mainClusterParameters.ClusteredgesConcurrencyActiveBox.size();u++)
    	{
    	
    		this.ParametersPanel.mainClusterParameters.ClusteredgesConcurrencyActiveBox.get(u).addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {}

			public void mousePressed(MouseEvent e) {
				
				JCheckBox check=(JCheckBox) e.getComponent();
				if(check.isSelected())
				{// se selecciono para borrarlo
				redrawGraphWithClusters(check.getName(),true);
				}
				else
				{	
				redrawGraphWithClusters(check.getName(),false);
				}
			}
			public void mouseReleased(MouseEvent e) {
				}
		});
    	}
	}
	
	public void AddCheckBoxesClusterEvents()
	{
	 	ParametersPanel.mainClusterParameters.numberOfClusters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Transformation.ClearRemovedClusters();
				Transformation.BuiltHeuristic();
				//ParametersPanel.mainClusterParameters.RestoreSelects();
				ParametersPanel.mainClusterParameters.AddClustersParameters(false);
				Transformation.GetData().ReturnToWorkingLog();

				AddClusterCheckEvents();
				AddSubClusterEvents();
				AddLabelsEvent();
				
				BuiltGraph();
			}
    	});
	 	
	 	AddClusterCheckEvents();
		AddSubClusterEvents();
	 	AddLabelsEvent();
		

	}
	  
	public void AddCheckBoxesEvent()
    {
   
    	for(int u=0;u<ParametersPanel.mainSocialParameters.edgesConcurrencyActiveBox.length;u++)
    	{
    		
    		this.ParametersPanel.mainSocialParameters.edgesConcurrencyActiveBox[u].addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {}

			public void mousePressed(MouseEvent e) {
				
				JCheckBox check=(JCheckBox) e.getComponent();
				if(check.isSelected())
					redrawGraph(check.getName(),true);
				else
				{
					redrawGraph(check.getName(),false);
				}
			}
			public void mouseReleased(MouseEvent e) {
				}
		});
		}
    }
    
    public void redrawGraphWithSubCluster(int cluster, int trace_ini,int trace_last,boolean remove)
    {
    	if(remove)
		{
    		Transformation.RemoveTracesFromSubCluster(cluster,trace_ini,trace_last);
    	}
    	else
    	{
    		Transformation.RestoreSubCluster(cluster,trace_ini,trace_last);
    	}
		BuiltGraph();

    }
   
    public void redrawGraphWithTraces(int cluster,int trace,boolean remove)
    {
		if(remove)
		{
			Transformation.RemoveTraceAndFilter(trace,cluster);
		}
		else
		{
			Transformation.RestoreCase(cluster,trace);
		}
		BuiltGraph();

    }
	
    public void redrawGraphWithSocialGroupSelection(boolean remove,String numberGroup)
    {
		if(remove)
		{
             Transformation.GroupFilter(numberGroup);
            
		}
		else
		{
		    Transformation.RestoreGroup(numberGroup);	
		}
		BuiltGraph();
    }
    public void redrawGraphWithClusters(String number,boolean remove)
	{
		Transformation.setContext(context);
		

		if(remove)
		{
             Transformation.RemoveClusterAndFilter(number);
            
		}
		else
		{
		    Transformation.RestoreCluster(number);	
		}
		BuiltGraph();
	}
	
	public void BuiltGraph()
	{
		if (comparator_panel!= null) {
			this.remove(comparator_panel);
			comparator_panel = null;
			this.revalidate();

		} 
		if (comparator_panel == null) {
				
		    if(Transformation.GetFixCase())
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

	
		if(remove)
		{
             Transformation.SocialFilter(id);
             
 	    }
		else
		{
			Transformation.AddSocialResource(id);
		}
		BuiltGraph();
	}


	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		
	    
		 this.setBackground( new Color(60, 60, 60));
		

	}

	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}


	

	 
	  



}
