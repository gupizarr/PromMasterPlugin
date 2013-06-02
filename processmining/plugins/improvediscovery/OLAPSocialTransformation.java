package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.socialnetwork.SocialNetwork;
import org.processmining.plugins.socialnetwork.miner.miningoperation.BasicOperation;
import org.processmining.plugins.socialnetwork.miner.miningoperation.UtilOperation;
import org.processmining.plugins.socialnetwork.miner.miningoperation.handover.HandoverCCCDCM;
import org.processmining.plugins.socialnetwork.miner.miningoperation.similartask.SimilartaskCC;
import org.processmining.plugins.socialnetwork.miner.miningoperation.workingtogether.WorkingtogetherSAR;

import cern.colt.matrix.DoubleMatrix2D;

public class OLAPSocialTransformation {

	protected WorkingtogetherSAR WorkingTogetherData;
	protected DoubleMatrix2D WorkingTogetherMatrix;
	
	protected WorkingtogetherSAR WorkingTogetherDataToShow;
	protected DoubleMatrix2D WorkingTogetherMatrixToShow;
	
	protected SimilartaskCC SimilarTaskData;
	protected DoubleMatrix2D SimilarTaskMatrix;

	protected SimilartaskCC SimilarTaskDataToShow;
	protected DoubleMatrix2D SimilarTaskMatrixToShow;
	
	protected HandoverCCCDCM HandoverWTaskData;
	protected DoubleMatrix2D  HandoverWTaskMatrix;

	protected HandoverCCCDCM HandoverWTaskDataToShow;
	protected DoubleMatrix2D  HandoverWTaskMatrixToShow;
	
	
	protected Map<String,SocialRelation> Relations=new HashMap<String,SocialRelation>();
	private   ArrayList<ArrayList<Integer>> Teams= new ArrayList<ArrayList<Integer>>();
	protected Map<Double,ArrayList<SocialRelation>> SubGroups=new HashMap<Double,ArrayList<SocialRelation>>();
	private   ArrayList<ArrayList<Integer>> GroupOfTwo= new ArrayList<ArrayList<Integer>>();
	private OLAPData Data;
	private double cuteValue=0;
	public OLAPSocialTransformation(OLAPData Data) {
		// TODO Auto-generated constructor stub
		WorkingTogetherData= new  WorkingtogetherSAR(Data.GetBaseLog());
		WorkingTogetherDataToShow= new  WorkingtogetherSAR(Data.GetBaseLog());
		
		SimilarTaskData = new SimilartaskCC(Data.GetBaseLog());
		SimilarTaskDataToShow= new SimilartaskCC(Data.GetBaseLog());
		
		HandoverWTaskData =new HandoverCCCDCM(Data.GetBaseLog());
		HandoverWTaskDataToShow=new HandoverCCCDCM(Data.GetBaseLog());
		this.Data=Data;
	}
	
	public HandoverCCCDCM GetHandoverWTaskDataToShow()
	{
		
			
		return HandoverWTaskDataToShow;	
	}
	
	public SimilartaskCC GetSimilarTaskDataToShow()
	{
		return  this.SimilarTaskDataToShow;
	}
	
	public WorkingtogetherSAR GetWorkingTogetherDataToShow()
	{
		return  WorkingTogetherDataToShow;
	}
	
	public void SetHandoverWTaskToShow(XLog CurrentLog)
	{
		HandoverWTaskDataToShow=new  HandoverCCCDCM(CurrentLog);
		HandoverWTaskMatrixToShow=HandoverWTaskDataToShow.calculation();
		CleanMatrixToShow(HandoverWTaskMatrixToShow);		
	}
	
	public void SetSimilarTaskToShow(XLog CurrentLog)
	{
		SimilarTaskDataToShow=new  SimilartaskCC(CurrentLog);
		SimilarTaskMatrixToShow=SimilarTaskDataToShow.calculation();
		CleanMatrixToShow(SimilarTaskMatrixToShow);		
	}
	
