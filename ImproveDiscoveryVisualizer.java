package PromMasterPlugin;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.gui.HeuristicsNetVisualization;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.gui.HeuristicsNetVisualizer;

import com.fluxicon.slickerbox.components.InspectorButton;
import com.fluxicon.slickerbox.components.StackedCardsTabbedPane;
import com.fluxicon.slickerbox.ui.SlickerCheckBoxUI;
import com.fluxicon.slickerbox.ui.SlickerRadioButtonUI;
import com.fluxicon.slickerbox.ui.SlickerSliderUI;
/*heuristic reference*/


@Plugin(name = "Show ImproveDiscover",
        parameterLabels = { "Person" },
        returnLabels = { "Person Viewer" },
        returnTypes = { JComponent.class },
        userAccessible = false)
@Visualizer
public class ImproveDiscoveryVisualizer 
{
	
	//Fuzzy parameters
	
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

	protected XLog log = null;
	protected ProMJGraph jgraph;
	protected ProMJGraph clusterGraph;
	protected ProMJGraph molecularGraph;
	protected PluginContext context;
   

	//	protected SidePanel sidePanel;
	//JF add for show the exported Fuzzy Graph object only
	protected JPanel rightPanel;
	protected boolean enableRedraw;

	protected JPanel rootPanel;
	protected ProMJGraphPanel graphPanel;
	protected ProMJGraphPanel clustGraphPanel;
	protected ProMJGraphPanel patternGraphPanel;
	//	protected ProMJGraphPanel molecularGraphPanel;
	//	protected ViewSpecificAttributeMap viewSpecificMap;
	protected JRadioButton edgesBestRadioButton;
	protected JRadioButton edgesFuzzyRadioButton;
	protected JSlider nodeSignificanceSlider;
	protected JSlider edgesFuzzyRatioSlider;
	protected JSlider edgesFuzzyPercentageSlider;
	protected JSlider edgesConcurrencyThresholdSlider;
	protected JSlider edgesConcurrencyRatioSlider;
	protected JLabel nodeSignificanceLabel;
	protected JLabel edgesFuzzyRatioLabel;
	protected JLabel edgesFuzzyPercentageLabel;
	protected JLabel edgesConcurrencyThresholdLabel;
	protected JLabel edgesConcurrencyRatioLabel;
	protected JCheckBox edgesFuzzyIgnoreLoopBox;
	protected JCheckBox edgesFuzzyInterpretAbsoluteBox;
	protected JCheckBox edgesConcurrencyActiveBox;
	
	protected Color COLOR_BG = new Color(60, 60, 60);
	protected Color COLOR_BG2 = new Color(120, 120, 120);
	protected Color COLOR_FG = new Color(30, 30, 30);
	protected Font smallFont;
	protected Integer timeToFormTranformerPanel = 0;
	protected HashSet<File> patternLogDirectorySet = new HashSet<File>();
	//	protected File patternLogDirectory;
	//to distinguish whether the log is a transformed log or not

	protected boolean isPatternBasedTransformedLog = false;

	
    //Main parameters
	public static JPanel final_view= new JPanel();
	private ImproveDiscoveryData DiscoveryData;

	
  @PluginVariant(requiredParameterLabels = { 0 })
  public  JComponent visualize(final PluginContext context,
                                     final ImproveDiscoveryData DiscoveryData) {
	  
	  this.DiscoveryData= DiscoveryData;
		
   // TitledBorder son remarcadores
	/*TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE),
			"Titulo Plugin");
	title.setTitleColor(Color.WHITE);
	HPanel.setBorder(title);
	HPanel.setBackground(new Color(30, 30, 30));
	HPanel.setSize(new Dimension(HPanel.getSize().width, 400));
	HPanel.setPreferredSize(new Dimension(HPanel.getSize().width, 30));
	HPanel.setMinimumSize(new Dimension(final_view.getWidth(), 30));
   */
	    HeuristicsNetVisualization LPanel=HeuristicsNetVisualizer.getVisualizationPanel(
			  DiscoveryData.getHeuristicsNetGraph(), DiscoveryData.getHeuristicNet(),
			  DiscoveryData.getHMinerAVSettings(),context.getProgress());
	  

	    LPanel.remove(0);
	    LPanel.remove(0);
	    LPanel.remove(0);
	    LPanel.remove(0);
	    LPanel.remove(0);
	    LPanel.remove(0);
	    LPanel.remove(0);
	    LPanel.remove(0);
	    LPanel.remove(0);
	    LPanel.remove(0);
	    
		//Cambiar tamaño a diagrama
	    LPanel.getComponent(1).setSize(new Dimension(1000,300));
		//LPanel.repaint();	
		LPanel.setAutoscrolls(false);
		LPanel.setBounds(0, 0,1000 ,300);
		LPanel.setPreferredSize(new Dimension(1000,320));


	final_view.add(LPanel);
	//final_view.add(BuiltheuristicPanel(1));
	
	
	 fuzzyview();
    return final_view;
  }
  
  

