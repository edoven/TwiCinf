package it.cybion.info.simulatedannealing.tuning;

import it.cybion.info.plot.Plotter;

import java.util.HashMap;
import java.util.Map;

public class TemperatureReductionTest
{
	public static void main(String[] args)
	{
		float TStart = 2.0F;
		float TFinal = 0.005F;
		float TReductionScale = 0.995F;
		
		
		
		Map<Integer, Float> points = new HashMap<Integer,Float>();
		int x = 0;
		int reductions = 0;
		while (TStart>TFinal) 
		{
			points.put((x++), TStart);			
			TStart = TStart * TReductionScale;	
			reductions++;
		}
		System.out.println("reductions="+reductions);
		Plotter.drawPlot(points);			
	}
}
