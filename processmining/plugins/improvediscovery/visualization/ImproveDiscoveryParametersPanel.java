package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryTransformation;

import com.fluxicon.slickerbox.components.InspectorButton;
import com.fluxicon.slickerbox.components.SlickerTabbedPane;
import com.fluxicon.slickerbox.ui.SlickerCheckBoxUI;
import com.fluxicon.slickerbox.ui.SlickerSliderUI;




public class ImproveDiscoveryParametersPanel extends JPanel {

	private static final long serialVersionUID = 4221149364708440299L;
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String EDGE_TRANSFORMER_SELECTION = "EdgeTransformerSelection";
	public static final String EDGE_TRANSFORMER_SELECTION_BEST_EDGES = "EdgeTransformerSelectionBestEdges";
	public static final String EDGE_TRANSFORMER_SELECTION_FUZZY_EDGES = "EdgeTransformerSelectionFuzzyEdges";
	public static final String CONCURRENCY_EDGE_TRANSFORMER_ACTIVE = "ConcurrencyEdgeTransformerActive";
	public static final String NODE_CUTOFF = "NodeCutoff";
	public static final String FUZZY_EDGE_RATIO = "FuzzyEdgeRatio";
	public static final String FUZZY_EDGE_CUTOFF = "FuzzyEdgeCutoff";
	public static final String CONCURRENCY_THRESHOLD = "ConcurrencyThreshold";
	public static final String CONCURRENCY_RATIO = "ConcurrencyRatio";
	protected static final String EDGES_FUZZY_IGNORE_LOOPS = "EdgesFuzzyIgnoreLoops";
	protected static final String EDGES_FUZZY_INTERPRET_ABSOLUTE = "EdgesFuzzyInterpretAbsolute";
	protected static final String FILE_SEPERATOR = File.separator;
	protected static final String FILE_LOCATION_LOG_ATT_KEY = "fileLocation";
	protected static final String PARENT_FILE_NAME_LOG_ATT_KEY = "parentFileName";
    protected JLabel minSliderHeader; 
	protected XLog log = null;
	protected ProMJGraph jgraph;
	protected ProMJGraph clusterGraph;
	protected ProMJGraph molecularGraph;
	protected PluginContext context;

	//	protected SidePanel sidePanel;
	//JF add for show the exported Fuzzy Graph object only
	protected JPanel rightPanel;
	protected boolean enableRedraw;
    protected ClusterParameters mainClusterParameters;
    protected SocialParametersPanel mainSocialParameters;
    
	protected JPanel rootPanel;
	protected ProMJGraphPanel graphPanel;
	protected ProMJGraphPanel clustGraphPanel;
	protected ProMJGraphPanel patternGraphPanel;
	//	protected ProMJGraphPanel molecularGraphPanel;
	//	protected ViewSpecificAttributeMap viewSpecificMap;
	protected JRadioButton edgesBestRadioButton;
	protected JRadioButton edgesFuzzyRadioButton;
	protected JSlider performanceSlider;
	protected JLabel maxMinTimeLabel;
	protected JLabel minMaxTimeLabel;
	protected JSlider minTimeSlider;
	protected JSlider maxTimeSlider;
	protected JSlider edgesConcurrencyThresholdSlider;
	protected JSlider edgesConcurrencyRatioSlider;
	protected JLabel nodeSignificanceLabel;
	protected JLabel minTimeLabel;
	protected JLabel maxTimeLabel;
	protected JLabel edgesConcurrencyThresholdLabel;
	protected JLabel edgesConcurrencyRatioLabel;
	protected JCheckBox meanPerformanceCheckBox;
	protected JCheckBox edgesFuzzyInterpretAbsoluteBox;
	
	 
	protected JComboBox numberOfClusters;
	
	protected Color COLOR_BG = new Color(60, 60, 60);
	protected Color COLOR_BG2 = new Color(120, 120, 120);
	protected Color COLOR_FG = new Color(30, 30, 30);
	protected Font smallFont;
	protected Integer timeToFormTranformerPanel = 0;
	protected HashSet<File> patternLogDirectorySet = new HashSet<File>();
	//	protected File patternLogDirectory;
	//to distinguish whether the log is a transformed log or not

	protected boolean isPatternBasedTransformedLog = false;
    protected ImproveDiscoveryData DataDiscovery;
	private ImproveDiscoveryTransformation DataTransformation;
	protected 	JPanel edgesConcurrencyHeaderPanel;
    protected ActionListener MainLogAction;
    
