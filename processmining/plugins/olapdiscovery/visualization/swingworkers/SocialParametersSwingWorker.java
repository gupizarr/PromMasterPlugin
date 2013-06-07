package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.swingworkers;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTransformation;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.OLAPDiscoveryPanel;

import com.fluxicon.slickerbox.ui.SlickerCheckBoxUI;


/**
 * Swing Worker Actions (social)
 *  @author Gustavo Pizarro
*/
public class SocialParametersSwingWorker {
	
	private JFrame frame;
	private JLabel percentage;
	public SocialParametersSwingWorker() {
		// TODO Auto-generated constructor stub
	}

	 //Add one resource
	 public void LoadResources( final OLAPDiscoveryPanel MainPanel, final OLAPTransformation Transformation, final Map<String,ArrayList<JCheckBox>> GroupMemberMap)
	 {
		 
			SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
				
				@Override
				protected Boolean doInBackground() throws InterruptedException {

			
					ShowAdviceFrame("Wait", "The plugin is working");
					Font resourceFont= new Font("8f", 12, 10);
					String resource;
				    String toolTip;
				    
					ArrayList<JCheckBox> JCheckList ;
					JCheckBox chec;
				    int member;
				    int value=0;
					for(int j=0;j<Transformation.GetSocialTransformation().GetTeams().size();j++)
					{
						JCheckList= new ArrayList<JCheckBox>() ;
						for(int k=0;k<Transformation.GetSocialTransformation().GetTeams().get(j).size();k++)
						{
							value=(1+(2*j))*(10+k);
							publish(value);
							member=Transformation.GetSocialTransformation().GetTeams().get(j).get(k);
							resource=Transformation.GetSocialTransformation().TranslateNode(member);
							chec= new JCheckBox(resource);
							toolTip=ResourceToolTipText(Transformation.GetActivities(resource));
							chec.setToolTipText(toolTip);	

							chec.setUI(new SlickerCheckBoxUI());
							chec.setSelected(true);
							chec.setOpaque(false);
						 	chec.setFont(resourceFont);
							chec.setName(""+member);
							JCheckList.add(chec);
						}
						GroupMemberMap.put(""+j,JCheckList);
					}
					if(Transformation.GetSocialTransformation().GroupOfTwo().size()>0)
					{
						JCheckList= new ArrayList<JCheckBox>() ;
						for(int k=0;k<Transformation.GetSocialTransformation().GroupOfTwo().size();k++)
						{
							for(int j=0;j<Transformation.GetSocialTransformation().GroupOfTwo().get(k).size();j++)
							{
								value+=k*(j)+1;
								publish(value);

							member=Transformation.GetSocialTransformation().GroupOfTwo().get(k).get(j);
							resource=	Transformation.GetSocialTransformation().TranslateNode(member);
							chec= new JCheckBox(resource);
							toolTip=ResourceToolTipText(Transformation.GetActivities(resource));
							chec.setToolTipText(toolTip);	
						
							chec.setUI(new SlickerCheckBoxUI());
							chec.setSelected(true);
							chec.setOpaque(false);
						 	chec.setFont(resourceFont);
							chec.setName(""+member);
							JCheckList.add(chec);
							}
						}
						System.out.print("\n list alones");
						GroupMemberMap.put("-1",JCheckList);
					}

						
						
	
		             return true;
		        }	     
		     
				protected void done() {
				    
				try {
					
		             boolean status= get();
					if(status)
					{
						MainPanel.BuiltGraph();
			           frame.dispose();
			         
			         //frame.dispose();
					}
				} catch (InterruptedException e) {
			           frame.dispose();
			        
				    } catch (ExecutionException e) {
				           frame.dispose();


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
	
	 public void ShowAdviceFrame(String about, String messageText)
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
	public String ResourceToolTipText(ArrayList Activities)
	{
			String info="<html>Executor of:<br>";
			for(int j=0; j<Activities.size();j++)
				info+= Activities.get(j)+"<br>";

			info+="</html>";
			return info;
	}
}

