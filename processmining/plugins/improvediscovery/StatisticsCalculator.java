package org.processmining.plugins.PromMasterPlugin.processmining.plugins.improvediscovery;

import java.util.Arrays;

public class StatisticsCalculator {


    double[] data;
    

    public StatisticsCalculator() 
    {
    }   

      double getMean(double[] data)
      {
        double sum = 0.0;
        for(double a : data)
            sum += a;
            return sum/data.length;
       }

        double getVariance(double[] data)
        {
            double mean = getMean(data);
            double temp = 0;
            for(double a :data)
                temp += (mean-a)*(mean-a);
                return temp/data.length;
        }

        double getStdDev(double[] data)
        {
            return Math.sqrt(getVariance(data));
        }

        public double median(double[] data) 
        {
               double[] b = new double[data.length];
               System.arraycopy(data, 0, b, 0, b.length);
               Arrays.sort(b);

               if (data.length % 2 == 0) 
               {
                  return (b[(b.length / 2) - 1] + b[b.length / 2]) / 2.0;
               } 
               else 
               {
                  return b[b.length / 2];
               }
        }

}
