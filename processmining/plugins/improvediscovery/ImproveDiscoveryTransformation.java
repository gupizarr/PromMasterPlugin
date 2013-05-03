package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.ArrayList;

import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XAttributeMapLazyImpl;
import org.deckfour.xes.model.impl.XLogImpl;

 public class ImproveDiscoveryTransformation {
    
	
	private ImproveDiscoveryData Data;
	public ImproveDiscoveryTransformation(ImproveDiscoveryData Data)  {
		// TODO Auto-generated constructor stub
		this.Data=Data;
	}
	


	public void PerformanceFilter(int minTimeValue, int maxTimeValue)
	{
		 System.out.print("\nTrace antes de filtrar:"+Data.GetCurrentLog().size());
		 ArrayList<XTrace> KeptXTrace= new ArrayList<XTrace>();
     		XTimeExtension extractor = XTimeExtension.instance();

		 System.out.print("\n Valor minimo:"+minTimeValue + ", "+ "Valor max:"+maxTimeValue);
	     for(int i=0; i<Data.GetCurrentLog().size(); i++)
	     {
	    	   
			    if(Data.GetPerformanceData().getFinalTimes()[i]>=minTimeValue && Data.GetPerformanceData().getFinalTimes()[i]<=maxTimeValue)
			    {    		 
	       		 KeptXTrace.add(Data.GetCurrentLog().get(i));
			    }
	
	    	 
	    	 
	     }
		 System.out.print("\n Quedaron:"+ KeptXTrace.size());
		 Data.SetCurrentLog( new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class)));
	     Data.GetCurrentLog().addAll(KeptXTrace);
	     Data.ResetPerformanceData();
         Data.UpdateGraph(true);

	}
}
