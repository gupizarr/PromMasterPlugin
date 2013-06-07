package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.swingworkers;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XAttributeMapLazyImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPSocialTransformation;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTransformation;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.OLAPDiscoveryPanel;

/**
 * Swing Worker Actions (cluster)
 *  @author Gustavo Pizarro
*/
public class SocialSwingWorker {

	private JFrame frame;
	private JLabel percentage;
	public SocialSwingWorker() {
		// TODO Auto-generated constructor stub
	}

	 public void  RemoveGroupsSwingWorker(final OLAPDiscoveryPanel MainPanel,final String numberGroup,final String currentSocialAnalysist,final OLAPTransformation Transformation)
	 {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				 ShowAdviceFrame("Remove Group","The plugin is working");
			   
					Transformation.GetSocialFilter().GroupFilter(numberGroup,currentSocialAnalysist,Transformation.GetSocialTransformation());			    
					publish(20);
			        int value=20;
		            int part= Transformation.GetData().GetBaseLog().size()/16;
		            int sum_part=20;
		         
			          for (int u=0; u<Transformation.GetData().GetBaseLog().size(); u++)
					  {	        	  
			    		  XTrace trace= new XTraceImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class));
			    		  Transformation.GetSocialFilter().SearchTrace(trace,Transformation.GetData(),u);
                		  if(u==sum_part)
        		          {
        			      value+=5;
        		      	  publish(value);
        		    	  sum_part+=part;
        		          }

					  } 
				Transformation.SetSocialGraph(currentSocialAnalysist,MainPanel.GetParametersPanel().GetSocialParametersPanel().GetSliderValue());
		         Transformation.UpdateGraphWithSocialChanges(currentSocialAnalysist);

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
		           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());
			    } catch (ExecutionException e) {
			           frame.dispose();
			           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());

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
	 
	 public void AddGroupSwingWorker(final OLAPDiscoveryPanel MainPanel,final String numberGroup,final String currentSocialAnalysist,final OLAPTransformation Transformation)
	 {
			
		    
			SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
				
				@Override
				protected Boolean doInBackground() throws InterruptedException {
			    	
					 ShowAdviceFrame("Adding group","The plugin is working");
				   
						Transformation.GetSocialFilter().RestoreGroup(numberGroup,currentSocialAnalysist, Transformation.GetSocialTransformation());			    
						publish(20);
				        int value=20;
			            int part= Transformation.GetData().GetBaseLog().size()/16;
			            int sum_part=20;
			         
				          for (int u=0; u<Transformation.GetData().GetBaseLog().size(); u++)
						  {	        	  
				    		  XTrace trace= new XTraceImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class));
				    		  Transformation.GetSocialFilter().SearchTrace(trace,Transformation.GetData(),u);
	                		  if(u==sum_part)
	        		          {
	        			      value+=5;
	        		      	  publish(value);
	        		    	  sum_part+=part;
	        		          }

						  } 
				     MainPanel.GetParametersPanel().GetSocialParametersPanel().ReCheckJResources(numberGroup);
			         Transformation.UpdateGraphWithSocialChanges(currentSocialAnalysist);

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
			           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());
				    } catch (ExecutionException e) {
				           frame.dispose();
				           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());

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
	
	 
	 //Remove one resource
	 public void RemoveResource(final OLAPDiscoveryPanel MainPanel,final String Resource,final String currentSocialAnalysist,final OLAPTransformation Transformation)
	 {
		 
			SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
				
				@Override
				protected Boolean doInBackground() throws InterruptedException {
			    	
					 ShowAdviceFrame("Remove Resource","The plugin is working");
					 System.out.print("a filtrar:"+ Resource);

					 Transformation.GetSocialFilter().RemovePerson(Resource,currentSocialAnalysist);	    
						publish(20);
				        int value=20;
			            int part= Transformation.GetData().GetBaseLog().size()/16;
			            int sum_part=20;
			         
				          for (int u=0; u<Transformation.GetData().GetBaseLog().size(); u++)
						  {	        	  
				    		  XTrace trace= new XTraceImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class));
				    		  Transformation.GetSocialFilter().SearchTrace(trace,Transformation.GetData(),u);
	                		  if(u==sum_part)
	        		          {
	        			      value+=5;
	        		      	  publish(value);
	        		    	  sum_part+=part;
	        		          }
						  } 
    				Transformation.SetSocialGraph(currentSocialAnalysist,MainPanel.GetParametersPanel().GetSocialParametersPanel().GetSliderValue());

			         Transformation.UpdateGraphWithSocialChanges(currentSocialAnalysist);

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
					System.out.print("\n hola");
			           frame.dispose();
			           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());
				    } catch (ExecutionException e) {
						System.out.print("\n hola");
				    	frame.dispose();
				           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());

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
	 
	 //Add one resource
	 public void AddResource(final OLAPDiscoveryPanel MainPanel,final String Resource,final String currentSocialAnalysist,final OLAPTransformation Transformation)
	 {
		 
			SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
				
				@Override
				protected Boolean doInBackground() throws InterruptedException {
			    	
					 ShowAdviceFrame("Add Resource","The plugin is working");
				   
					    Transformation.GetSocialFilter().AddPerson(Resource,currentSocialAnalysist);	
						publish(20);
				        int value=20;
			            int part= Transformation.GetData().GetBaseLog().size()/16;
			            int sum_part=20;
			         
				          for (int u=0; u<Transformation.GetData().GetBaseLog().size(); u++)
						  {	        	  
				    		  XTrace trace= new XTraceImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class));
				    		  Transformation.GetSocialFilter().SearchTrace(trace,Transformation.GetData(),u);
	                		  if(u==sum_part)
	        		          {
	        			      value+=5;
	        		      	  publish(value);
	        		    	  sum_part+=part;
	        		          }
						  } 
					Transformation.SetSocialGraph(currentSocialAnalysist,MainPanel.GetParametersPanel().GetSocialParametersPanel().GetSliderValue());

			         Transformation.UpdateGraphWithSocialChanges(currentSocialAnalysist);

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
			           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());
				    } catch (ExecutionException e) {
				           frame.dispose();
				           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());

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
	 


	 //Add one resource
	 public void LoadTab(final OLAPDiscoveryPanel MainPanel,final OLAPTransformation Transformation)
	 {
		 
			SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
				
				@Override
				protected Boolean doInBackground() throws InterruptedException {
			    	
					 ShowAdviceFrame("Social Tab","The plugin is working");
					 MainPanel.GetParametersPanel().GetSocialParametersPanel().GetJCheckBoxGroups().clear();
					 publish(10);
					 MainPanel.GetParametersPanel().GetSocialParametersPanel().GetJLabelGroups().clear();
					 publish(15);
					 MainPanel.GetParametersPanel().GetSocialParametersPanel().GetMapJCheckBoxResources().clear();
					 publish(20);
					 MainPanel.GetParametersPanel().GetSocialParametersPanel().GetJLabelResources().clear();		           
					 publish(25);
					 MainPanel.GetParametersPanel().GetSocialParametersPanel().GetResourceModelEvents().clear();
					 publish(30);

					 //    Transformation.ShowSocialData();
					 Transformation.SetSocialTransformation(new OLAPSocialTransformation(Transformation.GetData()));
					 MainPanel.ResetAuxiliarSocialNet();	 
					 publish(35);  
					 MainPanel.GetParametersPanel().GetSocialParametersPanel().ResetPanel();
					 publish(40);
					 MainPanel.GetParametersPanel().GetSocialParametersPanel().CreateResourceCheckBoxes();
					 publish(45);
					 MainPanel.GetParametersPanel().GetSocialParametersPanel().repaint();
					 publish(55);
					 MainPanel.GetParametersPanel().GetTabPane().repaint();
					 MainPanel.AddSocialCheckEvents();
					 publish(65);
					 Transformation.SetXLogBackUpUnit();
					 MainPanel.GetParametersPanel().GetSocialParametersPanel().GetOptions().getItemAt(0);
					 publish(85);
					 MainPanel.UpdateSNPanel();//="Working Together");
					 publish(90);
		            
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
			           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());
				    } catch (ExecutionException e) {
				           frame.dispose();
				           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());

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
	 

     public void LoadOption(final OLAPDiscoveryPanel MainPanel,final OLAPTransformation Transformation)
	 {
				 
					SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
						
						@Override
						protected Boolean doInBackground() throws InterruptedException {
					    	
							 ShowAdviceFrame("Social Option","The plugin is working");
							 publish(10);
							 JComboBox combo= MainPanel.GetParametersPanel().GetSocialParametersPanel().GetOptions();
							 publish(20);
							 Transformation.GetSocialFilter().Reset();
							 publish(30);
							 Transformation.SetSocialTransformation( new OLAPSocialTransformation(Transformation.GetData()));
							 publish(40);
							 MainPanel.SetCurrentSocialAnalysis(combo.getSelectedItem().toString());
							 publish(50);
							 MainPanel.ResetAuxiliarSocialNet();
							 publish(60);
							 MainPanel.GetParametersPanel().GetSocialParametersPanel().ResetPanel();
								MainPanel.GetParametersPanel().GetSocialParametersPanel().GetMapJCheckBoxResources().clear();
								MainPanel.GetParametersPanel().GetSocialParametersPanel().CreateResourceCheckBoxes();
								MainPanel.GetParametersPanel().GetSocialParametersPanel().repaint();
								 publish(70);

							
							 return true;
				        }	     
				     
						protected void done() {
						    
						try {
							
				             boolean status= get();
							if(status)
							{
								MainPanel.AddSocialCheckEvents();
								MainPanel.BuiltGraph();
					           frame.dispose();
					         
					         //frame.dispose();
							}
						} catch (InterruptedException e) {
					           frame.dispose();
					           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());
						    } catch (ExecutionException e) {
						           frame.dispose();
						           MainPanel.GetParametersPanel().add(MainPanel.GetLogWarning());

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
}
