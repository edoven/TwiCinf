package it.cybion.info.simulatedannealing.tuning;

import it.cybion.info.plot.Plotter;

import java.util.HashMap;
import java.util.Map;

public class AcceptanceFunctionTest
{
	public static double TEMPERATURE_MAX = 1.0;
	public static double TEMPERATURE_MIN = 0.01;
	public static double DELTA_MIN = 0.0001;
	public static double DELTA_MAX = 5;
	
	
	public static void main(String[] args)
	{
		double jumpProbability;
		double delta;
		double deltaIncrement = (DELTA_MAX+DELTA_MIN) / 100;
		int temperatureDecrementations = 10;
		double temperatureDecrement = (TEMPERATURE_MAX - TEMPERATURE_MIN)/ temperatureDecrementations;
		Map<Double,Double> points;
		
		for (double temperature=TEMPERATURE_MAX; temperature>TEMPERATURE_MIN; temperature=temperature-temperatureDecrement)
		{
			points = new HashMap<Double,Double>();
			for (delta=DELTA_MIN; delta<DELTA_MAX; delta=delta+deltaIncrement)
			{
				jumpProbability = 1.0 / Math.exp(delta/ temperature);
				System.out.printf("T=%4f - delta=%5f -  jumpProb=%5f \n", temperature, delta, jumpProbability  );
				points.put(delta, jumpProbability);
				
			}
			Plotter.drawPlot(points, "Temperature "+temperature);
			System.out.println("");		
		}
	}
}
