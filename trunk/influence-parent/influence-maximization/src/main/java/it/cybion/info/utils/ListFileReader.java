package it.cybion.info.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		Path path = Paths.get(filePath);
		List<Long> longs = new ArrayList<Long>();
		Long currentLong;
	    try (Scanner scanner =  new Scanner(path, "UTF-8"))
	    {
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
	    return longs;
	}
}
