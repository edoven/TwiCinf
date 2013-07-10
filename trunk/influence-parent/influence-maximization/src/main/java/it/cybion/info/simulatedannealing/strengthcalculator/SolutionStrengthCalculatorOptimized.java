package it.cybion.info.simulatedannealing.strengthcalculator;

import java.util.*;


public class SolutionStrengthCalculatorOptimized
{
//	private static final Logger logger = Logger.getLogger(SolutionStrengthCalculatorOptimizedVers2.class);
	
	private float[] linearizedMatrix;
	private int matrixDim;
	private int[][] nodesToDistOneNodes;
	private Map<Integer,Float> tweakedSolutionNode2ActivationProbability = new HashMap<Integer,Float>();
	private Map<Integer,Float> currentSolutionNode2ActivationProbability = new HashMap<Integer,Float>();


	public SolutionStrengthCalculatorOptimized(float[] linearizedMatrix,int matrixDim)
	{
		this.linearizedMatrix = linearizedMatrix;
		this.matrixDim = matrixDim;
		nodesToDistOneNodes = getNodesToDistOneNodes();
	}
	
	public float getSolutionStrength(List<Integer> currentSolution, 
									 List<Integer> tweakedSolution, 
									 int addedNode, 
									 int removedNode)
	{
		tweakedSolutionNode2ActivationProbability.clear();
		float solutionStrength = currentSolution.size();
		Set<Integer> distOneNodes = getDistOneNodes(tweakedSolution);		
		float nodeActivationProbability;
		List<Integer> unchangedDistOneNodes = getUnchangedDistOneNodes(currentSolution, addedNode, removedNode);
		for (Integer distOneNode : distOneNodes)
		{
			if (unchangedDistOneNodes.contains(distOneNode))
			{
				if (!currentSolutionNode2ActivationProbability.containsKey(distOneNode))
					System.out.println("ERROR");
				nodeActivationProbability = currentSolutionNode2ActivationProbability.get(distOneNode);
			}
			else
				nodeActivationProbability = calculateSingleNodeActivationProbability(tweakedSolution, distOneNode);
			solutionStrength = solutionStrength + nodeActivationProbability;
			tweakedSolutionNode2ActivationProbability.put(distOneNode, nodeActivationProbability);
		}
		return solutionStrength;
	}
	
	public void setCurrentSolutionAsLastMeasuredSolution()
	{
		currentSolutionNode2ActivationProbability.clear();
		currentSolutionNode2ActivationProbability.putAll(tweakedSolutionNode2ActivationProbability);
	}
	
	

	public float getStartingSolutionStrength(List<Integer> solution)
	{
		tweakedSolutionNode2ActivationProbability.clear();
		float solutionStrength = solution.size();
		Set<Integer> distOneNodes = getDistOneNodes(solution);		
		float nodeActivationProbability;
		for (Integer distOneNode : distOneNodes)
		{
			nodeActivationProbability = calculateSingleNodeActivationProbability(solution, distOneNode);
			currentSolutionNode2ActivationProbability.put(distOneNode, nodeActivationProbability);
			solutionStrength = solutionStrength + nodeActivationProbability;
		}
		return solutionStrength;
	}
	
	
	public Map<Integer,Float> getStartingSolutionActivationProbabilities(List<Integer> solution)
	{
		Map<Integer,Float> node2ActivationProbability = new HashMap<Integer,Float>();
		Set<Integer> distOneNodes = getDistOneNodes(solution);		
		float activationProbability;

		for (Integer distOneNode : distOneNodes)
		{			
			activationProbability = calculateSingleNodeActivationProbability(solution, distOneNode);
			node2ActivationProbability.put(distOneNode, activationProbability);
		}
		return node2ActivationProbability;
	}
	

	public float calculateSingleNodeActivationProbability(List<Integer> solution, int node)
	{
		float unActivationProability = 1;
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

	public List<Integer> getUnchangedDistOneNodes(List<Integer> currentSolution, int addedNode, int removedNode)
	{
		Set<Integer> currentSolutionDistOneNodes = getDistOneNodes(currentSolution);
		int[] addedNodeDistOneNodes = nodesToDistOneNodes[addedNode];
		int[] removedNodeDistOneNodes = nodesToDistOneNodes[removedNode];
		for (int i = 0; i < addedNodeDistOneNodes.length; i++)
			currentSolutionDistOneNodes.remove(addedNodeDistOneNodes[i]);
		for (int i = 0; i < removedNodeDistOneNodes.length; i++)
			currentSolutionDistOneNodes.remove(removedNodeDistOneNodes[i]);	
		currentSolutionDistOneNodes.remove(addedNode);
		currentSolutionDistOneNodes.remove(removedNode);
		return new ArrayList<Integer>(currentSolutionDistOneNodes);
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
	
}
