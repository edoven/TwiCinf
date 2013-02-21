package it.cybion.info.simulated_annealing;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;


public class SimulatedAnnealingCompressed
{
	private static final Logger logger = Logger.getLogger(SimulatedAnnealingCompressed.class);

	private float[][] matrix;
	private int matrixDim;
	private int solutionDim;
	private List<Integer> currentSolution;
	private float currentSolutionStrength;
	private Random random = new Random();
	private int[][] nodesToDistOneNodes;
	
	//these are for column compressed matrix handling
	private float[] vals;
	private int[] cols;
	private int[] rows;

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
		buildColumnCompressedMatrix(matrix);
		matrix = null;
		currentSolutionStrength = getSolutionStrength(currentSolution);
				
		float TCurrent = TStart;
		while (TCurrent > TFinal)
		{
			logger.info("####### TCurrent=" + TCurrent + " #######");
			logger.info(currentSolution + " - " + currentSolutionStrength);
			logger.info("freeMen=" + Runtime.getRuntime().freeMemory()/(1024*1024));
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
//				logger.info(currentSolution+" - "+currentSolutionStrength);

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
		float strength = 0;
		for (Integer nodeIndex : distOneNodes)
			strength = strength + (1 - getNodeProbability(nodeIndex, distOneNodes ) );
		return solutionDim + strength;
	}
	

	private float getNodeProbability(int nodeIndex, Set<Integer> distOneNodes)
	{
		int colIndex = 0;
		while (colIndex<vals.length && cols[colIndex]<nodeIndex)
			colIndex++;
		
		float nodeProbability = 1;
		while (colIndex<vals.length && cols[colIndex]==nodeIndex)
		{
			nodeProbability = nodeProbability * (1-vals[colIndex]);
			colIndex++;
		}	
		return nodeProbability;
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
	
	
	private void buildColumnCompressedMatrix(float[][] matrix)
	{
		int dim = matrix.length;
		int count = 0;
		for (int i=0; i<dim; i++)
			for (int j=0; j<dim; j++)
				if (matrix[i][j]!=-1)
					count++;		
		vals = new float[count];
		cols = new int[count];
		rows = new int[count];		
		count = 0;
		for (int col=0; col<dim; col++)
			for (int row=0; row<dim; row++)
				if (matrix[row][col]!=-1)
				{
					vals[count] = matrix[row][col];
					cols[count] = col;
					rows[count] = row;
					count++;
				}
		System.out.println("Elementi non nulli = "+count);
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
