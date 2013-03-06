package it.cybion.info.gephi;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GephiGraphFileGenerator
{
	
//	public static void main(String[] args)
//	{
//		Float[][] laPerla2800 = (Float[][])SerializationManager.deserializeObject("/home/godzy/Desktop/graphBuilder/serialization/probabilityGraphMatrix.data");
//		
//		printIntoGdfFileFormat(laPerla2800, "/home/godzy/Desktop/laPerla288.gdf");
//	}
	
//	private static void printIntoCsvFileFormat(float[][] matrix)
//	{	
//		System.out.println("===================");
//		System.out.println();
//		System.out.println();
//		
//		//prima riga
//		for (int i = 0; i < matrix.length; i++)
//		{
//			System.out.print(";"+i);
//		}
//		System.out.println("");
//		
//		for (int i = 0; i < matrix.length; i++)
//		{
//			String line = i+";"; //node name
//			for (int j = 0; j < matrix.length; j++)
//			{
//				line = line.concat(matrix[i][j]+";");
//			}
//			line = line.substring(0,line.lastIndexOf(";"));
//			System.out.println(line);
//		}
//		System.out.println();
//		System.out.println();
//		System.out.println("===================");
//	}
//	
//		
//	private static void printIntoTlpFileFormat(float[][] matrix, String filePath)
//	{	
//		
//		PrintWriter out = null;
//		try
//		{
//			out = new PrintWriter(new FileWriter(filePath), true);
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//			System.exit(0);
//		}
//		
//		out.println("(tlp \"2.0\"");
//		
//		//nodes
//		System.out.print("(nodes ");
//		for (int i = 0; i < matrix.length; i++)
//			out.print(i+" ");
//		out.println(")");
//		
//		int edgesCount = 0;
//		for (int i = 0; i < matrix.length; i++)
//			for (int j = 0; j < matrix.length; j++)
//				if (matrix[i][j]!=0)
//					out.println("(edge "+(edgesCount++)+" "+i+" "+j+")");
//		
//		out.println(")");	
//		out.close();
//	}
	
	
	
	//	nodedef>name VARCHAR,label VARCHAR
	//	s1,Site number 1
	//	s2,Site number 2
	//	s3,Site number 3
	//	edgedef>node1 VARCHAR,node2 VARCHAR, weight DOUBLE
	//	s1,s2,1.2341
	//	s2,s3,0.453
	//	s3,s2, 2.34
	//	s3,s1, 0.871	
	public static void printIntoGdfFileFormat(float[][] matrix, String filePath)
	{			
		PrintWriter out = null;
		try
		{
			out = new PrintWriter(new FileWriter(filePath), true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		//nodes
		out.println("nodedef>name VARCHAR");		
		for (int i = 0; i < matrix.length; i++)
			out.println(i);

		out.println("edgedef>node1 VARCHAR,node2 VARCHAR, weight DOUBLE");
		
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix.length; j++)
				if (matrix[i][j]!=0)
					out.println(i+","+j+","+matrix[i][j]);
		out.close();
	}
	
	public static void printIntoGdfFileFormat(Float[][] matrix, String filePath)
	{			
		PrintWriter out = null;
		try
		{
			out = new PrintWriter(new FileWriter(filePath), true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		//nodes
		out.println("nodedef>name VARCHAR");		
		for (int i = 0; i < matrix.length; i++)
			out.println(i);

		out.println("edgedef>node1 VARCHAR,node2 VARCHAR, weight DOUBLE");
		
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix.length; j++)
				if (matrix[i][j]!=0)
					out.println(i+","+j+","+matrix[i][j]);
		out.close();
	}
}
