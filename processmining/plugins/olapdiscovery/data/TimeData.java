package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data;

import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

/**
 * The data repository of Time Transformations
*/
public class TimeData {
	 
	 
	  private long[] LogIni;
	  private long[] LogFinish;

	  private boolean inDays;
      private boolean inHours;
      private boolean inMinutes;
	  private XLog transformLog;
      private boolean ContainTimes;
	
	  public int ponderator=0;
	  private double mean=0;
	  private double standartD;
	  
	  public TimeData(XLog log) {
		// TODO Auto-generated constructor stub

		  this.transformLog=log;
	
		 LogIni =new long[log.size()];
		 LogFinish=new long[log.size()];

		 
		 PerformanceInfo();


	  }
	  
	  public void SetLog(XLog log)
	  {
		this.transformLog=log;  
	  }
	  
	  public XLog GetLog()
	  {
		  return transformLog;
	  }
	  public long[] GetLogIni()
	  {
		  return LogIni;
	  }
	  
	  public double getMean()
	  {
		  
		  return mean;
	  }
	  
	  public double getStandartD()
	  {
		
		  return this.standartD;
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
	  
	  public void SetMean(double mean)
	  {
		  this.mean=mean;
	  }
	  
	  public void SetSD(double sd)
	  {
		  this.standartD=sd;
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
	    
	  public void SetIsInDays(boolean is)
	  {
		  this.inDays=is;
	  }
	 
	  public void SetIsInHours(boolean is)
	  {
		  this.inHours=is;
	  }
	 
	  public void SetIsInMinutes(boolean is)
	  {
		  this.inMinutes=is;
	  }
	 
	  public long[] GetLogFinish()
	  {
		  return LogFinish;

	  }
	  

}
