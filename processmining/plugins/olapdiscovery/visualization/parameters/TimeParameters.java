package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.parameters;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTransformation;

import com.fluxicon.slickerbox.ui.SlickerSliderUI;

/**
 *  Time parameters container
 *  @author Gustavo Pizarro
*/

public class TimeParameters extends JPanel {

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
	protected OLAPTransformation Transformation;
	protected 		JPanel performanceHeaderPanel;
	protected JPanel performanceParametersContainer;
	protected JPanel downSettingsPanel;
	protected boolean multiTime;
	protected JLabel maxSliderHeader;
	
	public TimeParameters(OLAPTransformation Transformation) {
		// TODO Auto-generated constructor stub
		this.Transformation= Transformation;
	    Transformation.GetTimeTransformation().InicializatePerformanceData(Transformation.GetData().GetOriginalLog());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setBackground(COLOR_BG2);
		this.setOpaque(true);
		this.setLayout(new BorderLayout());
		multiTime=CompositionTime();
		BuiltParameters();
	}
	
	public void JLabelProperties()
	{
		minSliderHeader.setFont(this.smallFont);
		minSliderHeader.setOpaque(false);
		minSliderHeader.setForeground(COLOR_FG);
		centerHorizontally(minSliderHeader);

		centerHorizontally(minTimeLabel);
		minTimeLabel.setSize(new Dimension(100, 25));
		minTimeLabel.setForeground(COLOR_FG);
		minTimeLabel.setFont(this.smallFont);
		
		maxSliderHeader.setSize(new Dimension(150, 25));
		maxSliderHeader.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		maxSliderHeader.setOpaque(false);
		maxSliderHeader.setForeground(COLOR_FG);
		maxSliderHeader.setFont(this.smallFont);
		centerHorizontally(maxSliderHeader);				


		maxTimeLabel.setForeground(COLOR_FG);
		maxTimeLabel.setSize(new Dimension(100, 25));
		maxTimeLabel.setFont(this.smallFont);					
		centerHorizontally(maxTimeLabel);	
	}
	
	
	public int ReturnMaxValue()
	{
    	
    	 return (int)Transformation.GetTimeTransformation().GetPerformanceDiff()[Transformation.GetTimeTransformation().GetPerformanceDiff().length-1];
		
    	
		
	}
	
	public int ReturnMinValue()
	{
	
  
    	  return (int)Transformation.GetTimeTransformation().GetPerformanceDiff()[0];
		 
    	
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
		
		
		//Slider Labels
		minSliderHeader = new JLabel(JLabelName("<html>Min Time <br>",Transformation.GetTimeTransformation().GetPerformanceDiff()[Transformation.GetTimeTransformation().GetPerformanceDiff().length-1]));	
		maxTimeLabel = new JLabel(JLabelName("",Transformation.GetTimeTransformation().GetPerformanceDiff()[0]));
		minTimeLabel = new JLabel(	JLabelName("",Transformation.GetTimeTransformation().GetPerformanceDiff()[0]));
		maxSliderHeader = new JLabel(JLabelName("<html>Max Time <br>",Transformation.GetTimeTransformation().GetPerformanceDiff()[Transformation.GetTimeTransformation().GetPerformanceDiff().length-1]));
	
		//Label Properties
		 JLabelProperties();
		
		 //Sliders
		// lower percentage slider panel						
	
		 
		 
		minTimeSlider = new JSlider(JSlider.VERTICAL,ReturnMinValue() , ReturnMaxValue(), ReturnMinValue());
		minTimeSlider.setUI(new SlickerSliderUI(minTimeSlider));
		minTimeSlider.setOpaque(false);
		minTimeSlider.addMouseListener(new MouseListener(){
          
			boolean pressed=false;
			public void mousePressed(MouseEvent arg0) {
				
				if(!pressed)
				{
				maxTimeSlider.setMinimum(minTimeSlider.getValue());
				
	
					maxTimeLabel.setText(JLabelName("",minTimeSlider.getValue()));

				    Transformation.GetPerformanceFilter().Filter(minTimeSlider.getValue(), maxTimeSlider.getValue(),Transformation.GetTimeTransformation(),Transformation.GetData());
				    Transformation.UpdateGraphWithPerformanceChanges();


				}
				pressed=false;
			}
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
					maxTimeSlider.setMinimum(minTimeSlider.getValue());
				
						maxTimeLabel.setText(JLabelName("",(minTimeSlider.getValue())));
						Transformation.GetPerformanceFilter().Filter(minTimeSlider.getValue(), maxTimeSlider.getValue(),Transformation.GetTimeTransformation(),Transformation.GetData());
					
				
			
	

			}
			public void mouseEntered(MouseEvent arg0) {	}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
         });
         
		minTimeSlider.setToolTipText("<html>Select the minimum time of the traces for filtering<br>"
				+ "lower value prefers correlation.</html>");
		
		parametersPerformancePanelLabel.add(packVerticallyCentered(minSliderHeader, 120, 50), BorderLayout.NORTH);
		parametersPerformancePanelLabel.add(minTimeSlider, BorderLayout.CENTER);
		parametersPerformancePanelLabel.add(packVerticallyCentered(minTimeLabel, 120, 20), BorderLayout.SOUTH);
		
