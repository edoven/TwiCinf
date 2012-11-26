package it.cybion.influence.util;

import it.cybion.influence.downloader.Token;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;



public class TokenBuilderTestCase {
		
	@Test
	public static void getTokenFromFormattedStringTEST() {
		Token token = TokenBuilder.getTokenFromFormattedString("aaa,bbb.");
		assertEquals(token.getTokenString(),"aaa");
		assertEquals(token.getSecretString(),"bbb");
	}

    
	@Test
	public static void getTokenFromFileTEST() {
		Token token = TokenBuilder.getTokenFromFile("src/test/resources/token.txt");
		assertEquals(token.getTokenString(),"962689441-yrFTbTzI3nAQ9sIMLnxLexyLWGAfZzhXCosTwuWp");
		assertEquals(token.getSecretString(),"elPwBu9NeAoGXunIl1wyPJDsSYgLWlFQXbXR8C2KQc");
	}
}
