package it.cybion.info.simulatedannealing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


public class SolutionStrengthCalculator
{
	private static final Logger logger = Logger.getLogger(SolutionStrengthCalculator.class);
	
	private float[] linearizedMatrix;
	private int matrixDim;
	private int[][] nodesToDistOneNodes;


	public SolutionStrengthCalculator(float[] linearizedMatrix,
									  int matrixDim)
	{
		this.linearizedMatrix = linearizedMatrix;
		this.matrixDim = matrixDim;
		nodesToDistOneNodes = getNodesToDistOneNodes();
	}
	
	
	public float getSolutionStrengthUnoptimized(List<Integer> solution)
	{
		float solutionStrength = solution.size();
		Set<Integer> distOneNodes = getDistOneNodes(solution);
		
		for (Integer distOneNodeIndex : distOneNodes)
		{			
			float unActivationProability = 1;
			//have to scan the column
			for (int columnIndex=0; columnIndex<matrixDim; columnIndex++)
			{
				if (solution.contains(columnIndex))
				{
					float edgeWeigth = linearizedMatrix[columnIndex*matrixDim + distOneNodeIndex];
					if (edgeWeigth>0)
						unActivationProability = unActivationProability * (1-edgeWeigth);
				}
			}		
			solutionStrength = solutionStrength + (1-unActivationProability);
		}
		logger.info("U) solution="+solution+" - distOneNodes="+distOneNodes.size()+" - strength="+solutionStrength);
		return solutionStrength;
	}
	
	
//	private float getTweakedSolutionStrengthOptimized(List<Integer> tweakedSolution)
//	{
//		float solutionStrength = tweakedSolution.size();
//		Set<Integer> distOneNodes = getDistOneNodes(tweakedSolution);
//		tweakedSolutionDistOneNode2ActivationProbability.clear();	
//		List<Integer> unchangedDistOneNodes = getUnchangedDistOneNodesNodes(tweakedSolution);
//		float distOneNodeActivationProbability;
//		for (Integer distOneNode : distOneNodes)
//		{	
//			if (unchangedDistOneNodes.contains(distOneNode))
//			{
//				distOneNodeActivationProbability = currentSolutionDistOneNode2ActivationProbability.get(distOneNode);
//			}
//			else
//			{			
//				float unActivationProability = 1;
//				//have to scan the column
//				for (int columnIndex=0; columnIndex<matrixDim; columnIndex++)
//				{
//					if (tweakedSolution.contains(columnIndex))
//					{
//						float edgeWeigth = linearizedMatrix[columnIndex*matrixDim + distOneNode];
//						if (edgeWeigth>0)
//							unActivationProability = unActivationProability * (1-edgeWeigth);
//					}
//				}
//				distOneNodeActivationProbability = 1-unActivationProability;			
//			}
//			tweakedSolutionDistOneNode2ActivationProbability.put(distOneNode, distOneNodeActivationProbability);
//			solutionStrength = solutionStrength + distOneNodeActivationProbability;		
//		}
////		currentSolutionDistOneNode2ActivationProbability = new HashMap<Integer,Float>();
////		currentSolutionDistOneNode2ActivationProbability.putAll(tweakedSolutionDistOneNode2ActivationProbability);
//		logger.info("O) solution="+tweakedSolution+" - distOneNodes="+distOneNodes.size()+" - strength="+solutionStrength);
//		return solutionStrength;
//	}
	
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
