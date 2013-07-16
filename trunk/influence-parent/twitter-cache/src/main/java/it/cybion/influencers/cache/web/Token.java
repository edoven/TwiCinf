package it.cybion.influencers.cache.web;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * File format is (property-like):
 * 
 * token_string=xxxx
 * token_secret=yyyy
 * 
 */

public class Token
{
	private static final Logger LOGGER = Logger.getLogger(Token.class);
	
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
        //TODO use getResourceAsStream
		Properties properties = new Properties();
		try
		{
			properties.load(new FileInputStream(filePath));
		}
		catch (IOException e)
		{
			LOGGER.error("can't read token properties from " + filePath);
			e.printStackTrace();
		}
		String tokenString = properties.getProperty("token_string");
		String tokenSecret = properties.getProperty("token_secret");

        if (tokenString == null || tokenSecret == null) {
            String message = "Error, token_string or token_secret not present in " + filePath;
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
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
