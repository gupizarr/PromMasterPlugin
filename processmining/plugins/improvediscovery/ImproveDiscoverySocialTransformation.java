package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.processmining.plugins.socialnetwork.miner.miningoperation.workingtogether.WorkingtogetherSAR;

import cern.colt.matrix.DoubleMatrix2D;

public class ImproveDiscoverySocialTransformation {

	protected WorkingtogetherSAR WorkingTogetherData;
	protected DoubleMatrix2D WorkingTogetherMatrix;
	
	protected WorkingtogetherSAR WorkingTogetherDataToShow;
	protected DoubleMatrix2D WorkingTogetherMatrixToShow;
	
	protected Map<String,SocialRelation> Relations=new HashMap<String,SocialRelation>();
	private   ArrayList<ArrayList<Integer>> Teams= new ArrayList<ArrayList<Integer>>();
	protected Map<Double,ArrayList<SocialRelation>> SubGroups=new HashMap<Double,ArrayList<SocialRelation>>();
	private   ArrayList<ArrayList<Integer>> GroupOfTwo= new ArrayList<ArrayList<Integer>>();
	private ImproveDiscoveryData Data;
	private double cuteValue=0;
	public ImproveDiscoverySocialTransformation(ImproveDiscoveryData Data) {
		// TODO Auto-generated constructor stub
		WorkingTogetherData= new  WorkingtogetherSAR(Data.GetBaseLog());
		WorkingTogetherDataToShow= new  WorkingtogetherSAR(Data.GetWorkingLog());

		this.Data=Data;
	}
	
	public WorkingtogetherSAR GetWorkingTogetherDataToShow()
	{
		return  WorkingTogetherDataToShow;
	}
	
	public void SetWorkingTogetherToShow()
	{
		WorkingTogetherDataToShow=new  WorkingtogetherSAR(Data.GetWorkingLog());
		WorkingTogetherMatrixToShow=WorkingTogetherDataToShow.calculation();
		CleanMatrixToShow();
		System.out.print("\n mundo de ToShow");
	}
	
	public DoubleMatrix2D GetWorkingTogetherMatrix2DToShow()
	{
		return WorkingTogetherMatrixToShow;
	}
	
	
	public int  getNumberOfWorkingTogTeam()
	{
		return Teams.size();
	}
	public void WTCalculation()
	{
		
		WorkingTogetherMatrixToShow=WorkingTogetherDataToShow.calculation();
		WorkingTogetherMatrix=WorkingTogetherData.calculation();
		String resource1="";
		String resource2="";
		double indicator=0;
		
		for(int k=0;k<WorkingTogetherMatrix.columns();k++)
		{
			for(int j=0;j<WorkingTogetherMatrix.rows();j++)
			{
				//System.out.print(Math.rint(WorkingTogetherMatrix.get(k, j)*10)/10+" ");
				if(k!=j && !Relations.containsKey(k+"-"+j) && !Relations.containsKey(j+"-"+k) && WorkingTogetherMatrix.get(k,j)>0 && WorkingTogetherMatrix.get(j,k)>0)
				{

				   resource1=WorkingTogetherData.getOriginatorList().get(k);
				   resource2=WorkingTogetherData.getOriginatorList().get(j);
                   indicator= Math.rint(WorkingTogetherMatrix.get(k, j)*10)/10;
                   SocialRelation SR= new SocialRelation(resource1,resource2,indicator);
                   Relations.put(k+"-"+j, SR);

				}
			}
			//System.out.print("\n");

		}
		
		SearchGroups();
	}
	
	public void SearchGroups()
	{
		
		Teams.clear();
		GroupOfTwo.clear();
		boolean isAlreadyInAGroup=false;
		for(int j=0;j<WorkingTogetherMatrix.columns();j++)
		{
			isAlreadyInAGroup=false;
			for(int c=0;c<Teams.size();c++)
			{
				//already is
				if(Teams.get(c).contains(j))
				{
					isAlreadyInAGroup=true;
				}
			}
			if(!isAlreadyInAGroup)
			{
				ArrayList<Integer> Members=MakeGroups(j,WorkingTogetherMatrix);
				if(Members.size()>2)
				{
					Teams.add(Members);
				}
					else
					GroupOfTwo.add(Members);
					
			}
		}
	}
	
	public void AsignSubGroup(ArrayList<Integer> Team)
	{
		for(int j=0;j<Team.size();j++)
		{
		for (int k=0; k<Team.size();k++)
		{
			SocialRelation SR=Relations.get(j+"-"+k);
        	if(this.SubGroups.get(SR.GetRate())!=null)
        	{
        	SubGroups.get(SR.GetRate()).add(SR);  
        	}
        	else
        	{
     	   	ArrayList<SocialRelation> SubGroup= new ArrayList<SocialRelation>();
     	   	SubGroup.add(SR);
     	   	SubGroups.put(SR.GetRate(), SubGroup);
        	}
		}
		}
       
	}
	
	public ArrayList<ArrayList<Integer>> GroupOfTwo()
	{
		return GroupOfTwo;	
	}
	
	public ArrayList<ArrayList<Integer>> GetTeamsWT()
	{
		return Teams;
	}
	
	public ArrayList<Integer> GetGroupsOneTwo()
	{
		ArrayList<Integer> AllOneTwo=new ArrayList<Integer>();
		
		for(int k=0;k<GroupOfTwo.size();k++)
		{
			AllOneTwo.addAll(GroupOfTwo.get(k));
		}
		return AllOneTwo;
	}
	
