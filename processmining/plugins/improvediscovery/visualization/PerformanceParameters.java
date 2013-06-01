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

import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.OLAPData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.OLAPTransformation;

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
	protected OLAPData Data;
	protected OLAPTransformation DataTransformation;
	protected 		JPanel performanceHeaderPanel;
	protected JPanel performanceParametersContainer;
	protected JPanel downSettingsPanel;
	protected boolean multiTime;
	protected JLabel maxSliderHeader;
	public PerformanceParameters(OLAPTransformation DataTransformation) {
		// TODO Auto-generated constructor stub
		this.DataTransformation= DataTransformation;
		Data=DataTransformation.GetData();
		Data.InicializatePerformanceData();
		
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
    	if(multiTime)
    		return (int)(Data.GetPerformanceDiff()[Data.GetPerformanceDiff().length-1]*Data.GetPerformanceData().ponderator);
    	else	
    	    return (int)Data.GetPerformanceDiff()[Data.GetPerformanceDiff().length-1];
		
    	
		
	}
	
	public int ReturnMinValue()
	{
	
    	if(multiTime)
    		return (int)(Data.GetPerformanceDiff()[0]*Data.GetPerformanceData().ponderator);
    	else	
    	    return (int)Data.GetPerformanceDiff()[0];
		 
    	
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
		minSliderHeader = new JLabel(JLabelName("<html>Min Time <br>",Data.GetPerformanceDiff()[Data.GetPerformanceDiff().length-1]));	
		maxTimeLabel = new JLabel(JLabelName("",Data.GetPerformanceDiff()[0]));
		minTimeLabel = new JLabel(	JLabelName("",Data.GetPerformanceDiff()[0]));
		maxSliderHeader = new JLabel(JLabelName("<html>Max Time <br>",Data.GetPerformanceDiff()[Data.GetPerformanceDiff().length-1]));
	
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
				System.out.print("\n mouse pressed");
				maxTimeSlider.setMinimum(minTimeSlider.getValue());
				
				if(multiTime)
				{
					maxTimeLabel.setText(JLabelName("",NormaliseTime(minTimeSlider.getValue())));
				    DataTransformation.PerformanceFilter(NormaliseTime(minTimeSlider.getValue()), maxTimeSlider.getValue());
				}
				else
				{
					DataTransformation.PerformanceFilter(minTimeSlider.getValue(), maxTimeSlider.getValue());
					maxTimeLabel.setText(JLabelName("",minTimeSlider.getValue()));
					
				}
				}
				pressed=false;
			}
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
				System.out.print("\n mouse click");			
					maxTimeSlider.setMinimum(minTimeSlider.getValue());
					if(multiTime)
					{
						maxTimeLabel.setText(JLabelName("",NormaliseTime(minTimeSlider.getValue())));
						DataTransformation.PerformanceFilter(NormaliseTime(minTimeSlider.getValue()), maxTimeSlider.getValue());
					}
					else
					{
						maxTimeLabel.setText(JLabelName("",minTimeSlider.getValue()));
						DataTransformation.PerformanceFilter(minTimeSlider.getValue(), maxTimeSlider.getValue());

					}
			
	

			}
			public void mouseEntered(MouseEvent arg0) {	}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
         });
         
		minTimeSlider.setToolTipText("<html>Select the minimum time of the traces for filtering<br>"
				+ "lower value prefers correlation.</html>");
		
		parametersPerformancePanelLabel.add(packVerticallyCentered(minSliderHeader, 120, 40), BorderLayout.NORTH);
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
				if(multiTime)
				{		
				DataTransformation.PerformanceFilter(NormaliseTime(minTimeSlider.getValue()),
													 NormaliseTime(maxTimeSlider.getValue()));
				minSliderHeader.setText(JLabelName("<html>Min Time <br>", NormaliseTime(maxTimeSlider.getValue())));			
				
				}
				else
				{
    			DataTransformation.PerformanceFilter(minTimeSlider.getValue(), maxTimeSlider.getValue());
				minSliderHeader.setText(JLabelName("<html>Min Time <br>",maxTimeSlider.getValue()));			
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
				minTimeSlider.setMaximum(maxTimeSlider.getValue());
				
				if(multiTime)
				{
					DataTransformation.PerformanceFilter(NormaliseTime(minTimeSlider.getValue()),
							 NormaliseTime(maxTimeSlider.getValue()));
					minSliderHeader.setText(JLabelName("<html>Min Time <br>",
							 NormaliseTime(maxTimeSlider.getValue())));

				}
				else
				{
    			DataTransformation.PerformanceFilter(minTimeSlider.getValue(), maxTimeSlider.getValue());
				minSliderHeader.setText(JLabelName("<html>Min Time <br>",maxTimeSlider.getValue()));

				}
				
			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
         
         });
		maxTimeSlider.setSize(new Dimension(10,100));
		maxTimeSlider.setToolTipText("<html>" +
						"Select the minimum time of the traces for filtering.</html>");
		
		
		parametersPerformancePanel.add(packVerticallyCentered(maxSliderHeader, 120, 40), BorderLayout.NORTH);
		parametersPerformancePanel.add(maxTimeSlider, BorderLayout.CENTER);
		parametersPerformancePanel.add(packVerticallyCentered(maxTimeLabel, 120, 20), BorderLayout.SOUTH);
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
	

	public String JLabelName(String ini, double time)
	{
    	String name="";
    	
    	if(multiTime)
    	{
    		System.out.print("\n multi time es verdadero");
    		name=	ini+ " "+	editTime(time);
    	}
    	else	
    	{
    		System.out.print("\n multi time falso");

    		name=   (int) time+" "+Data.GetPerformanceData().TagTime();
    	}
    	return name;
	}
	
	public boolean CompositionTime()
	{
		//max
		double min=Data.GetPerformanceDiff()[0];
		//min
		double max=Data.GetPerformanceDiff()[Data.GetPerformanceDiff().length-1];
		
		double diff= max-min;
		
		System.out.print("\n min:"+min);
		System.out.print("\n max:"+max);

		if(diff>10)
				return false;
		else
			return true;
	}
	
	public double NormaliseTime(double time)
	{
		double ponderator=1;
		System.out.print("\n time value"+time);

		if(multiTime)
		{
	     ponderator=DataTransformation.GetData().GetPerformanceData().ponderator;
		}
		
		System.out.print("\n ponderador:"+ponderator);
		double value= time/ponderator;
		System.out.print("\n final value"+value);

		return  value;
	}
	public String editTime(double time)
	{    
		double seconds;
		double minutes;
		double hours;
		
		String date="";
		//Days
		if(time>=2)
		{
			date= ""+Math.round(time)+" d";
			time=time-Math.round(time);
		}
		else if(time>=1)
		{
			date= ""+Math.round(time)+" d";
			time=time-Math.round(time);

		}
		//hours
		hours=time*24;
		if(hours>=1 && hours<2)
		{
			date+=", "+1+" h";
			hours=hours-1;
	
		}
		else if(hours>=2)
		{
			date+=", "+ Math.round(hours)   +" h";
			hours=hours-Math.round(hours);
		}
		
		//minutes
		minutes=hours*60;
		if(minutes>=1 && minutes<2)
		{
			date+=", "+1+" m";
			minutes=minutes-1;
		}
		else if(minutes>2)
		{
			date+=", "+ Math.round(minutes)   +" m";
			minutes=minutes-Math.round(minutes);
			
		}	
		seconds=minutes*60;
		if(seconds==1)
		{
			date+=", "+1+" s";
			minutes=minutes-1;
		}
		else if(seconds>=2)
		{
			date+=", "+ Math.round(seconds)   +" s";
			seconds=seconds-Math.round(seconds);
					
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
		this.remove(performanceHeaderPanel);
		this.remove(performanceParametersContainer);
		this.remove(downSettingsPanel);
		BuiltParameters();
		this.repaint();
		multiTime=CompositionTime();

	}

}
