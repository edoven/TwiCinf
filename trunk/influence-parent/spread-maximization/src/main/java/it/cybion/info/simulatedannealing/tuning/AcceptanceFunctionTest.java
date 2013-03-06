package it.cybion.info.simulatedannealing.tuning;

public class AcceptanceFunctionTest
{
	public static float TEMPERATURE_MAX = 1.0F;
	public static float TEMPERATURE_MIN = 0.01F;
	public static float DELTA_MIN = 0.0001F;
	public static float DELTA_MAX = 2;
	
	
	public static void main(String[] args)
	{
		double jumpProbability;
		float delta;
		float deltaIncrement = (DELTA_MAX+DELTA_MIN) / 100;
		int temperatureDecrementations = 10;
		float temperatureDecrement = (TEMPERATURE_MAX - TEMPERATURE_MIN)/ temperatureDecrementations;
		for (delta=DELTA_MIN; delta<DELTA_MAX; delta=delta+deltaIncrement)
		{
			for (float temperature=TEMPERATURE_MAX; temperature>TEMPERATURE_MIN; temperature=temperature-temperatureDecrement)
			{
				jumpProbability = 1.0 / Math.exp(delta/ temperature);
				System.out.printf("delta=%5f -  T=%4f - jumpProb=%5f \n", delta, temperature, jumpProbability  );
			}
			System.out.println("");			
		}
		
	}
}
