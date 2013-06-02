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
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;

import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.socialnetwork.SocialNetwork;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.heuristics.HeuristicsNetGraph;
import org.processmining.models.heuristics.elements.Activity;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.CustomSocialNetworkAnalysisUI;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.OLAPData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.OLAPSocialTransformation;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.OLAPTransformation;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.HeuristicsMiner;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationGenerator;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;
import org.processmining.plugins.socialnetwork.miner.miningoperation.UtilOperation;

import com.fluxicon.slickerbox.components.SlickerButton;
import com.fluxicon.slickerbox.ui.SlickerCheckBoxUI;


public class OLAPDiscoveryPanel extends JComponent  {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private JPanel WaitView;
	
	private OLAPData DiscoveryData;
	private  ProMJGraphPanel ModelPanel;
	//private ImproveDiscoveryModelPanel ModelPanel;
	protected OLAPDiscoveryParametersPanel ParametersPanel;
	private PluginContext context;
	private ProMJGraphPanel HeuristicComparatorVersion;
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
	private int SocialViewWidth=450;
	private int SocialViewHeigth=570;
	private int SocialViewDistance=470;
	private String currentSocialAnalysist="Working Together";
	private JLabel porcentaje;
	private ProgressMonitor progressMonitor;
	private boolean finishClusterUpdate=false;
	private  JPanel ButtonPanel;
	private ClusterSwingWorker clusterSwingWorker;
	private JLabel LogWarning;
	private boolean showAlwaysOriginalProcess=false;
	public OLAPDiscoveryPanel(final ProMJGraph jgraph,
			final OLAPTransformation Transformation,final PluginContext context) {

		this.jgraph=jgraph;
		this.setLayout(null);

		 this.Transformation=Transformation;
		 this.context=context;
		 this.DiscoveryData=Transformation.GetData();
         CreateBaseSocialPanel(DiscoveryData.getSocialNetwork());		 
		 CreateHeuristicModel(DiscoveryData.getHeuristicsNetGraph());	    
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
         clusterSwingWorker= new ClusterSwingWorker(Transformation,this);
         LogWarning=new JLabel("There are not enough traces, press Reset");
         
   }
	
