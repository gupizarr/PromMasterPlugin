package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.parameters;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data.ClusterData;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPClusterTransformation;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTransformation;

import com.fluxicon.slickerbox.ui.SlickerCheckBoxUI;

/**
 *  Cluster Parameters container.
 *  @author Gustavo Pizarro
*/

public class ClusterParameters  extends JPanel{


	private static final long serialVersionUID = 1L;
	protected ArrayList<JCheckBox> JCheckClusterList;
	protected Map<String,ArrayList<JLabel>> VariantsGroupConcurrencyActiveBox;
	protected Color COLOR_BG = new Color(60, 60, 60);
	protected Color COLOR_BG2 = new Color(120, 120, 120);
	protected Color COLOR_FG = new Color(30, 30, 30);
	protected Font smallFont;
	protected 		JPanel concurrencySliderPanel;
	protected JPanel[] option_cluster_panels;
    protected JPanel OptionPanel;
	protected Map<String,JCheckBox> ClustersCasesCheckBoxes;
	private OLAPTransformation DataTransformation;
	protected JComboBox numberOfClusters;
	protected JPanel checkClusterContainer;
    protected ArrayList<JLabel> JLabelClusterArray;
    protected Map<String,ArrayList<JLabel>> JLabelGroupsOfCluster;
    protected boolean big= true;
    protected JFrame frame;
    protected JPanel optionSubclusterCases;
    protected JPanel  ClusterMenuHeader;
    protected JLabel porcentaje;
    protected JLabel title;
    protected Map<String,MouseListener>  CaseEvents;
    protected ArrayList<JLabel> detailView;
    
    public ClusterParameters(OLAPTransformation Transformation) {
		// TODO Auto-generated constructor stub
    	ClustersCasesCheckBoxes=new HashMap<String,JCheckBox>();
		this.DataTransformation=Transformation;
		CaseEvents= new HashMap<String,MouseListener>();
		this.smallFont = new Font("11f", 12, 10);
		 JCheckClusterList= new ArrayList<JCheckBox>();
		 VariantsGroupConcurrencyActiveBox= new HashMap<String,ArrayList<JLabel>>();
		 detailView= new ArrayList<JLabel>();
         JLabelClusterArray=new ArrayList();
         JLabelGroupsOfCluster= new HashMap<String,ArrayList<JLabel>>();
 		
         this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
 		
         this.setBackground(COLOR_BG2);
 		
         this.setOpaque(true);
         this.setLayout(new BorderLayout());
 		 title= new JLabel("Select Number of clusters");
         AddHeader();
         CreateClustersOptions(3);
		 repaint();
	}
	
	
	public String LogData()
	{
		int cases=0;
		for(int j=0;j<DataTransformation.GetClusterTransformation().GetClusterData().GetActivityClusters().size();j++)
			cases+=DataTransformation.GetClusterTransformation().GetClusterData().GetActivityClusters().get(j).size();
		
		if(big)
		return "<html>Traces: "+DataTransformation.GetData().GetCurrentLog().size()+
				", Variants: "+cases+"<br></html>";		
		else
			return "<html> Traces: "+DataTransformation.GetData().GetCurrentLog().size()+"<br> Variants: "+cases+"<br>+</html>";
			
	}
	
	public void CleanPanels()
	{
		 CaseEvents= new HashMap<String, MouseListener>();
		 JCheckClusterList= new ArrayList<JCheckBox>();
		 VariantsGroupConcurrencyActiveBox= new HashMap<String,ArrayList<JLabel>>();
         JLabelClusterArray=new ArrayList();
         JLabelGroupsOfCluster= new HashMap<String,ArrayList<JLabel>>();
         this.remove(concurrencySliderPanel);
        
         checkClusterContainer.removeAll();
 		 ClusterMenuHeader.removeAll();
 		 
 		 checkClusterContainer.removeAll();
 		 checkClusterContainer.repaint();
 	 	 //concurrencySliderPanel.remove(checkClusterContainer);
 		 
 	 	 OptionPanel.removeAll();
 		 OptionPanel.repaint();
 		 //concurrencySliderPanel.remove(OptionPanel);
 		 
 		 optionSubclusterCases.removeAll();
 		 optionSubclusterCases.repaint();
 		 //concurrencySliderPanel.remove(optionSubclusterCases);
 		 
 		 concurrencySliderPanel.removeAll();
 		 concurrencySliderPanel.repaint();
	
	}
	
