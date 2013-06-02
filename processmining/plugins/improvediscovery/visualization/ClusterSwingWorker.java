package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.OLAPTransformation;

public class ClusterSwingWorker {
	private JFrame frame;
	private JLabel percentage;
	private OLAPTransformation Transformation;
	private OLAPDiscoveryPanel MainPanel;
	public ClusterSwingWorker(OLAPTransformation Transformation, OLAPDiscoveryPanel MainPanel) {
		// TODO Auto-generated constructor stub
		this.Transformation=Transformation;
		this.MainPanel=MainPanel;
	}
	 public void  RemoveClusterFilteringSwingWorker(final String num)
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				 ShowAdviceFrame("Aggrupation Filter","The plugin is working");
			   
	             Transformation.RemoveClusterAndFilter(num);
	             publish(10);
	             //Transformation. RemoveTracesFromClusters(); 
	             publish(20);
	             int part=Transformation.GetData().GetBaseLog().size()/8;
		         Transformation.GroupClusters();
		         int value=20;
	             for (int u=0; u<Transformation.GetData().GetBaseLog().size(); u++)
			     {
		    	  Transformation.SearchTrace(Transformation.GetData().GetBaseLog().get(u));
		    	  if(u==part)
		          {
		    	  publish(value+10);
		    	  value+=10;
		    	  part=part+part;
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
			     // This is thrown if the thread's interrupted.
			    } catch (ExecutionException e) {
			     // This is thrown if we throw an exception
			     // from doInBackground.
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

	    
	    public void  RestoreClusterFilteringSwingWorker(final String num)
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				 ShowAdviceFrame("Aggrupation Filter","The plugin is working");
				 Transformation.RestoreCluster(num);
	             //Transformation. RemoveTracesFromClusters(); 
	             publish(20);
	             //int part=Transformation.GetClusterTransformation().GetClusterData().GetActivityClusters().get(Integer.parseInt(num)).size()/8;
		         int part=Transformation.GetData().GetBaseLog().size()/8;
	             Transformation.GroupClusters();
		         int value=20;
	             for (int u=0; u<Transformation.GetData().GetBaseLog().size(); u++)
			     {
	            	 
		    	  Transformation.SearchTrace(Transformation.GetData().GetBaseLog().get(u));
		    	  
		    	  if(u==part)
		          {
		    	  publish(value+10);
		    	  value+=10;
		    	  part=part+part;
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
	               MainPanel.ParametersPanel.mainClusterParameters.add(MainPanel.GetLogWarning());

			    } catch (ExecutionException e) {
					frame.dispose();
		               MainPanel.ParametersPanel.mainClusterParameters.add(MainPanel.GetLogWarning());

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
			title.setFont(new Font("15f", 15, 20));

			JLabel message=new JLabel("Wait Please");
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
	    
	    public void  LoadClusterSwingWorker()
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
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
					   MainPanel.ParametersPanel.mainClusterParameters. AddHeader();
					   MainPanel.ParametersPanel.mainClusterParameters.ClustersParameters();
					   MainPanel.AddClusterCheckEvents();		
						//AddClusterCheckEvents();
					   MainPanel.AddSubClusterEvents();
					   MainPanel.AddLabelsEvent();
					   MainPanel.AddCheckBoxesClusterEvents();
						
					   //AddCheckBoxesClusterEvents();
					   MainPanel.repaint();
				   
				   
					   MainPanel.ParametersPanel.mainClusterParameters.repaint();
					   MainPanel.ParametersPanel.tabPane.repaint();
					   MainPanel.ParametersPanel.repaint();
					   MainPanel.revalidate();
					   MainPanel.repaint();
		           frame.dispose();
		         
		         //frame.dispose();
				}
			} catch (InterruptedException e) {
		           frame.dispose();
					MainPanel.ParametersPanel.mainClusterParameters.add(MainPanel.GetLogWarning());
} catch (ExecutionException e) {
			     // This is thrown if we throw an exception
			     // from doInBackground.
					frame.dispose();

	               MainPanel.ParametersPanel.mainClusterParameters.add(MainPanel.GetLogWarning());


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
	  
         public void  RemoveSubClusterFilteringSwingWorker(final int cluster, final int trace_ini,final int trace_last)
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				for(int j=0;j<50; j++)
				publish(j);
			  
		    		Transformation.RemoveTracesFromSubCluster(cluster,trace_ini,trace_last);
		    	
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
			     // This is thrown if the thread's interrupted.
			    } catch (ExecutionException e) {
			     // This is thrown if we throw an exception
			     // from doInBackground.
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

	    public void  AddSubClusterFilteringSwingWorker(final int cluster, final int trace_ini,final int trace_last)
	    {
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
			
		    		Transformation.RestoreSubCluster(cluster,trace_ini,trace_last);
		    	
		    	
		    	

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
			     // This is thrown if the thread's interrupted.
			    } catch (ExecutionException e) {
			     // This is thrown if we throw an exception
			     // from doInBackground.
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
	    
	    
	    public void  RemoveCaseFilteringSwingWorker(final int trace,final int cluster)
	    {
	    	
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				 ShowAdviceFrame("Case Filter","The plugin is working");
			   
		    	    publish(10);
					Transformation.RemoveTraceAndFilter(trace,cluster);
		    	    publish(20);

					int value=20;

                    int part=Transformation.GetData().GetCurrentLog().size()/8;

				    for (int u=0; u<Transformation.GetData().GetCurrentLog().size(); u++)
					{
				 	       XTrace trace = Transformation.GetData().GetCurrentLog().get(u);   
				 	      Transformation.SearchAndRemoveTrace(trace);
				 	      if(u==part)
				          {
				    	  publish(value+10);
				    	  value+=10;
				    	  part=part+part;
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
			     // This is thrown if the thread's interrupted.
			    } catch (ExecutionException e) {
			     // This is thrown if we throw an exception
			     // from doInBackground.
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
	    

	    public void  RestoreCaseFilteringSwingWorker(final int trace,final int cluster)
	    {
	    	
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				    ShowAdviceFrame("Restore Case ","The plugin is working");
			   
		    	    publish(10);
					Transformation.RestoreCase(trace,cluster);
		    	    publish(20);

					int value=20;

                    int part=Transformation.GetData().GetCurrentLog().size()/8;

				    for (int u=0; u<Transformation.GetData().GetCurrentLog().size(); u++)
					{
				 	       XTrace trace = Transformation.GetData().GetCurrentLog().get(u);   
				 	      Transformation.SearchAndRemoveTrace(trace);
				 	      if(u==part)
				          {
				    	  publish(value+10);
				    	  value+=10;
				    	  part=part+part;
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
			     // This is thrown if the thread's interrupted.
			    } catch (ExecutionException e) {
			     // This is thrown if we throw an exception
			     // from doInBackground.
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

	    
	    
	    public void  RemoveCaseFilterFromSubClusteringSwingWorker(final int cluster,final int  trace_ini, final int trace_last)
	    {
	    	
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				 ShowAdviceFrame("Case Filter","The plugin is working");
			   
		    	    publish(10);
					Transformation.RemoveTracesFromSubCluster(cluster, trace_ini,trace_last);
		    	    publish(20);

					int value=20;

                    int part=Transformation.GetData().GetCurrentLog().size()/8;

				    for (int u=0; u<Transformation.GetData().GetCurrentLog().size(); u++)
					{
				 	       XTrace trace = Transformation.GetData().GetCurrentLog().get(u);   
				 	      Transformation.SearchAndRemoveTrace(trace);
				 	      if(u==part)
				          {
				    	  publish(value+10);
				    	  value+=10;
				    	  part=part+part;
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
			     // This is thrown if the thread's interrupted.
			    } catch (ExecutionException e) {
			     // This is thrown if we throw an exception
			     // from doInBackground.
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
	    

	    public void  RestoreCaseFromSubClusterFilteringSwingWorker(final int cluster,final int  trace_ini,final int trace_last)
	    {
	    	
	    	
	    
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			
			@Override
			protected Boolean doInBackground() throws InterruptedException {
		    	
				    ShowAdviceFrame("Restore Case ","The plugin is working");
			   
		    	    publish(10);
					Transformation.RestoreSubCluster(cluster,trace_ini,trace_last);
		    	    publish(20);

					int value=20;

                    int part=Transformation.GetData().GetCurrentLog().size()/8;

				    for (int u=0; u<Transformation.GetData().GetCurrentLog().size(); u++)
					{
				 	       XTrace trace = Transformation.GetData().GetCurrentLog().get(u);   
				 	      Transformation.SearchAndRemoveTrace(trace);
				 	      if(u==part)
				          {
				    	  publish(value+10);
				    	  value+=10;
				    	  part=part+part;
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
			     // This is thrown if the thread's interrupted.
			    } catch (ExecutionException e) {
			     // This is thrown if we throw an exception
			     // from doInBackground.
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
