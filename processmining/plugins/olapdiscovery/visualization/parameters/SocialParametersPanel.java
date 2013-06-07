package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.parameters;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data.OLAPData;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTransformation;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.visualization.swingworkers.SocialParametersSwingWorker;
import org.processmining.plugins.socialnetwork.miner.gui.PanelSimilarTask;

import com.fluxicon.slickerbox.ui.SlickerCheckBoxUI;
import com.fluxicon.slickerbox.ui.SlickerSliderUI;


/**
 *  Social Parameters container
 *  @author Gustavo Pizarro
*/

public class SocialParametersPanel extends JPanel {
   

	protected ArrayList<JCheckBox> PeopleCheckBoxes;
	protected Color COLOR_BG = new Color(60, 60, 60);
	protected Color COLOR_BG2 = new Color(120, 120, 120);
	protected Color COLOR_FG = new Color(30, 30, 30);
	protected Font smallFont;
	protected JLabel filterMinValue;
	PanelSimilarTask STPanel;
	protected JLabel edgesConcurrencyThresholdLabel;
	protected JSlider filterSlider;
	protected OLAPTransformation DataTransformation;
	protected OLAPData DataDiscovery;
	protected JComboBox Options;
	protected JPanel GroupPanel;
	protected JPanel RelationsPanel;
	protected JPanel PeoplesPanel;
	protected JPanel MainPanel;
	protected JLabel actualValue;
	protected Font resourceFont = new Font("8f", 12, 10);
	protected ArrayList<JLabel> JLabelResources;
	protected ArrayList<JLabel> JLabelGroups;
	protected ArrayList<JCheckBox> JCheckBoxGroups;
	protected JPanel filterPanel;
	private Map<String,MouseListener> ResourceModelEvents;
	private Map<String,MouseListener> SubGroupModelEvents;
	private SocialParametersSwingWorker SocialParameterSwingWorker;
	private Map<String,ArrayList<JCheckBox>> GroupMemberMap;
	private double SliderValue;

	
	public SocialParametersPanel(OLAPTransformation Transformation) {
	
		SubGroupModelEvents= new HashMap<String, MouseListener>();
		GroupMemberMap= new HashMap<String,ArrayList<JCheckBox>>();
		SocialParameterSwingWorker= new SocialParametersSwingWorker();
		ResourceModelEvents= new HashMap<String, MouseListener>();
		//JCheckBoxResources= new ArrayList<JCheckBox>();
		JCheckBoxGroups= new ArrayList<JCheckBox>();		
		JLabelResources= new ArrayList<JLabel>();
		JLabelGroups= new ArrayList<JLabel>();
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setBackground(COLOR_BG2);
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		
		DataTransformation=Transformation;
		DataDiscovery= Transformation.GetData();
		BuiltOptions();
		
		MainPanel= new JPanel();
		MainPanel.setOpaque(false);
		MainPanel.setPreferredSize(new Dimension(300,600));
		this.add(MainPanel, BorderLayout.CENTER);
		
		GroupPanel= new JPanel();
		GroupPanel.setOpaque(false);
		buildComponent(5,80,330,GroupPanel,MainPanel,0,0);	
		
		RelationsPanel= new JPanel();
		RelationsPanel.setOpaque(false);
		buildComponent(5,125,330,RelationsPanel,MainPanel,90,0);
		
		PeoplesPanel= new JPanel();
		PeoplesPanel.setOpaque(false);
		buildComponent(5,95,330,PeoplesPanel,MainPanel,210,0);
		
		RationFilterPanel();
		AddGroupCheckBoxes();
		this.repaint();
		// TODO Auto-generated constructor stub
	}
	
	public Map<String,MouseListener> GetResourceModelEvents()
	{
		return ResourceModelEvents;
	}
	public JComboBox GetOptions()
	{
		return Options;
	}
	public void MakeAll()
	{
		
		GroupPanel= new JPanel();
		buildComponent(5,75,330,GroupPanel,MainPanel,0,0);	
		AddGroupCheckBoxes();
	}
		
