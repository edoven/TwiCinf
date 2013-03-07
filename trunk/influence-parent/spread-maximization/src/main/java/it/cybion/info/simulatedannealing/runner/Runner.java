package it.cybion.info.simulatedannealing.runner;

import it.cybion.info.plot.Plotter;
import it.cybion.info.simulatedannealing.SimulatedAnnealingLinearized;
import it.cybion.info.utils.MatrixGenerator;
import it.cybion.info.utils.SerializationManager;

import java.util.Map;


public class Runner {
	public static void main(String[] args) 
	{
//		float[][] adjacencyMatrix = MatrixGenerator.getRandomAdiacentMatrix(10, 30);	
//		printMatrix(adjacencyMatrix);
		
		
		
		float[][] adjacencyMatrix = getMatrixFromSerializedMatrix("/home/godzy/Desktop/graphBuilder/serialization/probabilityGraphMatrix.data");
		
		float TStart = 0.07F;
		float TFinal = 0.0001F;
		float TReductionScale = 0.99F;
		
		int innerIterations = 5000;
		int solutionDim = 5;
		
		
		Map<Integer,Float> solutionsStrengths = new SimulatedAnnealingLinearized().getSolution(adjacencyMatrix, solutionDim, 
											 TStart, TFinal,
											 TReductionScale, innerIterations);

	
    	Plotter.drawPlot(solutionsStrengths);
	}
	
	
	public static float[][] getMatrixFromSerializedMatrix(String filepath)
	{
		Float[][] floatObjAdjacencyMatrix = (Float[][])SerializationManager.deserializeObject(filepath);	
		float[][] adjacencyMatrix = new float[floatObjAdjacencyMatrix.length][floatObjAdjacencyMatrix.length];
		for (int i = 0; i < floatObjAdjacencyMatrix.length; i++)
			for (int j = 0; j < floatObjAdjacencyMatrix.length; j++)
				adjacencyMatrix[i][j] = floatObjAdjacencyMatrix[i][j];
		return adjacencyMatrix;
	}
		
	public static void printMatrix(float matrix[][])
	{
		for (int i = 0; i < matrix.length; i++) 
		{
			for (int j = 0; j < matrix.length; j++) 
				if ( matrix[i][j]==0)
					System.out.printf("-----\t");
				else
					System.out.printf("%.3f \t", matrix[i][j]);
			System.out.println("");
		}
	}

}
