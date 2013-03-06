package it.cybion.info.utils;

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
				adiacentMatrix[randomI][randomJ] = random.nextFloat();
				edgesCount--;
			}						
		}
		return adiacentMatrix;
	}
}
