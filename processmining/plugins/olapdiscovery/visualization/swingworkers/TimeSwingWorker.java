package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.swingworkers;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTransformation;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.OLAPDiscoveryPanel;

/**
 * Swing Worker Actions (time)
 *  @author Gustavo Pizarro
*/
public class TimeSwingWorker {


	private JFrame frame;
	private JLabel percentage;
	public TimeSwingWorker() {
		// TODO Auto-generated constructor stub

	}
	 public void  UpdateTab(final OLAPDiscoveryPanel panel, final OLAPTransformation Transformation)
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				 ShowAdviceFrame("Loading Tab","The plugin is working");
				 
				   publish(10);
			
 
				   Transformation.GetTimeTransformation().InicializatePerformanceData(Transformation.GetData().GetBaseLog());
				   publish(20);

		
				   panel.GetParametersPanel().GetPerformanceParametersPanel().ResetPanel();
				   publish(30);
		

				   panel.GetParametersPanel().GetPerformanceParametersPanel().repaint();
				   publish(40);
		

				   panel.GetParametersPanel().GetTabPane().repaint();
				   publish(50);


				   panel.UpdateSNPanel();
				   publish(60);


				   panel.AddPerformanceClickAction();				   
				   publish(70);


				   //ParametersPanel.mainSocialParameters.GetOptions().setSelectedIndex(0);
	           
				   return true;
	        }	     
	     
			protected void done() {
			    
			try {
				
	             boolean status= get();
				if(status)
				{
					panel.BuiltGraph();
				   
				   //panel.GetParametersPanel().GetPerformanceParametersPanel().repaint();
		           frame.dispose();
		         
		         //frame.dispose();
				}
			}
			catch (InterruptedException e) 
			{
		           frame.dispose();
		           System.out.print("\n error");
		           panel.GetLogWarning().setText("Error");
		           panel.GetParametersPanel().GetPerformanceParametersPanel().removeAll();
		           panel.GetParametersPanel().GetPerformanceParametersPanel().repaint();
		           panel.GetParametersPanel().GetPerformanceParametersPanel().add(panel.GetLogWarning());
		           panel.GetParametersPanel().GetPerformanceParametersPanel().repaint();
			} 
			catch (ExecutionException e) 
			{
			           frame.dispose();
			           System.out.print("\n error");

			           panel.GetLogWarning().setText("Error");
			           panel.GetParametersPanel().GetPerformanceParametersPanel().removeAll();
			           panel.GetParametersPanel().GetPerformanceParametersPanel().repaint();
			           panel.GetParametersPanel().GetPerformanceParametersPanel().add(panel.GetLogWarning());
			           panel.GetParametersPanel().GetPerformanceParametersPanel().repaint();

			}
			   }

			   @Override
			   protected void process(List<Integer> chunks) {
			     //progressMonitor.setNote("Finish");
				   int mostRecentValue = chunks.get(chunks.size()-1);

				    percentage.setText(mostRecentValue+"%");
					   
					
			   }
			   
		};
		worker.execute();
	    }

	    
	
	    
	    public void ShowAdviceFrame(String about,String messageText)
	    {


			frame = new JFrame(about);
		
			percentage= new JLabel("0 %");
			percentage.setFont(new Font("20f", 20, 20));
			percentage.setHorizontalAlignment(JLabel.CENTER);
			percentage.setHorizontalTextPosition(JLabel.CENTER);
			percentage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			
			JLabel title=new JLabel(messageText);
			title.setFont(new Font("12f", 12, 20));
			title.setHorizontalAlignment(JLabel.CENTER);
			title.setHorizontalTextPosition(JLabel.CENTER);
			title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

			JLabel message=new JLabel("Please wait");
			message.setFont(new Font("15f", 15, 20));
			message.setHorizontalAlignment(JLabel.CENTER);
			message.setHorizontalTextPosition(JLabel.CENTER);
			message.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			
			JPanel panel= new JPanel();
			panel.setLayout(new BorderLayout());
			

			
			panel.add(title,BorderLayout.NORTH);
			panel.add(message,BorderLayout.CENTER);
			panel.add(percentage,BorderLayout.SOUTH);
			
			frame.getContentPane().add(panel);
			frame.setBounds(900, 300, 100, 100);

			//4. Size the frame.
			frame.pack();


			frame.setVisible(true);
			
	 
			
	    }

}
