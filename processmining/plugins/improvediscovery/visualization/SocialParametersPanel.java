package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

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

import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryData;
import org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery.ImproveDiscoveryTransformation;
import org.processmining.plugins.socialnetwork.miner.gui.PanelSimilarTask;

import com.fluxicon.slickerbox.ui.SlickerCheckBoxUI;
import com.fluxicon.slickerbox.ui.SlickerSliderUI;


public class SocialParametersPanel extends JPanel {
   
	protected JCheckBox[] edgesConcurrencyActiveBox;
	protected Color COLOR_BG = new Color(60, 60, 60);
	protected Color COLOR_BG2 = new Color(120, 120, 120);
	protected Color COLOR_FG = new Color(30, 30, 30);
	protected Font smallFont;
	protected JLabel filterMinValue;
	PanelSimilarTask STPanel;
	protected JLabel edgesConcurrencyThresholdLabel;
	protected JSlider filterSlider;
	protected ImproveDiscoveryTransformation DataTransformation;
	protected ImproveDiscoveryData DataDiscovery;
	protected JComboBox Options;
	protected JPanel GroupPanel;
	protected JPanel RelationsPanel;
	protected JPanel PeoplesPanel;
	protected JPanel MainPanel;
	protected JLabel actualValue;
	protected Font resourceFont = new Font("8f", 12, 10);
	protected ArrayList<JLabel> JLabelResources;
	protected ArrayList<JLabel> JLabelGroups;
	protected ArrayList<JCheckBox> JCheckBoxResources;
	protected ArrayList<JCheckBox> JCheckBoxGroups;
	protected JPanel filterPanel;


	
	public SocialParametersPanel(ImproveDiscoveryTransformation Transformation) {
	
		JCheckBoxResources= new ArrayList<JCheckBox>();
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
		GroupPanel= new JPanel();
		GroupPanel.setOpaque(false);
		buildComponent(5,80,330,GroupPanel,MainPanel,0,0);	
		RelationsPanel= new JPanel();
		RelationsPanel.setOpaque(false);
		buildComponent(5,125,330,RelationsPanel,MainPanel,90,0);
		PeoplesPanel= new JPanel();
		PeoplesPanel.setOpaque(false);
		buildComponent(5,95,330,PeoplesPanel,MainPanel,210,0);
		this.add(MainPanel, BorderLayout.CENTER);
		
		RationFilterPanel();
		AddGroupCheckBoxes();
		this.repaint();
		// TODO Auto-generated constructor stub
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
	    panel.setBounds(insets.left+ypost, margin_top + insets.top+xpost,
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
			double value=Math.rint((Slider.getValue()/100))/10;
			actualValue.setText("Actual Value: "+value);
			
			if(value>0)
			{

				if(Options.getSelectedItem().toString().equals("Working Together"))
				{
				DataTransformation.GetSocialTransformation().RecalculateSocialRelations("WT",value);
				DataTransformation.GetSocialTransformation().SearchGroups(DataTransformation.GetSocialTransformation(). GetMatrix2D("WT"));
				}	
				else if(Options.getSelectedItem().toString().equals("Similar Task"))
				{
					DataTransformation.GetSocialTransformation().RecalculateSocialRelations("ST",value);
					DataTransformation.GetSocialTransformation().SearchGroups(DataTransformation.GetSocialTransformation(). GetMatrix2D("ST"));
								
				}
				else
				{
					
				}
				
				RelationsPanel.removeAll();
			    PeoplesPanel.removeAll();
			    RelationsPanel.repaint();
			    PeoplesPanel.repaint();
			    
				GroupPanel.removeAll();
				
				AddGroupCheckBoxes();
				
				GroupPanel.setOpaque(true);
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
		this.filterSlider.setValue(0);
		
		if(Options.getSelectedItem().toString().equals("Working Together"))
		{
		DataTransformation.GetSocialTransformation().RecalculateSocialRelations("WT",0);
		DataTransformation.GetSocialTransformation().SearchGroups(DataTransformation.GetSocialTransformation(). GetMatrix2D("WT"));
		}	
		else if(Options.getSelectedItem().toString().equals("Similar Task"))
		{
	    DataTransformation.GetSocialTransformation().RecalculateSocialRelations("ST",0);
		DataTransformation.GetSocialTransformation().SearchGroups(DataTransformation.GetSocialTransformation(). GetMatrix2D("ST"));
						
		}
		RelationsPanel.removeAll();
	    PeoplesPanel.removeAll();

	    RelationsPanel.repaint();
	    PeoplesPanel.repaint();
	    
		AddGroupCheckBoxes();
	
		
	}
	
	public void PeopleCheckBoxes(boolean group_check)
	{

							
				edgesConcurrencyActiveBox= new JCheckBox[DataDiscovery.Resources.length];
				for(int count=0; count<DataDiscovery.Resources.length;count++) {
			
					//adding the activities
					
				edgesConcurrencyActiveBox[count] = new JCheckBox(DataDiscovery.Resources[count]);
				edgesConcurrencyActiveBox[count].setUI(new SlickerCheckBoxUI());
				edgesConcurrencyActiveBox[count].setOpaque(false);
				edgesConcurrencyActiveBox[count].setForeground(COLOR_FG);
				edgesConcurrencyActiveBox[count].setFont(this.smallFont);
				//edgesConcurrencyActiveBox.addItemListener(this);
				edgesConcurrencyActiveBox[count].setSelected(group_check);
				edgesConcurrencyActiveBox[count].setToolTipText("<html>This control can be used to switch off<br>"
						+ "concurrency filtering in the model.</html>");
				
				//agrego Id para identificarlo
				
				edgesConcurrencyActiveBox[count].setName(DataDiscovery.Resources[count]);
				
				//edgesConcurrencyHeaderPanel.add(edgesConcurrencyActiveBox[count]);	
				
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
	
	public void MemberCheckBox(JCheckBox ch)
	{
		ch.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0){}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0){}
			public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			JCheckBox check=(JCheckBox) arg0.getComponent();
			}
			public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
			}});
	}
	
	public int[] MaxMinResource(int value,int total)
	{
		int[] values=new int[2];
		
		values[0]= (value-1)*11;
		if(total-values[0]<11)
		{
		values[1]= total-1;
		}
		
		else
		{
		values[1]= 11*value;
		}
		
		return values;
	}
	
	
	public void AddResourceSubGroupEvent(JLabel  subGroupLabel, final ArrayList<Integer>Members)
	{
		
		subGroupLabel.addMouseListener(new MouseListener(){
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
					
					for(int c=MinMax[0];c<MinMax[1];c++)
					{
						JCheckBox chec=getJCheckBoxResource(""+Members.get(c));
						if(chec==null)
						{
						chec= new JCheckBox(DataTransformation.GetSocialTransformation().TranslateNode(Members.get(c)));
						JCheckBoxResources.add(chec);
						chec.setUI(new SlickerCheckBoxUI());
						chec.setSelected(true);
						chec.setOpaque(false);
						}
					
						chec.setFont(resourceFont);
						chec.setName(""+Members.get(c));
						MemberCheckBox(chec);

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
		
		public void mouseReleased(MouseEvent arg0) {	}});
		
	}
	
	public void AddResourceToRelationPanel(ArrayList<Integer> Members)
	{
		this.RelationsPanel.setPreferredSize(new Dimension(90,300));
		for(int j=0; j<Members.size();j++)
		{
			JCheckBox chec=getJCheckBoxResource(""+Members.get(j));
			if(chec==null)
			{
			chec= new JCheckBox(DataTransformation.GetSocialTransformation().TranslateNode(Members.get(j)));
			JCheckBoxResources.add(chec);
			chec.setSelected(true);
			chec.setOpaque(false);
			chec.setUI(new SlickerCheckBoxUI());

			}
		
			chec.setFont(resourceFont);
			chec.setName(""+Members.get(j));
			MemberCheckBox(chec);MemberCheckBox(chec);
			RelationsPanel.add(chec);
		}
		RelationsPanel.revalidate();
		RelationsPanel.repaint();	
	}
	public void AddSocialOptions(int num_group,final ArrayList<Integer>Members)
	{

		JLabel Grouplabel= new JLabel("+");
		Grouplabel.setName(""+num_group);
		JLabelGroups.add(Grouplabel);
		JCheckBox check= new JCheckBox("Group "+""+num_group);
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
				   
					final ArrayList<Integer> Members=DataTransformation.GetSocialTransformation().GetTeamsWT().get(group_index);
					int indexMember=Members.size();
					int number_of_subgroups=Math.round(Members.size()/11);
					if(Members.size()%11!=0)
					{
						number_of_subgroups++;
					}
					RelationsPanel.setPreferredSize(new Dimension(120,300));
					if(number_of_subgroups>1)
					{
						int options=0;
						for(int j=0;j<indexMember;j++)
						{			
							if(j%11==0)
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
								AddResourceSubGroupEvent(label2,Members);
							}
						}
					}
					else
					{
						AddResourceToRelationPanel(Members);
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
		for(int j=0;j<DataTransformation.GetSocialTransformation().getNumberOfWorkingTogTeam();j++)
		{
	        ArrayList<Integer> Members=DataTransformation.GetSocialTransformation().GetTeamsWT().get(j);
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
	
	public JCheckBox getJCheckBoxResource(String name)
	{
		for(int j=0; j<JCheckBoxResources.size();j++)
		{
			if(JCheckBoxResources.get(j).getName().equals(name))
				return JCheckBoxResources.get(j);
		}
		return null;
	}
		

	public void AddGroupOfTwo()
	{
		JLabel label= new JLabel("+");
		final JCheckBox checkOneTwo= new JCheckBox("Others");
		checkOneTwo.setSelected(true);
		JPanel panel= new JPanel();
		checkOneTwo.setFont(resourceFont);
		checkOneTwo.setName("-1");		
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
		    		int number_of_subgroups=Math.round(Members.size()/11);
					if(Members.size()%11!=0)
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
							if(j%11==0)
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
								AddResourceSubGroupEvent(label2,Members);
							}
						}
					}
					else
					{
						AddResourceToRelationPanel(Members);
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
		JCheckBoxResources.clear();
		GroupPanel.removeAll();
		
		
			if(DataTransformation.GetSocialTransformation().GroupOfTwo().size()>0)
			{
				AddGroupOfTwo();
			}
	
			for(int j=0;j<DataTransformation.GetSocialTransformation().getNumberOfWorkingTogTeam();j++)
			{	
				
			        ArrayList<Integer> Members=DataTransformation.GetSocialTransformation().GetTeamsWT().get(j);
					AddSocialOptions(j, Members);
			}				
		}
	}



