package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.pivot;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.socialnetwork.SNEdge;
import org.processmining.models.graphbased.directed.socialnetwork.SNNode;
import org.processmining.models.graphbased.directed.socialnetwork.SocialNetwork;
import org.processmining.plugins.socialnetwork.analysis.SocialNetworkAnalysisUtil;
import org.processmining.plugins.socialnetwork.analysis.centrality.AbstractCentrality;
import org.processmining.plugins.socialnetwork.analysis.centrality.BaryRanker;
import org.processmining.plugins.socialnetwork.analysis.centrality.Betweenness;
import org.processmining.plugins.socialnetwork.analysis.centrality.Closeness;
import org.processmining.plugins.socialnetwork.analysis.centrality.DegreeCentrality;
import org.processmining.plugins.socialnetwork.analysis.centrality.InDegreeCentrality;
import org.processmining.plugins.socialnetwork.analysis.centrality.OutDegreeCentrality;
import org.processmining.plugins.socialnetwork.analysis.util.DirectionDisplayPredicate;
import org.processmining.plugins.socialnetwork.analysis.util.VertexShapeSizeAspect;
import org.processmining.plugins.socialnetwork.analysis.util.VertexStrokeHighlight;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.LensSupport;
import edu.uci.ics.jung.visualization.util.Animator;

/**
 * @author Minseok Song and modified by Gustavo Pizarro
 * @version 1.0
 */

public class CustomSocialNetworkAnalysisUI extends JPanel implements ActionListener {

	private static final long serialVersionUID = 5043450973943460401L;

	private JComboBox layoutBox = null;
	private JComboBox rankingBox = null;

	public final Color[] similarColors = { new Color(216, 134, 134), new Color(135, 137, 211),
			new Color(134, 206, 189), new Color(206, 176, 134), new Color(194, 204, 134), new Color(145, 214, 134),
			new Color(133, 178, 209), new Color(103, 148, 255), new Color(60, 220, 220), new Color(30, 250, 100) };

	//GUI property	
	// cluster
	public static final String ST_ClusterWeight = "Weight";
	public static final String ST_ClusterBetweenness = "Betweenness";

	// layout
	public static final String ST_KKLayout = "KKLayout";
	public static final String ST_CircleLayout = "CircleLayout";
	public static final String ST_FRLayout = "FRLayout";
	public static final String ST_SpringLayout = "SpringLayout";
	public static final String ST_ISOMLayout = "ISOMLayout";
	public static final String ST_RankingLayout = "Ranking View";

	// centrality
	public static final String ST_BETWEENNESS = "Betweenness";
	public static final String ST_DEGREE = "Degree";
	public static final String ST_CLOSENESS = "Closeness";
	public static final String ST_DISTANCE = "Distance";
	public static final String ST_BARYRANKER = "BaryCenter";
	public static final String ST_HITS = "HITS";
	public static final String ST_EIGEN = "EigenVector";

	// degree centrality
	public static final String INDEGREE = "In degree";
	public static final String OUTDEGREE = "Out degree";
	public static final String INDEGREEWEIGHT = "In degree (weight)";
	public static final String RATIO = "Ratio (otherwise Absolute value)";
	public static final String WEIGHT = "Consider weight";
	public static final String[] DEGREE_CENTRALITY_OPTIONS = { INDEGREE, OUTDEGREE, RATIO, WEIGHT };

	// node size property
	public static final String INTERNALFLOW = "Interner flow";
	public static final String FREQUENCY = "Frequency";

	// node property
	public static final Object ROLEKEY = "role";
	public static final Object ORGUNITKEY = "orgunit";
	public static final Object ORIKEY = "originator";
	public static final Object NODETYPE = "type";
	public static final Object SIZEPROPERTY = "sizeproperty";

	private SocialNetwork sn = null;
	private VisualizationViewer<SNNode, SNEdge> vv = null;
	private AggregateLayout<SNNode, SNEdge> layout = null;

	private Graph<SNNode, SNEdge> g = null;
	protected VisualizationServer.Paintable paintable = null;
	//    public static final LengthFunction<String> UNITLENGTHFUNCTION = new SpringLayout.UnitLengthFunction<String>(
	//            100);

