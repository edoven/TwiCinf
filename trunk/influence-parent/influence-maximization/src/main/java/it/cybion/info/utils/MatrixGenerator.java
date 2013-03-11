package it.cybion.info.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MatrixGenerator
{
	public static float[][] getRandomAdiacentMatrix(int dim, int edgesCount) 
	{
		float[][] adiacentMatrix = new float[dim][dim];
		Random random = new Random();
		for (int i = 0; i < adiacentMatrix.length; i++) 
			for (int j = 0; j < adiacentMatrix.length; j++) 
			adiacentMatrix[i][j] = 0;
		int randomI, randomJ;
		while (edgesCount>0)
		{
			randomI=random.nextInt(dim);
			randomJ=random.nextInt(dim);
			while( randomJ == randomI)
				randomJ=random.nextInt(dim);
			if (adiacentMatrix[randomI][randomJ] == 0)
			{
				float probability = random.nextFloat();
				BigDecimal fd = new BigDecimal(probability);
				BigDecimal cutted = fd.setScale(1, RoundingMode.DOWN);
				probability = cutted.floatValue();
				adiacentMatrix[randomI][randomJ] = probability;
				edgesCount--;
			}						
		}
		return adiacentMatrix;
	}
}
