package it.cybion.influence.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class InputReader {
	
	public static String fileContentToSingleLine(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        reader.close();
        return stringBuilder.toString();
    }
}
