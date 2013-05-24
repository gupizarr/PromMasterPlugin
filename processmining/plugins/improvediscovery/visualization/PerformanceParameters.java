package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryTransformation;

import com.fluxicon.slickerbox.ui.SlickerCheckBoxUI;
import com.fluxicon.slickerbox.ui.SlickerSliderUI;


public class PerformanceParameters extends JPanel {

	protected JLabel minSliderHeader;
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
	protected Color COLOR_BG = new Color(60, 60, 60);
	protected Color COLOR_BG2 = new Color(120, 120, 120);
	protected Color COLOR_FG = new Color(30, 30, 30);
	protected Font smallFont;
	protected ImproveDiscoveryData Data;
	protected ImproveDiscoveryTransformation DataTransformation;
	protected 		JPanel performanceHeaderPanel;
	protected JPanel performanceParametersContainer;
	protected JPanel downSettingsPanel;
	public PerformanceParameters(ImproveDiscoveryTransformation DataTransformation) {
		// TODO Auto-generated constructor stub
		this.DataTransformation= DataTransformation;
		Data=DataTransformation.GetData();
		Data.InicializatePerformanceData();
		
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setBackground(COLOR_BG2);
		this.setOpaque(true);
		this.setLayout(new BorderLayout());
		BuiltParameters();
	}
	