	public void ResetPanel(int CurrentNumberOfClusters)
	{
		// detailView.clear();
		 CleanPanels();
         AddHeader();
         CreateClustersOptions(CurrentNumberOfClusters);
		 repaint();		
	}
	
	public void AddHeader()
	{
		// concurrency edge transformerñ slider panel
		concurrencySliderPanel = new JPanel();
		concurrencySliderPanel.setOpaque(false);
		concurrencySliderPanel.setLayout(null);//new BoxLayout(concurrencySliderPanel, BoxLayout.PAGE_AXIS));	
		// concurrency edge preserve threshold slider panel
		JPanel concurrencyPreservePanel = new JPanel();
		concurrencyPreservePanel.setOpaque(false);
		concurrencyPreservePanel.setLayout(new BorderLayout());
		// setup concurrency parent panel

	    //Checkbox de grupos humanos
		ClusterMenuHeader = new JPanel();   
		checkClusterContainer= new JPanel();
		checkClusterContainer.setOpaque(false);
		JLabel data= new JLabel(LogData());
		ClusterMenuHeader.add(data);
		//Label Number of Clusters
		ClusterMenuHeader.add(title);
	}
	
	public void CreateClustersOptions(int CurrentNumberOfClusters)
	{

		//Numbers of clusters
		numberOfClusters = new JComboBox();
		numberOfClusters.setPreferredSize(new Dimension(40, 25));
		numberOfClusters.setMaximumSize(new Dimension(40,25));
		for(int j=1;j<=DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCase();j++ )
		{
			JCheckClusterList.add(new JCheckBox("Cluster "+j));
			numberOfClusters.addItem(j);
		}
		
		numberOfClusters.setSelectedIndex(CurrentNumberOfClusters-1);   
		
		ClusterMenuHeader.add(numberOfClusters);
		
		AddClustersParameters(true,CurrentNumberOfClusters);
		//ClusterMenuHeader.setLayout(new BoxLayout(ClusterMenuHeader, BoxLayout.Y_AXIS));
		ClusterMenuHeader.setOpaque(false);
		ClusterMenuHeader.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 10));
		ClusterMenuHeader.add(Box.createVerticalGlue());   		
		ClusterMenuHeader.setPreferredSize(new Dimension(150,70));
		this.add(ClusterMenuHeader, BorderLayout.NORTH);
		this.add(concurrencySliderPanel, BorderLayout.CENTER);
		buildComponent(110,530,checkClusterContainer,concurrencySliderPanel,0);


	}