	public void SetWorkingTogetherToShow(XLog CurrentLog)
	{
		WorkingTogetherDataToShow=new  WorkingtogetherSAR(CurrentLog);
		WorkingTogetherMatrixToShow=WorkingTogetherDataToShow.calculation();
		CleanMatrixToShow(WorkingTogetherMatrixToShow);

	}
	
	public DoubleMatrix2D GetMatrix2DToShow(String MatrixName)
	{
		if(MatrixName.equals("WT"))
		return WorkingTogetherMatrixToShow;
		else if(MatrixName.equals("ST"))
		return SimilarTaskMatrixToShow;
		else if(MatrixName.equals("HW"))
		return HandoverWTaskMatrixToShow;
		else
		return null;
	}
	
	public DoubleMatrix2D GetMatrix2D(String MatrixName)
	{
		if(MatrixName.equals("WT"))
		return WorkingTogetherMatrix;
		else if(MatrixName.equals("ST"))
		return SimilarTaskMatrix;
		else if(MatrixName.equals("HW"))
		return HandoverWTaskMatrix;
		else
		return null;
	}
	
	
	public int  getNumberOfTeam()
	{
		return Teams.size();
	}

	public void HWCalculation()
	{
		Relations.clear();
		this.HandoverWTaskMatrixToShow= this.HandoverWTaskDataToShow.calculation();
		this.HandoverWTaskMatrix= this.HandoverWTaskData.calculation();
		String resource1="";
		String resource2="";
		double indicator=0;
		
		for(int k=0;k<HandoverWTaskMatrix.columns();k++)
		{
			for(int j=0;j<HandoverWTaskMatrix.rows();j++)
			{
				if(k!=j && !Relations.containsKey(k+"-"+j) && !Relations.containsKey(j+"-"+k) && HandoverWTaskMatrix.get(k,j)>0 && HandoverWTaskMatrix.get(j,k)>0)
				{

				   resource1=HandoverWTaskData.getOriginatorList().get(k);
				   resource2=HandoverWTaskData.getOriginatorList().get(j);
                   indicator= Math.rint(HandoverWTaskMatrix.get(k, j)*10)/10;
                   SocialRelation SR= new SocialRelation(resource1,resource2,indicator);
                   Relations.put(k+"-"+j, SR);

				}
			}

		}
		
		SearchGroups(HandoverWTaskMatrix);
	}
	
	
	public void STCalculation()
	{
		Relations.clear();
		this.SimilarTaskMatrixToShow= this.SimilarTaskDataToShow.calculation();
		this.SimilarTaskMatrix= this.SimilarTaskData.calculation();
		String resource1="";
		String resource2="";
		double indicator=0;
		
		for(int k=0;k<SimilarTaskMatrix.columns();k++)
		{
			for(int j=0;j<SimilarTaskMatrix.rows();j++)
			{
				if(k!=j && !Relations.containsKey(k+"-"+j) && !Relations.containsKey(j+"-"+k) && SimilarTaskMatrix.get(k,j)>0 && SimilarTaskMatrix.get(j,k)>0)
				{

				   resource1=SimilarTaskData.getOriginatorList().get(k);
				   resource2=SimilarTaskData.getOriginatorList().get(j);
                   indicator= Math.rint(SimilarTaskMatrix.get(k, j)*10)/10;
                   SocialRelation SR= new SocialRelation(resource1,resource2,indicator);
                   Relations.put(k+"-"+j, SR);

				}
			}

		}
		
		SearchGroups(SimilarTaskMatrix);
	}
	
	public void WTCalculation()
	{
		
		Relations.clear();
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
		
		SearchGroups(WorkingTogetherMatrix);
	}
	