		// lower percentage slider panel
		JPanel parametersPerformancePanel = new JPanel();
		parametersPerformancePanel.setOpaque(false);
		parametersPerformancePanel.setLayout(new BorderLayout());
		
		maxTimeSlider = new JSlider(JSlider.VERTICAL, ReturnMinValue(), ReturnMaxValue(), ReturnMaxValue());
		maxTimeSlider.setUI(new SlickerSliderUI(maxTimeSlider));
		maxTimeSlider.setOpaque(false);
		maxTimeSlider.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				minTimeSlider.setMaximum(maxTimeSlider.getValue());
					
				Transformation.GetPerformanceFilter().Filter(minTimeSlider.getValue(),
													         maxTimeSlider.getValue(),
														     Transformation.GetTimeTransformation(),
														     Transformation.GetData());
				
				minSliderHeader.setText(JLabelName("<html>Min Time <br>", maxTimeSlider.getValue()));			
				
				
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
				
				Transformation.GetPerformanceFilter().Filter(minTimeSlider.getValue(),
							maxTimeSlider.getValue(), 
							 Transformation.GetTimeTransformation(),
							 Transformation.GetData());
					
					minSliderHeader.setText(JLabelName("<html>Min Time <br>",
							maxTimeSlider.getValue()));

				
			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
         
         });
		maxTimeSlider.setSize(new Dimension(10,100));
		maxTimeSlider.setToolTipText("<html>" +
						"Select the minimum time of the traces for filtering.</html>");
		
		parametersPerformancePanel.add(packVerticallyCentered(maxSliderHeader, 120, 50), BorderLayout.NORTH);
		parametersPerformancePanel.add(maxTimeSlider, BorderLayout.CENTER);
		parametersPerformancePanel.add(packVerticallyCentered(maxTimeLabel, 120, 20), BorderLayout.SOUTH);
				// assemble lower slider panel
		performanceParametersContainer.add(parametersPerformancePanel);
		performanceParametersContainer.add(parametersPerformancePanelLabel);
				// assemble check box panel
		downSettingsPanel = new JPanel();
		downSettingsPanel.setOpaque(false);
		downSettingsPanel.setLayout(new BoxLayout(downSettingsPanel, BoxLayout.Y_AXIS));
		
		//mean and standart deviation
		
	
	
		JLabel mean= new JLabel("Process mean duration: "+ editTime(Transformation.GetTimeTransformation().GetTimeData().getMean()));
		JLabel stan= new JLabel("Process standard deviation: "+editTime(Transformation.GetTimeTransformation().GetTimeData().getStandartD()));
		
		downSettingsPanel.add(mean);
		downSettingsPanel.add(stan);

	
				this.add(performanceHeaderPanel, BorderLayout.NORTH);
				this.add(performanceParametersContainer, BorderLayout.CENTER);
				this.add(downSettingsPanel, BorderLayout.SOUTH);
	}

	
	

	public String JLabelName(String ini, double time)
	{
		String name;
		if(ini.equals(""))
		{
             name=	ini+ " "+	editTime(time) ;
		}
		else
		{
	    	 name=	ini+ " "+	editTime(time) +"</html>";
		}

    	return name;
	}
	
	public boolean CompositionTime()
	{
		//max
		double min=Transformation.GetTimeTransformation().GetPerformanceDiff()[0];
		//min
		double max=Transformation.GetTimeTransformation().GetPerformanceDiff()[Transformation.GetTimeTransformation().GetPerformanceDiff().length-1];
		
		double diff= max-min;
		
		

		if(diff>10)
				return false;
		else
			return true;
	}
	

	public String editTime(double time)
	{    

		boolean HasDays=false;
		boolean HasHours=false;
		boolean HasMin=false;
		String date="";
		//Days
		double days=time/86400;
		if(days>=1)
		{
			date= ""+Math.round(days)+" d";
			time=time-Math.round(days)*86400;
			HasDays=true;
		}
		//hours
		double hours=time/3600;
		if(hours>=1)
		{
			if(HasDays)
			date+=", "+Math.round(hours)+" h";
			else
			date+=Math.round(hours)+" h";

			HasHours=true;
			time=time-Math.round(hours)*3600;
		}

		
		//minutes
		double minutes=time/60;
		if(minutes>=1)
		{
				if(HasHours)
				date+=", "+Math.round(minutes)+" m";
				else
				date+=Math.round(minutes)+" m";

				HasMin=true;
				time=time-Math.round(minutes)*60;
			
		}	
	
		if(time>0)
		{
			if(HasMin)
				date+=", "+Math.round(minutes)+" s";
				else
				date+=Math.round(minutes)+" s";


					
		}

		return date;
		
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
		performanceHeaderPanel.removeAll();
		this.remove(performanceHeaderPanel);
		performanceParametersContainer.removeAll();
		this.remove(performanceParametersContainer);
		downSettingsPanel.removeAll();
		this.remove(downSettingsPanel);
		
		//TransformationTime.InicializatePerformanceTransformationTime();
		multiTime=CompositionTime();
		BuiltParameters();
		

	}
	
	public JSlider GetMaxSlider()
	{
		return this.maxTimeSlider;
	}
	
	public JSlider GetMinSlider()
	{
		return this.minTimeSlider;
	}

}
