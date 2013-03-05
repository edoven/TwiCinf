package it.cybion.info.simulatedannealing;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;


public class SimulatedAnnealingLinearized
{
	private static final Logger logger = Logger.getLogger(SimulatedAnnealingLinearized.class);

	private float[] linearizedMatrix;
	private int matrixDim;
	private int solutionDim;
	private List<Integer> currentSolution;
	private float currentSolutionStrength;
	private Random random = new Random();
	private int[][] nodesToDistOneNodes;
	private int[][] node2InNodes;
//	private float[] singleNodeStrengths;
	

	public List<Integer> getSolution(float[][] matrix, int solutionDim,
									 float TStart, float TFinal, 
									 float TReductionScale,	int innerIterations)
	{			
		this.solutionDim = solutionDim;
		this.matrixDim = matrix.length;
		linearizeMatrix(matrix);
		float maxSolutionStrength = -1;
		nodesToDistOneNodes = getNodesToDistOneNodes();
		node2InNodes = getNode2InNodes(matrix);
		currentSolution = getRandomSolution();
		currentSolutionStrength = getSolutionStrength(currentSolution);
		
		
//		singleNodeStrengths = new float[matrixDim];
//		calculateSingleNodeStrengths();
		
		float TCurrent = TStart;
		while (TCurrent > TFinal)
		{
			logger.info("####### TCurrent=" + TCurrent + " #######");
			logger.info(currentSolution + " - " + currentSolutionStrength);
//			logger.info("freeMen=" + Runtime.getRuntime().freeMemory()/(1024*1024));
			for (int iterationCount = 0; iterationCount < innerIterations; iterationCount++)
			{
				long startTime = System.currentTimeMillis();
				List<Integer> tweakedSolution = getTweakedSolution(currentSolution, matrixDim);
				float tweakedSolutionStrength = getSolutionStrength(tweakedSolution);
				if (tweakedSolutionStrength > currentSolutionStrength)
				{
					currentSolution = tweakedSolution;
					currentSolutionStrength = tweakedSolutionStrength;
				} else
				{
					double jumpProbability = 1.0 / Math.exp((currentSolutionStrength - tweakedSolutionStrength)/ TCurrent);

//					logger.info("delta="+(currentSolutionStrength-tweakedSolutionStrength));
//					logger.info("jumpProbability="+jumpProbability);
					if (jumpProbability > random.nextDouble())
					{				
						currentSolution = tweakedSolution;
						currentSolutionStrength = tweakedSolutionStrength;
					}
				}
				if (currentSolutionStrength > maxSolutionStrength)
					maxSolutionStrength = currentSolutionStrength;
				
				logger.info("currentSolutionStrength="+currentSolutionStrength);
				
				long endTime = System.currentTimeMillis();
				logger.info("Iteration time = "+(endTime-startTime));

			}
			TCurrent = TCurrent * TReductionScale;
		}
		logger.info("maxSolutionStrength=" + maxSolutionStrength);
		
		return currentSolution;
	}
	
	
//	private void calculateSingleNodeStrengths()
//	{
//		List<Integer> singleNodeSolution = new ArrayList<Integer>();
//		for (int i=0; i<matrixDim; i++)
//		{
//			if (i!=0)
//				singleNodeSolution.remove(0);
//			singleNodeSolution.add(0,i);
//			singleNodeStrengths[i] = getSolutionStrength(singleNodeSolution);
//			logger.info(singleNodeStrengths[i]);
//		}
//	}


	private int[][] getNode2InNodes(float[][] matrix)
	{
		node2InNodes = new int[matrixDim][];
		for (int i=0; i<matrix.length; i++)
		{
			List<Integer> inNodes = new ArrayList<Integer>();
			for (int j=0; j<matrix.length; j++)
				if (linearizedMatrix[i*matrixDim + j] != 0)
					inNodes.add(j);			
			int[] inNodesArray = new int[inNodes.size()];
			int arrayIndex=0;
			for (Integer inNode : inNodes)
				inNodesArray[arrayIndex++] = inNode;
			node2InNodes[i] = inNodesArray;
		}
		return node2InNodes;
	}


	private void linearizeMatrix(float[][] matrix)
	{
		linearizedMatrix = new float[matrix.length * matrix.length];
		int linearixedIndex = 0;
		for (int i=0; i<matrix.length; i++)
			for (int j=0; j<matrix.length; j++)
				linearizedMatrix[linearixedIndex++] = matrix[i][j];
	}

	
	private float getSolutionStrength(List<Integer> solution)
	{
		long startTime = System.currentTimeMillis();
		float strength = solution.size();
		Set<Integer> distOneNodes = getDistOneNodes(solution);
			
		for (Integer distOneNodeIndex : distOneNodes)
		{
			float edgesTotalProbability = 1;
			int[] inNodesIndexes = node2InNodes[distOneNodeIndex];
			for (int i=0; i<inNodesIndexes.length; i++)
			{
				if (solution.contains(i) && linearizedMatrix[i*matrixDim + distOneNodeIndex] != 0) 
					edgesTotalProbability = edgesTotalProbability	* (1 - linearizedMatrix[i*matrixDim + distOneNodeIndex]);
			}
			strength = strength + (1 - edgesTotalProbability);
		}
		long endTime = System.currentTimeMillis();
		logger.info("getSolutionStrength time = "+(endTime-startTime));
		return strength;
	}
	

	private Set<Integer> getDistOneNodes(List<Integer> solution)
	{
		Set<Integer> distOneNodes = new HashSet<Integer>();
		for (Integer solutionNode : solution)
		{
			int[] neighbors = nodesToDistOneNodes[solutionNode];
			for (int i = 0; i < neighbors.length; i++)
				distOneNodes.add(neighbors[i]);
		}
		distOneNodes.removeAll(solution);
		return distOneNodes;
	}

	
	//for each node this gives an array of his distance-one neighbors
	private int[][] getNodesToDistOneNodes()
	{
		int[][] nodesToDistOneNodes = new int[matrixDim][];
		for (int node=0; node<matrixDim; node++)
		{
			List<Integer> distOneNodes = new ArrayList<Integer>();
			for (int neighbor=0; neighbor<matrixDim; neighbor++)
				if ((linearizedMatrix[node*matrixDim + neighbor] != 0))
					distOneNodes.add(neighbor);
			nodesToDistOneNodes[node] = new int[distOneNodes.size()];
			for (int i = 0; i < distOneNodes.size(); i++)
				nodesToDistOneNodes[node][i] = distOneNodes.get(i);
		}
		return nodesToDistOneNodes;
	}	

	//this is used to generate the starting solution
	private List<Integer> getRandomSolution()
	{
		List<Integer> randomSolution = new ArrayList<Integer>();
		for (int i = 0; i < solutionDim; i++)
		{
			int element = random.nextInt(matrixDim);
			while (randomSolution.contains(element))
				element = random.nextInt(matrixDim);
			randomSolution.add(element);
		}
		return randomSolution;
	}

	//this creates a tweaked solution removing a node in the solution
	//for a node that is not in the solution
	private List<Integer> getTweakedSolution(List<Integer> currentSolution, int matrixDim)
	{
		List<Integer> tweakedSolution = new ArrayList<Integer>();
		tweakedSolution.addAll(currentSolution);
		int elementToRemove = random.nextInt(tweakedSolution.size());
		tweakedSolution.remove(elementToRemove);
		int elementToAdd = random.nextInt(matrixDim);
		while (currentSolution.contains(elementToAdd))
			elementToAdd = random.nextInt(matrixDim);
		tweakedSolution.add(elementToAdd);
		return tweakedSolution;
	}

}
