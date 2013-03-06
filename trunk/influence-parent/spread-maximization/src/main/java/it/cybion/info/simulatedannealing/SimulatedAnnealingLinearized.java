package it.cybion.info.simulatedannealing;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import scala.actors.threadpool.Arrays;


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
	private int removedNode; //node that is removed from current solution when tweaked solution is generated
	private int addedNode;//node that is added to current solution when tweaked solution is generated
	private float[] singleNodeStrengths;
	private Map<Integer,Float> currentSolutionDistOneNode2ActivationProbability = new HashMap<Integer,Float>();
	private Map<Integer,Float> tweakedSolutionDistOneNode2ActivationProbability = new HashMap<Integer,Float>();
	

	public Map<Integer,Float> getSolution(float[][] matrix, int solutionDim,
									 float TStart, float TFinal, 
									 float TReductionScale,	int innerIterations)
	{			
		Map<Integer,Float> solutionsStrengths = new HashMap<Integer,Float>();
		int solutionsCount = 0;
		
		this.solutionDim = solutionDim;
		this.matrixDim = matrix.length;
		linearizeMatrix(matrix);
		float maxSolutionStrength = -1;
		nodesToDistOneNodes = getNodesToDistOneNodes();
		node2InNodes = getNode2InNodes(matrix);
		singleNodeStrengths = new float[matrixDim];
		calculateSingleNodeStrengths();
		currentSolution = getStartingSolution();
		currentSolutionStrength = getInitialSolutionStrength(currentSolution);	
		
		
		float TCurrent = TStart;
		while (TCurrent > TFinal)
		{
			logger.info(String.format("T=%5f - value=%7f - solution=%s",TCurrent, currentSolutionStrength, currentSolution ));
			for (int iterationCount = 0; iterationCount < innerIterations; iterationCount++)
			{
				List<Integer> tweakedSolution = getTweakedSolution(currentSolution, matrixDim);	
				float tweakedSolutionStrength = getTweakedSolutionStrength(tweakedSolution);
				boolean solutionIsChanged = false;
				if (tweakedSolutionStrength >= currentSolutionStrength)
				{
					currentSolution = tweakedSolution;
					currentSolutionStrength = tweakedSolutionStrength;
					solutionIsChanged = true;
				} else
				{
					double jumpProbability = 1.0 / Math.exp((currentSolutionStrength - tweakedSolutionStrength)/ TCurrent);
//					jumpProbability = jumpProbability/15;
//					logger.info("delta="+(currentSolutionStrength-tweakedSolutionStrength));
//					logger.info("jumpProbability="+jumpProbability);
					if (jumpProbability > random.nextDouble())
					{				
						currentSolution = tweakedSolution;
						currentSolutionStrength = tweakedSolutionStrength;
						solutionIsChanged = true;
					}
				}
				if (currentSolutionStrength > maxSolutionStrength)
					maxSolutionStrength = currentSolutionStrength;
				if (solutionIsChanged==true)
					currentSolutionDistOneNode2ActivationProbability = tweakedSolutionDistOneNode2ActivationProbability;				
//				logger.info("currentSolutionStrength="+currentSolutionStrength);
				
			}
			solutionsStrengths.put(solutionsCount++, currentSolutionStrength);
			TCurrent = TCurrent * TReductionScale;
		}
		logger.info("maxSolutionStrength=" + maxSolutionStrength);
		
		return solutionsStrengths;
	}
	
	
	private void calculateSingleNodeStrengths()
	{
		float nodeStrength;
		float min = 999999, 
			  max = -999999;
		for (int nodeIndex=0; nodeIndex<matrixDim; nodeIndex++)
		{		
			nodeStrength = 0;
			int[] distOneNodes = node2InNodes[nodeIndex];
			if (distOneNodes.length>0)
				for (Integer distOneNodeIndex : distOneNodes)
					nodeStrength = nodeStrength + linearizedMatrix[nodeIndex*matrixDim + distOneNodeIndex];	
			singleNodeStrengths[nodeIndex] = nodeStrength;
			if (nodeStrength>max)
				max = nodeStrength;
			if (nodeStrength<min)
				min = nodeStrength;
		}
		for (int nodeIndex=0; nodeIndex<matrixDim; nodeIndex++)
		{
			nodeStrength = (singleNodeStrengths[nodeIndex]-min)/(max-min);
			singleNodeStrengths[nodeIndex] = nodeStrength;
			logger.info("node "+nodeIndex+" - strength="+nodeStrength);
		}			
	}

	
	private float getInitialSolutionStrength(List<Integer> solution)
	{
		float strength = solution.size();
		Set<Integer> distOneNodes = getDistOneNodes(solution);
		Map<Integer, Float> distOneNode2ActivationProbability = new HashMap<Integer,Float>();
		for (Integer distOneNodeIndex : distOneNodes)
		{
			float distOneNodeProbability = 1;
			int[] inNodesIndexes = node2InNodes[distOneNodeIndex];
			for (int i=0; i<inNodesIndexes.length; i++)
				if (solution.contains(i) && (linearizedMatrix[i*matrixDim + distOneNodeIndex] != 0) )
					distOneNodeProbability = distOneNodeProbability	* (1 - linearizedMatrix[i*matrixDim + distOneNodeIndex]);
			distOneNode2ActivationProbability.put(distOneNodeIndex, distOneNodeProbability);
			strength = strength + (1 - distOneNodeProbability);
		}
		currentSolutionDistOneNode2ActivationProbability.putAll(distOneNode2ActivationProbability);
		return strength;
	}
	
	
	private float getTweakedSolutionStrength(List<Integer> tweakedSolution)
	{
		float solutionStrength = tweakedSolution.size();
		Set<Integer> distOneNodes = getDistOneNodes(tweakedSolution);
		Map<Integer, Float> distOneNode2ActivationProbability = new HashMap<Integer,Float>();	
		List<Integer> unchangedDistOneNodes = getUnchangedDistOneNodesNodes(tweakedSolution);
		float distOneNodeProbability;
		for (Integer distOneNodeIndex : distOneNodes)
		{			
			if (unchangedDistOneNodes.contains(distOneNodeIndex))
				distOneNodeProbability = currentSolutionDistOneNode2ActivationProbability.get(distOneNodeIndex);
			else
			{
				distOneNodeProbability = 1.0F;
				int[] inNodesIndexes = node2InNodes[distOneNodeIndex];
				for (int i=0; i<inNodesIndexes.length; i++)
					if (tweakedSolution.contains(i) && linearizedMatrix[i*matrixDim + distOneNodeIndex] != 0) 
						distOneNodeProbability = distOneNodeProbability	* (1.0F - linearizedMatrix[i*matrixDim + distOneNodeIndex]);
			}
			distOneNode2ActivationProbability.put(distOneNodeIndex, distOneNodeProbability);
			solutionStrength = solutionStrength + (1.0F - distOneNodeProbability);
		}
		tweakedSolutionDistOneNode2ActivationProbability = new HashMap<Integer,Float>();
		tweakedSolutionDistOneNode2ActivationProbability.putAll(distOneNode2ActivationProbability);
		return solutionStrength;
	}

	
	private List<Integer> getUnchangedDistOneNodesNodes(List<Integer> tweakedSolution)
	{
		Set<Integer> distOneNodesTweakedSolution = getDistOneNodes(currentSolution);
		int[] removedNodeDistOneNodes = nodesToDistOneNodes[removedNode];
		int[] addedNodeDistOneNodes = nodesToDistOneNodes[addedNode];
		for (int i = 0; i < removedNodeDistOneNodes.length; i++)
			distOneNodesTweakedSolution.remove(removedNodeDistOneNodes[i]);	
		for (int i = 0; i < addedNodeDistOneNodes.length; i++)
			distOneNodesTweakedSolution.remove(addedNodeDistOneNodes[i]);
		return new ArrayList<Integer>(distOneNodesTweakedSolution);
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
	private List<Integer> getStartingSolution()
	{
		List<Integer> randomSolution = new ArrayList<Integer>();
		for (int i = 0; i < solutionDim; i++)
		{
			int element = random.nextInt(matrixDim);
			while (randomSolution.contains(element) ||
				   singleNodeStrengths[element]==0)
				element = random.nextInt(matrixDim);
			randomSolution.add(element);
		}
		return randomSolution;
	}

	//this creates a tweaked solution removing a node in the solution
	//and adding a node that is not in the solution
	private List<Integer> getTweakedSolution(List<Integer> currentSolution, int matrixDim)
	{
		List<Integer> tweakedSolution = new ArrayList<Integer>();
		tweakedSolution.addAll(currentSolution);
		int elementToRemove = random.nextInt(tweakedSolution.size());
		tweakedSolution.remove(elementToRemove);
		int elementToAdd = random.nextInt(matrixDim);
		while (
			   singleNodeStrengths[elementToAdd] == 0 ||
			   currentSolution.contains(elementToAdd) || 		   		   
			   elementToAdd==elementToRemove ||
			   singleNodeStrengths[elementToAdd]<random.nextFloat() 
			   )
			elementToAdd = random.nextInt(matrixDim);
		tweakedSolution.add(elementToAdd);
		addedNode = elementToAdd;
		removedNode = elementToRemove;
		return tweakedSolution;
	}

	
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
	
}