/*	
	public void ResetClustersParameters(int numberOfCheckBoxes)
	{
		//Numbers of clusters
		numberOfClusters = new JComboBox();
		numberOfClusters.setPreferredSize(new Dimension(40, 25));
		numberOfClusters.setMaximumSize(new Dimension(40,25));
		for(int j=1;j<=DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCase();j++ )
		{
			JCheckClusterList.add(new JCheckBox("Cluster "+j));
			numberOfClusters.addItem(j);
		}
		numberOfClusters.setSelectedIndex(2);   
		
		ClusterMenuHeader.add(numberOfClusters);
		
		AddClustersParameters(true,numberOfCheckBoxes);
		//ClusterMenuHeader.setLayout(new BoxLayout(ClusterMenuHeader, BoxLayout.Y_AXIS));
		ClusterMenuHeader.setOpaque(false);
		ClusterMenuHeader.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 10));
		ClusterMenuHeader.add(Box.createVerticalGlue());   		
		ClusterMenuHeader.setPreferredSize(new Dimension(150,70));
		this.add(ClusterMenuHeader, BorderLayout.NORTH);
		this.add(concurrencySliderPanel, BorderLayout.CENTER);
		buildComponent(110,530,checkClusterContainer,concurrencySliderPanel,0);

	}
	*/
	public void LoadViewAndCleanBefore(int numberOfCheckBoxes)
	{
		OptionPanel.removeAll();
		OptionPanel.repaint();
		
		checkClusterContainer.removeAll();
		checkClusterContainer.repaint();
		
		optionSubclusterCases.removeAll();
		optionSubclusterCases.repaint();
	
		checkClusterContainer.removeAll();
		DataTransformation.GetClusterTransformation().SetClusterData(new ClusterData(DataTransformation.GetData().GetCurrentLog(),numberOfCheckBoxes));		
		DataTransformation.SetClusterTransformation(new OLAPClusterTransformation(new ClusterData(DataTransformation.GetData().GetCurrentLog(),numberOfCheckBoxes)));		
		checkClusterContainer.repaint();
	}
	
	public void InitializePanels()
	{
		option_cluster_panels= new JPanel[DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCase()];
	    OptionPanel= new JPanel();			    
	    OptionPanel.setOpaque(false);
		buildComponent(110,530,OptionPanel,concurrencySliderPanel,105);
		optionSubclusterCases= new JPanel();			    
		optionSubclusterCases.setSize(40, 40);
		optionSubclusterCases.setBounds(0, 0, 50, 50);
		optionSubclusterCases.setForeground(COLOR_FG);
	    optionSubclusterCases.setOpaque(false);
		buildComponent(95,530,optionSubclusterCases,concurrencySliderPanel,220);
	}
	public void AddClustersParameters(boolean firstTime, int numberOfCheckBoxes)
	{


		if(!firstTime)
		{
			RestoreSelects();
			//numberOfCheckBoxes=Integer.parseInt(numberOfClusters.getSelectedItem().toString());
			LoadViewAndCleanBefore(numberOfCheckBoxes);
			//InitializePanels();
			//InicializeVariants(numberOfCheckBoxes);
		}			
		//else
		//{
			InitializePanels();
			InicializeVariants(numberOfCheckBoxes);
		//}
		
		this.validate();
		this.repaint();
	}
    
	public void InicializeVariants(int numberOfCheckBoxes)
	{
		System.out.print("\n clusters:"+numberOfCheckBoxes);
		String key="";
		int number;
		  for(int count=0; count<numberOfCheckBoxes;count++) 
			{
				for(int c=0;c<DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(count);c++)
				{
					
					//save the variants
					
						key=count+"-"+c;
					    number=c+1;
						JCheckBox J= new JCheckBox("Variant "+number);
						J.setSelected(true);
						J.setName(key);
						ClustersCasesCheckBoxes.put(key,J);
					    JLabel label= new JLabel("+");
					    label.setName(key);
					    detailView.add(label);
				}
				AddClusterOption(count);
			}
			
	}
	
	public void buildComponent(int width,int height, JPanel panel, JPanel ContainerPanel,int xpost)
	{
		Insets insets = ContainerPanel.getInsets();
		panel.setPreferredSize(new Dimension(width,height));    
	    Dimension size = panel.getPreferredSize();
	    panel.setBounds(insets.left+xpost, 5 + insets.top,
	                          size.width,size.height);
	    
	    ContainerPanel.add(panel);
	}
	
	
	public void InicializeClusterCheckBox(int count)
	{
        int number=count+1;
        JCheckClusterList.get(count).setText("Cluster "+number+" ["+DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(count)+"]");
		JCheckClusterList.get(count).setUI(new SlickerCheckBoxUI());
		JCheckClusterList.get(count).setOpaque(false);
		JCheckClusterList.get(count).setForeground(COLOR_FG);
		JCheckClusterList.get(count).setFont(this.smallFont);
		JCheckClusterList.get(count).setSelected(true);
		JCheckClusterList.get(count).setToolTipText("<html>This control select the clusters of the model" +
				"visualization</html>");
		JCheckClusterList.get(count).setName(""+count);
	}
	
	public void DrillDownAction(JLabel label)
	{
		label.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {
						
				JLabel label= (JLabel) arg0.getComponent();
				int numberCluster= Integer.parseInt(label.getName());
				if(label.getText().equals("+") && JCheckClusterList.get(numberCluster).isSelected())
				{
				
					for(int j=0; j<JLabelClusterArray.size();j++)
					{
						JLabelClusterArray.get(j).setText("+");
					}
					
					label.setText("-");
					OptionPanel.removeAll();							
					OptionPanel.revalidate();
					OptionPanel.repaint();
					optionSubclusterCases.removeAll();
					optionSubclusterCases.revalidate();
					optionSubclusterCases.repaint();
					
					if(DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(numberCluster)<15)
					{
						ShowSimpleCases(numberCluster,OptionPanel,DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(numberCluster),
								       0, DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(numberCluster) );
					}
					else
					{
						ShowComplexCases(numberCluster);
			
					}

				}
				else
				{
					
					label.setText("+");
					for(int j=0;j<JLabelGroupsOfCluster.get(""+numberCluster).size();j++)
						JLabelGroupsOfCluster.get(""+numberCluster).get(j).setText("+");
					
					OptionPanel.removeAll();							
					OptionPanel.revalidate();
					OptionPanel.repaint();
					
					optionSubclusterCases.removeAll();							
					optionSubclusterCases.revalidate();
					optionSubclusterCases.repaint();
					
				}
				
			}
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub				
			}
		});
	
	}
	
	public void DrillDownToBottom(JLabel label,final int caseToShow,final int from,final int to)
	{
					label.addMouseListener(new MouseListener(){
					public void mouseClicked(MouseEvent arg0) {}
					public void mouseEntered(MouseEvent arg0) {}
					public void mouseExited(MouseEvent arg0) {}
					public void mousePressed(MouseEvent arg0) {
						JLabel label= (JLabel) arg0.getComponent();
						int clusterNumber= Integer.parseInt(label.getName().substring(0,label.getName().indexOf("-")));

						if(label.getText().equals("+"))
						{
							for(int j=0; j<JLabelGroupsOfCluster.get(""+clusterNumber).size();j++)
							{
								JLabelGroupsOfCluster.get(""+clusterNumber).get(j).setText("+");
							}

							label.setText("-");			
							optionSubclusterCases.removeAll();							
							optionSubclusterCases.revalidate();
							optionSubclusterCases.repaint();
			        		ShowSimpleCases(clusterNumber,optionSubclusterCases,caseToShow,from,to);			
						}
						else
						{
							label.setText("+");
							optionSubclusterCases.removeAll();							
							optionSubclusterCases.revalidate();
							optionSubclusterCases.repaint();
						}					
					}
					public void mouseReleased(MouseEvent arg0){	}
					
				});
	}
	
	
	public void ShowComplexCases(int numberOfCluster)
	{
		// Get the variants and calculate number of subgroups of variants
		int variants=DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(numberOfCluster);
		int sub_check_boxes =  Math.round(variants/16);
		
		if(variants%16!=0)
			sub_check_boxes++;
		
		
		//clean panel of subgroups
		OptionPanel.removeAll();
		OptionPanel.repaint();
		OptionPanel.setLayout(null);
		
		
		int margin_top=0;		
		for(int c=0;c<sub_check_boxes;c++)		
		{
				JLabel label= JLabelGroupsOfCluster.get(""+numberOfCluster).get(c);				
				JLabel name=VariantsGroupConcurrencyActiveBox.get(""+numberOfCluster).get(c);
		        JPanel cluster_panel= new JPanel();					
				cluster_panel.add(label,BorderLayout.LINE_START);
				cluster_panel.add(name,BorderLayout.PAGE_START);
				cluster_panel.setOpaque(false);
			    Insets insets = OptionPanel.getInsets();
			    margin_top=insets.top+5+24*c;
			    cluster_panel.setPreferredSize(new Dimension(130,24));
			    Dimension size = cluster_panel.getPreferredSize();
			    cluster_panel.setBounds( insets.left, margin_top,
			                 size.width, size.height);
		   	    OptionPanel.add(cluster_panel);
		}
		
		OptionPanel.repaint();	
	}
	
	public void ShowSimpleCases(int clusterNumber, JPanel panel,int casesToShow,int from,int to)
	{
		
		int top=0;
		panel.setLayout(null);
		String key="";
		for(int c=0;c<casesToShow;c++)		
		{
			 	JPanel aux= new JPanel();
			 	aux.setSize(100,30);
			 	aux.setPreferredSize(new Dimension(100,30));
			 	aux.setOpaque(false);
			 	key= clusterNumber+"-"+from;
			 	JCheckBox check=ClustersCasesCheckBoxes.get(key);
				check.setUI(new SlickerCheckBoxUI());
				check.setOpaque(false);
				check.setForeground(COLOR_FG);
				check.setFont(new Font("11f", 12, 10));

				check.setToolTipText("<html>This control select the clusters of the model" +"visualization</html>");
			    JLabel label= GetParticularCaseDetailView(key);
			    aux.add(check);
			    aux.add(label);
			    
				Insets insets = panel.getInsets();
				if(c>0)
				top=insets.top+25*c;
				else
				top=insets.top;
				
			    Dimension size = aux.getPreferredSize();
			    aux.setBounds(insets.left,top ,
			                    size.width,
			                    size.height);
			
			
			    panel.add(aux);
			    
			    from++;
		}
		panel.repaint();	
	}
	
	public boolean LabelExist(String name,int cluster)
	{
		if(JLabelGroupsOfCluster.get(""+cluster)!=null)
		for(int j=0;j<JLabelGroupsOfCluster.get(""+cluster).size();j++)
		{
			if(JLabelGroupsOfCluster.get(""+cluster)!=null)
			{
				if(JLabelGroupsOfCluster.get(""+cluster).get(j).getName().toString().equals(name))
				return true;
			}
		}
		return false;
		
	}
	
	public JLabel GetJLabelGroupsOfCluster(String name, int cluster)
	{
		if(JLabelGroupsOfCluster.get(""+cluster)!=null)
		for(int j=0;j<JLabelGroupsOfCluster.get(""+cluster).size();j++)
			
			if(JLabelGroupsOfCluster.get(""+cluster)!=null)
			if(JLabelGroupsOfCluster.get(""+cluster).get(j).getName().toString().equals(name))
				return JLabelGroupsOfCluster.get(""+cluster).get(j);
		
		return null;
	}
	public void AddClusterOption(int count)
	{
		        InicializeClusterCheckBox(count);
				JLabel label= new JLabel("+");
				JLabelClusterArray.add(label);
				JPanel cluster_panel= new JPanel();
				label.setName(""+count);
				cluster_panel.setForeground(COLOR_FG);
			    cluster_panel.add(label,BorderLayout.LINE_START);
			    cluster_panel.add(JCheckClusterList.get(count),BorderLayout.PAGE_START);
			    cluster_panel.setOpaque(false);
			  			    
			    Insets insets = concurrencySliderPanel.getInsets();
			    cluster_panel.setPreferredSize(new Dimension(110,24));
			    Dimension size = cluster_panel.getPreferredSize();
			    cluster_panel.setBounds( insets.left, 5 + insets.top,
			                 size.width-30, size.height-10);			    
			    checkClusterContainer.add(cluster_panel);
			    
			    DrillDownAction(label);

				if(DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(count)>=15)
				{
					int cases=DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(count);
					int numberOfGroups =  Math.round(cases/16);
					
					if(cases%16!=0)
						numberOfGroups++;
					
					//Agregar la tercera columna
					int number;
					int check_cases=16;
			
					int from=0;
					int to=0;
					ArrayList<JLabel> JLabelList= new ArrayList<JLabel>();
					ArrayList<JLabel> JLabelDetails= new ArrayList<JLabel>();

					for(int c=0;c<numberOfGroups;c++)		
					{
						    
							number=c+1;
							if(!LabelExist(count+"-"+number,count))
							{
								label= new JLabel("+");
								label.setName(count+"-"+number);
								JLabelDetails.add(label);
							}
							else
							label=GetJLabelGroupsOfCluster(count+"-"+number,count);
							
							
							if(c<numberOfGroups-1)
							{
								check_cases=16;
								from=16*(c);
	                            to=16*(1+c);
							}
							else 
							{
								if(cases%16!=0)
								{
								check_cases=cases%16;
								from=16*(c);
	                            to=16*(c)+check_cases;
								}
								else
								{
								check_cases=16;
								from=16*(c);
	                            to=16*(1+c);
								}
							}

							
							JLabel name=new JLabel("Variants "+number+"["+check_cases+"]");
							name.setOpaque(false);
							name.setForeground(COLOR_FG);
							name.setFont(new Font("11f", 12, 10));
							name.setToolTipText("<html>This control select the clusters of the model" +"visualization</html>");			

							name.setName(""+count+"-"+c);
							name.setForeground(COLOR_FG);	
							JLabelList.add(name);

						    DrillDownToBottom(label,check_cases,from,to-1);

					}
					VariantsGroupConcurrencyActiveBox.put(""+count,JLabelList);
					JLabelGroupsOfCluster.put(""+count,JLabelDetails);

				}
				

	}

	public void RestoreSelects()
	{
		
		ClustersCasesCheckBoxes.clear();
		VariantsGroupConcurrencyActiveBox.clear();	
		JLabelGroupsOfCluster.clear();
		JLabelClusterArray.clear();
		
	}
	
	public Map<String,JCheckBox> GetClustersCasesCheckBoxes()
	{
		return ClustersCasesCheckBoxes;
	}
	
  
	public Map<String,MouseListener>  GetCaseEvents()
	{
		return   CaseEvents;

	}
	
	public  ArrayList<JLabel> GetJLabelClusterArray()
	{
		return JLabelClusterArray;
	}
	
	public  Map<String,ArrayList<JLabel>> JLabelGroupsOfCluster()
	{
		return JLabelGroupsOfCluster;
	}
	
	public ArrayList<JCheckBox> GetJCheckClusterList()
	{
		return JCheckClusterList;
	}
	 
	public JPanel GetOptionPanel()
	{
		return OptionPanel;
	}
	
	public JComboBox GetNumberOfClusters()
	{
		return numberOfClusters;
	}
	
	public JLabel GetParticularCaseDetailView(String name)
	{

		for(int j=0;j<detailView.size();j++)
		{
			if(detailView.get(j).getName().equals(name))
			{
				return detailView.get(j);
			}
		}
		return null;
	}
	
	public void CloseDetailsView()
	{
		for(int j=0;j<detailView.size();j++)
		{
			detailView.get(j).setText("+");
		}		
	}
	public boolean IsOpenDetailsView()
	{
		for(int j=0;j<detailView.size();j++)
		{
			if(detailView.get(j).getText().equals("-"))
			{
				return true;
			}
		}		
		return false;
	}
	public void CloseGroupLabels(String cluster)
	{
		for(int j=0;j<JLabelGroupsOfCluster.get(cluster).size();j++)
		{
			JLabelGroupsOfCluster.get(""+cluster).get(j).setText("+");
		}		
	}
	
	public void ReCheckVariants(String cluster)
	{
		
	    Iterator it = ClustersCasesCheckBoxes.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        	ClustersCasesCheckBoxes.get(pairs.getKey()).setSelected(true);
	        	// avoids a ConcurrentModificationException
	    }
	}
}