	protected int tabPanelWidth;
	
	public ImproveDiscoveryParametersPanel(ImproveDiscoveryTransformation Transformation,ActionListener action) {
		// TODO Auto-generated constructor stub
		 this.MainLogAction=action;
		 DataTransformation= Transformation;
		 DataDiscovery=Transformation.GetData();
		 mainClusterParameters=	new ClusterParameters(this.DataTransformation);
		 mainSocialParameters= new SocialParametersPanel(this.DataTransformation);
		 
		 
			 this.setBounds(990, 0, 350, 640);
			 this.setSize(new Dimension(350,640));
			 tabPanelWidth=365;
		 
		 this.fuzzyview(true); 
		 this.setBackground(new Color(100,100,100));
		 this.repaint();
   


	}
	public String TransformLabel(String label)
	{
		int ini=label.indexOf("<b>")+3;
		int fin=label.indexOf("</b>");
		label=label.substring(ini,fin);
		return label;
	}
	
	
	

	

	public void fuzzyview(boolean group_check)
	{
			// derive standard control element font

			this.smallFont = new Font("11f", 12, 10);
			// root panel
			JPanel rootPanel = new JPanel();
			rootPanel.setBorder(BorderFactory.createEmptyBorder());
			rootPanel.setBackground(new Color(100, 100, 100));
			rootPanel.setLayout(new BorderLayout());

        
			// Make the organizational tab
			SocialParametersPanel concurrencyParentPanel=mainSocialParameters;
		
			//PerformanceParameters
			JPanel upperControlPanel=PerformanceParameters();
			
			// assemble slick tab pane
			SlickerTabbedPane tabPane = new SlickerTabbedPane("", COLOR_BG2, Color.white,COLOR_BG2);
			tabPane.addTab("Organizational", concurrencyParentPanel, MainLogAction);
			tabPane.addTab("Flow",mainClusterParameters,MainLogAction);
			tabPane.addTab("Performance", upperControlPanel,MainLogAction);
			
		
			tabPane.setMinimumSize(new Dimension(tabPanelWidth, 220));
			tabPane.setMaximumSize(new Dimension(tabPanelWidth, 600));
			tabPane.setPreferredSize(new Dimension(tabPanelWidth, 600));
			tabPane.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
			
			
			//log Conformance Replay button
			InspectorButton replayButton = new InspectorButton();
			replayButton.setToolTipText("click to show log replay inspector");
			replayButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			replayButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
			replayButton.setMinimumSize(new Dimension(20, 20));
			
			//Set up Replay Panel 
			JPanel replayPanel = new JPanel();
			replayPanel.setLayout(new BoxLayout(replayPanel, BoxLayout.Y_AXIS));
			replayPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 2));
			replayPanel.setOpaque(false);
		
			replayPanel.add(Box.createVerticalStrut(5));
			
			replayPanel.add(replayButton);
			//JF comment
			//	JPanel rightPanel = new JPanel();
			rightPanel = new JPanel();
			rightPanel.setBorder(BorderFactory.createEmptyBorder());
			rightPanel.setLayout(new BorderLayout());
			rightPanel.setOpaque(false);
			rightPanel.add(tabPane, BorderLayout.CENTER);
			if (log != null) {
				rightPanel.add(replayPanel, BorderLayout.WEST);
			}
			//		sidePanel = new SidePanel();
			//		rootPanel.add(sidePanel, BorderLayout.WEST);
			rootPanel.add(rightPanel, BorderLayout.EAST);
			//not put the graphPanel in the rootPanel yet
			
			this.add(rootPanel);

	}
	
	public void ResetPerformance()
    {
		
		System.out.print("\n Reset parameters");
		JPanel upperControlPanel=PerformanceParameters();
		upperControlPanel.repaint();
	}
	public JPanel PerformanceParameters()
	{
		DataDiscovery.InicializatePerformanceData();
		//start  of the "Edge filter" panel
		// lower edge transformer panel
		JPanel mainPerformanceContainer = new JPanel(); // mainPerformanceContainer is the Edge filter panel
		mainPerformanceContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPerformanceContainer.setBackground(COLOR_BG2);
		mainPerformanceContainer.setOpaque(true);
		mainPerformanceContainer.setLayout(new BorderLayout());
		
		// lower header panel (radio buttons etc.)
		JPanel performanceHeaderPanel = new JPanel();
		performanceHeaderPanel.setOpaque(false);
		performanceHeaderPanel.setLayout(new BoxLayout(performanceHeaderPanel, BoxLayout.Y_AXIS));
		
		//centerHorizontally(lowerHeaderLabel);
		JPanel performanceParametersContainer = new JPanel();
		performanceParametersContainer.setOpaque(false);
		performanceParametersContainer.setLayout(new BoxLayout(performanceParametersContainer, BoxLayout.X_AXIS));
		
		// lower ratio slider panel
		JPanel parametersPerformancePanelLabel = new JPanel();
		parametersPerformancePanelLabel.setOpaque(false);
		parametersPerformancePanelLabel.setLayout(new BorderLayout());		

		// lower percentage slider panel				
		minSliderHeader = new JLabel("<html>Min Time <br>"+""+ (int) DataDiscovery.GetPerformanceDiff()[DataDiscovery.GetPerformanceDiff().length-1]+" "+DataDiscovery.GetPerformanceData().TagTime());
		minSliderHeader.setFont(this.smallFont);
		minSliderHeader.setOpaque(false);
		minSliderHeader.setForeground(COLOR_FG);
		centerHorizontally(minSliderHeader);
		
		minTimeSlider = new JSlider(JSlider.VERTICAL,(int) DataDiscovery.GetPerformanceDiff()[0] ,  (int) DataDiscovery.GetPerformanceDiff()[DataDiscovery.GetPerformanceDiff().length-1], (int) DataDiscovery.GetPerformanceDiff()[0]);
		minTimeSlider.setUI(new SlickerSliderUI(minTimeSlider));
		minTimeSlider.setOpaque(false);
		minTimeSlider.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
			
				maxTimeSlider.setMinimum(minTimeSlider.getValue());
				maxTimeLabel.setText(""+minTimeSlider.getValue()+" "+DataDiscovery.GetPerformanceData().TagTime());
				DataTransformation.PerformanceFilter(minTimeSlider.getValue(), maxTimeSlider.getValue());
		
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
				
			}
         
         });
         
		minTimeSlider.setToolTipText("<html>Select the minimum time of the traces for filtering<br>"
				+ "lower value prefers correlation.</html>");

		
		minTimeLabel = new JLabel(""+  (int) DataDiscovery.GetPerformanceDiff()[0]+" "+DataDiscovery.GetPerformanceData().TagTime());
		centerHorizontally(minTimeLabel);
		minTimeLabel.setSize(new Dimension(100, 25));
		minTimeLabel.setForeground(COLOR_FG);
		minTimeLabel.setFont(this.smallFont);
		
		parametersPerformancePanelLabel.add(packVerticallyCentered(minSliderHeader, 50, 40), BorderLayout.NORTH);
		parametersPerformancePanelLabel.add(minTimeSlider, BorderLayout.CENTER);
		parametersPerformancePanelLabel.add(packVerticallyCentered(minTimeLabel, 40, 20), BorderLayout.SOUTH);
		
		// lower percentage slider panel
		JPanel parametersPerformancePanel = new JPanel();
		parametersPerformancePanel.setOpaque(false);
		parametersPerformancePanel.setLayout(new BorderLayout());
		
		
		JLabel maxSliderHeader = new JLabel("<html>Max Time <br>"+
		(int) DataDiscovery.GetPerformanceDiff()[DataDiscovery.GetPerformanceDiff().length-1]+" "+
		DataDiscovery.GetPerformanceData().TagTime());
		maxSliderHeader.setSize(new Dimension(150, 25));
		maxSliderHeader.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		maxSliderHeader.setOpaque(false);
		maxSliderHeader.setForeground(COLOR_FG);
		maxSliderHeader.setFont(this.smallFont);
		centerHorizontally(maxSliderHeader);
				
		maxTimeSlider = new JSlider(JSlider.VERTICAL, (int) DataDiscovery.GetPerformanceDiff()[0],  (int) DataDiscovery.GetPerformanceDiff()[DataDiscovery.GetPerformanceDiff().length-1], (int) DataDiscovery.GetPerformanceDiff()[DataDiscovery.GetPerformanceDiff().length-1]);
		maxTimeSlider.setUI(new SlickerSliderUI(maxTimeSlider));
		maxTimeSlider.setOpaque(false);
		maxTimeSlider.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				minTimeSlider.setMaximum(maxTimeSlider.getValue());
				DataTransformation.PerformanceFilter(minTimeSlider.getValue(), maxTimeSlider.getValue());
				minSliderHeader.setText("<html>Min Time <br>"+""+ (int) maxTimeSlider.getValue() +" "+DataDiscovery.GetPerformanceData().TagTime());
				
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
				
			}
         
         });
		maxTimeSlider.setSize(new Dimension(10,100));
		maxTimeSlider.setToolTipText("<html>" +
						"Select the minimum time of the traces for filtering.</html>");
		maxTimeLabel = new JLabel(""+(int) DataDiscovery.GetPerformanceDiff()[0]+" "+DataDiscovery.GetPerformanceData().TagTime());
		maxTimeLabel.setForeground(COLOR_FG);
		maxTimeLabel.setSize(new Dimension(100, 25));
		maxTimeLabel.setFont(this.smallFont);
				
		centerHorizontally(maxTimeLabel);
		parametersPerformancePanel.add(packVerticallyCentered(maxSliderHeader, 50, 40), BorderLayout.NORTH);
		parametersPerformancePanel.add(maxTimeSlider, BorderLayout.CENTER);
		parametersPerformancePanel.add(packVerticallyCentered(maxTimeLabel, 40, 20), BorderLayout.SOUTH);
				// assemble lower slider panel
		performanceParametersContainer.add(parametersPerformancePanel);
		performanceParametersContainer.add(parametersPerformancePanelLabel);
				// assemble check box panel
		JPanel downSettingsPanel = new JPanel();
		downSettingsPanel.setOpaque(false);
		downSettingsPanel.setLayout(new BoxLayout(downSettingsPanel, BoxLayout.Y_AXIS));
		
		meanPerformanceCheckBox = new JCheckBox("See the mean");
		meanPerformanceCheckBox.setUI(new SlickerCheckBoxUI());
		meanPerformanceCheckBox.setOpaque(false);
		meanPerformanceCheckBox.setForeground(COLOR_FG);
		meanPerformanceCheckBox.setFont(this.smallFont);
		//meanPerformanceCheckBox.addItemListener(this);
		meanPerformanceCheckBox.setToolTipText("<html>See the mean</html>");
	
	
				downSettingsPanel.add(meanPerformanceCheckBox);
	
	    
				// assemble lower control panel
				mainPerformanceContainer.add(performanceHeaderPanel, BorderLayout.NORTH);
				mainPerformanceContainer.add(performanceParametersContainer, BorderLayout.CENTER);
				mainPerformanceContainer.add(downSettingsPanel, BorderLayout.SOUTH);
				//end of the "Edge filter" panel

				return mainPerformanceContainer;
	}
	
	protected void centerHorizontally(JLabel label) {
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	}

	protected JPanel packVerticallyCentered(JComponent component, int width, int height) {
		JPanel boxed = new JPanel();
		boxed.setLayout(new BoxLayout(boxed, BoxLayout.X_AXIS));
		boxed.setBorder(BorderFactory.createEmptyBorder());
		boxed.setOpaque(false);
		Dimension dim = new Dimension(width, height);
		component.setMinimumSize(dim);
		component.setMaximumSize(dim);
		component.setPreferredSize(dim);
		component.setSize(dim);
		boxed.add(Box.createHorizontalGlue());
		boxed.add(component);
		boxed.add(Box.createHorizontalGlue());
		
		return boxed;
	}
	
	protected JPanel packClusterSelector(JComponent component, int width, int height) {
		
		
		JPanel boxed = new JPanel();
		boxed.setLayout(new BoxLayout(boxed, BoxLayout.X_AXIS));
		boxed.setBorder(BorderFactory.createEmptyBorder());
		boxed.setOpaque(false);
		
		Dimension dim = new Dimension(width, height);
		component.setMinimumSize(dim);
		component.setMaximumSize(dim);
		component.setPreferredSize(dim);
		component.setSize(dim);
		
		boxed.add(component,BorderLayout.LINE_START);
		
		
		return boxed;
	}
	
		
	protected void updatePerformanceSlider() {

	    redrawGraph("Performance");
		
	}
	
	protected double getNodeThresholdFromSlider() {
		double threshold = performanceSlider.getValue() / 1000.0;
		// normalize threshold to minimal node frequency
		//threshold = ((1.0 - graph.getMinimalNodeSignificance()) * threshold) + graph.getMinimalNodeSignificance();
		return threshold;
	}
	
	
	public void redrawGraph(String Dimension) {
	

	}

	public void itemStateChanged(ItemEvent evt) {
		if (evt.getSource() == meanPerformanceCheckBox) {
			System.out.print("Ver media");
		}
	}
}