	public void GetIsNeighbor(int column, ArrayList<Integer> arrayNeighbors,DoubleMatrix2D socialMatrix)
	{
        ArrayList<Integer> array= new ArrayList<Integer>();
	
		for(int j=0;j<socialMatrix.rows();j++)
		{
			if(arrayNeighbors.indexOf(j)==-1)
			{
				if(socialMatrix.get(column, j)!=0)
				{
					array.add(j);
				}
				
			}

		}	
		
		for(int k=0;k<array.size();k++)
		{
			if(arrayNeighbors.indexOf(array.get(k))==-1)
			{
				arrayNeighbors.add(array.get(k));
			    GetIsNeighbor(arrayNeighbors.get(k),arrayNeighbors,socialMatrix); 
			}
		}
		
	
		
	}
	
	public boolean IsInOtherTeam(int member)
	{
		for(int j=0; j<Teams.size();j++)
		{
			for(int c=0;c<Teams.get(j).size();c++)
			{
				if(Teams.get(j).get(c)==member)
				{
				return true;
				}
			}
		}
		return false;
	}
	
	
	public ArrayList<Integer> MakeGroups(int from,DoubleMatrix2D socialMatrix)
	{
            ArrayList<Integer> nodes= new ArrayList<Integer>();

			if(!nodes.contains(from))
			nodes.add(from);
			for(int j=0;j<socialMatrix.rows();j++)
			{
				if(socialMatrix.get(from, j)!=0 && nodes.indexOf(j)==-1)
				{
					
					nodes.add(j);
					for(int c=0; c<nodes.size(); c++)
					{
						GetIsNeighbor(nodes.get(c),nodes,socialMatrix); 
					}
				}
			}
	
		
		
		    return nodes;
	}
	
	public  Map<String,SocialRelation> getRelationsFromTeamWT(int index)
	{
		int column=0;
		
	    Map<String,SocialRelation> GroupRelation= new HashMap<String,SocialRelation>();
 
		for(int j=0; j<Teams.get(index).size(); j++)
		{
			column=Teams.get(index).get(j);
			for(int k=0;k<this.WorkingTogetherMatrix.rows();k++)
			{
				if(Relations.containsKey(column+"-"+k))
				{
				  GroupRelation.put(j+"-"+k, Relations.get(column+"-"+k));
				}
				else if(Relations.containsKey(k+"-"+j))
				{
				   GroupRelation.put(k+"-"+column, Relations.get(k+"-"+column));
				}
			
			}
		}
		
		
		return GroupRelation;
	}
	
	public ArrayList<Double> GroupRateValues(int index)
	{
	
		ArrayList<Double> values= new ArrayList<Double>();
		for (Map.Entry<Double, ArrayList<SocialRelation>> entry : SubGroups.entrySet())
		{
			values.add(entry.getKey());
		}
		Collections.sort(values);
		
		return values;
		
	}
	
	public void RecalculateSocialRelations(String Analysist, double Value)
	{
		cuteValue=Value;
		
		//WorkingTogetherDataToShow= new  WorkingtogetherSAR(Data.GetCurrentLog());
		WorkingTogetherData= new  WorkingtogetherSAR(Data.GetBaseLog());
		WorkingTogetherMatrix=WorkingTogetherData.calculation();
		Relations= new HashMap<String,SocialRelation>();
		String resource1="";
		String resource2="";
		Double indicator;
		
		for(int k=0;k<WorkingTogetherMatrix.columns();k++)
		{
			for(int j=0;j<WorkingTogetherMatrix.rows();j++)
			{
				if(k!=j && WorkingTogetherMatrix.get(k,j)>=Value && WorkingTogetherMatrix.get(j,k)>=Value)
				{
					   resource1=WorkingTogetherData.getOriginatorList().get(k);
					   resource2=WorkingTogetherData.getOriginatorList().get(j);
	                   indicator= Math.rint(WorkingTogetherMatrix.get(k, j)*10)/10;
					  
	                   SocialRelation SR= new SocialRelation(resource1,resource2,indicator);

                   Relations.put(k+"-"+j, SR);

				}
				else
				{
						WorkingTogetherMatrix.set(k, j, 0);
						WorkingTogetherMatrix.set(j, k, 0);
				}
			}
		}
		SetWorkingTogetherToShow();
	}
	
	public String TranslateNode(Integer j)
	{
		return this.WorkingTogetherData.getOriginatorList().get(j);
	}
	
	public void GetOriginatorList()
	{
		//for(int j=0;j<this.WorkingTogetherData.getOriginatorList().size();j++)
		//	System.out.print("\n"+this.WorkingTogetherData.getOriginatorList().get(j));
	}
	
	public void CleanMatrixToShow()
	{
		
		for(int k=0;k<WorkingTogetherMatrixToShow.columns();k++)
		{
			for(int j=0;j<WorkingTogetherMatrixToShow.rows();j++)
			{
				if(k!=j && WorkingTogetherMatrixToShow.get(k,j)>=cuteValue && WorkingTogetherMatrixToShow.get(j,k)>=cuteValue)
				{}
				else
				{
						WorkingTogetherMatrixToShow.set(j, k, 0);
						WorkingTogetherMatrixToShow.set(k, j, 0);
				}
			}
		}
		
	}
		
}


