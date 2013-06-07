package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization;

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
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.HeuristicsMiner;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationGenerator;
import org.processmining.plugins.heuristicsnet.visualizer.annotatedvisualization.AnnotatedVisualizationSettings;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data.OLAPData;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPSocialTransformation;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTransformation;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.pivot.CustomSocialNetworkAnalysisUI;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.swingworkers.ClusterSwingWorker;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.swingworkers.SocialParametersSwingWorker;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.swingworkers.SocialSwingWorker;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.swingworkers.TimeSwingWorker;
import org.processmining.plugins.socialnetwork.miner.miningoperation.UtilOperation;

import com.fluxicon.slickerbox.components.SlickerButton;
import com.fluxicon.slickerbox.ui.SlickerCheckBoxUI;

/*
 *  The most important class
 *  Methods for events view update
*/
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
	/*private JLabel porcentaje;
	private ProgressMonitor progressMonitor;
	private boolean finishClusterUpdate=false;
	private  JPanel ButtonPanel;*/
	private  JPanel ButtonPanel;

	private ClusterSwingWorker clusterSwingWorker;
	private SocialSwingWorker socialSwingWorker;
	private  TimeSwingWorker timeWorker;
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
         ParametersPanel.mainSocialParameters.CreateResourceCheckBoxes();

	     AddSocialCheckEvents();
         basicSocialEvents();
	     this.add(ModelPanel);
         this.add(ParametersPanel);
         
         //Button Panel
         CreateButtonPanel();
         this.add(ButtonPanel);
         Transformation.SetXLogBackUpUnit();
         clusterSwingWorker= new ClusterSwingWorker();
         socialSwingWorker= new SocialSwingWorker();
         timeWorker= new TimeSwingWorker();
         LogWarning=new JLabel("<html>There are not enough traces or maybe the plugin have an error,<br> press Reset</html>");
         
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
        
        JCheckBox j= new JCheckBox("Show always the original model");
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
				   Transformation.GetTimeTransformation().InicializatePerformanceData(Transformation.GetData().GetOriginalLog());
				   Transformation.SetSocialTransformation(new OLAPSocialTransformation(Transformation.GetData()));
				   
				   ParametersPanel.mainPerformanceContainer.ResetPanel();
				   ParametersPanel.mainPerformanceContainer.repaint();
				   ParametersPanel.repaint();
				
				   ParametersPanel.tabPane.selectTab("Performance");
				   
                   AddPerformanceClickAction();
                 //Remove comparative view
                   RemoveComparativeView();
       			   currentSocialAnalysist="Working Together";
                   ResetAuxiliarSocialNet();
                   UpdateSNPanel();
                  // BuiltGraph();
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
	
	
    public void UpdateBaseLog()
    {
   
    	this.Transformation.GetData().SetTransformLogFromWorkingLog();
    }
	
    public ActionListener AddSocialTabEvents()
	{
        		ActionListener Action= new     		ActionListener(){

    			public void actionPerformed(ActionEvent arg0) {		
    				
    				   UpdateBaseLog();			
    				   socialSwingWorker.LoadTab(GetMainPanel(),Transformation);
    				   }
        		};
        		
    			return Action;
    		
		
	}
	
	public ActionListener AddPerformanceTabEvent()
	{
		final OLAPDiscoveryPanel panel=this;
    		ActionListener Action= new     		ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				   UpdateBaseLog();
				   
		   
		    timeWorker.UpdateTab(panel,Transformation);
                 
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
				    ParametersPanel.mainClusterParameters.add(LogWarning);
					
					if(Transformation.GetData().GetCurrentLog().size()>3)
					{
					ParametersPanel.mainClusterParameters.remove(LogWarning);
					clusterSwingWorker.LoadClusterSwingWorker(3,GetMainPanel(), Transformation);

					//AddClusterCheckEvents();

					}
					else
					{
						ParametersPanel.mainClusterParameters.add(LogWarning);
					}
					UpdateSNPanel();
					
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
		 ParametersPanel.mainPerformanceContainer.GetMaxSlider().addMouseListener(new MouseListener(){
				public void mouseClicked(MouseEvent arg0) {		
					Transformation.UpdateGraphWithPerformanceChanges();
					BuiltGraph();}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseReleased(MouseEvent arg0) {}
	         });
	               
		 ParametersPanel.mainPerformanceContainer.GetMinSlider().addMouseListener(new MouseListener(){

	 			public void mouseClicked(MouseEvent arg0) {
					Transformation.UpdateGraphWithPerformanceChanges();
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
	
	public void AddDetaiClusterLabelEvent(JLabel label)
	{
		label.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				JLabel label= (JLabel) arg0.getComponent();
				if(label.getText().equals("+"))
				{
					

					ParametersPanel.GetClustersParametersPanel().CloseDetailsView();

					String variant= label.getName().substring(label.getName().indexOf("-")+1);
					String cluster= label.getName().substring(0,label.getName().indexOf("-"));		
					label.setText("-");
					BuiltCaseGraph(cluster,variant);
				}
				else
				{
				
					label.setText("+");
					Transformation.BuiltHeuristic();
					BuiltGraph();
				}
			}

			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
	}
	public void LabelMouseListenerAction(JLabel label)
	{

		label.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0){
			// Inicializar los checkbox de cases
				if(ParametersPanel.mainClusterParameters.IsOpenDetailsView())
				{
					ParametersPanel.mainClusterParameters.CloseDetailsView();
					BuiltGraph();
				}
			}

			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			
 		});
	
	}
	
	public MouseListener CaseMouseListener()
	{
		return new MouseListener(){
			
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
	
		   };
	}


	public void ClusterGroupLabelEvent(JLabel label)
	{
		label.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				JLabel label= (JLabel) e.getComponent();	
				if(label.getText().equals("-"))
				{
					if(ParametersPanel.GetClustersParametersPanel().IsOpenDetailsView())
					{
						ParametersPanel.GetClustersParametersPanel().CloseDetailsView();
						BuiltGraph();
					}
				}
				else
				{
					if(ParametersPanel.GetClustersParametersPanel().IsOpenDetailsView())
					{
						ParametersPanel.GetClustersParametersPanel().CloseDetailsView();
						BuiltGraph();
					}
				}
			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}});
	}
	public void SocialGroupLabelEvents()
	{
		//Slider event
		if(ParametersPanel.mainSocialParameters.GetResourceModelEvents().get("Slider")==null)
		{
	    MouseListener Ml= SocialSliderEvent();
		ParametersPanel.mainSocialParameters.GetFilterSlider().addMouseListener(Ml);
		ParametersPanel.mainSocialParameters.GetResourceModelEvents().put("Slider", Ml);
		}
		// JCheckBoxGroups Events
		for(int j=0; j<ParametersPanel.mainSocialParameters.GetJCheckBoxGroups().size();j++)
		{
			ParametersPanel.mainSocialParameters.GetJCheckBoxGroups().get(j).addMouseListener(new MouseListener(){

				public void mouseClicked(MouseEvent arg0) {}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {
					
					JCheckBox check=(JCheckBox) arg0.getComponent();
					if(check.isSelected() && ParametersPanel.mainSocialParameters.GetRelationsPanel().getComponentCount()==0)
					{
				
							redrawGraphWithSocialGroupSelection(true,check.getName());
							
						
					}
					else if(check.isSelected() && ParametersPanel.mainSocialParameters.GetRelationsPanel().getComponentCount()>0)
					{
						check.setSelected(false);
					}
					else
					{	
					
						redrawGraphWithSocialGroupSelection(false,check.getName());
						
					}
				}

				public void mouseReleased(MouseEvent arg0) {}});
		}		
	
		for(int k=0;k<ParametersPanel.mainSocialParameters.GetJLabelGroups().size();k++)
		{

			ParametersPanel.mainSocialParameters.GetJLabelGroups().get(k).addMouseListener(new MouseListener(){

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
					JLabel label=(JLabel) arg0.getComponent();
					AddResourcesEvent(label.getName());
					
						for(int j=0;j<ParametersPanel.mainSocialParameters.GetJLabelResources().size();j++)
						{
							ParametersPanel.mainSocialParameters.GetJLabelResources().get(j).addMouseListener(new MouseListener(){

								public void mouseClicked(MouseEvent e) {}
								public void mouseEntered(MouseEvent e) {}
								public void mouseExited(MouseEvent e) {}
								public void mousePressed(MouseEvent e) {
									
									JLabel label=(JLabel) e.getComponent();
									String key= label.getName().substring(0,label.getName().indexOf("-"));
									AddResourcesEvent(key);
								}
								public void mouseReleased(MouseEvent e) {	
								}});
						}
				}

				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}});
		}
		
	}
	
	
	public void AddResourcesEvent(String group)
	{
	
		for(int j=0; j<ParametersPanel.mainSocialParameters.GetMapJCheckBoxResources().get(group).size();j++)
		{
		

			if(ParametersPanel.mainSocialParameters.GetResourceModelEvents().get(group+"-"+j)==null)
			{
			
			MouseListener Ml= CreateResourceModelEvent();
			ParametersPanel.mainSocialParameters.GetMapJCheckBoxResources().get(group).get(j).addMouseListener(Ml);
			ParametersPanel.mainSocialParameters.GetResourceModelEvents().put(group+"-"+j, Ml);
			}
		}		
	}
	public void AddSocialCheckEvents()
	{

		SocialGroupLabelEvents();
		if(ParametersPanel.mainSocialParameters.GetResourceModelEvents().get("Slider")==null)
		{
	    MouseListener Ml= SocialSliderEvent();
		ParametersPanel.mainSocialParameters.GetFilterSlider().addMouseListener(Ml);
		ParametersPanel.mainSocialParameters.GetResourceModelEvents().put("Slider", Ml);
		}
	}
	
	public  MouseListener SocialSliderEvent()
	{
		return new MouseListener(){

			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
			
			ParametersPanel.mainSocialParameters.GetMapJCheckBoxResources().clear();
			SocialParametersSwingWorker swing= new SocialParametersSwingWorker();
			
			swing.LoadResources(GetMainPanel(),Transformation, ParametersPanel.mainSocialParameters.GetMapJCheckBoxResources());
			SocialGroupLabelEvents();
	
				

			
			}
			public void mouseReleased(MouseEvent e) {}
			
		};
	}
    
	
	public MouseListener CreateResourceModelEvent()
	{
		return new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				JCheckBox check=(JCheckBox) arg0.getComponent();
				String resource= Transformation.GetSocialTransformation().TranslateNode(Integer.parseInt(check.getName()));

				if(check.isSelected())
				{// se selecciono para borrarlo
					redrawGraphSocialResource(resource,true);
				}
				else
				{	
					redrawGraphSocialResource(resource,false);
				}
			}

			public void mouseReleased(MouseEvent arg0) {}
			};
	
		
	}


	public void AddClusterCheckEvents()
	{
		for(int u=0;u<ParametersPanel.mainClusterParameters.GetJCheckClusterList().size();u++)
    	{
    	
    		this.ParametersPanel.mainClusterParameters.GetJCheckClusterList().get(u).addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {}

			public void mousePressed(MouseEvent e) {
				
				JCheckBox check=(JCheckBox) e.getComponent();
				if(check.isSelected() && ParametersPanel.mainClusterParameters.GetOptionPanel().getComponentCount()==0)
				{// se selecciono para borrarlo
				redrawGraphWithClusters(check.getName(),true);
				}
				else if(ParametersPanel.mainClusterParameters.GetOptionPanel().getComponentCount()==0)
				{	
				redrawGraphWithClusters(check.getName(),false);
				}
				else if(check.isSelected())
				{
					check.setSelected(false);
				}
			}
			public void mouseReleased(MouseEvent e) {
				}
		});
    	}
	}
	
	public void AddEventsAfterChangeNumberOfClusters()
	{
		
	 	ParametersPanel.mainClusterParameters.GetNumberOfClusters().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int clusters=Integer.parseInt(ParametersPanel.mainClusterParameters.GetNumberOfClusters().getSelectedItem().toString());

				Transformation.GetData().ReturnToBaseLog();
				Transformation.GetClusterFilter().ClearRemovedClusters();
				clusterSwingWorker.LoadClusterSwingWorker(clusters,GetMainPanel(), Transformation);


				//AddClusterCheckEvents();
		
			}
    	});

	}
	
	public void SetCurrentSocialAnalysis(String current)
	{
	this.currentSocialAnalysist=current;	
		
	}
	
	public void basicSocialEvents()
	{
		ParametersPanel.mainSocialParameters.GetOptions().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				socialSwingWorker.LoadOption(GetMainPanel(), Transformation);
			}});
	}
	
	public OLAPDiscoveryPanel GetMainPanel()
	{
		return this;
	}
	
	
    public void redrawGraphWithSubCluster(int cluster, int trace_ini,int trace_last,boolean remove)
    {
    	
    	if(remove) 
		{
    		clusterSwingWorker.RemoveSubClusterFilteringSwingWorker(cluster,trace_ini,trace_last, Transformation, GetMainPanel());   	    	
    	}
    	else
    	{
    		clusterSwingWorker.AddSubClusterFilteringSwingWorker(cluster,trace_ini,trace_last, Transformation, GetMainPanel());
    	}

    }
   
    public void redrawGraphWithTraces(int cluster,int trace,boolean remove)
    {

		if(remove)
		{
			clusterSwingWorker.RemoveVariantFilteringSwingWorker(trace,cluster, Transformation, GetMainPanel());
		
		}
		else
		{
			clusterSwingWorker.RestoreVariantFilteringSwingWorker(cluster,trace, Transformation, GetMainPanel());
		}

    }
	
    public void redrawGraphWithSocialGroupSelection(boolean remove,String numberGroup)
    {
		if(remove)
		{
			socialSwingWorker.RemoveGroupsSwingWorker(this, numberGroup,currentSocialAnalysist, Transformation);

		}
		else
		{
			socialSwingWorker.AddGroupSwingWorker(this, numberGroup,currentSocialAnalysist, Transformation);	
  


		}
       

    }
    
    public void redrawGraphWithClusters(String number,boolean remove)
	{
		
		if(remove)
		{
			
			clusterSwingWorker.RemoveClusterFilteringSwingWorker(number, Transformation, GetMainPanel());
		}
		else
		{
			clusterSwingWorker.RestoreClusterFilteringSwingWorker(number, Transformation, GetMainPanel());
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

	public void BuiltCaseGraph(String cluster,String variant)
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
							    HeuristicComparatorVersion=ProMJGraphVisualizer.instance().visualizeGraphWithoutRememberingLayout(Transformation.FindVariantHeuristicModel(cluster,variant));
							    
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
	
	public void redrawGraphSocialResource(String id,boolean remove) {

	
		if(remove)
		{
			
			socialSwingWorker.RemoveResource(this, id,currentSocialAnalysist, Transformation);
             
 	    }
		else
		{
			socialSwingWorker.RemoveResource(this, id,currentSocialAnalysist, Transformation);
		}
	
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
	
	public OLAPDiscoveryParametersPanel GetParametersPanel()
	{
		return ParametersPanel;
	}
	
	public void  VariantsAndClusterEvents(int numberOfCluster)
	{
		JLabel label;
 		
		for(int j=0;j<ParametersPanel.mainClusterParameters.GetJLabelClusterArray().size();j++)
	 	{
	 		label=ParametersPanel.mainClusterParameters.GetJLabelClusterArray().get(j);
	 	    LabelMouseListenerAction(label);
	 	}
	 		
		for(int j=0;j<ParametersPanel.mainClusterParameters. JLabelGroupsOfCluster().size();j++)
		{
			if(ParametersPanel.mainClusterParameters. JLabelGroupsOfCluster().get(""+j)!=null)
			for(int c=0;c<ParametersPanel.mainClusterParameters. JLabelGroupsOfCluster().get(""+j).size();c++)
			{
				ClusterGroupLabelEvent(ParametersPanel.mainClusterParameters. JLabelGroupsOfCluster().get(""+j).get(c));
			}
		}
	
		
		String key;
		   for(int j=0;j< numberOfCluster;j++)
		   {
				   Map<String,JCheckBox> MapCase= this.GetParametersPanel().GetClustersParametersPanel().GetClustersCasesCheckBoxes();

				   for(int c=0;c<Transformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(j);c++)
				   {        

					   key=j+"-"+c;
					   if(this.GetParametersPanel().GetClustersParametersPanel().GetCaseEvents().get(key)==null)
					   {
					   MouseListener Ml= this.CaseMouseListener();
					   MapCase.get(key).addMouseListener(Ml);
					   this.GetParametersPanel().GetClustersParametersPanel().GetCaseEvents().put(key,Ml);
					   JLabel detailLabel=this.GetParametersPanel().GetClustersParametersPanel().GetParticularCaseDetailView(key);
					   if(detailLabel.getMouseListeners().length==0)
					   this.AddDetaiClusterLabelEvent(detailLabel);
					   }
				   }
				

		   }
	}
	

}
