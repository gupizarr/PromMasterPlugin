package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.filters;

import java.util.ArrayList;

import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XAttributeMapLazyImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.data.OLAPData;
import org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation.OLAPTimeTransformation;

/**
 *  Methods to filter log in time context
*/
public class TimeFilter {

	public TimeFilter() {
		// TODO Auto-generated constructor stub
	}
	
	public void Filter(double minTimeValue, double maxTimeValue, OLAPTimeTransformation Transformation, OLAPData Data)
	{
		 ArrayList<XTrace> KeptXTrace= new ArrayList<XTrace>();
		  double[] dataList= new double[Data.GetBaseLog().size()];
          int count=0;		

			 for(int i=0; i<Data.GetBaseLog().size(); i++)
		     {
				    if((Transformation.getFinalTimes()[i])>=minTimeValue && (Transformation.getFinalTimes()[i])<=maxTimeValue)
				    {    
				    	dataList[count]=(Transformation.getFinalTimes()[i]);
				    	count++;
		       		    KeptXTrace.add(Data.GetBaseLog().get(i));
				    }
		    	 
		     }
			
      
		
		 Data.SetCurrentLog( new XLogImpl(new XAttributeMapLazyImpl<XAttributeMapImpl>(XAttributeMapImpl.class)));
	     Data.GetCurrentLog().addAll(KeptXTrace);

	}

}
