package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tail
{
	
	public static void main(String[] args) throws IOException
	{
		List<String> lines = getLastLines("/home/godzy/Desktop/log4j.properties",2);
		for (String string : lines)
		{
			System.out.println(string);
		}
	}
	
	
	public static List<String> getLastLines(String filePath, int n) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		int lineNumber = 0;
		List<String> lines = new ArrayList<String>();
		String line = reader.readLine();
		while (line != null) 
		{
			lines.add(lineNumber, line);
			line = reader.readLine();
			lineNumber++;
		}
		reader.close();
		
		if (lines.size()>n)
			lines = lines.subList(lines.size()-n, lines.size());
		return lines;
	}
}
