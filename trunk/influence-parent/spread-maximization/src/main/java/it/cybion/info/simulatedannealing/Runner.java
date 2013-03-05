package it.cybion.info.simulatedannealing;

import java.util.Random;


public class Runner {
	public static void main(String[] args) 
	{
		float[][] adjacencyMatrix = createAdiacentMatrix(5000);
//		Float[][] floatObjAdjacencyMatrix = (Float[][])SerializationManager.deserializeObject("/home/godzy/Desktop/graphBuilder/serialization/probabilityGraphMatrix.data");
//		
//		float[][] adjacencyMatrix = new float[floatObjAdjacencyMatrix.length][floatObjAdjacencyMatrix.length];
//		for (int i = 0; i < floatObjAdjacencyMatrix.length; i++)
//			for (int j = 0; j < floatObjAdjacencyMatrix.length; j++)
//				adjacencyMatrix[i][j] = floatObjAdjacencyMatrix[i][j];
		
		//printMatrix(adiacentMatrix);
	
		
		int solutionDim = 300;
		float TStart = 3.0F;
		float TFinal = 0.2F;
		float TReductionScale = 0.98F;
		int innerIterations = 1000;
		
		
		
		//printMatrix(adjacencyMatrix);
		new SimulatedAnnealingLinearized().getSolution(adjacencyMatrix, solutionDim, 
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
				if (i!=j && random.nextFloat()>0.8)
					adiacentMatrix[i][j] = random.nextFloat()/2;
				else
					adiacentMatrix[i][j] = 0;
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
