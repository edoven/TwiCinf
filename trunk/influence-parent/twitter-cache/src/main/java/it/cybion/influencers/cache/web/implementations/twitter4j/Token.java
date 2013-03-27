package it.cybion.influencers.cache.web.implementations.twitter4j;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/*
 * File format is (property-like):
 * 
 * token_string=xxxx
 * token_secret=yyyy
 * 
 */

public class Token
{
	private static final Logger logger = Logger.getLogger(Token.class);
	
	private String tokenString;
	private String secretString;

	public Token(String tokenString, String secretString)
	{
		this.tokenString = tokenString;
		this.secretString = secretString;
	}

	
	public Token(String filePath)
	{
		Token token = buildTokenFromFile(filePath);
		if (token != null)
		{
			this.tokenString = token.getTokenString();
			this.secretString = token.getSecretString();
		}
	}

	private Token buildTokenFromFile(String filePath)
	{
		Properties properties = new Properties();
		try
		{
			properties.load(new FileInputStream(filePath));
		}
		catch (IOException e)
		{
			logger.info("Error with file "+filePath);
			e.printStackTrace();
			System.exit(0);
		}
		String tokenString = properties.getProperty("token_string");
		String tokenSecret = properties.getProperty("token_secret");
		if (tokenString==null || tokenSecret==null)
		{
			logger.info("Error, token_string or token_secret not present in "+filePath);
			System.exit(0);
		}
		return new Token(tokenString, tokenSecret);
		
	}

	public String getTokenString()
	{
		return tokenString;
	}

	public void setTokenString(String tokenString)
	{
		this.tokenString = tokenString;
	}

	public String getSecretString()
	{
		return secretString;
	}

	public void setSecretString(String secretString)
	{
		this.secretString = secretString;
	}

}