	public void SearchGroups(DoubleMatrix2D matrix)
	{
		
		Teams.clear();
		GroupOfTwo.clear();
		boolean isAlreadyInAGroup=false;
		for(int j=0;j<matrix.columns();j++)
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
				ArrayList<Integer> Members=MakeGroups(j,matrix);
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
	
	public ArrayList<ArrayList<Integer>> GetTeams()
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
		Relations= new HashMap<String,SocialRelation>();		
		//WorkingTogetherDataToShow= new  WorkingtogetherSAR(Data.GetCurrentLog());
		if(Analysist.equals("WT"))
		{
			WorkingTogetherData= new  WorkingtogetherSAR(Data.GetBaseLog());
			WorkingTogetherMatrix=WorkingTogetherData.calculation();
			ApplyValueThredhold(WorkingTogetherMatrix,WorkingTogetherData,Value);
			SetWorkingTogetherToShow(Data.GetBaseLog());

		}
		else if(Analysist.equals("HW"))
		{
		    this.HandoverWTaskData= new  HandoverCCCDCM(Data.GetBaseLog());
		    HandoverWTaskMatrix=HandoverWTaskData.calculation();
			ApplyValueThredhold(HandoverWTaskMatrix,this.HandoverWTaskData,Value);
			this.SetHandoverWTaskToShow(Data.GetBaseLog());		
		}
		else
		{
			SimilarTaskData=new SimilartaskCC(Data.GetBaseLog());
			SimilarTaskMatrix=SimilarTaskData.calculation();
			ApplyValueThredhold(SimilarTaskMatrix,SimilarTaskData,Value);
			SetSimilarTaskToShow(Data.GetBaseLog());

		}

		
		
	}
	
	public void ApplyValueThredhold(DoubleMatrix2D socialMatrix,BasicOperation Operation,double Value)
	{
		
		String resource1="";
		String resource2="";
		Double indicator;
		
		for(int k=0;k<socialMatrix.columns();k++)
		{
			for(int j=0;j<socialMatrix.rows();j++)
			{
				if(k!=j && socialMatrix.get(k,j)>=Value && socialMatrix.get(j,k)>=Value)
				{
					   resource1=Operation.getOriginatorList().get(k);
					   resource2=Operation.getOriginatorList().get(j);
	                   indicator= Math.rint(socialMatrix.get(k, j)*10)/10;
					  
	                   SocialRelation SR= new SocialRelation(resource1,resource2,indicator);

                   Relations.put(k+"-"+j, SR);

				}
				else
				{
					socialMatrix.set(k, j, 0);
					socialMatrix.set(j, k, 0);
				}
			}
		}
	}
	
	public String TranslateNode(Integer j)
	{
		return this.WorkingTogetherData.getOriginatorList().get(j);
	}
	

	
	public void CleanMatrixToShow(DoubleMatrix2D matrix)
	{
		
		for(int k=0;k<matrix.columns();k++)
		{
			for(int j=0;j<matrix.rows();j++)
			{
				if(k!=j && matrix.get(k,j)>=cuteValue && matrix.get(j,k)>=cuteValue)
				{}
				else
				{
					matrix.set(j, k, 0);
					matrix.set(k, j, 0);
				}
			}
		}
		
	}
	
	public SocialNetwork GenerateBaseSocialNetwork(String Analysist,XLog Log)
	{
		SocialNetwork generate;
		cuteValue=0;
		Relations= new HashMap<String,SocialRelation>();		
		//WorkingTogetherDataToShow= new  WorkingtogetherSAR(Data.GetCurrentLog());
		if(Analysist.equals("Working Together"))
		{
			WorkingtogetherSAR WorkingT= new  WorkingtogetherSAR(Log);
			DoubleMatrix2D WorkingTMatrix=WorkingT.calculation();
			generate=UtilOperation.generateSN(WorkingTMatrix, WorkingT.getOriginatorList());

		}
		else if(Analysist.equals("Similar Task"))
		{
			SimilartaskCC STData=new SimilartaskCC(Log);
			DoubleMatrix2D STMatrix=STData.calculation();
			generate=UtilOperation.generateSN(STMatrix, STData.getOriginatorList());


		}
		else
		{
			HandoverCCCDCM  HWData= new  HandoverCCCDCM(Log);
		    DoubleMatrix2D  HaTMatrix=HWData.calculation();
			generate=UtilOperation.generateSN(HaTMatrix, HWData.getOriginatorList());

		}

		return generate;
	}
	
}


