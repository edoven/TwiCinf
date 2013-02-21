package it.cybion.info.simulated_annealing;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;


public class SimulatedAnnealing
{
	private static final Logger logger = Logger.getLogger(SimulatedAnnealing.class);

	private float[][] matrix;
	private int matrixDim;
	private int solutionDim;
	private List<Integer> currentSolution;
	private float currentSolutionStrength;
	private Random random = new Random();
	private int[][] nodesToDistOneNodes;
	

	public List<Integer> getSolution(float[][] matrix, int solutionDim,
									 float TStart, float TFinal, 
									 float TReductionScale,	int innerIterations)
	{
		float maxSolutionStrength = -1;
		this.matrix = matrix;
		this.solutionDim = solutionDim;
		this.matrixDim = matrix.length;
		nodesToDistOneNodes = getNodesToDistOneNodes();
		currentSolution = getRandomSolution();
		currentSolutionStrength = getSolutionStrength(currentSolution);
		
		
		float TCurrent = TStart;
		while (TCurrent > TFinal)
		{
			logger.info("####### TCurrent=" + TCurrent + " #######");
			logger.info(currentSolution + " - " + currentSolutionStrength);
			for (int iterationCount = 0; iterationCount < innerIterations; iterationCount++)
			{
				List<Integer> tweakedSolution = getTweakedSolution(currentSolution, matrixDim);
				float tweakedSolutionStrength = getSolutionStrength(tweakedSolution);
				if (tweakedSolutionStrength > currentSolutionStrength)
				{
					currentSolution = tweakedSolution;
					currentSolutionStrength = tweakedSolutionStrength;
				} else
				{
					double jumpProbability = 1.0 / Math.exp((currentSolutionStrength - tweakedSolutionStrength)
															 / TCurrent);

					// logger.info("delta="+(currentSolutionStrength-tweakedSolutionStrength));
					// logger.info("jumpProbability="+jumpProbability);
					if (jumpProbability > random.nextDouble())
					{
						
						currentSolution = tweakedSolution;
						currentSolutionStrength = tweakedSolutionStrength;
					}
				}
				if (currentSolutionStrength > maxSolutionStrength)
					maxSolutionStrength = currentSolutionStrength;

			}
			TCurrent = TCurrent * TReductionScale;
		}
		logger.info("maxSolutionStrength=" + maxSolutionStrength);
		return currentSolution;
	}

	
	private float getSolutionStrength(List<Integer> solution)
	{
		Set<Integer> distOneNodes = getDistOneNodes(solution);
		distOneNodes.removeAll(solution);

		float strength = solutionDim;
		for (Integer nodeIndex : distOneNodes)
		{
			float edgesTotalProbability = 1;
			for (int i = 0; i < matrixDim; i++)
			{
				if (solution.contains(i) && matrix[i][nodeIndex] != -1)
					edgesTotalProbability = edgesTotalProbability	* (1 - matrix[i][nodeIndex]);
			}
			strength = strength + (1 - edgesTotalProbability);
		}
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
				if ((matrix[node][neighbor] != -1))
					distOneNodes.add(neighbor);
			nodesToDistOneNodes[node] = new int[distOneNodes.size()];
			for (int i = 0; i < distOneNodes.size(); i++)
				nodesToDistOneNodes[node][i] = distOneNodes.get(i);
		}
		return nodesToDistOneNodes;
	}
	

	private List<Integer> getRandomSolution()
	{
		List<Integer> randomSolution = new ArrayList<Integer>();
		for (int i = 0; i < solutionDim; i++)
		{
			int element = random.nextInt(matrixDim);
			while (randomSolution.contains(element) == true)
				element = random.nextInt(matrixDim);
			randomSolution.add(element);
		}
		return randomSolution;
	}

	//this creates a tweaked solution removing a node in the solution
	//for a node that is not in the solution
	public List<Integer> getTweakedSolution(List<Integer> currentSolution, int matrixDim)
	{
		List<Integer> tweakedSolution = new ArrayList<Integer>();
		tweakedSolution.addAll(currentSolution);
		tweakedSolution.remove(random.nextInt(tweakedSolution.size()));
		int elementToAdd = random.nextInt(matrixDim);
		while (currentSolution.contains(elementToAdd))
			elementToAdd = random.nextInt(matrixDim);
		tweakedSolution.add(elementToAdd);
		return tweakedSolution;
	}

}
