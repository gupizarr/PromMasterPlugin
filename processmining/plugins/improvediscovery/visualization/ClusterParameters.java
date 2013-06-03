package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ClusterData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ClusterTransformation;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.OLAPTransformation;

import com.fluxicon.slickerbox.ui.SlickerCheckBoxUI;

public class ClusterParameters  extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ArrayList<JCheckBox> ClusteredgesConcurrencyActiveBox;
	protected ArrayList<JLabel> SubClusteredgesConcurrencyActiveBox;
	protected Color COLOR_BG = new Color(60, 60, 60);
	protected Color COLOR_BG2 = new Color(120, 120, 120);
	protected Color COLOR_FG = new Color(30, 30, 30);
	protected Font smallFont;
	protected 		JPanel concurrencySliderPanel;
	protected JPanel[] option_cluster_panels;
    protected JPanel option_panel;
	protected Map<String,JCheckBox> ClustersCasesCheckBoxes=new HashMap<String,JCheckBox>();
	private OLAPTransformation DataTransformation;
	protected JComboBox numberOfClusters;
	protected JPanel checkClusterContainer;
    protected ArrayList<JLabel> JLabelClusterArray;
    protected ArrayList<JLabel> JLabelSubClusterArray;
    protected boolean big= true;
    protected JFrame frame;
    protected JPanel optionSubclusterCases;
    protected JPanel  ClusterMenuHeader;
    protected JLabel porcentaje;
    protected JLabel title;
    protected Map<String,MouseListener>  CaseEvents;
    
    public ClusterParameters(OLAPTransformation Transformation) {
		// TODO Auto-generated constructor stub
		this.DataTransformation=Transformation;
		CaseEvents= new HashMap<String,MouseListener>();
		this.smallFont = new Font("11f", 12, 10);
		 ClusteredgesConcurrencyActiveBox= new ArrayList<JCheckBox>();
		 SubClusteredgesConcurrencyActiveBox= new ArrayList<JLabel>();
         JLabelClusterArray=new ArrayList();
         JLabelSubClusterArray= new ArrayList<JLabel>();
 		
         this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
 		
         this.setBackground(COLOR_BG2);
 		
         this.setOpaque(true);
         this.setLayout(new BorderLayout());
 		 title= new JLabel("Select Number of clusters");
         AddHeader();
         ClustersParameters();
		 repaint();
	}
	
	
	public String LogData()
	{
		int cases=0;
		for(int j=0;j<DataTransformation.GetClusterTransformation().GetClusterData().GetActivityClusters().size();j++)
			cases+=DataTransformation.GetClusterTransformation().GetClusterData().GetActivityClusters().get(j).size();
		
		if(big)
		return "Traces: "+DataTransformation.GetData().GetCurrentLog().size()+
				", Cases: "+cases;		
		else
			return "<html> Traces: "+DataTransformation.GetData().GetCurrentLog().size()+"<br> Cases: "+cases;
			
	}
	
	public void CleanPanels()
	{
		 CaseEvents= new HashMap<String, MouseListener>();
		 ClusteredgesConcurrencyActiveBox= new ArrayList<JCheckBox>();
		 SubClusteredgesConcurrencyActiveBox= new ArrayList<JLabel>();
         JLabelClusterArray=new ArrayList();
         JLabelSubClusterArray= new ArrayList<JLabel>();
         this.remove(concurrencySliderPanel);
        
         checkClusterContainer.removeAll();
 		 ClusterMenuHeader.removeAll();
 		 
 		 checkClusterContainer.removeAll();
 		 checkClusterContainer.repaint();
 	 	 //concurrencySliderPanel.remove(checkClusterContainer);
 		 
 	 	 option_panel.removeAll();
 		 option_panel.repaint();
 		 //concurrencySliderPanel.remove(option_panel);
 		 
 		 optionSubclusterCases.removeAll();
 		 optionSubclusterCases.repaint();
 		 //concurrencySliderPanel.remove(optionSubclusterCases);
 		 
 		 concurrencySliderPanel.removeAll();
 		 concurrencySliderPanel.repaint();
	
	}
	public void ResetPanel()
	{
	
		 CleanPanels();
         AddHeader();
         ClustersParameters();
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
	
	public void ClustersParameters()
	{

		//Numbers of clusters
		numberOfClusters = new JComboBox();
		numberOfClusters.setPreferredSize(new Dimension(40, 25));
		numberOfClusters.setMaximumSize(new Dimension(40,25));
		int index=0;
		for(int j=1;j<=DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCase();j++ )
		{
			ClusteredgesConcurrencyActiveBox.add(new JCheckBox("Cluster "+j));
			numberOfClusters.addItem(j);
			index++;
		}
		
		numberOfClusters.setSelectedIndex(2);   
		
		ClusterMenuHeader.add(numberOfClusters);
		
		AddClustersParameters(true);
		//ClusterMenuHeader.setLayout(new BoxLayout(ClusterMenuHeader, BoxLayout.Y_AXIS));
		ClusterMenuHeader.setOpaque(false);
		ClusterMenuHeader.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 10));
		ClusterMenuHeader.add(Box.createVerticalGlue());   		
		ClusterMenuHeader.setPreferredSize(new Dimension(150,70));
		this.add(ClusterMenuHeader, BorderLayout.NORTH);
		this.add(concurrencySliderPanel, BorderLayout.CENTER);
		buildComponent(110,530,checkClusterContainer,concurrencySliderPanel,0);


	}
	
	public void ResetClustersParameters()
	{
		//Numbers of clusters
		numberOfClusters = new JComboBox();
		numberOfClusters.setPreferredSize(new Dimension(40, 25));
		numberOfClusters.setMaximumSize(new Dimension(40,25));
		for(int j=1;j<=DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCase();j++ )
		{
			ClusteredgesConcurrencyActiveBox.add(new JCheckBox("Cluster "+j));
			numberOfClusters.addItem(j);
		}
		numberOfClusters.setSelectedIndex(2);   
		
		ClusterMenuHeader.add(numberOfClusters);
		
		AddClustersParameters(true);
		//ClusterMenuHeader.setLayout(new BoxLayout(ClusterMenuHeader, BoxLayout.Y_AXIS));
		ClusterMenuHeader.setOpaque(false);
		ClusterMenuHeader.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 10));
		ClusterMenuHeader.add(Box.createVerticalGlue());   		
		ClusterMenuHeader.setPreferredSize(new Dimension(150,70));
		this.add(ClusterMenuHeader, BorderLayout.NORTH);
		this.add(concurrencySliderPanel, BorderLayout.CENTER);
		buildComponent(110,530,checkClusterContainer,concurrencySliderPanel,0);

	}
	
	
	public void AddClustersParameters(boolean firstTime)
	{
		String value= numberOfClusters.getSelectedItem().toString();
		DataTransformation.GetClusterTransformation().GetClusterData().SetNumberOfClusters(Integer.parseInt(value));
		DataTransformation.GetClusterTransformation().MakeProcessAlign();
	
		int numberOfCheckBoxes=3;

		if(!firstTime)
		{
			
			option_panel.removeAll();
			option_panel.repaint();
			
			checkClusterContainer.removeAll();
			checkClusterContainer.repaint();
			
			optionSubclusterCases.removeAll();
			optionSubclusterCases.repaint();
			this.RestoreSelects();
		
			checkClusterContainer.removeAll();
		
			numberOfCheckBoxes=Integer.parseInt(numberOfClusters.getSelectedItem().toString());
		
			DataTransformation.GetClusterTransformation().SetClusterData(new ClusterData(DataTransformation.GetData().GetCurrentLog(),numberOfCheckBoxes));		
		
			DataTransformation.SetClusterTransformation(new ClusterTransformation(
		
			new ClusterData(DataTransformation.GetData().GetCurrentLog(),numberOfCheckBoxes)));		
		
			checkClusterContainer.repaint();
		}			
	    
		option_cluster_panels= new JPanel[DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCase()];
	    option_panel= new JPanel();			    
	    option_panel.setOpaque(false);
		buildComponent(130,530,option_panel,concurrencySliderPanel,105);
		optionSubclusterCases= new JPanel();			    
		optionSubclusterCases.setSize(40, 40);
		optionSubclusterCases.setBounds(0, 0, 50, 50);
		optionSubclusterCases.setForeground(COLOR_FG);
	    optionSubclusterCases.setOpaque(false);
		buildComponent(75,530,optionSubclusterCases,concurrencySliderPanel,230);
	
        int number=0;

        
        for(int count=0; count<numberOfCheckBoxes;count++) 
		{
			for(int c=0;c<DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(count);c++)
			{
				    number=c+1;
					JCheckBox J= new JCheckBox("Case "+number);
					J.setSelected(true);
					J.setName(""+count+"-"+c);
					ClustersCasesCheckBoxes.put(count+"-"+c ,J);
			}
			AddClusterOption(count);
		}
		
		this.validate();
		this.repaint();
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
	
	
	public void CheckBoxClusterBuilder(int count)
	{
        int number=count+1;
        ClusteredgesConcurrencyActiveBox.get(count).setText("Cluster "+number+" ["+DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(count)+"]");
		ClusteredgesConcurrencyActiveBox.get(count).setUI(new SlickerCheckBoxUI());
		ClusteredgesConcurrencyActiveBox.get(count).setOpaque(false);
		ClusteredgesConcurrencyActiveBox.get(count).setForeground(COLOR_FG);
		ClusteredgesConcurrencyActiveBox.get(count).setFont(this.smallFont);
		ClusteredgesConcurrencyActiveBox.get(count).setSelected(true);
		ClusteredgesConcurrencyActiveBox.get(count).setToolTipText("<html>This control select the clusters of the model" +
				"visualization</html>");
		ClusteredgesConcurrencyActiveBox.get(count).setName(""+count);
	}
	
	public void DrillDownAction(JLabel label)
	{
		label.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {
						
				JLabel label= (JLabel) arg0.getComponent();
				int value= Integer.parseInt(label.getName());
				if(label.getText().equals("+") && ClusteredgesConcurrencyActiveBox.get(value).isSelected())
				{
				
					for(int j=0; j<JLabelClusterArray.size();j++)
					{
						JLabelClusterArray.get(j).setText("+");
					}
					
					label.setText("-");
					option_panel.removeAll();							
					option_panel.revalidate();
					option_panel.repaint();
					optionSubclusterCases.removeAll();
					optionSubclusterCases.revalidate();
					optionSubclusterCases.repaint();
					
					if(DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(value)<15)
					{
						ShowSimpleCases(value,option_panel,DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(value));
					}
					else
					{
						ShowComplexCases(value);
			
					}

				}
				else
				{
					
					label.setText("+");
					for(int j=0;j<JLabelSubClusterArray.size();j++)
						JLabelSubClusterArray.get(j).setText("+");
					
					option_panel.removeAll();							
					option_panel.revalidate();
					option_panel.repaint();
					
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
	
	public void DrillDownToBottom(JLabel label ,final int cicle)
	{
					label.addMouseListener(new MouseListener(){
					public void mouseClicked(MouseEvent arg0) {}
					public void mouseEntered(MouseEvent arg0) {}
					public void mouseExited(MouseEvent arg0) {}
					public void mousePressed(MouseEvent arg0) {
						JLabel label= (JLabel) arg0.getComponent();
						int value= Integer.parseInt(label.getName().substring(0,label.getName().indexOf("-")));
						int value_subcluster=Integer.parseInt(label.getName().substring(label.getName().indexOf("-")+1))-1;

						if(label.getText().equals("+"))
						{
							for(int j=0; j<JLabelSubClusterArray.size();j++)
							{
								JLabelSubClusterArray.get(j).setText("+");
							}

							label.setText("-");			
							optionSubclusterCases.removeAll();							
							optionSubclusterCases.revalidate();
							optionSubclusterCases.repaint();
			        		ShowSimpleCases(value,optionSubclusterCases,cicle);			
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
	
	
	public void ShowComplexCases(int value)
	{
		int cases=DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(value);
		int sub_check_boxes =  Math.round(cases/16);
		
		if(cases%16!=0)
			sub_check_boxes++;
		
		option_panel.removeAll();
		option_panel.repaint();
		option_panel.setLayout(null);
		int margin_top=0;
		
		int index=0;
		
		if(value==0)
		index=0;
		else
		{
			int clusters= Integer.parseInt(this.numberOfClusters.getSelectedItem().toString());
			if(value+1==clusters)
			{
				value--;
			}
			for(int j=0;j<value;j++)
			{
				int number=DataTransformation.GetClusterTransformation().GetClusterData().GetNumberOfCaseOnCluster(j);
				if(number>16)
				{
				index+=Math.round(number/16);
				if(index%16!=0)
					index++;
				}
			}
			
		}
		
		for(int c=0;c<sub_check_boxes;c++)		
		{
			
				JLabel label= JLabelSubClusterArray.get(index);				
				JLabel name=SubClusteredgesConcurrencyActiveBox.get(index);
		        JPanel cluster_panel= new JPanel();					
				cluster_panel.add(label,BorderLayout.LINE_START);
				cluster_panel.add(name,BorderLayout.PAGE_START);
				cluster_panel.setOpaque(false);
			    Insets insets = option_panel.getInsets();
			    margin_top=insets.top+5+24*c;
			    cluster_panel.setPreferredSize(new Dimension(130,24));
			    Dimension size = cluster_panel.getPreferredSize();
			    cluster_panel.setBounds( insets.left, margin_top,
			                 size.width, size.height);
		   	    option_panel.add(cluster_panel);
		   	    index++;
		}
		option_panel.repaint();	
	}
	
	public void ShowSimpleCases(int value, JPanel panel,int casesToShow)
	{
		int top=0;
		panel.setLayout(null);
		for(int c=0;c<casesToShow;c++)		
		{
				JCheckBox check=ClustersCasesCheckBoxes.get(value+"-"+c);
				check.setUI(new SlickerCheckBoxUI());
				check.setOpaque(false);
				check.setForeground(COLOR_FG);
				check.setFont(new Font("11f", 12, 10));
				//edgesConcurrencyActiveBox.addItemListener(this);
				
				//check.setSelected(true);
				check.setToolTipText("<html>This control select the clusters of the model" +"visualization</html>");
				//check.setName(""+value);
			
				Insets insets = panel.getInsets();
				
				top=10+ insets.top+25*c;
			    Dimension size = check.getPreferredSize();
			    check.setBounds(5+insets.left,top ,
			                          size.width+5,size.height);
			    
			    panel.add(check);
		}
		panel.repaint();	
	}
	
	public boolean LabelExist(String name)
	{
		for(int j=0;j<JLabelSubClusterArray.size();j++)
			if(JLabelSubClusterArray.get(j).getName().toString().equals(name))
				return true;
		
		return false;
		
	}
	
	public JLabel GetJLabelSubClusterArray(String name)
	{
		for(int j=0;j<JLabelSubClusterArray.size();j++)
			if(JLabelSubClusterArray.get(j).getName().toString().equals(name))
				return JLabelSubClusterArray.get(j);
		
		return null;
	}
	public void AddClusterOption(int count)
	{
	         	CheckBoxClusterBuilder(count);
				JLabel label= new JLabel("+");
				JLabelClusterArray.add(label);
				JPanel cluster_panel= new JPanel();
				label.setName(""+count);
				cluster_panel.setForeground(COLOR_FG);
			    cluster_panel.add(label,BorderLayout.LINE_START);
			    cluster_panel.add(ClusteredgesConcurrencyActiveBox.get(count),BorderLayout.PAGE_START);
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
					int sub_check_boxes =  Math.round(cases/16);
					
					if(cases%16!=0)
						sub_check_boxes++;
					
					//Agregar la tercera columna
					int number;
					int check_cases=16;
			
					
					for(int c=0;c<sub_check_boxes;c++)		
					{

							number=c+1;
							if(!LabelExist(count+"-"+number))
							{
								label= new JLabel("+");
								label.setName(count+"-"+number);
								JLabelSubClusterArray.add(label);
							}
							else
							label=GetJLabelSubClusterArray(count+"-"+number);
							
							
							if(c<sub_check_boxes-1)
								check_cases=16;
							else
								check_cases=cases%16;
							JLabel name=new JLabel("Group "+number+"["+check_cases+"]");
							name.setOpaque(false);
							name.setForeground(COLOR_FG);
							name.setFont(new Font("11f", 12, 10));
							name.setToolTipText("<html>This control select the clusters of the model" +"visualization</html>");			
							name.setName(""+count+"-"+c);
							name.setForeground(COLOR_FG);	
							SubClusteredgesConcurrencyActiveBox.add(name);
							
						    DrillDownToBottom(label,check_cases);

					}
		
				}

	}

	public void RestoreSelects()
	{
		ClustersCasesCheckBoxes.clear();
		SubClusteredgesConcurrencyActiveBox.clear();	
		JLabelSubClusterArray.clear();
		JLabelClusterArray.clear();
	}
	
	
  
		  
}
