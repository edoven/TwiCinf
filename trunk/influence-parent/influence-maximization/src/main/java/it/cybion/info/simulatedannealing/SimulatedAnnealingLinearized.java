package it.cybion.info.simulatedannealing;


import it.cybion.info.simulatedannealing.strengthcalculator.SolutionStrengthCalculatorOptimized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;


public class SimulatedAnnealingLinearized
{
	private static final Logger logger = Logger.getLogger(SimulatedAnnealingLinearized.class);

	private float[] linearizedMatrix;
	private int matrixDim;
	private List<Integer> currentSolution;
	private float currentSolutionStrength;
	private Random random = new Random();
	private int[][] node2InNodes;
	private float[] singleNodeStrengths;
	private Map<Double,Double> solutionsStrengths = new HashMap<Double,Double>();
	private int addedNode;
	private int removedNode;

	
	public Map<Double,Double> getSolution(float[][] matrix, int solutionDim,
									 double TStart, double TFinal, 
									 int temperatureReductions,	int innerIterations)
	{					
		this.matrixDim = matrix.length;
		linearizeMatrix(matrix); //this has to do before other initiliazation methods
		this.node2InNodes = getNode2InNodes(matrixDim);		
		SolutionStrengthCalculatorOptimized solutionStrengthCalculatorOptimized = new SolutionStrengthCalculatorOptimized(linearizedMatrix,matrixDim);
		singleNodeStrengths = new float[matrixDim];
		calculateSingleNodeNormalizedStrengths();
		
		currentSolution = getBestStartingSolution(solutionDim);
		currentSolutionStrength = solutionStrengthCalculatorOptimized.getStartingSolutionStrength(currentSolution);
			
				
//		int temperatureReductions = getTemperatureReductionsCount(TStart, TFinal, TReductionScale);
		int solutionsCount = 0;
		float maxSolutionStrength = -1;
		List<Integer> tweakedSolution;
		float tweakedSolutionStrength;
		double jumpProbability;
		boolean solutionChanged;
		
		double TCurrent = getCurrentTemperature(0.0, TStart, TFinal, temperatureReductions);
		for (int i=0; i<temperatureReductions; i++)
		{
			logger.info(String.format("T=%5f (reductionsLeft=%d) - value=%7f - solution=%s",TCurrent, (temperatureReductions-i), currentSolutionStrength, currentSolution ));
			for (int iterationCount = 0; iterationCount < innerIterations; iterationCount++)
			{
				tweakedSolution = getTweakedSolution(currentSolution, matrixDim);	
				tweakedSolutionStrength = solutionStrengthCalculatorOptimized.getSolutionStrength(currentSolution,
						  tweakedSolution, 
						  addedNode, 
						  removedNode);
				solutionChanged = false;
				if (tweakedSolutionStrength >= currentSolutionStrength)
				{
					currentSolution = tweakedSolution;
					currentSolutionStrength = tweakedSolutionStrength;
					solutionChanged = true;
				} 
				else
				{
					jumpProbability = 1.0 / Math.exp((currentSolutionStrength - tweakedSolutionStrength)/ TCurrent);
//					logger.info("delta="+(currentSolutionStrength-tweakedSolutionStrength)+" - JP="+jumpProbability);
					if (jumpProbability > random.nextDouble())
					{				
						currentSolution = tweakedSolution;
						currentSolutionStrength = tweakedSolutionStrength;
						solutionChanged = true;
					}
				}
				if (currentSolutionStrength > maxSolutionStrength)
					maxSolutionStrength = currentSolutionStrength;
				if (solutionChanged == true)
					solutionStrengthCalculatorOptimized.setCurrentSolutionAsLastMeasuredSolution();
			}
			solutionsStrengths.put(new Double(solutionsCount++), new Double(currentSolutionStrength)); //this is for solutions graph plotting
			TCurrent = getCurrentTemperature(i, TStart, TFinal, temperatureReductions);
		}
		logger.info("maxSolutionStrength=" + maxSolutionStrength);
		
		return solutionsStrengths;
	}
	
	
	private int getTemperatureReductionsCount(float TStart, float TFinal, float reductionScale)
	{
		int reductions = 0;
		while (TStart>TFinal) 
		{		
			TStart = TStart * reductionScale;	
			reductions++;
		}
		return reductions;
	}
	
	
	private void calculateSingleNodeNormalizedStrengths()
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
	
	private List<Integer> getBestStartingSolution(int solutionDim)
	{
		float[] singleNodeStrengthsCopy = singleNodeStrengths.clone();	
		List<Integer> solution = new ArrayList<Integer>();
		for (int i = 0; i < solutionDim; i++)
		{
			int element = getNodeWithMaxStrength(singleNodeStrengthsCopy);
			while (solution.contains(element))
				element = getNodeWithMaxStrength(singleNodeStrengthsCopy);			
			singleNodeStrengthsCopy[element] = -1;
			solution.add(element);
		}
		return solution;
	}
	
	private int getNodeWithMaxStrength(float[] singleNodeStrengths)
	{
		float max = -1;
		int maxNodeIndex = 0;
		for (int i=0; i<singleNodeStrengths.length; i++)
		{
			if (singleNodeStrengths[i]>max)
			{
				max = singleNodeStrengths[i];
				maxNodeIndex = i;
			}
		}
		return maxNodeIndex;
	}
	

	//this creates a tweaked solution removing a node in the solution
	//and adding a node that is not in the solution
	private List<Integer> getTweakedSolution(List<Integer> currentSolution, int matrixDim)
	{
		List<Integer> tweakedSolution = new ArrayList<Integer>();
		tweakedSolution.addAll(currentSolution);
		int elementToRemoveIndex = random.nextInt(tweakedSolution.size());
		int elementToRemove = tweakedSolution.get(elementToRemoveIndex);
		tweakedSolution.remove(elementToRemoveIndex);
		int elementToAdd = random.nextInt(matrixDim);
		while (
			   singleNodeStrengths[elementToAdd] == 0 ||
			   currentSolution.contains(elementToAdd) || 		   		   
			   elementToAdd==elementToRemove ||
			   singleNodeStrengths[elementToAdd]<random.nextFloat() 
			   )
			elementToAdd = random.nextInt(matrixDim);
		tweakedSolution.add(elementToAdd);
		this.removedNode = elementToRemove;
		this.addedNode = elementToAdd;
		return tweakedSolution;
	}

	
	private int[][] getNode2InNodes(int matrixDim)
	{
		node2InNodes = new int[matrixDim][];
		for (int i=0; i<matrixDim; i++)
		{
			List<Integer> inNodes = new ArrayList<Integer>();
			for (int j=0; j<matrixDim; j++)
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
	
	
	private static double getCurrentTemperature(double currentIteration, double TStart, double TFinal, double iterationsCount)
	{
		double a = (1.0/(iterationsCount*iterationsCount))*Math.log(TStart/TFinal);
		System.out.println(a);
		return TStart*Math.pow(Math.E, -a*currentIteration*currentIteration);
	}
	
}
