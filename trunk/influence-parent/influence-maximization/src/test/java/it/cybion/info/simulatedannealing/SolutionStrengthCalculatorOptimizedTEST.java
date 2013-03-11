package it.cybion.info.simulatedannealing;

import it.cybion.info.simulatedannealing.strengthcalculator.SolutionStrengthCalculatorOptimized;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;




public class SolutionStrengthCalculatorOptimizedTEST
{
	int matrixDim = 4;
	
	SolutionStrengthCalculatorOptimized calculator;
	
	
	@BeforeClass
	public void init()
	{
		float[] linearizedAdjacentMatrix = {0.0F, 0.0F, 0.0F, 0.2F, 
											0.1F, 0.0F, 0.1F, 0.0F, 
											0.0F, 0.0F, 0.0F, 0.0F, 
											0.0F, 0.0F, 0.0F, 0.0F};
		calculator = new SolutionStrengthCalculatorOptimized(linearizedAdjacentMatrix, matrixDim);
	}
	
	
	@Test
	public void getUnchangedNodesTEST()
	{
		
		
		List<Integer> currentSolution = new ArrayList<Integer>();
		currentSolution.add(0);
		currentSolution.add(1);
		List<Integer> unchangedDistOneNodes = calculator.getUnchangedDistOneNodes(currentSolution, 3, 0);
		Assert.assertEquals(unchangedDistOneNodes.size(), 1);
		Assert.assertEquals(unchangedDistOneNodes.contains(2), true);

//		currentSolution = new ArrayList<Integer>();
//		currentSolution.add(2);
//		currentSolution.add(3);
//		unchangedDistOneNodes = calculator.getUnchangedDistOneNodes(currentSolution, 4, 2);
//		Assert.assertEquals(unchangedDistOneNodes.size(), 0);

	}
	
}
