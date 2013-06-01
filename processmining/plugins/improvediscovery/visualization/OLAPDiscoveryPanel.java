package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.BorderLayout;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.heuristics.elements.Activity;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.CustomSocialNetworkAnalysisUI;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.OLAPData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.OLAPSocialTransformation;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.OLAPTransformation;
import org.processmining.plugins.socialnetwork.miner.miningoperation.UtilOperation;

import com.fluxicon.slickerbox.components.SlickerButton;


public class OLAPDiscoveryPanel extends JComponent  {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private JPanel WaitView;
	
	private OLAPData DiscoveryData;
	private  ProMJGraphPanel ModelPanel;
	//private ImproveDiscoveryModelPanel ModelPanel;
	private OLAPDiscoveryParametersPanel ParametersPanel;
	private PluginContext context;
	private ProMJGraphPanel comparator_panel;
	//private SocialNetworkAnalysisUI SNPanel;
	private CustomSocialNetworkAnalysisUI SNPanel;
	private CustomSocialNetworkAnalysisUI WorkingSNPanel;
    protected JLabel minSliderHeader; 
	private OLAPTransformation Transformation;
	private ProMJGraph jgraph;
	private boolean IsHeuristicAnalysis=true;
	private boolean LargeView=false;
	private int HeuristicViewWidth=1020;
	private int HeuristicViewHeigth=350;
	private int HeuristicViewDistance=250;
	private JFrame frame;
	private int SocialViewWidth=450;
	private int SocialViewHeigth=570;
	private int SocialViewDistance=470;
	private String currentSocialAnalysist="Working Together";
	private JLabel porcentaje;
	private ProgressMonitor progressMonitor;
	private boolean finishClusterUpdate=false;
	private  JPanel ButtonPanel;
	
	public OLAPDiscoveryPanel(final ProMJGraph jgraph,
			final OLAPTransformation Transformation,final PluginContext context) {

		this.jgraph=jgraph;
		this.setLayout(null);

		 this.Transformation=Transformation;
		 this.context=context;
		 this.DiscoveryData=Transformation.GetData();
         CreateBaseSocialPanel();		 
		 CreateHeuristicModel();	    
	    // ModelPanel= new ImproveDiscoveryModelPanel(jgraph,this.DiscoveryData.getHeuristicNet(),this.DiscoveryData.getHMinerAVSettings()); 
	     ParametersPanel= new OLAPDiscoveryParametersPanel(Transformation,
	    		 											  this.AddSocialTabEvents(),
	    		 											  this.AddClusterTabEvent(),
	    		 											  this.AddPerformanceTabEvent());   
         AddSocialCheckEvents();
         basicSocialEvents();
	     this.add(ModelPanel);
         this.add(ParametersPanel);
         
         //Button Panel
         CreateButtonPanel();
         this.add(ButtonPanel);
         Transformation.SetXLogBackUpUnit();
   }
	