	protected VertexShapeSizeAspect vssa;
	protected VertexStrokeHighlight vsh;
	protected DirectionDisplayPredicate show_edge;
	protected JCheckBox v_shape;
	protected JCheckBox v_size;
	protected JCheckBox v_aspect;
	protected JCheckBox v_labels;
	protected JCheckBox e_labels;
	protected JCheckBox e_show_d;
	protected JCheckBox v_stroke;
	protected LensSupport hyperbolicViewSupport;
	protected JPanel eastControls;
	protected JRadioButton groupVertices;
	//    protected JToggleButton groupVertices;
	protected JSlider edgeBetweennessSlider;
	protected JSlider edgeWeightSlider;
	protected JPanel weightControls;

	protected Transformer<SNNode, String> vs_none;
	protected Transformer<SNEdge, String> es_none;
	protected Transformer<SNNode, String> stringerVertex;
	protected Transformer<SNEdge, String> stringer;

	@SuppressWarnings("unchecked")
	private final Map<SNNode, Paint> vertexPaints = LazyMap.<SNNode, Paint>decorate(new HashMap<SNNode, Paint>(),
			new ConstantTransformer(Color.black));

	@SuppressWarnings("unchecked")
	private final Map<SNEdge, Paint> edgePaints = LazyMap.<SNEdge, Paint>decorate(new HashMap<SNEdge, Paint>(),
			new ConstantTransformer(Color.gray));