	public  JLabel GetLogWarning()
	{
		return LogWarning;
	}
	public void CreateBaseSocialPanel(SocialNetwork sn)
	{
		 this.SNPanel=new  CustomSocialNetworkAnalysisUI(context,sn);	 
		 this.SNPanel.setSize(new Dimension(450,570));
		 this.SNPanel.setBounds(20, 20, 450,570);
		 this.SNPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		 this.SNPanel.repaint();
	}
	public void CreateButtonPanel()
	{
        ButtonPanel= new JPanel();

        ButtonPanel.setSize(new Dimension(350,60));		 
        ButtonPanel.setBounds(1020, 0, 350,60);
        ButtonPanel.setBackground(new Color(100,100,100));
		 ButtonPanel.repaint();
	     ButtonPanel.add(ResourceBotton());
        ButtonPanel.add(ResetButton());
        ButtonPanel.add(FullScreen());
        
        JCheckBox j= new JCheckBox("Kept always the original model");
        j.setSelected(false);
        j.setOpaque(false);
        j.setUI(new SlickerCheckBoxUI());
        j.addMouseListener(new MouseListener(){

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
				JCheckBox check=(JCheckBox) arg0.getComponent();
				if(check.isSelected())
				{
				showAlwaysOriginalProcess=false;
				UpdateSNPanel();
				}
				else
				{
					showAlwaysOriginalProcess=true;
					GetOriginalViews();
				}	
			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
        ButtonPanel.add(j);
	}
	public void CreateHeuristicModel(HeuristicsNetGraph HN)
	{
		 ModelPanel=ProMJGraphVisualizer.instance().visualizeGraphWithoutRememberingLayout(HN);
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
				
				  // ParametersPanel.tabPane.selectTab("Organizational");
				   ParametersPanel.tabPane.selectTab("Performance");
				   
                   AddPerformanceClickAction();
                 //Remove comparative view
                   RemoveComparativeView();
       			   currentSocialAnalysist="Working Together";
                   ResetAuxiliarSocialNet();
                   UpdateSNPanel();
                   BuiltGraph();
			}});

		
		return detailButton;
	}
	
	public void ResetAuxiliarSocialNet()
	{
		if(currentSocialAnalysist.equals("Similar Task"))
		{
		

			currentSocialAnalysist="Similar Task";
			Transformation.GetSocialTransformation().RecalculateSocialRelations("ST",0);
			Transformation.GetSocialTransformation().SearchGroups(Transformation.GetSocialTransformation(). GetMatrix2D("ST"));
		}
		else if(currentSocialAnalysist.equals("Working Together"))
		{
			currentSocialAnalysist="Working Together";
			Transformation.GetSocialTransformation().RecalculateSocialRelations("WT",0);
			Transformation.GetSocialTransformation().SearchGroups(Transformation.GetSocialTransformation(). GetMatrix2D("WT"));
	
			//Transformation.GetSocialTransformation().WTCalculation();				 						  
		}
		else
		{
			currentSocialAnalysist="Handover of Work";
			Transformation.GetSocialTransformation().RecalculateSocialRelations("HW",0);
			Transformation.GetSocialTransformation().SearchGroups(Transformation.GetSocialTransformation(). GetMatrix2D("HW"));
	
			//Transformation.GetSocialTransformation().HWCalculation();	
		}
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
                   ParametersPanel.mainSocialParameters.Options.getItemAt(0);
                   UpdateSNPanel();//="Working Together");

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
				   UpdateSNPanel();
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
					ParametersPanel.mainClusterParameters.CleanPanels();
					
					if(Transformation.GetData().GetCurrentLog().size()>3)
					{
					ParametersPanel.mainClusterParameters.remove(LogWarning);
					clusterSwingWorker.ShowAdviceFrame("Cluster calculation","This plugin is recalculating the clusters");
					clusterSwingWorker.LoadClusterSwingWorker();
					AddClusterCheckEvents();
					AddSubClusterEvents();
					AddLabelsEvent();
					}
					else
					{
						ParametersPanel.mainClusterParameters.add(LogWarning);
					}
					UpdateSNPanel();
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
		if(Arrays.asList(this.getComponents()).contains(HeuristicComparatorVersion))
		{
		this.remove(HeuristicComparatorVersion);	
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
					
					SocialPanelData(currentSocialAnalysist);
					CreateSocialWorkingView();
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
				UpdateSNPanel();
			}});

		return detailButton;
		
	}
	
	public void AddHeuristic()
	{
		this.Transformation.GenerateHeuristicModel();
		if(this.HeuristicComparatorVersion!=null)
		this.add(HeuristicComparatorVersion);

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
	  
	  if(this.HeuristicComparatorVersion!=null)
	  this.remove(this.HeuristicComparatorVersion);
	  
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
		System.out.print("\n Label:"+ label.getName());

		label.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0){
			// Inicializar los checkbox de cases
				
				int value= -1;
				String key="";
				JLabel label= (JLabel) arg0.getComponent();
				
				value= Integer.parseInt(label.getName());

				if(label.getText().equals("-") && !ParametersPanel.mainClusterParameters.eventClusterCaseAssign.get(""+value))
				{
						if(Transformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(value)<15)
							
						{
							
						   Map<String,JCheckBox> MapCase=ParametersPanel.mainClusterParameters.ClustersCasesCheckBoxes;

						   for(int c=0;c<Transformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(value);c++)
						   {        

							   key=value+"-"+c;
							   System.out.print("\n evento for: "+value+"-"+c);
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
						
						ParametersPanel.mainClusterParameters.eventClusterCaseAssign.remove(""+value);
				 		ParametersPanel.mainClusterParameters.eventClusterCaseAssign.put(""+value,true);
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
			    value=Integer.parseInt(label.getName());

				if(label.getText().equals("-") && !ParametersPanel.mainClusterParameters.eventClusterCaseAssign.get(""+value))
				{
									
						   Map<String,JCheckBox> MapCase=ParametersPanel.mainClusterParameters.ClustersCasesCheckBoxes;
						   for(int c=0;c<Transformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(value);c++)
						   {        
							   key=value+"-"+c;
							   System.out.print("\n evento for: "+value+"-"+c);

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
							ParametersPanel.mainClusterParameters.eventClusterCaseAssign.remove(""+value);
					 		ParametersPanel.mainClusterParameters.eventClusterCaseAssign.put(""+value,true);

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
	 		LabelMouseListenerSubCluster(label);
	 
	 	}
		
		/*
		for(int c=0;c<ParametersPanel.mainClusterParameters.JLabelSubClusterArray.size();c++)
		{

	 		label=ParametersPanel.mainClusterParameters.JLabelSubClusterArray.get(c);
	 		LabelMouseListenerSubCluster(label);
		}
		*/
		
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

	}
	

	public void basicSocialEvents()
	{
		ParametersPanel.mainSocialParameters.Options.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				JComboBox combo= ParametersPanel.mainSocialParameters.Options;
				Transformation.ResetResourceFilter();

				Transformation.SetSocialTransformation( new OLAPSocialTransformation(Transformation.GetData()));
				currentSocialAnalysist=combo.getSelectedItem().toString();
				ResetAuxiliarSocialNet();
				ParametersPanel.mainSocialParameters.ResetPanel();
				ParametersPanel.mainSocialParameters.repaint();
		
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
    		clusterSwingWorker.RemoveSubClusterFilteringSwingWorker(cluster,trace_ini,trace_last);   	    	
    	}
    	else
    	{
    		clusterSwingWorker.AddSubClusterFilteringSwingWorker(cluster,trace_ini,trace_last);
    	}

    }
   
    public void redrawGraphWithTraces(int cluster,int trace,boolean remove)
    {

		if(remove)
		{
			clusterSwingWorker.RemoveCaseFilteringSwingWorker(trace,cluster);
		
		}
		else
		{
			clusterSwingWorker.RestoreCaseFilteringSwingWorker(cluster,trace);
		}

    }
	
    public void redrawGraphWithSocialGroupSelection(boolean remove,String numberGroup)
    {
		if(remove)
		{
             Transformation.GroupFilter(numberGroup,currentSocialAnalysist);
            
		}
		else
		{
		    Transformation.RestoreGroup(numberGroup,currentSocialAnalysist);	
		}
		BuiltGraph();
    }
    
    public void redrawGraphWithClusters(String number,boolean remove)
	{
		
		if(remove)
		{
			
			clusterSwingWorker.RemoveClusterFilteringSwingWorker(number);
		}
		else
		{
			clusterSwingWorker.RestoreClusterFilteringSwingWorker(number);
		    //Transformation.RestoreCluster(number);	
		}
		//BuiltGraph();
	}
	
	public void BuiltGraph()
	{
		System.out.print("\n built graph");
		if(IsHeuristicAnalysis)
		{
						if (HeuristicComparatorVersion!= null) {
							this.remove(HeuristicComparatorVersion);
							HeuristicComparatorVersion = null;
							this.revalidate();
				
						} 
						if (HeuristicComparatorVersion == null) {
								
						    if(Transformation.GetFixCase())
						    {
								Iterator<Activity> iterador= this.DiscoveryData.getHeuristicsNetGraph().getActivities().iterator();
								
								Activity ac= iterador.next();
								this.DiscoveryData.getHeuristicsNetGraph().removeActivity(ac);  
						
							}
							    HeuristicComparatorVersion=ProMJGraphVisualizer.instance().visualizeGraphWithoutRememberingLayout(DiscoveryData.getHeuristicsNetGraph());
							    
							    HeuristicComparatorVersion.remove(HeuristicComparatorVersion.getComponent(1));
							    HeuristicComparatorVersion.remove(HeuristicComparatorVersion.getComponent(1));
							    HeuristicComparatorVersion.getComponent(0).repaint();
							    HeuristicComparatorVersion.repaint();
							    HeuristicComparatorVersion.setAutoscrolls(false);
								HeuristicComparatorVersion.setBounds(0, this.HeuristicViewDistance,this.HeuristicViewWidth ,this.HeuristicViewHeigth);
								HeuristicComparatorVersion.setSize(new Dimension(this.HeuristicViewWidth ,this.HeuristicViewHeigth));
								HeuristicComparatorVersion.setPreferredSize(new Dimension(this.HeuristicViewWidth ,this.HeuristicViewHeigth));
							    
								this.add(HeuristicComparatorVersion);
								this.revalidate();	
						        
								
						}
						
		}
		//Social view
		else
		{
			SocialPanelData(this.currentSocialAnalysist);

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
             Transformation.SocialFilter(id,currentSocialAnalysist);
             
 	    }
		else
		{
			Transformation.AddSocialResource(id,currentSocialAnalysist);
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
		else
		{

			DiscoveryData.SetSocialNetwork(UtilOperation.generateSN(
					Transformation.GetSocialTransformation().GetMatrix2DToShow("HW"), 
					Transformation.GetSocialTransformation().GetHandoverWTaskDataToShow().getOriginatorList()));
			
		}
		UpdateSNPanel();
	}

	public void UpdateSNPanel()
	{
		if(!showAlwaysOriginalProcess)
		{
    	if(!IsHeuristicAnalysis && !LargeView)
		{	
			if(WorkingSNPanel!=null)
    		remove(WorkingSNPanel);
			
    		remove(SNPanel);
		    CreateBaseSocialPanel(Transformation.GetSocialTransformation().GenerateBaseSocialNetwork(this.currentSocialAnalysist, Transformation.GetData().GetBaseLog()));
    		add(SNPanel);
		}
		else if(!LargeView)
		{
			if(HeuristicComparatorVersion!=null)
			remove(HeuristicComparatorVersion);
			
			remove(ModelPanel);
			CreateHeuristicModel(DiscoveryData.getHeuristicsNetGraph());	    
			add(ModelPanel);

		}
		repaint();		
		}
	}
	
	public void GetOriginalViews()
	{
    	if(!IsHeuristicAnalysis)
		{	
    		remove(SNPanel);
		    CreateBaseSocialPanel(Transformation.GetSocialTransformation().GenerateBaseSocialNetwork(currentSocialAnalysist,this.Transformation.GetData().GetOriginalLog()));
	    	if(!LargeView)
	    	{
	    		add(SNPanel);
	    	}
		}
		else
		{
			  remove(ModelPanel);
			  XLogInfo Info=XLogInfoFactory.createLogInfo(Transformation.GetData().GetOriginalLog());

			  HeuristicsMiner fhm = new HeuristicsMiner(context,Transformation.GetData().GetOriginalLog(),Info ); 
			  HeuristicsNet net=fhm.mine(); 
			  AnnotatedVisualizationSettings AVS= new AnnotatedVisualizationSettings();
			  AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
			  HeuristicsNetGraph HM= generator.generate(net,AVS);
		
			CreateHeuristicModel(HM);	    
			
	    	if(!LargeView)
	    	{
	    		add(ModelPanel);
	    	}

		}

    	revalidate();
		repaint();				
	}

}
