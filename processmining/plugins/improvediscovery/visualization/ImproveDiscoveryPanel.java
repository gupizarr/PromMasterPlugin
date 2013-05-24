package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.heuristics.elements.Activity;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.CustomSocialNetworkAnalysisUI;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryClusterData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoverySocialTransformation;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryTransformation;
import org.processmining.plugins.socialnetwork.miner.miningoperation.BasicOperation;
import org.processmining.plugins.socialnetwork.miner.miningoperation.UtilOperation;

import com.fluxicon.slickerbox.components.SlickerButton;


public class ImproveDiscoveryPanel extends JComponent  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImproveDiscoveryData DiscoveryData;
	private  ImproveDiscoveryModelPanel ModelPanel;
	private ImproveDiscoveryParametersPanel ParametersPanel;
	private PluginContext context;
	private ProMJGraphPanel comparator_panel;
	//private SocialNetworkAnalysisUI SNPanel;
	private CustomSocialNetworkAnalysisUI SNPanel;
	private CustomSocialNetworkAnalysisUI WorkingSNPanel;
    protected JLabel minSliderHeader; 
	private ImproveDiscoveryTransformation Transformation;
	private ProMJGraph jgraph;
	private boolean IsHeuristicAnalysis=true;
	private boolean LargeView=false;
	private int HeuristicViewWidth=1020;
	private int HeuristicViewHeigth=350;
	private int HeuristicViewDistance=250;
	
	private int SocialViewWidth=950;
	private int SocialViewHeigth=570;
	private int SocialViewDistance=250;
	
	public ImproveDiscoveryPanel(final ProMJGraph jgraph,
			final ImproveDiscoveryTransformation Transformation,final PluginContext context) {

		this.jgraph=jgraph;
		this.setLayout(null);

		 this.Transformation=Transformation;
		 this.context=context;
		 this.DiscoveryData=Transformation.GetData();
		 this.SNPanel=new  CustomSocialNetworkAnalysisUI(context,DiscoveryData.getSocialNetwork());
		// this.WorkingSNPanel=new  CustomSocialNetworkAnalysisUI(context,DiscoveryData.getSocialNetwork());
		 
		 this.SNPanel.setSize(new Dimension(450,570));
		 this.SNPanel.setBounds(20, 20, 450,570);
		 this.SNPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		 this.SNPanel.repaint();
	     
	
	     		 
	           
	     
	     ModelPanel= new ImproveDiscoveryModelPanel(jgraph,this.DiscoveryData.getHeuristicNet(),this.DiscoveryData.getHMinerAVSettings()); 
	     ParametersPanel= new ImproveDiscoveryParametersPanel(Transformation,
	    		 											  this.AddSocialTabEvents(),
	    		 											  this.AddClusterTabEvent(),
	    		 											  this.AddPerformanceTabEvent());   
         AddSocialCheckEvents();
	     this.add(ModelPanel);

	    // this.add(ResetButton());
	     //this.add(ChangeViewAnalysistMode());
         this.add(ParametersPanel);
         
         JPanel ButtonPanel= new JPanel();
         ButtonPanel.setSize(new Dimension(350,50));		 
         ButtonPanel.setBounds(1020, 0, 350,50);
         ButtonPanel.setBackground(new Color(100,100,100));
		 ButtonPanel.repaint();
	     ButtonPanel.add(ResourceBotton());
         ButtonPanel.add(ResetButton());
         ButtonPanel.add(FullScreen());
         this.add(ButtonPanel);
         
   }
	
	public SlickerButton FullScreen()
	{
		final SlickerButton detailButton = new SlickerButton();
		detailButton.setToolTipText("click to view without compare");
		detailButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		detailButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		detailButton.setMinimumSize(new Dimension(55, 55));
		detailButton.setSize(new Dimension(55,55));
		detailButton.setBounds(960,140 , 55,55);	 
		detailButton.setText("Large View");
		detailButton.setBackground(Color.BLUE);
		detailButton.setFont(new Font("10f", 10, 9));
		detailButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(LargeView)
				{
					    detailButton.setText("Large View");
					    LargeView=false;
					    AddBasePanels();
						HeuristicViewDistance=250;
						HeuristicViewHeigth=350;
						SocialViewWidth=450;
						SocialViewDistance=480;
						
						BuiltGraph();
						
				}
				else
				{
					detailButton.setText("Compare View");
					LargeView=true;
					RemoveBaseHeuristicPanel();
					HeuristicViewDistance=0;
					HeuristicViewHeigth=650;
	    			SocialViewWidth=900;
					SocialViewDistance=20;
					BuiltGraph();
				}
				}
		});

		return detailButton;
	}
	
	public SlickerButton ChangeViewAnalysistMode()
	{
		final SlickerButton detailButton = new SlickerButton();
		detailButton.setToolTipText("click to view without compare");
		detailButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		detailButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		detailButton.setMinimumSize(new Dimension(55, 55));
		detailButton.setSize(new Dimension(55,55));
		detailButton.setBounds(960,140 , 55,55);	 
		detailButton.setText("Not \n Compare");
		detailButton.setBackground(Color.BLUE);
		detailButton.setFont(new Font("10f", 11, 9));
		return detailButton;
	}
	public  SlickerButton ResetButton()
	{
		final SlickerButton detailButton = new SlickerButton();
		detailButton.setToolTipText("click to reset analysist");
		detailButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		detailButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		detailButton.setMinimumSize(new Dimension(55, 55));
		detailButton.setSize(new Dimension(55,55));
		detailButton.setBounds(960, 70, 55,55);	 
		detailButton.setText("Reset");
		detailButton.setBackground(Color.BLUE);
	
		detailButton.setFont(new Font("10f", 11, 9));
		
		detailButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Transformation.GetData().ResetToOriginalLog();

				//Performance update
				   Transformation.GetData().InicializatePerformanceData();
				   ParametersPanel.mainPerformanceContainer.ResetPanel();
				   ParametersPanel.mainPerformanceContainer.repaint();
				   ParametersPanel.repaint();
				   System.out.print("\n nombre"+ParametersPanel.tabPane.getSelected().getName());
				   System.out.print("\n nombre"+ParametersPanel.tabPane.getSelected());

				   ParametersPanel.tabPane.selectTab("Organizational");
				   ParametersPanel.tabPane.selectTab("Performance");
				   
                   AddPerformanceClickAction();
               //Remove comparative view
                   RemoveComparativeView();
			}});

		
		return detailButton;
	}
	public void repaintThis()
	{
	this.repaint();
	}
	
    public void UpdateBaseLog()
    {
   
    	this.Transformation.GetData().SetTransformLogFromWorkingLog();
    }
	public ActionListener AddSocialTabEvents()
	{

    		ActionListener Action= new     		ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
			
				
				   UpdateBaseLog();
				   
					  
				    Transformation.SetSocialTransformation(
				    		new ImproveDiscoverySocialTransformation(Transformation.GetData()));
					Transformation.GetSocialTransformation().WTCalculation();				 

				   
				   ParametersPanel.mainSocialParameters.ResetPanel();
				   ParametersPanel.mainSocialParameters.repaint();
				   ParametersPanel.tabPane.repaint();
                   AddSocialCheckEvents();

			}
    		};
    		
			return Action;
	}
	
	public ActionListener AddPerformanceTabEvent()
	{

    		ActionListener Action= new     		ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				   UpdateBaseLog();
				   Transformation.GetData().InicializatePerformanceData();
				   ParametersPanel.mainPerformanceContainer.ResetPanel();
				   ParametersPanel.mainPerformanceContainer.repaint();
				   ParametersPanel.tabPane.repaint();
                   AddPerformanceClickAction();
                 
			}
    		};
    		
			return Action;
	}
	
	
	public ActionListener AddClusterTabEvent()
	{
		ActionListener Action= new     		ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				   
				  
 
				   UpdateBaseLog();
				   Transformation.GetClusterTransformation().SetClusterData(
				   new ImproveDiscoveryClusterData(Transformation.GetData().GetCurrentLog(), 1));
				   

				   Transformation.GetClusterTransformation().MakeProcessAlign();
				   ParametersPanel.mainClusterParameters.ResetPanel();
				   ParametersPanel.mainClusterParameters.repaint();
				   ParametersPanel.tabPane.repaint();
				   
       		 	    AddClusterCheckEvents();

			}
    		};
    		
			return Action;
	}
	

	public boolean HeuristicViewIsVisibile()
	{
		return Arrays.asList(this.getComponents()).contains(ModelPanel);
	}
	
	public void RemoveComparativeView()
	{
		if(Arrays.asList(this.getComponents()).contains(comparator_panel))
		{
		this.remove(comparator_panel);	
		}
		if(Arrays.asList(this.getComponents()).contains(this.WorkingSNPanel))
		{
		this.remove(this.WorkingSNPanel);
		}		
		
		this.repaint();
			
	}
	public SlickerButton ResourceBotton()
	{
		
		final SlickerButton detailButton = new SlickerButton();
		detailButton.setToolTipText("click to show model detail inspector");
		detailButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		detailButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		detailButton.setMinimumSize(new Dimension(55, 55));
		detailButton.setSize(new Dimension(55,55));
		detailButton.setBounds(960, 0, 55,55);	 
		detailButton.setText("Resources");
		detailButton.setBackground(Color.BLUE);
	
		detailButton.setFont(new Font("10f", 11, 9));
		detailButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				if(IsHeuristicAnalysis)
				{
					IsHeuristicAnalysis=false;
					detailButton.setText("Process");
					RemoveHeuristicPanel();
                    AddSocialPanel();

				}
				else
				{
					IsHeuristicAnalysis=true;
					RemoveSocialPanel();
					detailButton.setText("Resources");

					AddHeuristic();
			
				}
			}});

		return detailButton;
		
	}
	
	public void AddHeuristic()
	{
			
		if(this.comparator_panel!=null)
		this.add(comparator_panel);

		if(!this.LargeView)
		this.add(this.ModelPanel);
		
		
		this.revalidate();
		this.repaint();		
	}
	public void AddSocialPanel()
	{
		if(!this.LargeView)
		{
		this.add(this.SNPanel);
		}
		else if (this.LargeView && this.WorkingSNPanel==null)
		{
			SocialPanelData("Working Together");
			this.WorkingSNPanel=new  CustomSocialNetworkAnalysisUI(context,DiscoveryData.getSocialNetwork());
			this.WorkingSNPanel.setSize(new Dimension(SocialViewWidth,SocialViewHeigth));
			this.WorkingSNPanel.setBounds(SocialViewDistance, 20, SocialViewWidth,SocialViewHeigth);
			this.WorkingSNPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			this.WorkingSNPanel.repaint();	
		}
		if(this.WorkingSNPanel!=null)
		{
		    this.add(this.WorkingSNPanel);
		}
		//this.add(WorkingSNPanel);
		this.validate();
		this.repaint();

	}
	
	public void RemoveSocialPanel()
	{
		
		if(this.WorkingSNPanel!=null)
	    this.remove(this.WorkingSNPanel);

			
	    this.remove(this.SNPanel);
	    this.revalidate();
	    this.repaint();

	}
	
	public void RemoveBaseHeuristicPanel()
	{
		  if(IsHeuristicAnalysis)
			  this.remove(this.ModelPanel);
		  else
			  this.remove(SNPanel);
		  
		  this.repaint();
	}
	
	public void AddBasePanels()
	{
		  if(IsHeuristicAnalysis)
		  {
			  this.add(this.ModelPanel);
		  }
		  else
		  {
			  this.remove(this.WorkingSNPanel);
			  this.add(SNPanel);
		  }
		  this.repaint();	
	}
	
		
	public void RemoveHeuristicPanel()
	{
	  
	  this.remove(this.ModelPanel);
	  
	  if(this.comparator_panel!=null)
	  this.remove(this.comparator_panel);
	  
	  this.revalidate();
	  this.repaint();

	}

	public void AddPerformanceClickAction()
	{
		 ParametersPanel.mainPerformanceContainer.maxTimeSlider.addMouseListener(new MouseListener(){
				public void mouseClicked(MouseEvent arg0) {					
					BuiltGraph();}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseReleased(MouseEvent arg0) {}
	         });
	               
		 ParametersPanel.mainPerformanceContainer.minTimeSlider.addMouseListener(new MouseListener(){

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
							for(int c=0;c<ParametersPanel.mainClusterParameters.JLabelSubClusterArray.size();c++)
							{
						 		label=ParametersPanel.mainClusterParameters.JLabelSubClusterArray.get(c);
						 		
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
		ParametersPanel.mainSocialParameters.filterSlider.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				BuiltGraph();
			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
		
		
		for(int j=0; j<ParametersPanel.mainSocialParameters.JCheckBoxGroups.size();j++)
		{
			ParametersPanel.mainSocialParameters.JCheckBoxGroups.get(j).addMouseListener(new MouseListener(){

				public void mouseClicked(MouseEvent arg0) {}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					JCheckBox check=(JCheckBox) arg0.getComponent();
					if(check.isSelected() && ParametersPanel.mainSocialParameters.RelationsPanel.getComponentCount()==0)
					{// se selecciono para borrarlo
						redrawGraphWithSocialGroupSelection(true,check.getName());
					}
					else if(check.isSelected() && ParametersPanel.mainSocialParameters.RelationsPanel.getComponentCount()>0)
					{
						check.setSelected(false);
					}
					else
					{	
						redrawGraphWithSocialGroupSelection(true,check.getName());
					}
				}
//aqui
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
				Transformation.GetData().ReturnToBaseLog();

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
		if(IsHeuristicAnalysis)
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
							    
							    comparator_panel.remove(comparator_panel.getComponent(1));
							    comparator_panel.remove(comparator_panel.getComponent(1));
							    comparator_panel.getComponent(0).repaint();
							    comparator_panel.repaint();
							    comparator_panel.setAutoscrolls(false);
								comparator_panel.setBounds(0, this.HeuristicViewDistance,this.HeuristicViewWidth ,this.HeuristicViewHeigth);
								comparator_panel.setSize(new Dimension(this.HeuristicViewWidth ,this.HeuristicViewHeigth));
								comparator_panel.setPreferredSize(new Dimension(this.HeuristicViewWidth ,this.HeuristicViewHeigth));
								comparator_panel.setBackground(Color.orange);
							    
								this.add(comparator_panel);
								this.revalidate();	
						        
								
						}
						
						//update working panel
						SocialPanelData("Working Together");
						this.CreateSocialWorkingView();
					
						
		}
		//Social view
		else
		{
			SocialPanelData("Working Together");

			if(WorkingSNPanel!=null)
			{
			this.remove(WorkingSNPanel);
			}
			
			this.repaint();
			this.revalidate();
			CreateSocialWorkingView();

			this.add(WorkingSNPanel);
			this.repaint();
			this.revalidate();
			
			System.out.print("resource");
			//this.Transformation.GetCurrentResourceList();

		}
	}
	
	public void CreateSocialWorkingView()
	{
		this.WorkingSNPanel=new  CustomSocialNetworkAnalysisUI(context,DiscoveryData.getSocialNetwork());
		this.WorkingSNPanel.setSize(new Dimension(SocialViewWidth,SocialViewHeigth));
		this.WorkingSNPanel.setBounds(SocialViewDistance, 20, SocialViewWidth,SocialViewHeigth);
		this.WorkingSNPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.WorkingSNPanel.repaint();
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
	
	public void SocialPanelData(String type)
	{
		BasicOperation baseOprtation = null;
		if(type.equals("Working Together"))
		{
	  	 
				DiscoveryData.SetSocialNetwork(UtilOperation.generateSN(
						Transformation.GetSocialTransformation().GetWorkingTogetherMatrix2DToShow(), 
						Transformation.GetSocialTransformation().GetWorkingTogetherDataToShow().getOriginatorList()));
					
		}
		else if(type.equals("Similar Task"))
		{
		
		}
		 
		

	}


	

	 
	  



}
