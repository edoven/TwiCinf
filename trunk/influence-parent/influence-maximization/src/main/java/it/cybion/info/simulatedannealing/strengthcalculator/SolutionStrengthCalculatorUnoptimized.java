package it.cybion.info.simulatedannealing.strengthcalculator;

import org.apache.log4j.Logger;

import java.util.*;


public class SolutionStrengthCalculatorUnoptimized
{
	private static final Logger logger = Logger.getLogger(SolutionStrengthCalculatorUnoptimized.class);
	
	private float[] linearizedMatrix;
	private int matrixDim;
	private int[][] nodesToDistOneNodes;
	private Map<Integer,Float> activationProbabilities;


	public SolutionStrengthCalculatorUnoptimized(float[] linearizedMatrix,
									  int matrixDim)
	{
		this.linearizedMatrix = linearizedMatrix;
		this.matrixDim = matrixDim;
		nodesToDistOneNodes = getNodesToDistOneNodes();
	}
	
	
	public float getSolutionStrength(List<Integer> solution)
	{
		float solutionStrength = solution.size();
		Set<Integer> distOneNodes = getDistOneNodes(solution);	
		float activationProbability;
		activationProbabilities = new HashMap<Integer,Float>();
		for (Integer distOneNodeIndex : distOneNodes)
		{
			activationProbability = getNodeActivationProbability(solution, distOneNodeIndex);
			solutionStrength = solutionStrength + activationProbability;
			activationProbabilities.put(distOneNodeIndex,activationProbability);
		}
//		logger.info("U) solution="+solution+" - distOneNodes="+distOneNodes.size()+" - strength="+solutionStrength);
		return solutionStrength;
	}
	
	private float getNodeActivationProbability(List<Integer> solution, int node)
	{
		float unActivationProability = 1;
		//have to scan the column
		for (int columnIndex=0; columnIndex<matrixDim; columnIndex++)
		{
			if (solution.contains(columnIndex))
			{
				float edgeWeigth = linearizedMatrix[columnIndex*matrixDim + node];
				if (edgeWeigth>0)
					unActivationProability = unActivationProability * (1-edgeWeigth);
			}
		}	
		return (1-unActivationProability);
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
	
	
	public Map<Integer,Float> getActivationProbabilities()
	{
		return this.activationProbabilities;
	}
}
