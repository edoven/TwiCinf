package it.cybion.influencers.ranking.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ListFileReader
{
//	public static void main(String[] args)
//	{
//		List<Long> longs = ListFileReader.readLongListFile("/home/godzy/Desktop/laPerla2800UsersIds.txt");
//		System.out.println(longs.get(43));
//	}
	
	
	public static List<Long> readLongListFile(String filePath)
	{
		List<Long> longs = new ArrayList<Long>();
		Long currentLong;
		Scanner scanner = null;
	    try
	    {
	    	scanner = new Scanner(new File(filePath), "UTF-8");
	    	while (scanner.hasNextLine())
	    	{
	    		currentLong = Long.parseLong(scanner.nextLine());
	    		longs.add(currentLong);
	    	}	    	   
	    }
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	    if (scanner!=null)
	    	scanner.close();
	    return longs;
	}
}
