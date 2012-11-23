package it.cybion.influence.util;

import it.cybion.influence.downloader.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TokenBuilder {
	
	
		
	public static Token getTokenFromFile(String filePath) {
        BufferedReader reader = null;
        String line = "";
        try {
			reader = new BufferedReader(new FileReader(filePath));
			line = reader.readLine(); 
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        if (line=="")
        	return null;
        return getTokenFromFormattedString(line);
    }
		
	//line format is "token,secret."
	//TODO : can be better (regex, etc..)
	public static Token getTokenFromFormattedString(String line) {
		int i=0;
        char c;
        String tokenString = "", secretString = "";
		while ( (c=line.charAt(i)) != ',' ) {
        	tokenString = tokenString + c;
			i++;
		}
		i++;
        while ( (c=line.charAt(i)) != '.' ) {
        	secretString = secretString + c;
        	i++;
        }
        return new Token(tokenString, secretString);
        
	}
}
