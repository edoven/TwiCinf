package it.cybion.info.simulatedannealing.tuning;

import it.cybion.info.plotter.Plotter;

import java.util.HashMap;
import java.util.Map;

public class TemperatureReductionTest
{
//	public static void main(String[] args)
//	{
//		double TStart = 2.0;
//		double TFinal = 0.0001;
//		double TReductionScale = 0.95;
//		
//		
//		
//		Map<Double, Double> points = new HashMap<Double,Double>();
//		double x = 0;
//		int reductions = 0;
//		double TCurrent = TStart;
//
//		while (TCurrent>TFinal) 
//		{
//			points.put((x++), TCurrent);			
//			TCurrent = (TCurrent * TReductionScale);	
//			reductions++;
//		}
//		System.out.println("reductions="+reductions);
//		Plotter.drawPlot(points, "Temperature Reduction Function");			
//	}
	
	public static void main(String[] args)
	{
		double TStart = 2.0;
		double TFinal = 0.0001;
		double iterations = 100;
		
		
		
		
		Map<Double, Double> pointsF1 = new HashMap<Double,Double>();
		Map<Double, Double> pointsF2 = new HashMap<Double,Double>();
		Map<Double, Double> pointsF3 = new HashMap<Double,Double>();
		Map<Double, Double> pointsF4 = new HashMap<Double,Double>();
		double f1,f2,f3,f4;
		for (double i=0; i<iterations; i++)
		{
			f1 = f1(i, TStart,TFinal,iterations);
			pointsF1.put(new Double(i), f1);	
			f2 = f2(i, TStart,TFinal,iterations);
			pointsF2.put(new Double(i), f2);
			f3 = f3(i, TStart,TFinal,iterations);
			pointsF3.put(new Double(i), f3);
			f4 = f4(i, TStart,TFinal,iterations);
			pointsF4.put(new Double(i), f4);
			System.out.println(f1+" - "+f2+" - "+f3);

		}
		Plotter.drawPlot(pointsF1, "f1");		
		Plotter.drawPlot(pointsF2, "f2");		
		Plotter.drawPlot(pointsF3, "f3");		
		Plotter.drawPlot(pointsF4, "f4");
	}
	
	private static double f1(double currentIteration, double TStart, double TFinal, double iterationsCount)
	{
		return (1.0/2.0)*(TStart-TFinal)*(1.0+Math.cos(currentIteration*Math.PI/iterationsCount)+TFinal);
	}
	
	private static double f2(double currentIteration, double TStart, double TFinal, double iterationsCount)
	{
		return TStart*Math.pow((TFinal/TStart), currentIteration/iterationsCount);
	}
	
	private static double f3(double currentIteration, double TStart, double TFinal, double iterationsCount)
	{
		double a = (1.0/(iterationsCount*iterationsCount))*Math.log(TStart/TFinal);
		System.out.println(a);
		return TStart*Math.pow(Math.E, -a*currentIteration*currentIteration);
	}
	
	private static double f4(double currentIteration, double TStart, double TFinal, double iterationsCount)
	{
		double a = (1.0/(iterationsCount))*Math.log(TStart/TFinal);
		System.out.println(a);
		return TStart*Math.pow(Math.E, -a*currentIteration);
	}
	
	
}
