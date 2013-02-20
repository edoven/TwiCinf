package simulated_annealing;

import java.util.Random;

public class SimultedAnnealingRunner {
	public static void main(String[] args) 
	{
		float[][] adiacentMatrix = createAdiacentMatrix(1000);
		//printMatrix(adiacentMatrix);
		
//		getSolution(float[][] matrix, int solutionDim, 
//				    float TStart, float TFinal, 
//				    float TReductionScale, int iterations)
		int solutionDim = 5;
		float TStart = 3.0F;
		float TFinal = 0.4F;
		float TReductionScale = 0.98F;
		int innerIterations = 1000;
		new SimulatedAnnealingLinearized().getSolution(adiacentMatrix, solutionDim, 
											 TStart, TFinal,
											 TReductionScale, innerIterations);
	}
	
	
	
	public static float[][] createAdiacentMatrix(int dim) 
	{
		float[][] adiacentMatrix = new float[dim][dim];
		Random random = new Random();
		for (int i = 0; i < adiacentMatrix.length; i++) 
		{
			for (int j = 0; j < adiacentMatrix.length; j++) 
			{
				if (i!=j && random.nextFloat()>0.85)
					adiacentMatrix[i][j] = random.nextFloat()/2;
				else
					adiacentMatrix[i][j] = -1;
			}
		}
		return adiacentMatrix;
	}
	
	
	
	public static void printMatrix(float matrix[][])
	{
		for (int i = 0; i < matrix.length; i++) 
		{
			for (int j = 0; j < matrix.length; j++) 
				if ( matrix[i][j]==-1)
					System.out.printf("-----\t");
				else
					System.out.printf("%.3f \t", matrix[i][j]);
			System.out.println("");
		}
	}
}
