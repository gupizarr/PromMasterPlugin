package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.filters;

import java.util.ArrayList;

import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data.OLAPData;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPSocialTransformation;

/**
 *  Methods to filter log in a social context
*/
public class SocialFilter {

	private ArrayList<XTrace> Traces;
    private ArrayList<String> RemovedResources;  
	

	public SocialFilter(OLAPSocialTransformation SocialTransformation,ArrayList<XTrace> Trace) {
		// TODO Auto-generated constructor stub
		
		RemovedResources= new ArrayList<String>();
		this.Traces= Trace;

	}
	
	  public void RemovePerson(String Resource,String type)
	  {
		    Traces.clear();

		    if(!RemovedResources.contains(Resource))
		    {
			RemovedResources.add(Resource);
		    }
		 
		
	  }
	  
	  
	  public void ShowResources()
	  {
		  for(int k=0;k<RemovedResources.size();k++)
			 System.out.print("\n sacando en el grupo"+ RemovedResources.get(k));
	  }
	  public void SearchTrace(XTrace trace,OLAPData Data,int index)
	  {
		  //ShowResources();
	    String resource;
  	    Traces.add(trace);
  	    //si es que tiene eventos
  	    if(Data.GetBaseLog().get(index).size()>0)
  	    {
  	    	//por cada evento
		 			for (XEvent event : Data.GetBaseLog().get(index)) 
					{		
	        	            resource=XLogInfoFactory.createLogInfo(Data.GetBaseLog()).getResourceClasses().getClassOf(event).getId();
			     		    //si el evento es ejecutado por la persona que se quiere remover
	        	            if(!RemovedResources.contains(resource))
			     			{
	        	            	trace.add(event);	     				          
			     			}  			        	     
					}
	   	    }
  	    if(trace.size()==0)
  	    	Traces.remove(trace);
	  }
	  
	  public ArrayList<XTrace> GetResult()
	  {
		  return Traces;
	  }
	  

	  public void GroupFilter(String GroupName,String type, OLAPSocialTransformation SocialTransformation)
	  {
			ArrayList<Integer> Team;
			
		  if(GroupName.equals("others"))
    		 Team= SocialTransformation.GetGroupsOneTwo();
		  else
			 Team=SocialTransformation.GetTeams().get(Integer.parseInt(GroupName));

			 for(int j=0; j<Team.size();j++)
			 {
				String person= SocialTransformation.TranslateNode(Team.get(j));
				if(!RemovedResources.contains(person))
				{
				RemovedResources.add(person);
				System.out.print("\n"+ person);
				}			    
			
			 }

	  }
	  
	  public void RestoreGroup(String GroupName,String type,OLAPSocialTransformation SocialTransformation)
	  {
		 
		  		ArrayList<Integer> Team;
			
		  if(GroupName.equals("others"))
    		 Team=SocialTransformation.GetGroupsOneTwo();
		  else
			 Team=SocialTransformation.GetTeams().get(Integer.parseInt(GroupName));
			 
			 for(int j=0; j<Team.size();j++)
			 {
				String person=SocialTransformation.TranslateNode(Team.get(j));
				if(RemovedResources.contains(person))
				{
					RemovedResources.remove(person);	
				}	
				
			 }
			 
			  Traces.clear();
			 
	  }
	  
	  public void AddPerson(String resource,String type)
	  {
		  
		 if(RemovedResources.contains(resource))
	     RemovedResources.remove(resource);

	  }
	  
	  public void Reset()
	  {
		  RemovedResources.clear();
		  
	  }

}
