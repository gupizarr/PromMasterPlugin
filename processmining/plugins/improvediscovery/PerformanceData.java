package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.Arrays;

import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class PerformanceData {
	 
	 
	  private long[] LogIni;
	  private long[] LogFinish;
	  private long[] LogDaysDiff;
	  private long[] LogSecondsDiff;
	  private boolean inDays;
      private boolean inHours;
      private boolean inMinutes;
	  private XLog transformLog;
      private boolean ContainTimes;
	  private double[] finalTime;
	  private double[] sortedFinalTime;
	  public int ponderator=0;
	  
	  public PerformanceData(XLog log) {
		// TODO Auto-generated constructor stub
		 this.transformLog=log;
	
		 LogIni =new long[log.size()];
		 LogFinish=new long[log.size()];
		 LogDaysDiff=new long[log.size()];
		 LogSecondsDiff=new long[log.size()];
		 
		 ContainTimes=true;
		 PerformanceInfo();
		 if(LogIni.length>0)
		 {
		 ConvertLongToIntDate();
		 SortData();
		 }
		 else
		 {
			 ContainTimes=false;
		 }
	  }
	  
	  public boolean IsInHours()
	  {
		    if(inDays && !inHours && !inMinutes)
		    	return false;
		    else if(inHours && !inMinutes)
		    	return true;
		    else 
		    	return false;
	  }
	  
	  public boolean IsInDays()
	  {
		    if(inDays && !inHours && !inMinutes)
		    	return true;
		    else if(inHours && !inMinutes)
		    	return false;
		    else 
		    	return false;
	  }
	  
	  public boolean IsInMinutes()
	  {
		    if(inDays && !inHours && !inMinutes)
		    	return false;
		    else if(inHours && !inMinutes)
		    	return false;
		    else 
		    	return true;
	  }
	  
	  public boolean HaveTimes()
	  {
		  return ContainTimes;
	  }
	  public void ConvertLongToIntDate()
	  {
		  
		  for (int j=0;j<transformLog.size();j++) {
			  
		    LogSecondsDiff[j] = (LogFinish[j] - LogIni[j]) / 1000;
	
		  }
	  }
	  
	  public void PerformanceInfo()
	  {
			XTimeExtension extractor = XTimeExtension.instance();
	        LogIni= new long[transformLog.size()];
	        LogFinish= new long[transformLog.size()];
	  		
	        for (int j=0;j<transformLog.size();j++) {
				XTrace trace = transformLog.get(j);
				// get timestamp of the first event
				XEvent first= trace.get(0);
				XEvent last= trace.get(trace.size()-1);
				
					if (extractor.extractTimestamp(first) != null) 
					{	LogIni[j]=extractor.extractTimestamp(first).getTime();
					   
					}
					if (extractor.extractTimestamp(last)!=null)
					{	LogFinish[j]=extractor.extractTimestamp(last).getTime();
					  
					}
					

				}

	  }
	    

	  public double[] getFinalTimes()
	  {
		
			return  finalTime;
			
	  }
	  
	  public void SetFinalTimes(double[] newTimes)
	  {
		  finalTime=newTimes;
		  sortedFinalTime=finalTime;
		  sortedFinalTime=finalTime.clone();
		  Arrays.sort(sortedFinalTime);
		  
	  }
	  public double[] getSortedFinalTime()
	  {
		  return sortedFinalTime;
	  }
	  
	  public long[] getDaysDiff()
	  {
		    return  LogDaysDiff;

	  }
	 
	  public void SortData()
	  {

		 double diffInSeconds;
		 double[] timeInDays= new double[LogSecondsDiff.length];
		 double[] timeInHours= new double[LogSecondsDiff.length];
		 double[] timeInMinutes= new double[LogSecondsDiff.length];
		 double[] timeInSeconds= new double[LogSecondsDiff.length];
		 inDays=false;
		 inHours=false;
		 inMinutes=false;
		
		 for(int j=0;j<LogSecondsDiff.length;j++)
		 {
			    diffInSeconds=LogSecondsDiff[j];
			    timeInDays[j]=diffInSeconds/86400;
			    timeInHours[j]= diffInSeconds/3600;
			    timeInMinutes[j]= diffInSeconds/60;
			    timeInSeconds[j]= diffInSeconds;
	    
			    //System.out.print("\n"+timeInDays[j]+" days, "+ timeInHours[j]+ " hours, "+timeInMinutes[j]+" minutes,"+timeInSeconds[j]+" secondes");

			    //is in days
			    if(timeInDays[j]>=1)  	
			    inDays=true;
			    else if(timeInHours[j]>=1)
			    inHours=true;
			    else if(timeInMinutes[j]>=1)
			    inMinutes=true;
		 }
			    if(inDays && !inHours && !inMinutes)
			    {
			    	finalTime=timeInDays;	
			    	ponderator=86400;
			    }
			    else if(inHours && !inMinutes)
			    {
			    	finalTime=timeInHours;
			    	ponderator=3600;

			    }
			    else if(inMinutes)
			    {	
			    	finalTime=timeInMinutes;
			    	ponderator=60;

			    }
			    else
			    {
			    	finalTime=timeInSeconds;
			    	ponderator=1;
			    }

		  //}
		 
		    sortedFinalTime=finalTime.clone(); 
		    Arrays.sort(sortedFinalTime);

			 
	  }
	  
	  public String TagTime()
	  {
		    if(inDays && !inHours && !inMinutes)
		    	return "Days";
		    else if(inHours && !inMinutes)
		    	return "Hours";
		    else if(inMinutes)
		    	return "Minutes";
		    else
		    	return "Seconds";
	  }
}