	public void CreateBaseSocialPanel()
	{
		 this.SNPanel=new  CustomSocialNetworkAnalysisUI(context,DiscoveryData.getSocialNetwork());	 
		 this.SNPanel.setSize(new Dimension(450,570));
		 this.SNPanel.setBounds(20, 20, 450,570);
		 this.SNPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		 this.SNPanel.repaint();
	}
	public void CreateButtonPanel()
	{
        ButtonPanel= new JPanel();

        ButtonPanel.setSize(new Dimension(350,50));		 
        ButtonPanel.setBounds(1020, 0, 350,50);
        ButtonPanel.setBackground(new Color(100,100,100));
		 ButtonPanel.repaint();
	     ButtonPanel.add(ResourceBotton());
        ButtonPanel.add(ResetButton());
        ButtonPanel.add(FullScreen());
	}
	public void CreateHeuristicModel()
	{
		 ModelPanel=ProMJGraphVisualizer.instance().visualizeGraphWithoutRememberingLayout(DiscoveryData.getHeuristicsNetGraph());
		 ModelPanel.remove(ModelPanel.getComponent(1));
		 ModelPanel.remove(ModelPanel.getComponent(1));
		 ModelPanel.remove(ModelPanel.getComponent(3));
		 ModelPanel.remove(ModelPanel.getComponent(3));
		

		 
		 ModelPanel.getComponent(0).repaint();
		 ModelPanel.repaint();
		 ModelPanel.setAutoscrolls(false);
		 ModelPanel.setBounds(0,0,this.HeuristicViewWidth ,this.HeuristicViewHeigth-50);
		 ModelPanel.setSize(new Dimension(this.HeuristicViewWidth ,this.HeuristicViewHeigth-50));
		 ModelPanel.setPreferredSize(new Dimension(this.HeuristicViewWidth ,this.HeuristicViewHeigth-50));
		
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
				
				
				Transformation.ResetLog();
                   
				//Performance update
				   Transformation.GetData().InicializatePerformanceData();
				   Transformation.SetSocialTransformation(new OLAPSocialTransformation(Transformation.GetData()));
				   
				   ParametersPanel.mainPerformanceContainer.ResetPanel();
				   ParametersPanel.mainPerformanceContainer.repaint();
				   ParametersPanel.repaint();
				
				   ParametersPanel.tabPane.selectTab("Organizational");
				   ParametersPanel.tabPane.selectTab("Performance");
				   
                   AddPerformanceClickAction();
               //Remove comparative view
                   RemoveComparativeView();
                   BuiltGraph();
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
				    		new OLAPSocialTransformation(Transformation.GetData()));
					Transformation.GetSocialTransformation().WTCalculation();				 
				   ParametersPanel.mainSocialParameters.ResetPanel();
				   ParametersPanel.mainSocialParameters.repaint();
				   ParametersPanel.tabPane.repaint();
                   AddSocialCheckEvents();
                   Transformation.SetXLogBackUpUnit();

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
				   Transformation.GetClusterTransformation().GetClusterData().SetMainLog(Transformation.GetData().GetCurrentLog());
				   // Transformation.GetClusterTransformation()SetClusterData(
				  // new ImproveDiscoveryClusterData(Transformation.GetData().GetCurrentLog(), 1));
				   //ShowAdviceFrame();
					ParametersPanel.mainClusterParameters.CleanPanels();
				    ShowAdviceFrame();
				   ClusterSwingWorker();
				   //UpdateClusterParameters();

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
		this.Transformation.GenerateHeuristicModel();
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
			SocialPanelData(this.currentSocialAnalysist);
			this.WorkingSNPanel=new  CustomSocialNetworkAnalysisUI(context,DiscoveryData.getSocialNetwork());
			this.WorkingSNPanel.setSize(new Dimension(SocialViewWidth,SocialViewHeigth));
			this.WorkingSNPanel.setBounds(SocialViewDistance, 20, SocialViewWidth,SocialViewHeigth);
			this.WorkingSNPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			this.WorkingSNPanel.repaint();	
		}
		if(this.WorkingSNPanel!=null)
		{
			if(!this.LargeView)
			{
				WorkingSNPanel.setSize(new Dimension(SocialViewWidth,SocialViewHeigth));
			}
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
						redrawGraphWithSocialGroupSelection(false,check.getName());
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
	
	public void basicSocialEvents()
	{
		ParametersPanel.mainSocialParameters.Options.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				JComboBox combo= ParametersPanel.mainSocialParameters.Options;
				  Transformation.SetSocialTransformation(
						    new OLAPSocialTransformation(Transformation.GetData()));
				if(combo.getSelectedItem().toString().equals("Similar Task"))
				{
					currentSocialAnalysist="Similar Task";
					Transformation.GetSocialTransformation().STCalculation();			 
				}
				else if(combo.getSelectedItem().toString().equals("Working Together"))
				{
					currentSocialAnalysist="Working Together";
					Transformation.GetSocialTransformation().WTCalculation();				 						  
				}
	
			       ParametersPanel.mainSocialParameters.ResetPanel();
				   ParametersPanel.mainSocialParameters.repaint();
				   ParametersPanel.tabPane.selectTab("Performance");
				   ParametersPanel.tabPane.selectTab("Organizational");

				   AddSocialCheckEvents();
				   BuiltGraph();
			}});
	}
	public void AddCheckBoxesEvent()
    {
	
		
		
		//Options
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
			
			System.out.print("\n resource");

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
		if(type.equals("Working Together"))
		{
				DiscoveryData.SetSocialNetwork(UtilOperation.generateSN(
						Transformation.GetSocialTransformation().GetMatrix2DToShow("WT"), 
						Transformation.GetSocialTransformation().GetWorkingTogetherDataToShow().getOriginatorList()));
					
		}
		else if(type.equals("Similar Task"))
		{

			DiscoveryData.SetSocialNetwork(UtilOperation.generateSN(
					Transformation.GetSocialTransformation().GetMatrix2DToShow("ST"), 
					Transformation.GetSocialTransformation().GetSimilarTaskDataToShow().getOriginatorList()));
	
		
		}
		 
		

	}


	
	  public void ShowAdviceFrame()
	    {
		  ParametersPanel.mainClusterParameters.CleanPanels();


			frame = new JFrame("Cluster calculation");
		
			porcentaje= new JLabel("0 %");
			porcentaje.setFont(new Font("20f", 20, 20));
			porcentaje.setHorizontalAlignment(JLabel.CENTER);
			porcentaje.setHorizontalTextPosition(JLabel.CENTER);
			porcentaje.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			
			JLabel title=new JLabel("This plugin is recalculating the clusters");
			title.setFont(new Font("15f", 15, 20));

			JLabel message=new JLabel("Wait Please");
			message.setFont(new Font("15f", 15, 20));
			message.setHorizontalAlignment(JLabel.CENTER);
			message.setHorizontalTextPosition(JLabel.CENTER);
			message.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			
			JPanel panel= new JPanel();
			panel.setLayout(new BorderLayout());
			

			
			panel.add(title,BorderLayout.NORTH);
			panel.add(message,BorderLayout.CENTER);
			panel.add(porcentaje,BorderLayout.SOUTH);
			
			frame.getContentPane().add(panel);
			frame.setBounds(900, 300, 100, 100);

			//4. Size the frame.
			frame.pack();

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			frame.setVisible(true);
			
	 
			
	    }
	 
	public void  ClusterSwingWorker()
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
				   publish(1);
				   Transformation.GetClusterTransformation().Step1();
			       publish(40);
			       Transformation.GetClusterTransformation().Step2();
			       publish(50);
			       Transformation.GetClusterTransformation().Step3();
			       publish(60);
			       Transformation.GetClusterTransformation().Step4();
			       publish(70);
			       Transformation.GetClusterTransformation().Step5();
			       publish(80);


				   System.out.print("\n Update");
					Thread.sleep(1000);
					publish(0);
				    publish(100);

					     return true;
	        }	     
	     
			protected void done() {
			    
			try {
				
	             boolean status= get();
				if(status)
				{
				// Retrieve the return value of doInBackground.
					   ParametersPanel.mainClusterParameters. AddHeader();
				 	   ParametersPanel.mainClusterParameters.ClustersParameters();
					   AddClusterCheckEvents();		
					   AddCheckBoxesClusterEvents();
				       repaint();
				   
				   
					   ParametersPanel.mainClusterParameters.repaint();
					   ParametersPanel.tabPane.repaint();
					   ParametersPanel.repaint();
					   revalidate();
					   repaint();
		           System.out.print("\n done");
		           frame.dispose();
		         
		         //frame.dispose();
				}
			} catch (InterruptedException e) {
			     // This is thrown if the thread's interrupted.
			    } catch (ExecutionException e) {
			     // This is thrown if we throw an exception
			     // from doInBackground.
			    }
			   }

			   @Override
			   protected void process(List<Integer> chunks) {
			     //progressMonitor.setNote("Finish");
				   int mostRecentValue = chunks.get(chunks.size()-1);

				    porcentaje.setText(mostRecentValue+"%");
					   
					
			   }
			   
		};
		worker.execute();
	    }


}
