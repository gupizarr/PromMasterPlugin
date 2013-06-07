package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation;

import java.util.Arrays;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data.TimeData;

/**
 * Methods to get time data ( Log info)
*/
public class OLAPTimeTransformation {

	private StatisticsCalculator Calculator;
	private TimeData TimeData;
    private double[] finalTime;
    private boolean ContainTimes;
    private long[] LogDaysDiff;
	private long[] LogSecondsDiff;
	private double[] sortedFinalTime;
	private int ponderator;
	
	public OLAPTimeTransformation(TimeData TimeData) {
		// TODO Auto-generated constructor stub
		ponderator=1;
		this.TimeData=TimeData;
		Calculator= new  StatisticsCalculator();
        ContainTimes=true;
        
		 LogDaysDiff=new long[TimeData.GetLog().size()];
		 LogSecondsDiff=new long[TimeData.GetLog().size()];
		 
		 if(TimeData.GetLogIni().length>0)
		 {
		 ConvertLongToIntDate();
		 SortData();
		 }
		 else
		 {
			 ContainTimes=false;
		 }
		 
	}
	
	public TimeData GetTimeData()
	{
		return TimeData;
	}
	
	public int GetPonderator()
	{
		return ponderator;
	}
	
	public void InicializatePerformanceData(XLog log)
    {
			 this.TimeData= new TimeData(log);
				LogSecondsDiff=new long[TimeData.GetLog().size()];
				 LogDaysDiff=new long[TimeData.GetLog().size()];		 
			 if(TimeData.GetLogIni().length>0)
			 {
			 ConvertLongToIntDate();
			 SortData();
			 }
			 else
			 {
				 ContainTimes=false;
			 }
	
			 
	  }
	  
	  public void SortData()
	  {

		 double diffInSeconds;
		 double[] timeInDays= new double[LogSecondsDiff.length];
		 double[] timeInHours= new double[LogSecondsDiff.length];
		 double[] timeInMinutes= new double[LogSecondsDiff.length];
		 double[] timeInSeconds= new double[LogSecondsDiff.length];

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
			    TimeData.SetIsInDays(true);
			    else if(timeInHours[j]>=1)
			    TimeData.SetIsInHours(true);
			    else if(timeInMinutes[j]>=1)
			    TimeData.SetIsInMinutes(true);
		 }

			    	finalTime=timeInSeconds;
			    	ponderator=1;
			    

		 
		    sortedFinalTime=finalTime.clone(); 
		    Arrays.sort(sortedFinalTime);
		    CalculateData();
	  }
	  
	  public String TagTime()
	  {
		    if(TimeData.IsInDays() && !TimeData.IsInHours() && !TimeData.IsInMinutes())
		    	return "Days";
		    else if(TimeData.IsInHours() && !TimeData.IsInMinutes())
		    	return "Hours";
		    else if(TimeData.IsInMinutes())
		    	return "Minutes";
		    else
		    	return "Seconds";
	  }
	  
	  public void CalculateData()
	  {
		  TimeData.SetMean(Calculator.getMean(this.finalTime));
		 TimeData.SetSD(Calculator.getStdDev(this.finalTime));

	  }
	  
	  public void ConvertLongToIntDate()
	  {
		  
		  for (int j=0;j<TimeData.GetLog().size();j++) {
			  
		    LogSecondsDiff[j] = (TimeData.GetLogFinish()[j] - TimeData.GetLogIni()[j]) / 1000;
	
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
	  
	  public double[] GetPerformanceDiff()
	  {
		  return getSortedFinalTime();
	  }
}
