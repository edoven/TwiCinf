package it.cybion.info.simulatedannealing.runner;

import it.cybion.info.plotter.Plotter;
import it.cybion.info.simulatedannealing.SimulatedAnnealingLinearized;
import it.cybion.info.utils.SerializationManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CommandLineRunner
{
	/**
	 * 
	 * PROPERTIES FILE EXAMPLE:
	 * 
	adjacentMatrixFile=/home/godzy/Desktop/graphBuilder/serialization/probabilityGraphMatrix.data
	TStart = 1.0
	TFinal = 0.0001
	temperatureReductions = 100
	innerIterations = 5000
	solutionDim = 20
	 * 
	 * 
	 */
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
//		args = new String[1];
//		args[0] = "/home/godzy/Desktop/laPerlaSimulated.properties";

		
		if (args.length!=1)
		{
			System.out.println("Error. Usage \"java -jar <this-jar> propertiesFilePath\"");
			System.exit(0);
		}
		String propertiesFile = args[0];
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
		
		String adjacentMatrixFile = (String)properties.get("adjacentMatrixFile");
		float TStart = Float.parseFloat((String)properties.get("TStart"));
		float TFinal = Float.parseFloat((String)properties.get("TFinal"));
		int temperatureReductions = Integer.parseInt((String)properties.get("temperatureReductions"));
		int innerIterations = Integer.parseInt((String)properties.get("innerIterations"));
		int solutionDim = Integer.parseInt((String)properties.get("solutionDim"));
		
		String blacklistString = (String)properties.get("blacklist");
		String[] blacklistElements = blacklistString.split(",");
		List<Integer> blacklist = new ArrayList<Integer>();
		for (String blacklistElement : blacklistElements)
			blacklist.add(Integer.parseInt(blacklistElement));
	
		float[][] adjacencyMatrix = getMatrixFromSerializedMatrix(adjacentMatrixFile);
		
		Map<Double,Double> solutionsStrengths = new SimulatedAnnealingLinearized().getSolution(adjacencyMatrix, solutionDim, 
																								TStart, TFinal,
																								temperatureReductions, innerIterations,
																								blacklist);


		Plotter.drawPlot(solutionsStrengths, "Solution Strength Evolution");
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
}