  public HeuristicsNetVisualization BuiltheuristicPanel(int number)
  {
	  HeuristicsNetVisualization HPanel=HeuristicsNetVisualizer.getVisualizationPanel(
			  DiscoveryData.getHeuristicsNetGraph(), DiscoveryData.getHeuristicNet(),
			  DiscoveryData.getHMinerAVSettings(),context.getProgress());
	  
	    HPanel.remove(0);
	    HPanel.remove(0);
	    HPanel.remove(0);
	    HPanel.remove(0);
	    HPanel.remove(0);
	    HPanel.remove(0);
	    HPanel.remove(0);
	    HPanel.remove(0);
	    HPanel.remove(0);
	    HPanel.remove(0);
	    
		//Cambiar tamaño a diagrama
	    HPanel.getComponent(1).setSize(new Dimension(950,300));
		HPanel.repaint();	
		HPanel.setAutoscrolls(false);
		HPanel.setBounds(0, 305*number, 1000, 310);

		HPanel.setPreferredSize(new Dimension(950,320));

		
	    HPanel.setScale(0.75);
	    
	    return HPanel;  
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
  
  public JPanel organizationView()
  {
	// concurrency edge transformer slider panel
			JPanel concurrencySliderPanel = new JPanel();
			concurrencySliderPanel.setOpaque(false);
			concurrencySliderPanel.setLayout(new BoxLayout(concurrencySliderPanel, BoxLayout.X_AXIS));
			
			// concurrency edge preserve threshold slider panel
			JPanel concurrencyPreservePanel = new JPanel();
			concurrencyPreservePanel.setOpaque(false);
			concurrencyPreservePanel.setLayout(new BorderLayout());
			
			edgesConcurrencyThresholdLabel = new JLabel("0.000");
			edgesConcurrencyThresholdLabel.setSize(new Dimension(100, 25));
			edgesConcurrencyThresholdLabel.setForeground(COLOR_FG);
			edgesConcurrencyThresholdLabel.setFont(this.smallFont);
			centerHorizontally(edgesConcurrencyThresholdLabel);
					
			
	// concurrency edge ratio threshold slider panel
			JPanel concurrencyRatioPanel = new JPanel();
			concurrencyRatioPanel.setOpaque(false);
			concurrencyRatioPanel.setLayout(new BorderLayout());
			JLabel concurrencyRatioHeader = new JLabel("Ratio of the group definition");
			concurrencyRatioHeader.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			concurrencyRatioHeader.setOpaque(false);
			concurrencyRatioHeader.setForeground(COLOR_FG);
			concurrencyRatioHeader.setFont(this.smallFont);
			centerHorizontally(concurrencyRatioHeader);
			edgesConcurrencyRatioSlider = new JSlider(JSlider.VERTICAL, 0, 1000, 0);
			edgesConcurrencyRatioSlider.setUI(new SlickerSliderUI(edgesConcurrencyRatioSlider));
			edgesConcurrencyRatioSlider.setOpaque(false);
			//edgesConcurrencyRatioSlider.addChangeListener(this);
			edgesConcurrencyRatioSlider.setToolTipText("<html>For conflicting relations which have fallen<br>"
					+ "victim to simplification, this determines ratio<br>"
					+ "threshold. A lower value prefers sequentialization of<br>"
					+ "conflicting relations, a higher value tends to<br>"
					+ "interpret them as being scheduled concurrently.</html>");
			edgesConcurrencyRatioLabel = new JLabel("0.000");
			edgesConcurrencyRatioLabel.setSize(new Dimension(100, 25));
			edgesConcurrencyRatioLabel.setForeground(COLOR_FG);
			edgesConcurrencyRatioLabel.setFont(this.smallFont);
			
			centerHorizontally(edgesConcurrencyRatioLabel);
			
			concurrencyRatioPanel.add(packVerticallyCentered(concurrencyRatioHeader, 140, 20), BorderLayout.NORTH);
			concurrencyRatioPanel.add(edgesConcurrencyRatioSlider, BorderLayout.CENTER);
			concurrencyRatioPanel.add(packVerticallyCentered(edgesConcurrencyRatioLabel, 40, 20), BorderLayout.SOUTH);
			
			
			// assemble concurrency slider panel
			concurrencySliderPanel.add(concurrencyPreservePanel);
			concurrencySliderPanel.add(concurrencyRatioPanel);
			// setup concurrency parent panel
			JPanel concurrencyParentPanel = new JPanel();
			concurrencyParentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			concurrencyParentPanel.setBackground(COLOR_BG2);
			concurrencyParentPanel.setOpaque(true);
			concurrencyParentPanel.setLayout(new BorderLayout());
			
			
		    //Checkbox de grupos humanos
			JPanel edgesConcurrencyHeaderPanel = new JPanel();
			

			edgesConcurrencyActiveBox = new JCheckBox("Group ");
			edgesConcurrencyActiveBox.setUI(new SlickerCheckBoxUI());
			edgesConcurrencyActiveBox.setOpaque(false);
			edgesConcurrencyActiveBox.setForeground(COLOR_FG);
			edgesConcurrencyActiveBox.setFont(this.smallFont);
			//edgesConcurrencyActiveBox.addItemListener(this);
			edgesConcurrencyActiveBox.setSelected(false);
			edgesConcurrencyActiveBox.setToolTipText("<html>This control can be used to switch off<br>"
					+ "concurrency filtering in the model.</html>");
			edgesConcurrencyHeaderPanel.add(edgesConcurrencyActiveBox);
			this.edgesConcurrencyActiveBox.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent e) {
					int s=0;
				}

				public void mouseEntered(MouseEvent e) {
					int s=0;}

				public void mouseExited(MouseEvent e) {
					int s=0;}

				public void mousePressed(MouseEvent e) {
					if(!edgesConcurrencyActiveBox.isSelected())
					final_view.removeAll();
				}

				public void mouseReleased(MouseEvent e) {
					int s=0;}
			});
			
			
			
			edgesConcurrencyHeaderPanel.setLayout(new BoxLayout(edgesConcurrencyHeaderPanel, BoxLayout.Y_AXIS));
			edgesConcurrencyHeaderPanel.setOpaque(false);
			edgesConcurrencyHeaderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 10));
			edgesConcurrencyHeaderPanel.add(Box.createVerticalGlue());
			concurrencyParentPanel.add(edgesConcurrencyHeaderPanel, BorderLayout.NORTH);
			concurrencyParentPanel.add(concurrencySliderPanel, BorderLayout.CENTER);
			
			return concurrencyParentPanel;
  }
  
  public void fuzzyview()
  {
		// derive standard control element font
		this.smallFont = new Font("11f", 12, 10);
		// root panel
		JPanel rootPanel = new JPanel();
		rootPanel.setBorder(BorderFactory.createEmptyBorder());
		rootPanel.setBackground(new Color(100, 100, 100));
		rootPanel.setLayout(new BorderLayout());
		
		// upper node filter panel
		JPanel upperControlPanel = new JPanel();
		upperControlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		upperControlPanel.setBackground(COLOR_BG2);
		upperControlPanel.setOpaque(true);
		upperControlPanel.setLayout(new BorderLayout());
		JLabel nodeSigSliderLabel = new JLabel("Faster");
		nodeSigSliderLabel.setFont(this.smallFont);
		nodeSigSliderLabel.setOpaque(false);
		nodeSigSliderLabel.setForeground(COLOR_FG);
		
		centerHorizontally(nodeSigSliderLabel);
		
		upperControlPanel.add(nodeSigSliderLabel, BorderLayout.NORTH);
		nodeSignificanceLabel = new JLabel("Slowest");
		nodeSignificanceLabel.setOpaque(false);
		nodeSignificanceLabel.setForeground(COLOR_FG);
		nodeSignificanceLabel.setFont(this.smallFont);
		centerHorizontally(nodeSignificanceLabel);
		upperControlPanel.add(packVerticallyCentered(nodeSignificanceLabel, 50, 20), BorderLayout.SOUTH);
		nodeSignificanceSlider = new JSlider(JSlider.VERTICAL, 0, 1000, 0);
		nodeSignificanceSlider.setUI(new SlickerSliderUI(nodeSignificanceSlider));
		//nodeSignificanceSlider.addChangeListener(this);
		nodeSignificanceSlider.setOpaque(false);
		nodeSignificanceSlider.setToolTipText("<html>The lower this value, the more<br>"
				+ "events are shown as single activities,<br>" + "increasing the detail and complexity<br>"
				+ "of the model.</html>");
		upperControlPanel.add(nodeSignificanceSlider, BorderLayout.CENTER);
		//start  of the "Edge filter" panel
		// lower edge transformer panel
		JPanel lowerControlPanel = new JPanel(); // lowerControlPanel is the Edge filter panel
		lowerControlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		lowerControlPanel.setBackground(COLOR_BG2);
		lowerControlPanel.setOpaque(true);
		lowerControlPanel.setLayout(new BorderLayout());
		// lower header panel (radio buttons etc.)
		JPanel lowerHeaderPanel = new JPanel();
		lowerHeaderPanel.setOpaque(false);
		lowerHeaderPanel.setLayout(new BoxLayout(lowerHeaderPanel, BoxLayout.Y_AXIS));
		JLabel lowerHeaderLabel = new JLabel("Edge transformer");
		lowerHeaderLabel.setOpaque(false);
		lowerHeaderLabel.setForeground(COLOR_FG);
		lowerHeaderLabel.setFont(this.smallFont);
		//centerHorizontally(lowerHeaderLabel);
		edgesBestRadioButton = new JRadioButton("Best edges");
		edgesBestRadioButton.setUI(new SlickerRadioButtonUI());
		edgesBestRadioButton.setFont(this.smallFont);
		edgesBestRadioButton.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 2));
		edgesBestRadioButton.setOpaque(false);
		edgesBestRadioButton.setForeground(COLOR_FG);
		edgesBestRadioButton.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
		edgesBestRadioButton.setHorizontalAlignment(JRadioButton.LEFT);
		//edgesBestRadioButton.addItemListener(this);
		edgesBestRadioButton.setToolTipText("<html>Activates the 'Best edges'<br>"
				+ "edge filtering strategy, which<br>" + "preserves for each node the two most<br>"
				+ "significant connections.</html>");
		edgesFuzzyRadioButton = new JRadioButton("Fuzzy edges");
		edgesFuzzyRadioButton.setUI(new SlickerRadioButtonUI());
		edgesFuzzyRadioButton.setFont(this.smallFont);
		edgesFuzzyRadioButton.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 2));
		edgesFuzzyRadioButton.setOpaque(false);
		edgesFuzzyRadioButton.setForeground(COLOR_FG);
		edgesFuzzyRadioButton.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
		edgesFuzzyRadioButton.setHorizontalAlignment(JRadioButton.LEFT);
		//edgesFuzzyRadioButton.addItemListener(this);
		edgesFuzzyRadioButton.setToolTipText("<html>Activates the 'Fuzzy edges'<br>"
				+ "edge filtering strategy, which is<br>" + "based on the utility value of each<br>"
				+ "edge for any node.</html>");
		ButtonGroup radioEdgesGroup = new ButtonGroup();
		radioEdgesGroup.add(edgesBestRadioButton);
		radioEdgesGroup.add(edgesFuzzyRadioButton);
		lowerHeaderPanel.add(lowerHeaderLabel);
		lowerHeaderPanel.add(Box.createVerticalStrut(2));
		lowerHeaderPanel.add(edgesBestRadioButton);
		lowerHeaderPanel.add(edgesFuzzyRadioButton);
		lowerHeaderPanel.add(Box.createVerticalStrut(5));
		// lower slider parent panel
		JPanel lowerSliderPanel = new JPanel();
		lowerSliderPanel.setOpaque(false);
		lowerSliderPanel.setLayout(new BoxLayout(lowerSliderPanel, BoxLayout.X_AXIS));
		// lower ratio slider panel
		JPanel fuzzyRatioPanel = new JPanel();
		fuzzyRatioPanel.setOpaque(false);
		fuzzyRatioPanel.setLayout(new BorderLayout());
		JLabel fuzzyRatioHeader = new JLabel("Utility rt.");
		fuzzyRatioHeader.setFont(this.smallFont);
		fuzzyRatioHeader.setOpaque(false);
		fuzzyRatioHeader.setForeground(COLOR_FG);
		centerHorizontally(fuzzyRatioHeader);
		edgesFuzzyRatioSlider = new JSlider(JSlider.VERTICAL, 0, 1000, 0);
		edgesFuzzyRatioSlider.setUI(new SlickerSliderUI(edgesFuzzyRatioSlider));
		edgesFuzzyRatioSlider.setOpaque(false);
		//edgesFuzzyRatioSlider.addChangeListener(this);
		edgesFuzzyRatioSlider.setToolTipText("<html>Controls the utility ratio used<br>"
				+ "for edge filtering. A higher value will<br>" + "give more preference to edges' significance,<br>"
				+ "lower value prefers correlation.</html>");
		edgesFuzzyRatioLabel = new JLabel("0.000");
		centerHorizontally(edgesFuzzyRatioLabel);
		edgesFuzzyRatioLabel.setSize(new Dimension(100, 25));
		edgesFuzzyRatioLabel.setForeground(COLOR_FG);
		edgesFuzzyRatioLabel.setFont(this.smallFont);
		fuzzyRatioPanel.add(packVerticallyCentered(fuzzyRatioHeader, 60, 20), BorderLayout.NORTH);
		fuzzyRatioPanel.add(edgesFuzzyRatioSlider, BorderLayout.CENTER);
		fuzzyRatioPanel.add(packVerticallyCentered(edgesFuzzyRatioLabel, 40, 20), BorderLayout.SOUTH);
		// lower percentage slider panel
		JPanel fuzzyPercentagePanel = new JPanel();
		fuzzyPercentagePanel.setOpaque(false);
		fuzzyPercentagePanel.setLayout(new BorderLayout());
		JLabel fuzzyPercentageHeader = new JLabel("Cutoff");
		fuzzyPercentageHeader.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		fuzzyPercentageHeader.setOpaque(false);
		fuzzyPercentageHeader.setForeground(COLOR_FG);
		fuzzyPercentageHeader.setFont(this.smallFont);
		centerHorizontally(fuzzyPercentageHeader);
		edgesFuzzyPercentageSlider = new JSlider(JSlider.VERTICAL, 0, 1000, 0);
		edgesFuzzyPercentageSlider.setUI(new SlickerSliderUI(edgesFuzzyPercentageSlider));
		edgesFuzzyPercentageSlider.setOpaque(false);
		//edgesFuzzyPercentageSlider.addChangeListener(this);
		edgesFuzzyPercentageSlider.setToolTipText("<html>Determines the minimal utility for<br>"
				+ "an edge to be included, with a larger value<br>" + "allowing more edges to be displayed, thus<br>"
				+ "increasing the detail of the model.</html>");
		edgesFuzzyPercentageLabel = new JLabel("0.000");
		edgesFuzzyPercentageLabel.setForeground(COLOR_FG);
		edgesFuzzyPercentageLabel.setSize(new Dimension(100, 25));
		edgesFuzzyPercentageLabel.setFont(this.smallFont);
		centerHorizontally(edgesFuzzyPercentageLabel);
		fuzzyPercentagePanel.add(packVerticallyCentered(fuzzyPercentageHeader, 40, 20), BorderLayout.NORTH);
		fuzzyPercentagePanel.add(edgesFuzzyPercentageSlider, BorderLayout.CENTER);
		fuzzyPercentagePanel.add(packVerticallyCentered(edgesFuzzyPercentageLabel, 40, 20), BorderLayout.SOUTH);
		// assemble lower slider panel
		lowerSliderPanel.add(fuzzyPercentagePanel);
		lowerSliderPanel.add(fuzzyRatioPanel);
		// assemble check box panel
		JPanel lowerSettingsPanel = new JPanel();
		lowerSettingsPanel.setOpaque(false);
		lowerSettingsPanel.setLayout(new BoxLayout(lowerSettingsPanel, BoxLayout.Y_AXIS));
		edgesFuzzyIgnoreLoopBox = new JCheckBox("ignore self-loops");
		edgesFuzzyIgnoreLoopBox.setUI(new SlickerCheckBoxUI());
		edgesFuzzyIgnoreLoopBox.setOpaque(false);
		edgesFuzzyIgnoreLoopBox.setForeground(COLOR_FG);
		edgesFuzzyIgnoreLoopBox.setFont(this.smallFont);
		//edgesFuzzyIgnoreLoopBox.addItemListener(this);
		edgesFuzzyIgnoreLoopBox.setToolTipText("<html>If active, length-1-loops (i.e.,<br>"
				+ "repeptitions of one event) will not be,<br>" + "taken into account when filtering edges.</html>");
		edgesFuzzyInterpretAbsoluteBox = new JCheckBox("interpret absolute");
		edgesFuzzyInterpretAbsoluteBox.setUI(new SlickerCheckBoxUI());
		edgesFuzzyInterpretAbsoluteBox.setOpaque(false);
		edgesFuzzyInterpretAbsoluteBox.setForeground(COLOR_FG);
		edgesFuzzyInterpretAbsoluteBox.setFont(this.smallFont);
		//edgesFuzzyInterpretAbsoluteBox.addItemListener(this);
		edgesFuzzyInterpretAbsoluteBox.setToolTipText("<html>If active, all edges' utility value<br>"
				+ "must exceed the cutoff globally, i.e. in an<br>"
				+ "absolute way, rather than locally, i.e. in a<br>" + "relative way.</html>");
		lowerSettingsPanel.add(edgesFuzzyIgnoreLoopBox);
		lowerSettingsPanel.add(edgesFuzzyInterpretAbsoluteBox);
		// assemble lower control panel
		lowerControlPanel.add(lowerHeaderPanel, BorderLayout.NORTH);
		lowerControlPanel.add(lowerSliderPanel, BorderLayout.CENTER);
		lowerControlPanel.add(lowerSettingsPanel, BorderLayout.SOUTH);
		//end of the "Edge filter" panel

		
		// Make the organizational tab
		JPanel concurrencyParentPanel=organizationView();
		
		// assemble slick tab pane
		StackedCardsTabbedPane tabPane = new StackedCardsTabbedPane();
		tabPane.addTab("Organizational perspective", concurrencyParentPanel);
		tabPane.addTab("Flow perspective", lowerControlPanel);
		tabPane.addTab("Performance perspective", upperControlPanel);
		tabPane.setActive(2);
		tabPane.setMinimumSize(new Dimension(190, 220));
		tabPane.setMaximumSize(new Dimension(190, 10000));
		tabPane.setPreferredSize(new Dimension(190, 10000));
		tabPane.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		

	
		InspectorButton detailButton = new InspectorButton();
		detailButton.setToolTipText("click to show model detail inspector");
		detailButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		detailButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		detailButton.setMinimumSize(new Dimension(20, 20));
		
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
		replayPanel.add(detailButton);
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
		final_view.setBorder(BorderFactory.createEmptyBorder());
		final_view.setLayout(new BorderLayout());
		final_view.add(rootPanel, BorderLayout.CENTER);
  }
}