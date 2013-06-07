package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.swingworkers;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTransformation;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.OLAPDiscoveryPanel;

/**
 *  Time parameters container
 *  @author Gustavo Pizarro
*/
public class ClusterSwingWorker {
	private JFrame frame;
	private JLabel percentage;

	public ClusterSwingWorker() {
		// TODO Auto-generated constructor stub

	}
	 public void  RemoveClusterFilteringSwingWorker(final String num, final OLAPTransformation Transformation, final OLAPDiscoveryPanel MainPanel)
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				 ShowAdviceFrame("Cluster Filter","The plugin is working");
			   
	             Transformation.GetClusterFilter().RemoveClusterAndFilter(num);
	             publish(20);
	             int part=Transformation.GetData().GetWorkingLog().size()/16;
	             int sum_part=part;
		         Transformation.GetClusterFilter().GroupClusters();
		         int value=20;
	             for (int u=0; u<Transformation.GetData().GetWorkingLog().size(); u++)
			     {
		    	  Transformation.GetClusterFilter().SearchTrace(Transformation.GetData().GetBaseLog().get(u));
		    	  if(u==sum_part)
		          {
			      value+=5;
		      	  publish(value);
		    	  sum_part+=part;
		          }
		    	  
			     }         
	             Transformation. UpdateForClusters();

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

	    
	    public void  RestoreClusterFilteringSwingWorker(final String num, final OLAPTransformation Transformation, final OLAPDiscoveryPanel MainPanel)
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				 ShowAdviceFrame("Group Filter","The plugin is working");
				 Transformation.GetData().ReturnToBaseLog();
				 Transformation.GetClusterFilter().RestoreCluster(num);
	             //Transformation. RemoveTracesFromClusters(); 
	             publish(20);
	             int part=Transformation.GetData().GetWorkingLog().size()/16;
	             int sum_part=part;
		         Transformation.GetClusterFilter().GroupClusters();
		         int value=20;
	             for (int u=0; u<Transformation.GetData().GetWorkingLog().size(); u++)
			     {
		    	  Transformation.GetClusterFilter().SearchTrace(Transformation.GetData().GetBaseLog().get(u));
		    	  if(u==sum_part)
		          {
		    	  value+=5;
		      	  publish(value);
		    	  sum_part+=part;
		          }
		    	  
			     }              
	             Transformation. UpdateForClusters();
	             MainPanel.GetParametersPanel().GetClustersParametersPanel().ReCheckVariants(num);
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
       			 MainPanel.GetParametersPanel().GetClustersParametersPanel().removeAll();
	               MainPanel.GetParametersPanel().GetClustersParametersPanel().add(MainPanel.GetLogWarning());

			    } catch (ExecutionException e) {
					frame.dispose();
	       			 MainPanel.GetParametersPanel().GetClustersParametersPanel().removeAll();
		               MainPanel.GetParametersPanel().GetClustersParametersPanel().add(MainPanel.GetLogWarning());

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
	    
	    public void  LoadClusterSwingWorker(final int numberOfCluster, final OLAPDiscoveryPanel Panel, final OLAPTransformation Transformation)
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
				  ShowAdviceFrame("Cluster calculation",
			  "<html><br>This plugin is working...</html>");
				  	Panel.GetParametersPanel().GetClustersParametersPanel().CleanPanels();
					Transformation.GetClusterTransformation().GetClusterData().SetNumberOfClusters(numberOfCluster);

				   publish(1);
				   Transformation.GetClusterTransformation().Step1();
			       publish(40);
			       Transformation.GetClusterTransformation().Step2();
			       publish(50);
			       Transformation.GetClusterTransformation().Step3();
			       publish(60);
			       Transformation.GetClusterTransformation().Step4();
			       publish(85);
			       Transformation.GetClusterTransformation().Step5();
			       publish(90);

					//Thread.sleep(1000);
					publish(0);
				    publish(100);

					     return true;
	        }	     
	     
			protected void done() {
			    
			try {
				
	             boolean status= get();
				if(status)
				{
				// Retrieve the return value of doInBackground.
					   Panel.GetParametersPanel().GetClustersParametersPanel(). AddHeader();
					   Panel.GetParametersPanel().GetClustersParametersPanel().CreateClustersOptions(numberOfCluster);
					   Panel.AddClusterCheckEvents();		
						//AddClusterCheckEvents();
					  // MainPanel.AddSubClusterEvents();
			
					   
					  
					   Panel.AddEventsAfterChangeNumberOfClusters();
						
					   //AddCheckBoxesClusterEvents();
					   Panel.repaint();
				   
				   
					   Panel.GetParametersPanel().GetClustersParametersPanel().repaint();
					   Panel.GetParametersPanel().GetTabPane().repaint();
					   Panel.GetParametersPanel().repaint();
					   Panel.revalidate();
					   Panel.repaint();
					   Panel.VariantsAndClusterEvents(numberOfCluster);
		               frame.dispose();
		         
		         //frame.dispose();
				}
			} catch (InterruptedException e) {
		           frame.dispose();
					Panel.GetParametersPanel().GetClustersParametersPanel().add(Panel.GetLogWarning());
} catch (ExecutionException e) {
			     // This is thrown if we throw an exception
			     // from doInBackground.
					frame.dispose();

	               Panel.GetParametersPanel().GetClustersParametersPanel().add(Panel.GetLogWarning());


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
	  
         public void  RemoveSubClusterFilteringSwingWorker(final int cluster, final int trace_ini,final int trace_last,final OLAPTransformation Transformation, final OLAPDiscoveryPanel MainPanel)
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				for(int j=0;j<50; j++)
				publish(j);
			  
		    		Transformation.GetClusterFilter().RemoveTracesFromSubCluster(cluster,trace_ini,trace_last);
		    	
		    	for(int j=0;j<100; j++)
				publish(j);
				

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

			     // This is thrown if the thread's interrupted.
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

	    public void  AddSubClusterFilteringSwingWorker(final int cluster, final int trace_ini,final int trace_last,final OLAPTransformation Transformation, final OLAPDiscoveryPanel MainPanel)
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
			
					Transformation.GetData().ReturnToBaseLog();
		    		Transformation.GetClusterFilter().RestoreSubCluster(cluster,trace_ini,trace_last);
		    	
		    	
		    	

					//Thread.sleep(1000);
					publish(0);
				    publish(100);

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
			    
				   int mostRecentValue = chunks.get(chunks.size()-1);

				    percentage.setText(mostRecentValue+"%");
					   
					
			   }
			   
		};
		worker.execute();
	    }
	    
	    
	    public void  RemoveVariantFilteringSwingWorker(final int trace,final int cluster,final OLAPTransformation Transformation, final OLAPDiscoveryPanel MainPanel)
	    {
	    	
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				 ShowAdviceFrame("Variant Filter","The plugin is working");
			   
		    	    publish(10);
					Transformation.GetClusterFilter().RemoveTraceAndFilter(trace,cluster);
		    	    publish(20);

					int value=25;

                    int part=Transformation.GetData().GetCurrentLog().size()/16;
                    int sum=part;
				    for (int u=0; u<Transformation.GetData().GetCurrentLog().size(); u++)
					{
				 	       XTrace trace = Transformation.GetData().GetCurrentLog().get(u);   
				 	      Transformation.GetClusterFilter().SearchAndRemoveTrace(trace);
				 	      if(u==sum)
				          {
				    	  publish(value);
				    	  value+=5;
				    	  sum+=part;
				          }
					}
					Transformation.UpdateForClusters();
				    
	            
		          
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
	    

	    public void  RestoreVariantFilteringSwingWorker(final int trace,final int cluster,final OLAPTransformation Transformation, final OLAPDiscoveryPanel MainPanel)
	    {
	    	
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				    ShowAdviceFrame("Restore Variant ","The plugin is working");
			   
				    Transformation.GetData().ReturnToBaseLog();
		    	    publish(10);
					Transformation.GetClusterFilter().RestoreCase(trace,cluster);
		    	    publish(20);

					int value=25;

                    int part=Transformation.GetData().GetCurrentLog().size()/8;
                    int sum=part;
				    for (int u=0; u<Transformation.GetData().GetCurrentLog().size(); u++)
					{
				 	       XTrace trace = Transformation.GetData().GetCurrentLog().get(u);   
				 	      Transformation.GetClusterFilter().SearchAndRemoveTrace(trace);
				 	      if(u==sum)
				          {
				    	  publish(value);
				    	  value+=5;
				    	  sum+=part;
				          }
					}
					Transformation.UpdateForClusters();
				    
	            
		          
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
			     
				   int mostRecentValue = chunks.get(chunks.size()-1);

				    percentage.setText(mostRecentValue+"%");
					   
					
			   }
			   
		};
		worker.execute();
	    }

	    

}
