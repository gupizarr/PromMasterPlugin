package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data.OLAPData;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTransformation;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.parameters.ClusterParameters;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.parameters.SocialParametersPanel;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.parameters.TimeParameters;

import com.fluxicon.slickerbox.components.SlickerTabbedPane;



/*
 *  Parameters panel, tab container.
 *  @author Gustavo Pizarro
*/

public class OLAPDiscoveryParametersPanel extends JPanel {



	protected XLog log = null;

	protected PluginContext context;
	protected SlickerTabbedPane tabPane;
	//	protected SidePanel sidePanel;
	//JF add for show the exported Fuzzy Graph object only
	protected JPanel rightPanel;
	protected boolean enableRedraw;
    protected ClusterParameters mainClusterParameters;
    protected SocialParametersPanel mainSocialParameters;
    protected TimeParameters mainPerformanceContainer;
	protected JPanel tabContainerPanel;
	protected ProMJGraphPanel graphPanel;
	protected ProMJGraphPanel clustGraphPanel;
	protected ProMJGraphPanel patternGraphPanel;
	//	protected ProMJGraphPanel molecularGraphPanel;
	//	protected ViewSpecificAttributeMap viewSpecificMap;
	protected JRadioButton edgesBestRadioButton;
	protected JRadioButton edgesFuzzyRadioButton;

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
    protected OLAPData DataDiscovery;
	private OLAPTransformation DataTransformation;
	protected 	JPanel edgesConcurrencyHeaderPanel;
    protected ActionListener SocialLogAction;
    protected ActionListener ClusterLogAction;
    protected ActionListener PerformanceLogAction;

    protected int tabPanelWidth;
	
	public OLAPDiscoveryParametersPanel(OLAPTransformation Transformation,ActionListener SocialAction, ActionListener ClusterAction, ActionListener PerformanceAction) {
		// TODO Auto-generated constructor stub
		 this.SocialLogAction=SocialAction;
		 this.PerformanceLogAction=PerformanceAction;
		 this.ClusterLogAction=ClusterAction;
		 
		 DataTransformation= Transformation;
		 DataDiscovery=Transformation.GetData();
		 mainClusterParameters=	new ClusterParameters(this.DataTransformation);
		 mainSocialParameters= new SocialParametersPanel(this.DataTransformation);

		    tabContainerPanel = new JPanel();
			tabContainerPanel.setBorder(BorderFactory.createEmptyBorder());
			tabContainerPanel.setBackground(new Color(100, 100, 100));
			tabContainerPanel.setLayout(new BorderLayout());

		 
			 this.setBounds(1020, 55, 350, 600);
			 this.setSize(new Dimension(350,600));
			 tabPanelWidth=365;
		 
		 this.fuzzyview(); 
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
	
	
	

	public void CreateTabs()
	{
		tabContainerPanel.removeAll();
		// Make the organizational tab
		//PerformanceParameters
		mainPerformanceContainer=new TimeParameters(DataTransformation);	
		// assemble slick tab pane
		tabPane = new SlickerTabbedPane("", COLOR_BG2, Color.white,COLOR_BG2);
		tabPane.addTab("Organizational", mainSocialParameters, this.SocialLogAction);
		tabPane.addTab("Variants",mainClusterParameters,this.ClusterLogAction);
		tabPane.addTab("Performance", mainPerformanceContainer,this.PerformanceLogAction);
		

		tabPane.setMinimumSize(new Dimension(tabPanelWidth, 220));
		tabPane.setMaximumSize(new Dimension(tabPanelWidth, 560));
		tabPane.setPreferredSize(new Dimension(tabPanelWidth, 560));
		tabPane.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
	}

	public void fuzzyview()
	{
			CreateTabs();
	        rightPanel = new JPanel();
			rightPanel.setBorder(BorderFactory.createEmptyBorder());
			rightPanel.setLayout(new BorderLayout());
			rightPanel.setOpaque(false);
			rightPanel.add(tabPane, BorderLayout.CENTER);
			rightPanel.repaint();
			tabContainerPanel.add(rightPanel, BorderLayout.EAST);
			tabContainerPanel.repaint();
			this.add(tabContainerPanel);
			this.repaint();
	}
	
	public void ResetPerformance()
    {		 
		mainClusterParameters=	new ClusterParameters(this.DataTransformation);
	    mainSocialParameters= new SocialParametersPanel(this.DataTransformation);
	    fuzzyview();
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
	

	
	
	public void redrawGraph(String Dimension) {
	

	}
	
	public TimeParameters GetPerformanceParametersPanel()
	{
		return this.mainPerformanceContainer;
	}
	
	public SocialParametersPanel GetSocialParametersPanel()
	{
		return this.mainSocialParameters;
	}
	
	public ClusterParameters GetClustersParametersPanel()
	{
		return this.mainClusterParameters;
	}
	
	public SlickerTabbedPane GetTabPane()
	{
		return tabPane;
	}

}