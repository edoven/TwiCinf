package it.cybion.influence.downloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Token {
	
	private String tokenString;
	private String secretString;
	
	
	public Token(String tokenString, String secretString ) {
		this.tokenString = tokenString;
		this.secretString = secretString;
	}

	/*
	 * file format is one single line:
	 * token,secret.	 
	 */
	public Token(String filePath) {
		Token token = getTokenFromFile(filePath);
		if (token!=null) {
			this.tokenString = token.getTokenString();
			this.secretString = token.getSecretString();
		}
		
	}
	
	private Token getTokenFromFile(String filePath) {
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
	private Token getTokenFromFormattedString(String line) {
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

	public String getTokenString() {
		return tokenString;
	}

	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}

	public String getSecretString() {
		return secretString;
	}

	public void setSecretString(String secretString) {
		this.secretString = secretString;
	}

	
	
}