	public void BuiltParameters()
	{
		
		
		// lower header panel (radio buttons etc.)
		performanceHeaderPanel = new JPanel();
		performanceHeaderPanel.setOpaque(false);
		performanceHeaderPanel.setLayout(new BoxLayout(performanceHeaderPanel, BoxLayout.Y_AXIS));
		
		//centerHorizontally(lowerHeaderLabel);
		performanceParametersContainer = new JPanel();
		performanceParametersContainer.setOpaque(false);
		performanceParametersContainer.setLayout(new BoxLayout(performanceParametersContainer, BoxLayout.X_AXIS));
		
		// lower ratio slider panel
		JPanel parametersPerformancePanelLabel = new JPanel();
		parametersPerformancePanelLabel.setOpaque(false);
		parametersPerformancePanelLabel.setLayout(new BorderLayout());		

		// lower percentage slider panel				
		minSliderHeader = new JLabel("<html>Min Time <br>"+""+ (int) Data.GetPerformanceDiff()[Data.GetPerformanceDiff().length-1]+" "+Data.GetPerformanceData().TagTime());
		minSliderHeader.setFont(this.smallFont);
		minSliderHeader.setOpaque(false);
		minSliderHeader.setForeground(COLOR_FG);
		centerHorizontally(minSliderHeader);
		
		minTimeSlider = new JSlider(JSlider.VERTICAL,(int) Data.GetPerformanceDiff()[0] , 
				                                     (int) Data.GetPerformanceDiff()[Data.GetPerformanceDiff().length-1], 
				                                     (int) Data.GetPerformanceDiff()[0]);
		minTimeSlider.setUI(new SlickerSliderUI(minTimeSlider));
		minTimeSlider.setOpaque(false);
		minTimeSlider.addMouseListener(new MouseListener(){

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
				maxTimeSlider.setMinimum(minTimeSlider.getValue());
				maxTimeLabel.setText(""+minTimeSlider.getValue()+" "+Data.GetPerformanceData().TagTime());
				DataTransformation.PerformanceFilter(minTimeSlider.getValue(), maxTimeSlider.getValue());
		
			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
         
         });
         
		minTimeSlider.setToolTipText("<html>Select the minimum time of the traces for filtering<br>"
				+ "lower value prefers correlation.</html>");

		
		minTimeLabel = new JLabel(""+  (int) Data.GetPerformanceDiff()[0]+" "+Data.GetPerformanceData().TagTime());
		centerHorizontally(minTimeLabel);
		minTimeLabel.setSize(new Dimension(100, 25));
		minTimeLabel.setForeground(COLOR_FG);
		minTimeLabel.setFont(this.smallFont);
		
		parametersPerformancePanelLabel.add(packVerticallyCentered(minSliderHeader, 100, 40), BorderLayout.NORTH);
		parametersPerformancePanelLabel.add(minTimeSlider, BorderLayout.CENTER);
		parametersPerformancePanelLabel.add(packVerticallyCentered(minTimeLabel, 100, 20), BorderLayout.SOUTH);
		
		// lower percentage slider panel
		JPanel parametersPerformancePanel = new JPanel();
		parametersPerformancePanel.setOpaque(false);
		parametersPerformancePanel.setLayout(new BorderLayout());
		
		
		JLabel maxSliderHeader = new JLabel("<html>Max Time <br>"+
		(int) Data.GetPerformanceDiff()[Data.GetPerformanceDiff().length-1]+" "+
		Data.GetPerformanceData().TagTime());
		maxSliderHeader.setSize(new Dimension(150, 25));
		maxSliderHeader.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		maxSliderHeader.setOpaque(false);
		maxSliderHeader.setForeground(COLOR_FG);
		maxSliderHeader.setFont(this.smallFont);
		centerHorizontally(maxSliderHeader);
				
		maxTimeSlider = new JSlider(JSlider.VERTICAL, (int) Data.GetPerformanceDiff()[0],  (int) Data.GetPerformanceDiff()[Data.GetPerformanceDiff().length-1], 
				(int) Data.GetPerformanceDiff()[Data.GetPerformanceDiff().length-1]);
		maxTimeSlider.setUI(new SlickerSliderUI(maxTimeSlider));
		maxTimeSlider.setOpaque(false);
		maxTimeSlider.addMouseListener(new MouseListener(){

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
				minTimeSlider.setMaximum(maxTimeSlider.getValue());
				DataTransformation.PerformanceFilter(minTimeSlider.getValue(), maxTimeSlider.getValue());
				minSliderHeader.setText("<html>Min Time <br>"+""+ (int) maxTimeSlider.getValue() +" "+Data.GetPerformanceData().TagTime());
	
			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
         
         });
		maxTimeSlider.setSize(new Dimension(10,100));
		maxTimeSlider.setToolTipText("<html>" +
						"Select the minimum time of the traces for filtering.</html>");
		maxTimeLabel = new JLabel(""+(int) Data.GetPerformanceDiff()[0]+" "+Data.GetPerformanceData().TagTime());
		maxTimeLabel.setForeground(COLOR_FG);
		maxTimeLabel.setSize(new Dimension(100, 25));
		maxTimeLabel.setFont(this.smallFont);
				
		centerHorizontally(maxTimeLabel);
		parametersPerformancePanel.add(packVerticallyCentered(maxSliderHeader, 100, 40), BorderLayout.NORTH);
		parametersPerformancePanel.add(maxTimeSlider, BorderLayout.CENTER);
		parametersPerformancePanel.add(packVerticallyCentered(maxTimeLabel, 100, 20), BorderLayout.SOUTH);
				// assemble lower slider panel
		performanceParametersContainer.add(parametersPerformancePanel);
		performanceParametersContainer.add(parametersPerformancePanelLabel);
				// assemble check box panel
		downSettingsPanel = new JPanel();
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
				this.add(performanceHeaderPanel, BorderLayout.NORTH);
				this.add(performanceParametersContainer, BorderLayout.CENTER);
				this.add(downSettingsPanel, BorderLayout.SOUTH);
				//end of the "Edge filter" panel
	}
	
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getSource() == meanPerformanceCheckBox) {
			System.out.print("Ver media");
		}
	}
	
	protected double getNodeThresholdFromSlider() {
		double threshold = performanceSlider.getValue() / 1000.0;
		// normalize threshold to minimal node frequency
		//threshold = ((1.0 - graph.getMinimalNodeSignificance()) * threshold) + graph.getMinimalNodeSignificance();
		return threshold;
	}
	
	protected void centerHorizontally(JLabel label) {
		label.setSize(new Dimension(100,30));
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
	
	public void ResetPanel()
	{
		this.remove(performanceHeaderPanel);
		this.remove(performanceParametersContainer);
		this.remove(downSettingsPanel);
		BuiltParameters();
		this.repaint();
	}

}
