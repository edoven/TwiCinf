package it.cybion.influence.downloader;

import it.cybion.influence.util.TokenBuilder;

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
		Token token = TokenBuilder.getTokenFromFile(filePath);
		if (token!=null) {
			this.tokenString = token.getTokenString();
			this.secretString = token.getSecretString();
		}
		
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