	public CustomSocialNetworkAnalysisUI(PluginContext context, SocialNetwork sn) {
		this.sn = sn;
		try {
			// Initialize Graph
			initGraph();
			initVertexShape();
			changeLayout();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void initGraph() {
		Graph<SNNode, SNEdge> ig = new DirectedSparseGraph<SNNode, SNEdge>();
		for (SNNode node : sn.getNodes()) {
			ig.addVertex(node);
		}
		for (SNEdge edge : sn.getEdges()) {
			ig.addEdge(edge, edge.getSource(), edge.getTarget());
		}

		ObservableGraph<SNNode, SNEdge> og = new ObservableGraph<SNNode, SNEdge>(ig);
		og.addGraphEventListener(new GraphEventListener<SNNode, SNEdge>() {
			public void handleGraphEvent(GraphEvent<SNNode, SNEdge> evt) {
			}
		});
		g = og;

		// showing
		FRLayout2<SNNode, SNEdge> fr = new FRLayout2<SNNode, SNEdge>(ig);
		layout = new AggregateLayout<SNNode, SNEdge>(fr);
		vv = new VisualizationViewer<SNNode, SNEdge>(layout, new Dimension(400, 350));
		vv.getModel().getRelaxer().setSleepTime(500);
		vv.setGraphMouse(new DefaultModalGraphMouse<Number, Number>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
		stringerVertex = new Transformer<SNNode, String>() {
			public String transform(SNNode v) {
				return v.getLabel();
			}
		};
		vs_none = new ConstantTransformer(null);
		vv.getRenderContext().setVertexLabelTransformer(stringerVertex);

	
		stringer = new Transformer<SNEdge, String>() {
			public String transform(SNEdge e) {
				String temp = e.getLabel();
				int index = temp.indexOf(":");
				if (temp.contains("E")) {
					temp = temp.substring(0, Math.min(index + 5, temp.indexOf("E")))
							+ temp.substring(temp.indexOf("E"), temp.length());
				} else {
					temp = temp.substring(0, Math.min(index + 8, temp.length()));
				}
				return temp;
			}
		};
		es_none = new ConstantTransformer(null);
		vv.getRenderContext().setEdgeLabelTransformer(es_none);
		vv.getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<SNEdge>(vv.getPickedEdgeState(), Color.gray, Color.cyan));
		vv.setForeground(Color.gray);

		setLayout(new BorderLayout());
		setBackground(java.awt.Color.lightGray);
		setFont(new Font("Serif", Font.PLAIN, 11));
		this.add(vv, BorderLayout.CENTER);
		initOptionPanel();
	}

	@SuppressWarnings("unchecked")
	private void initOptionPanel() {
		// layout
		
		String[] layoutStrings = { ST_KKLayout, ST_CircleLayout, ST_FRLayout, ST_SpringLayout, ST_ISOMLayout,
				ST_RankingLayout };
		layoutBox = new JComboBox(layoutStrings);
		layoutBox.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel layoutPanel = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 1095761087490016363L;

			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};
		layoutPanel.setBorder(BorderFactory.createTitledBorder("Layout"));
		layoutPanel.add(layoutBox);
		layoutBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout();
			}
		});
		
		String[] rankingStrings = { ST_DEGREE, INDEGREE, OUTDEGREE, ST_CLOSENESS, ST_BARYRANKER, ST_BETWEENNESS };//, ST_PAGERANKER}; //ST_BETWEENNESS
		rankingBox = new JComboBox(rankingStrings);
		rankingBox.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel rankingPanel = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = -7638705980657145260L;

			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};

		
		rankingPanel.setBorder(BorderFactory.createTitledBorder("Ranking"));
		rankingPanel.add(rankingBox);
		rankingBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeVertexRanking();
				changeLayout();
			}
		});

		// view options

		JPanel viewPanel = new JPanel() {
			private static final long serialVersionUID = -3662393248754522997L;

			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};
		
		/*
		viewPanel.setOpaque(true);
		viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
		viewPanel.setBorder(BorderFactory.createTitledBorder("View options"));
		v_shape = new JCheckBox("shape by degree");
		v_shape.addActionListener(this);
		v_size = new JCheckBox("size by ranking");
		v_size.addActionListener(this);
		v_aspect = new JCheckBox("stretch by degree ratio");
		v_aspect.addActionListener(this);
		v_labels = new JCheckBox("show vertex names");
		v_labels.setSelected(true);
		v_labels.addActionListener(this);
		e_labels = new JCheckBox("show edge weight values");
		e_labels.addActionListener(this);
		e_show_d = new JCheckBox("show edge");
		e_show_d.addActionListener(this);
		e_show_d.setSelected(true);
		v_stroke = new JCheckBox("stroke highlight on selection");
		v_stroke.addActionListener(this);
		viewPanel.add(v_shape);
		viewPanel.add(v_size);
		viewPanel.add(v_aspect);
		viewPanel.add(v_labels);
		viewPanel.add(e_labels);
		viewPanel.add(e_show_d);
		viewPanel.add(v_stroke);
*/
		// mouse mode box
		final DefaultModalGraphMouse<String, SNEdge> graphMouse = new DefaultModalGraphMouse<String, SNEdge>();
		vv.setGraphMouse(graphMouse);
		JComboBox modeBox = graphMouse.getModeComboBox();
		modeBox.addItemListener(((DefaultModalGraphMouse<String, SNEdge>) vv.getGraphMouse()).getModeListener());
		JPanel modePanel = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 562625726019814360L;

			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};
		modePanel.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
		modePanel.add(modeBox);

		JPanel scalePanel = new JPanel();
		scalePanel.setLayout(new BoxLayout(scalePanel, BoxLayout.LINE_AXIS));
		scalePanel.setBorder(BorderFactory.createTitledBorder("Scale"));


		initWeightClustering();

		// hyperbolic
	/*	hyperbolicViewSupport = new ViewLensSupport<SNNode, SNEdge>(vv, new HyperbolicShapeTransformer(vv, vv
				.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW)), new ModalLensGraphMouse());

		final JRadioButton hyperView = new JRadioButton("Hyperbolic View");
		hyperView.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				hyperbolicViewSupport.activate(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		graphMouse.addItemListener(hyperbolicViewSupport.getGraphMouse().getModeListener());
*/
		// make menu
		/*
		JPanel topControls = new JPanel();
		topControls.setLayout(new BoxLayout(topControls, BoxLayout.Y_AXIS));
		topControls.setAlignmentX(Component.LEFT_ALIGNMENT);
		topControls.add(layoutPanel);
		topControls.add(rankingPanel);
		topControls.add(modePanel);
		topControls.add(weightControls);
		topControls.add(viewPanel);
		topControls.add(groupVertices);
		topControls.add(hyperView);
		this.add(topControls, BorderLayout.WEST);*/
		JPanel topControls = new JPanel();
		topControls.add(modePanel);
		topControls.add(layoutPanel);

		this.add(topControls, BorderLayout.NORTH);
		
		//changeLayout();
	}

	private void initVertexShape() {
		vssa = new VertexShapeSizeAspect(g, null, 0.0, 0.0);
		changeVertexRanking();
		vv.getRenderContext().setVertexShapeTransformer(vssa);
		show_edge = new DirectionDisplayPredicate(true, true);
		vv.getRenderContext().setEdgeIncludePredicate(show_edge);
		vsh = new VertexStrokeHighlight(g, vv.getPickedVertexState());
		vv.getRenderContext().setVertexStrokeTransformer(vsh);
	}

	private void changeVertexRanking() {
		String ranking = (String) rankingBox.getSelectedItem();
		AbstractCentrality absCen = null;
		if (ranking.equals(CustomSocialNetworkAnalysisUI.ST_DEGREE)) {
			absCen = new DegreeCentrality();
		} else if (ranking.equals(CustomSocialNetworkAnalysisUI.INDEGREE)) {
			absCen = new InDegreeCentrality();
		} else if (ranking.equals(CustomSocialNetworkAnalysisUI.OUTDEGREE)) {
			absCen = new OutDegreeCentrality();
		} else if (ranking.equals(CustomSocialNetworkAnalysisUI.ST_BARYRANKER)) {
			absCen = new BaryRanker();
		} else if (ranking.equals(CustomSocialNetworkAnalysisUI.ST_CLOSENESS)) {
			absCen = new Closeness();
		} else if (ranking.equals(CustomSocialNetworkAnalysisUI.ST_BETWEENNESS)) {
			absCen = new Betweenness();
		}
		Map<SNNode, Double> rankings = absCen.getRankings(g);
		vssa.setRanking(rankings, absCen.getMin(), absCen.getMax());
	}

	private void initWeightClustering() {
		//Create slider to adjust the number of edges to remove when clustering
		edgeWeightSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 1001, 0);
		edgeWeightSlider.setBackground(Color.WHITE);
		edgeWeightSlider.setPreferredSize(new Dimension(150, 50));
		edgeWeightSlider.setPaintTicks(true);
		edgeWeightSlider.setMajorTickSpacing(100);
		edgeWeightSlider.setPaintTicks(true);

		weightControls = new JPanel() {
			private static final long serialVersionUID = -7190716810300622629L;

			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};
		weightControls.setOpaque(true);
		weightControls.setLayout(new BoxLayout(weightControls, BoxLayout.Y_AXIS));
		weightControls.add(edgeWeightSlider);

		final String COMMANDSTRING = "Edges removed for clusters: ";
		String stThresholdValue = String.valueOf(getThresholdFromSlider());
		if (stThresholdValue.contains("E")) {
			stThresholdValue = stThresholdValue.substring(0, Math.min(5, stThresholdValue.indexOf("E")))
					+ stThresholdValue.substring(stThresholdValue.indexOf("E"), stThresholdValue.length());
		} else {
			stThresholdValue = stThresholdValue.substring(0, Math.min(5, stThresholdValue.length()));
		}

		final TitledBorder sliderBorder = BorderFactory.createTitledBorder(COMMANDSTRING + stThresholdValue);
		weightControls.setBorder(sliderBorder);
		weightControls.add(Box.createVerticalGlue());

		groupVertices = new JRadioButton("Group Clusters");
		groupVertices.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				double thresholdValue = getThresholdFromSlider();
				clusterAndRecolorByWeight(layout, thresholdValue, similarColors,
						e.getStateChange() == ItemEvent.SELECTED);
				vv.repaint();
			}
		});

		clusterAndRecolorByWeight(layout, 0, similarColors, groupVertices.isSelected());

		//Tell the renderer to use our own customized color rendering
		vv.getRenderContext().setVertexFillPaintTransformer(MapTransformer.<SNNode, Paint>getInstance(vertexPaints));
		vv.getRenderContext().setVertexDrawPaintTransformer(new Transformer<SNNode, Paint>() {
			public Paint transform(SNNode v) {
				if (vv.getPickedVertexState().isPicked(v)) {
					return Color.cyan;
				} else {
					return Color.BLACK;
				}
			}
		});

		vv.getRenderContext().setEdgeDrawPaintTransformer(MapTransformer.<SNEdge, Paint>getInstance(edgePaints));
		vv.getRenderContext().setEdgeStrokeTransformer(new Transformer<SNEdge, Stroke>() {
			protected final Stroke THIN = new BasicStroke(1);

			public Stroke transform(SNEdge e) {
				return THIN;
			}
		});

		edgeWeightSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					double thresholdValue = getThresholdFromSlider();
					clusterAndRecolorByWeight(layout, thresholdValue, similarColors, groupVertices.isSelected());
					String stThresholdValue = String.valueOf(getThresholdFromSlider());
					if (stThresholdValue.contains("E")) {
						stThresholdValue = stThresholdValue.substring(0, Math.min(5, stThresholdValue.indexOf("E")))
								+ stThresholdValue.substring(stThresholdValue.indexOf("E"), stThresholdValue.length());
					} else {
						stThresholdValue = stThresholdValue.substring(0, Math.min(5, stThresholdValue.length()));
					}
					sliderBorder.setTitle(COMMANDSTRING + stThresholdValue);
					weightControls.repaint();
					vv.validate();
					vv.repaint();
				}
			}
		});
	}

	private double getThresholdFromSlider() {
		double threshold = edgeWeightSlider.getValue() / 1000.0;
		threshold = (sn.getMaxFlowValue() - sn.getMinFlowValue()) * threshold + sn.getMinFlowValue();
		return threshold;
	}

	@SuppressWarnings("unchecked")
	private void changeLayout() {
		
		String st_layout = (String) layoutBox.getSelectedItem();
		Layout<SNNode, SNEdge> l = null;
		if (st_layout.equals(ST_KKLayout)) {
			l = new KKLayout<SNNode, SNEdge>(g);
		} else if (st_layout.equals(ST_CircleLayout)) {
			l = new CircleLayout<SNNode, SNEdge>(g);
		} else if (st_layout.equals(ST_FRLayout)) {
			l = new FRLayout<SNNode, SNEdge>(g);
		} else if (st_layout.equals(ST_SpringLayout)) {
			l = new SpringLayout<SNNode, SNEdge>(g);
		} else if (st_layout.equals(ST_ISOMLayout)) {
			l = new ISOMLayout<SNNode, SNEdge>(g);
		} else if (st_layout.equals(ST_RankingLayout)) {
			String str = (String) rankingBox.getSelectedItem();
			l = new SocialNetworkAnalysisUtil<SNNode, SNEdge>(g, str);
		}

		layout = new AggregateLayout<SNNode, SNEdge>(l);
		layout.setInitializer(vv.getGraphLayout());
		layout.setSize(vv.getSize());
		if (paintable != null) {
			vv.removePreRenderPaintable(paintable);
		}
		if (l instanceof SocialNetworkAnalysisUtil) {
			paintable = ((SocialNetworkAnalysisUtil) l).getBackground(vv);
			vv.addPreRenderPaintable(paintable);
		}

		LayoutTransition<SNNode, SNEdge> lt = new LayoutTransition<SNNode, SNEdge>(vv, vv.getGraphLayout(), layout);
		Animator animator = new Animator(lt);
		animator.start();
		vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
		vv.repaint();

		double thresholdValue = getThresholdFromSlider();
		clusterAndRecolorByWeight(layout, thresholdValue, similarColors, groupVertices.isSelected());
	}

	private void clusterAndRecolorByWeight(AggregateLayout<SNNode, SNEdge> layout, double thresholdvalue,
			Color[] colors, boolean groupClusters) {

		Graph<SNNode, SNEdge> g = layout.getGraph();
		layout.removeAll();

		Set<Set<SNNode>> clusterSet = extractbyWeight(g, thresholdvalue);
		List<SNEdge> edges = extractbyWeightList(g, thresholdvalue);

		int i = 0;
		//Set the colors of each node so that each cluster's vertices have the same color
		for (Iterator<Set<SNNode>> cIt = clusterSet.iterator(); cIt.hasNext();) {

			Set<SNNode> vertices = cIt.next();
			Color c = colors[i % colors.length];

			colorCluster(vertices, c);
			if (groupClusters == true) {
				groupCluster(layout, vertices);
			}
			i++;
		}
		for (SNEdge e : g.getEdges()) {
			if (edges.contains(e)) {
				edgePaints.put(e, Color.lightGray);
			} else {
				edgePaints.put(e, Color.gray);
			}
		}
	}

	private Set<Set<SNNode>> extractbyWeight(Graph<SNNode, SNEdge> g, double thresholdvalue) {

		Graph<SNNode, SNEdge> graph = g;

		List<SNEdge> edgesRemoved = new ArrayList<SNEdge>();
		edgesRemoved.clear();
		for (Iterator<SNEdge> it = g.getEdges().iterator(); it.hasNext();) {
			SNEdge tempEdge = it.next();
			if (tempEdge.getWeight() < thresholdvalue) {
				edgesRemoved.add(tempEdge);
			}
		}

		for (Iterator<SNEdge> it = edgesRemoved.iterator(); it.hasNext();) {
			graph.removeEdge(it.next());
		}

		WeakComponentClusterer<SNNode, SNEdge> wcSearch = new WeakComponentClusterer<SNNode, SNEdge>();
		Set<Set<SNNode>> clusterSet = wcSearch.transform(graph);
		for (Iterator<SNEdge> iter = edgesRemoved.iterator(); iter.hasNext();) {
			SNEdge edge = iter.next();
			graph.addEdge(edge, edge.getSource(), edge.getTarget());
		}
		return clusterSet;
	}

	private List<SNEdge> extractbyWeightList(Graph<SNNode, SNEdge> g, double thresholdvalue) {
		List<SNEdge> mEdgesRemoved = new ArrayList<SNEdge>();
		for (Iterator<SNEdge> it = g.getEdges().iterator(); it.hasNext();) {
			SNEdge tempEdge = it.next();
			if (tempEdge.getWeight() < thresholdvalue) {
				mEdgesRemoved.add(tempEdge);
			}
		}
		return mEdgesRemoved;
	}

	private void colorCluster(Set<SNNode> vertices, Color c) {
		for (SNNode v : vertices) {
			vertexPaints.put(v, c);
		}
	}

	private void groupCluster(AggregateLayout<SNNode, SNEdge> layout, Set<SNNode> vertices) {
		if (vertices.size() < layout.getGraph().getVertexCount()) {
			Point2D center = layout.transform(vertices.iterator().next());
			Graph<SNNode, SNEdge> subGraph = SparseMultigraph.<SNNode, SNEdge>getFactory().create();
			for (SNNode v : vertices) {
				subGraph.addVertex(v);
			}
			Layout<SNNode, SNEdge> subLayout = new CircleLayout<SNNode, SNEdge>(subGraph);
			subLayout.setInitializer(vv.getGraphLayout());
			subLayout.setSize(new Dimension(40, 40));

			layout.put(subLayout, center);
			vv.repaint();
		}
	}

	public void actionPerformed(ActionEvent e) {
		AbstractButton source = (AbstractButton) e.getSource();
		if (source == v_shape) {
			vssa.useFunnyShapes(source.isSelected());
		} else if (source == v_size) {
			vssa.setScaling(source.isSelected());
		} else if (source == v_aspect) {
			vssa.setStretching(source.isSelected());
		} else if (source == e_labels) {
			if (source.isSelected()) {
				vv.getRenderContext().setEdgeLabelTransformer(stringer);
			} else {
				vv.getRenderContext().setEdgeLabelTransformer(es_none);
			}
		} else if (source == v_labels) {
			if (source.isSelected()) {
				vv.getRenderContext().setVertexLabelTransformer(stringerVertex);
			} else {
				vv.getRenderContext().setVertexLabelTransformer(vs_none);
			}
		} else if (source == e_labels) {
			if (source.isSelected()) {
				vv.getRenderContext().setEdgeLabelTransformer(stringer);
			} else {
				vv.getRenderContext().setEdgeLabelTransformer(es_none);
			}
		} else if (source == e_show_d) {
			show_edge.showDirected(source.isSelected());
		} else if (source == v_stroke) {
			vsh.setHighlight(source.isSelected());
		}
		vv.repaint();
	}
}
