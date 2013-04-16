package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
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

import com.fluxicon.slickerbox.components.InspectorButton;
import com.fluxicon.slickerbox.components.StackedCardsTabbedPane;
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
	protected JCheckBox[] edgesConcurrencyActiveBox;
	protected JCheckBox[] ClusteredgesConcurrencyActiveBox;
	
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

	
	public ImproveDiscoveryParametersPanel(ImproveDiscoveryData DataDiscovery) {
		// TODO Auto-generated constructor stub
		
		 this.DataDiscovery=DataDiscovery;
		 ClusteredgesConcurrencyActiveBox= new JCheckBox[DataDiscovery.getTraceAlignTransformation().GetClusters().size()];
		 this.fuzzyview(true);
		 this.setBounds(1160, 0, 190, 610);
		 this.setSize(new Dimension(190,610));
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
	
	public JPanel ClustersParameters(boolean clusterCheck)
	{
		// concurrency edge transformer slider panel
		JPanel concurrencySliderPanel = new JPanel();
		concurrencySliderPanel.setOpaque(false);
		concurrencySliderPanel.setLayout(new BoxLayout(concurrencySliderPanel, BoxLayout.X_AXIS));
		
		// concurrency edge preserve threshold slider panel
		JPanel concurrencyPreservePanel = new JPanel();
		concurrencyPreservePanel.setOpaque(false);
		concurrencyPreservePanel.setLayout(new BorderLayout());
		

		// setup concurrency parent panel
		JPanel concurrencyParentPanel = new JPanel();
		concurrencyParentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		concurrencyParentPanel.setBackground(COLOR_BG2);
		concurrencyParentPanel.setOpaque(true);
		concurrencyParentPanel.setLayout(new BorderLayout());
		
	    //Checkbox de grupos humanos
		JPanel edgesConcurrencyHeaderPanel = new JPanel();
	    int num=1;
		for(int count=0; count<DataDiscovery.getTraceAlignTransformation().GetClusters().size();count++) {
	
			//adding the activities
		num+=1;
		ClusteredgesConcurrencyActiveBox[count] = new JCheckBox("Cluster "+num);
		ClusteredgesConcurrencyActiveBox[count].setUI(new SlickerCheckBoxUI());
		ClusteredgesConcurrencyActiveBox[count].setOpaque(false);
		ClusteredgesConcurrencyActiveBox[count].setForeground(COLOR_FG);
		ClusteredgesConcurrencyActiveBox[count].setFont(this.smallFont);
		//edgesConcurrencyActiveBox.addItemListener(this);
		ClusteredgesConcurrencyActiveBox[count].setSelected(clusterCheck);
		ClusteredgesConcurrencyActiveBox[count].setToolTipText("<html>This control select the clusters of the model" +
				"visualization</html>");
		
		//agrego Id para identificarlo
		
		ClusteredgesConcurrencyActiveBox[count].setName(""+DataDiscovery.getTraceAlignTransformation().GetClusters().get(count));
		
		edgesConcurrencyHeaderPanel.add(ClusteredgesConcurrencyActiveBox[count]);	
		
		}
		
		edgesConcurrencyHeaderPanel.setLayout(new BoxLayout(edgesConcurrencyHeaderPanel, BoxLayout.Y_AXIS));
		edgesConcurrencyHeaderPanel.setOpaque(false);
		edgesConcurrencyHeaderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 10));
		edgesConcurrencyHeaderPanel.add(Box.createVerticalGlue());
		concurrencyParentPanel.add(edgesConcurrencyHeaderPanel, BorderLayout.NORTH);
		concurrencyParentPanel.add(concurrencySliderPanel, BorderLayout.CENTER);
		
		return concurrencyParentPanel;
	}
	
    public JPanel organizationView(boolean group_check)
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
				edgesConcurrencyActiveBox= new JCheckBox[DataDiscovery.Resources.length];
				for(int count=0; count<DataDiscovery.Resources.length;count++) {
			
					//adding the activities
					
				edgesConcurrencyActiveBox[count] = new JCheckBox(DataDiscovery.Resources[count]);
				edgesConcurrencyActiveBox[count].setUI(new SlickerCheckBoxUI());
				edgesConcurrencyActiveBox[count].setOpaque(false);
				edgesConcurrencyActiveBox[count].setForeground(COLOR_FG);
				edgesConcurrencyActiveBox[count].setFont(this.smallFont);
				//edgesConcurrencyActiveBox.addItemListener(this);
				edgesConcurrencyActiveBox[count].setSelected(group_check);
				edgesConcurrencyActiveBox[count].setToolTipText("<html>This control can be used to switch off<br>"
						+ "concurrency filtering in the model.</html>");
				
				//agrego Id para identificarlo
				
				edgesConcurrencyActiveBox[count].setName(DataDiscovery.Resources[count]);
				
				edgesConcurrencyHeaderPanel.add(edgesConcurrencyActiveBox[count]);	
				
				}
				
				edgesConcurrencyHeaderPanel.setLayout(new BoxLayout(edgesConcurrencyHeaderPanel, BoxLayout.Y_AXIS));
				edgesConcurrencyHeaderPanel.setOpaque(false);
				edgesConcurrencyHeaderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 10));
				edgesConcurrencyHeaderPanel.add(Box.createVerticalGlue());
				concurrencyParentPanel.add(edgesConcurrencyHeaderPanel, BorderLayout.NORTH);
				concurrencyParentPanel.add(concurrencySliderPanel, BorderLayout.CENTER);
				
				return concurrencyParentPanel;
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
            
			//ClusgerParameters
            JPanel lowerControlPanel=ClustersParameters(true);
			
			// Make the organizational tab
			JPanel concurrencyParentPanel=this.organizationView(group_check);
			
			// assemble slick tab pane
			StackedCardsTabbedPane tabPane = new StackedCardsTabbedPane();
			tabPane.addTab("Organizational perspective", concurrencyParentPanel);
			tabPane.addTab("Flow perspective", lowerControlPanel);
			tabPane.addTab("Performance perspective", upperControlPanel);
			tabPane.setActive(2);
			tabPane.setMinimumSize(new Dimension(190, 220));
			tabPane.setMaximumSize(new Dimension(190, 620));
			tabPane.setPreferredSize(new Dimension(190, 620));
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
			
			this.add(rootPanel);

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

}