	public void buildComponent(int margin_top,int width,int height, JComponent panel, JPanel ContainerPanel,int ypost,int xpost)
	{
		panel.setForeground(COLOR_FG);
		//panel.setOpaque(false);
		Insets insets = ContainerPanel.getInsets();
		panel.setPreferredSize(new Dimension(width,height));    
	    Dimension size = panel.getPreferredSize();
	    panel.setBounds(insets.left +ypost, /*margin_top +*/ insets.top+xpost,
	                          size.width,size.height);
	    
	    ContainerPanel.add(panel);
	}
	
	public void BuiltOptions()
	{
		JPanel edgesConcurrencyHeaderPanel = new JPanel();
		
		JLabel label=new JLabel("Select a type of analysist");
		Options= new JComboBox();
		Options.addItem("Working Together");
		Options.addItem("Similar Task");
		Options.addItem("Handover of Work");
		edgesConcurrencyHeaderPanel.add(label);
		edgesConcurrencyHeaderPanel.add(Options);	
		edgesConcurrencyHeaderPanel.setLayout(new BoxLayout(edgesConcurrencyHeaderPanel, BoxLayout.Y_AXIS));
		edgesConcurrencyHeaderPanel.setOpaque(false);
		edgesConcurrencyHeaderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 10));
		edgesConcurrencyHeaderPanel.add(Box.createVerticalGlue());
		this.add(edgesConcurrencyHeaderPanel, BorderLayout.NORTH);
	}
	

	
	public void RationFilterPanel()
	{
		// concurrency edge transformer slider panel
		
		
		if(Arrays.asList(MainPanel.getComponents()).contains(filterPanel))
		{
			MainPanel.remove(filterPanel);
			filterPanel.removeAll();
		}
		else
		{
		 filterPanel = new JPanel();
		filterPanel.setOpaque(false);
		filterPanel.setLayout(null);
		}
		this.buildComponent(0,280, 300, filterPanel,MainPanel ,0,380);
	
		
        // concurrency edge ratio threshold slider panel
		JPanel concurrencyRatioPanel = new JPanel();
		concurrencyRatioPanel.setOpaque(false);
		concurrencyRatioPanel.setLayout(null);
		
		
		//Label tittle
		JLabel TitleOfFilterPanel = new JLabel("Ratio of the group definition");
		TitleOfFilterPanel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		TitleOfFilterPanel.setOpaque(false);
		TitleOfFilterPanel.setForeground(COLOR_FG);
		TitleOfFilterPanel.setFont(this.smallFont);
		
		//Min Value
		JLabel MinValue = new JLabel("0.0");
		MinValue.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		MinValue.setOpaque(false);
		MinValue.setForeground(COLOR_FG);
		MinValue.setFont(this.smallFont);

		//Max Value
		JLabel MaxValue = new JLabel("1.0");
		MaxValue.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		MaxValue.setOpaque(false);
		MaxValue.setForeground(COLOR_FG);
		MaxValue.setFont(this.smallFont);
		
		actualValue= new JLabel("Actual Value: 0.0");
		
		//Slider con los valores
		filterSlider = new JSlider(JSlider.VERTICAL, 0, 1000, 0);
		filterSlider.setOrientation(SwingConstants.HORIZONTAL);
		filterSlider.setUI(new SlickerSliderUI(filterSlider));
		filterSlider.setOpaque(false);
		filterSlider.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {

			JSlider Slider= (JSlider) arg0.getComponent();
			SliderValue=Math.rint((Slider.getValue()/100))/10;
			actualValue.setText("Actual Value: "+SliderValue);
			
			if(SliderValue>0)
			{

				if(Options.getSelectedItem().toString().equals("Working Together"))
				{
				DataTransformation.GetSocialTransformation().RecalculateSocialRelations("WT",SliderValue);
				DataTransformation.GetSocialTransformation().SearchGroups(DataTransformation.GetSocialTransformation(). GetMatrix2D("WT"));
				}	
				else if(Options.getSelectedItem().toString().equals("Similar Task"))
				{

					DataTransformation.GetSocialTransformation().RecalculateSocialRelations("ST",SliderValue);
					DataTransformation.GetSocialTransformation().SearchGroups(DataTransformation.GetSocialTransformation(). GetMatrix2D("ST"));
								
				}
				else
				{

					DataTransformation.GetSocialTransformation().RecalculateSocialRelations("HW",SliderValue);
					DataTransformation.GetSocialTransformation().SearchGroups(DataTransformation.GetSocialTransformation(). GetMatrix2D("HW"));
				
				}
				
				RelationsPanel.removeAll();
			    PeoplesPanel.removeAll();
			    RelationsPanel.repaint();
			    PeoplesPanel.repaint();
			    
				GroupPanel.removeAll();
				
				AddGroupCheckBoxes();
				
				GroupPanel.setOpaque(false);
				GroupPanel.repaint();
				
			}
			
			}
			public void mouseReleased(MouseEvent arg0){}
			
		})
		;
		
		filterSlider.setToolTipText("<html>Instrucciones</html>");	
		filterMinValue = new JLabel("0.000");
		filterMinValue.setSize(new Dimension(25, 25));
		filterMinValue.setForeground(COLOR_FG);
		filterMinValue.setFont(this.smallFont);
		
		buildComponent(0,150, 30, TitleOfFilterPanel,filterPanel, 0, 0);
		buildComponent(0,150,60, actualValue, filterPanel,0,0);
		buildComponent(5,40, 40, MinValue,filterPanel, 0, 30);
		buildComponent(5,220, 40, filterSlider,filterPanel, 30, 30);
		buildComponent(5,40, 40, MaxValue,filterPanel, 250, 30);
	
		// Armado Final
		filterPanel.setOpaque(false);
	
	}
	
	public void ResetPanel()
	{
	
		SetSliderValue(0);
		ResourceModelEvents.clear();
		JLabelResources.clear();
	    JLabelGroups.clear();
	    GroupMemberMap.clear();
		JCheckBoxGroups.clear();
		actualValue.setText("Actual Value: 0.0");
		this.filterSlider.setValue(0);
		SubGroupModelEvents.clear();

		RelationsPanel.removeAll();
	    PeoplesPanel.removeAll();

	    RelationsPanel.repaint();
	    PeoplesPanel.repaint();
	    
		AddGroupCheckBoxes();
	
		
	}
	
	public void PeopleCheckBoxes(boolean group_check)
	{

							
				PeopleCheckBoxes= new ArrayList<JCheckBox>();
				for(int count=0; count<DataDiscovery.Resources.length;count++) {
			
					//adding the activities
					JCheckBox resource=new JCheckBox(DataDiscovery.Resources[count]);
				PeopleCheckBoxes.add(resource);
				resource.setUI(new SlickerCheckBoxUI());
				resource.setOpaque(false);
				resource.setForeground(COLOR_FG);
				resource.setFont(this.smallFont);
				//PeopleCheckBoxes.addItemListener(this);
				resource.setSelected(group_check);
				resource.setToolTipText("<html>This control can be used to switch off<br>"
						+ "concurrency filtering in the model.</html>");
				
				//agrego Id para identificarlo
				
				resource.setName(DataDiscovery.Resources[count]);
				
				//edgesConcurrencyHeaderPanel.add(PeopleCheckBoxes[count]);	
				
				}
				
	
				
		
	}
	
	protected void centerHorizontally(JLabel label) {
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
	

	
	public int[] MaxMinResource(int value,int total)
	{
		int[] values=new int[2];
		
		values[0]= (value-1)*10;
		if(total-values[0]<10)
		{
		values[1]= total-1;
		}
		
		else
		{
		values[1]= 10*value;
		}
		
		return values;
	}
	
	
	public void AddResourceSubGroupEvent(int group,JLabel  subGroupLabel, ArrayList<Integer>Members)
	{
		String key=group+"-"+subGroupLabel.getName();
		if(SubGroupModelEvents.get(key)==null)
		{				
			MouseListener ML=SubGroupMouseListener(group,Members);
			subGroupLabel.addMouseListener(ML);
			SubGroupModelEvents.put(key,ML);
		}
	}
	
	public MouseListener SubGroupMouseListener(final int group, final ArrayList<Integer>Members)
	{
		return new MouseListener(){
			public void mouseClicked(MouseEvent arg0){}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0){}
			public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub										
				JLabel label=(JLabel) arg0.getComponent();
				if(label.getText().equals("+"))
				{
					for(int j=0; j<JLabelResources.size();j++)
						JLabelResources.get(j).setText("+");
					
					label.setText("-");	
					PeoplesPanel.removeAll();
					PeoplesPanel.revalidate();
					PeoplesPanel.repaint();
					int value= Integer.parseInt(label.getName().toString().substring(label.getName().indexOf("-")+1));		
					int[] MinMax=MaxMinResource(value,Members.size());
					
					if(GroupMemberMap.size()==0)
					AddResourceToRelationPanel(MinMax[0],MinMax[1],group,Members);
		
					 for(int c=MinMax[0];c<MinMax[1];c++)
					 {
						JCheckBox chec=getJCheckBoxResource(""+group,""+Members.get(c));
					 	chec.setFont(resourceFont);
						chec.setName(""+Members.get(c));
						
						PeoplesPanel.add(chec);
						PeoplesPanel.revalidate();
						PeoplesPanel.repaint();
					 }
					 if(MinMax[0]==MinMax[1])
					 {
							JCheckBox chec=getJCheckBoxResource(""+group,""+Members.get(MinMax[0]));
						 	chec.setFont(resourceFont);
							chec.setName(""+Members.get(MinMax[0]));
							
							PeoplesPanel.add(chec);
							PeoplesPanel.revalidate();
							PeoplesPanel.repaint();
					 }
				
				
				}
				else
				{
					for(int j=0;j<JLabelResources.size();j++)
						JLabelResources.get(j).setText("+");
					
				
					PeoplesPanel.removeAll();
					PeoplesPanel.revalidate();
					PeoplesPanel.repaint();
				}
		}
		
		public void mouseReleased(MouseEvent arg0) {	}};
	}
	public void CreateResourceCheckBoxes()
	{
		String resource;
	    String toolTip;
	    
		this.RelationsPanel.setPreferredSize(new Dimension(90,300));
		ArrayList<JCheckBox> JCheckList ;
		JCheckBox chec;
	    int member;
		for(int j=0;j<DataTransformation.GetSocialTransformation().GetTeams().size();j++)
		{
			JCheckList= new ArrayList<JCheckBox>() ;
			for(int k=0;k<DataTransformation.GetSocialTransformation().GetTeams().get(j).size();k++)
			{
				member=DataTransformation.GetSocialTransformation().GetTeams().get(j).get(k);
				resource=	DataTransformation.GetSocialTransformation().TranslateNode(member);
				chec= new JCheckBox(resource);
				toolTip=ResourceToolTipText(DataTransformation.GetActivities(resource));
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
		if(DataTransformation.GetSocialTransformation().GroupOfTwo().size()>0)
		{
			JCheckList= new ArrayList<JCheckBox>() ;
			for(int k=0;k<DataTransformation.GetSocialTransformation().GroupOfTwo().size();k++)
			{
				for(int j=0;j<DataTransformation.GetSocialTransformation().GroupOfTwo().get(k).size();j++)
				{
				member=DataTransformation.GetSocialTransformation().GroupOfTwo().get(k).get(j);
				resource=	DataTransformation.GetSocialTransformation().TranslateNode(member);
				chec= new JCheckBox(resource);
				toolTip=ResourceToolTipText(DataTransformation.GetActivities(resource));
				chec.setToolTipText(toolTip);	
			
				chec.setUI(new SlickerCheckBoxUI());
				chec.setSelected(true);
				chec.setOpaque(false);
			 	chec.setFont(resourceFont);
				chec.setName(""+member);
				JCheckList.add(chec);
				}
			}
			GroupMemberMap.put("-1",JCheckList);
		}

	
	}
	public void AddResourceToRelationPanel(int ini,int max, int numberGroup,ArrayList<Integer> Members)
	{
		
	    
		this.RelationsPanel.setPreferredSize(new Dimension(90,300));
		for(int c=ini;c<max;c++)
		{
			JCheckBox chec=getJCheckBoxResource(""+numberGroup,""+Members.get(c));
			
			RelationsPanel.add(chec);
			RelationsPanel.revalidate();
			RelationsPanel.repaint();	
		}
		
	}
	
	public void AddSocialOptions(final int num_group,final ArrayList<Integer>Members)
	{

		JLabel Grouplabel= new JLabel("+");
		Grouplabel.setName(""+num_group);
		JLabelGroups.add(Grouplabel);
		int num=num_group+1;
		JCheckBox check= new JCheckBox("Group "+num);
		check.setUI(new SlickerCheckBoxUI());
       
		JCheckBoxGroups.add(check);
		check.setFont(resourceFont);
		check.setSelected(true);
		check.setName(""+num_group);
		check.setOpaque(false);
		JPanel panel= new JPanel();
		panel.add(Grouplabel);
		panel.add(check);
		panel.setOpaque(false);
		GroupPanel.add(panel);
		Grouplabel.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {	}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) 
			{
			    JLabel label=(JLabel) arg0.getComponent();						
			    int group_index=Integer.parseInt(label.getName());
			    if(label.getText().equals("+") && JCheckBoxGroups.get(group_index).isSelected())
				{

			    	ResetAllChecks();
					
					label.setText("-");							
				   
					final ArrayList<Integer> Members=DataTransformation.GetSocialTransformation().GetTeams().get(group_index);
					int indexMember=Members.size();
					int number_of_subgroups=Math.round(Members.size()/10);
					if(Members.size()%10!=0)
					{
						number_of_subgroups++;
					}
					RelationsPanel.setPreferredSize(new Dimension(120,300));
					if(number_of_subgroups>1)
					{
						int options=0;
						for(int j=0;j<indexMember;j++)
						{			
							if(j%10==0)
							{
								options++;
								JLabel label2=getJLabelResourceOption(group_index+"-"+options);
								
								if(label2==null)
								{
								label2= new JLabel("+");
								label2.setName(group_index+"-"+options);
								JLabelResources.add(label2);						
								}
								
								int[] MinMax=MaxMinResource(options,Members.size());
					
								int min=MinMax[0]+1;
								JLabel ch=null;
								
								if(min!=MinMax[1] && min<MinMax[1])
								ch= new JLabel("Resources "+""+min+"-"+MinMax[1]);
								else
								ch= new JLabel("Resources "+""+min);	
						
								ch.setFont(resourceFont);
								JPanel panel2= new JPanel();
								panel2.add(label2);
								panel2.add(ch);
								panel2.setOpaque(false);
								RelationsPanel.add(panel2);
								RelationsPanel.revalidate();
								RelationsPanel.repaint();	
								AddResourceSubGroupEvent(num_group,label2,Members);
							}
						}
					}
					else
					{
						AddResourceToRelationPanel(0,Members.size(),num_group,Members);
					}
				}
				else
				{
					
					ResetAllChecks();
					
				}
			}
			public void mouseReleased(MouseEvent arg0) {}
		});
		GroupPanel.repaint();
	}
	
	public void ResetAllChecks()
	{
		for(int j=0;j<JLabelGroups.size();j++)
			JLabelGroups.get(j).setText("+");
		
		for(int j=0;j<JLabelResources.size();j++)
			JLabelResources.get(j).setText("+");
		
		RelationsPanel.removeAll();
		RelationsPanel.repaint();
		PeoplesPanel.removeAll();
		PeoplesPanel.repaint();
	}
	
	public void CreateOptions()
	{
		for(int j=0;j<DataTransformation.GetSocialTransformation().getNumberOfTeam();j++)
		{
	        ArrayList<Integer> Members=DataTransformation.GetSocialTransformation().GetTeams().get(j);
			AddSocialOptions(j, Members);

		}
	}
	
	public JLabel getJLabelResourceOption(String option)
	{
		for(int j=0; j<JLabelResources.size();j++)
		{
			if(JLabelResources.get(j).getName().equals(option))
				return JLabelResources.get(j);
		}
		return null;		
	}
	
	public JLabel getJLabelGroup( String name)
	{
		for(int j=0; j<JLabelGroups.size();j++)
		{
			if(JLabelGroups.get(j).getName().equals(name))
				return JLabelGroups.get(j);
		}
		return null;
	}
	
	public JCheckBox getJCheckBoxResource(String group,String name)
	{
		if(GroupMemberMap.get(group)!=null)
		{
		for(int j=0; j<GroupMemberMap.get(group).size();j++)
		{
			if(GroupMemberMap.get(group).get(j).getName().equals(name))
				return GroupMemberMap.get(group).get(j);
		}
		}
		return null;
	}
		

	public void AddGroupOfTwo()
	{
		JLabel label= new JLabel("+");
		label.setName("-1");
		final JCheckBox checkOneTwo= new JCheckBox("Others");
		JCheckBoxGroups.add(checkOneTwo);
		checkOneTwo.setSelected(true);
		JPanel panel= new JPanel();
		checkOneTwo.setFont(resourceFont);
		checkOneTwo.setName("others");		
		checkOneTwo.setUI(new SlickerCheckBoxUI());

		panel.add(label);
		panel.add(checkOneTwo);
		JLabelGroups.add(label);
		panel.setOpaque(false);
		GroupPanel.add(panel);
		label.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) 
			{
				JLabel label=(JLabel) e.getComponent();
		    	if(checkOneTwo.isSelected() && label.getText().equals("+"))
		    	{
		    		ResetAllChecks();
				
		    		label.setText("-");							
			    
		    		final ArrayList<Integer> Members=DataTransformation.GetSocialTransformation().GetGroupsOneTwo();
			
		    		int indexMember=Members.size();
		    		int number_of_subgroups=Math.round(Members.size()/10);
					if(Members.size()%10!=0)
					{
					number_of_subgroups++;
					}
					else
					{
				
					}
					if(number_of_subgroups>1)
					{
						int options=0;
						for(int j=0;j<indexMember;j++)
						{			
							if(j%10==0)
							{
								options++;
								JLabel label2= new JLabel("+");
								label2.setName(""+options);
								JLabelResources.add(label2);						
					    		int[] MinMax=MaxMinResource(options,Members.size());
								int min=MinMax[0]+1;
								JLabel ch=null;
								
								if(min!=MinMax[1])
								ch= new JLabel("Resources "+""+min+"-"+MinMax[1]);
								else
								ch= new JLabel("Resources "+""+min);	
						
								ch.setFont(resourceFont);
								JPanel panel2= new JPanel();
								panel2.add(label2);
								panel2.add(ch);
								panel2.setOpaque(false);
								RelationsPanel.add(panel2);
								RelationsPanel.revalidate();
								RelationsPanel.repaint();	
								AddResourceSubGroupEvent(-1,label2,Members);
							}
						}
					}
					else
					{
						AddResourceToRelationPanel(0,Members.size(),-1,Members);
					}
				}
		    	else
		    	{
		    		ResetAllChecks();
		    		label.setText("+");
		    	}
			}
			public void mouseReleased(MouseEvent e) {}					
		});
	}
	
	public void AddGroupCheckBoxes()
	{
		JLabelResources.clear();
		JCheckBoxGroups.clear();
		GroupMemberMap.clear();
		GroupPanel.removeAll();
		
		
			if(DataTransformation.GetSocialTransformation().GroupOfTwo().size()>0)
			{
				AddGroupOfTwo();
			}
	
			for(int j=0;j<DataTransformation.GetSocialTransformation().getNumberOfTeam();j++)
			{	
				
			        ArrayList<Integer> Members=DataTransformation.GetSocialTransformation().GetTeams().get(j);
					AddSocialOptions(j, Members);
			}				
	}
	

	public  JSlider GetFilterSlider()
	{
		return filterSlider;
	}
	
	public ArrayList<JCheckBox> GetJCheckBoxGroups()
	{
		return JCheckBoxGroups;
		
	}
	
	public JPanel GetRelationsPanel()
	{
		return RelationsPanel;
	}
	
	public ArrayList<JLabel> GetJLabelGroups()
	{
		return JLabelGroups;
	}
	
	public ArrayList<JLabel> GetJLabelResources()
	{
		return JLabelResources;
	}
	
	public Map<String,ArrayList<JCheckBox>> GetMapJCheckBoxResources()
	{
		return GroupMemberMap;
	}
	
	public ArrayList<JCheckBox> GetPeopleCheckBoxes()
	{
		return PeopleCheckBoxes;
	}
	
	public String ResourceToolTipText(ArrayList Activities)
	{
		String info="<html>Executor of:<br>";
		for(int j=0; j<Activities.size();j++)
			info+= Activities.get(j)+"<br>";

		info+="</html>";
		return info;
	}
	
	public void ReCheckJResources(String group)
	{
		for(int j=0;j<GroupMemberMap.get(group).size();j++)
			GroupMemberMap.get(group).get(j).setSelected(true);
	}
	
	public void SetSliderValue(double newValue)
	{
		SliderValue=newValue;
	}
	
	public double GetSliderValue()
	{
		return SliderValue;
	}
}



