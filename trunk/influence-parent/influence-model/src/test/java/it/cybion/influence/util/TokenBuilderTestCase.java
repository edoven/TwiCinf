package it.cybion.influence.util;

import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;

import it.cybion.influence.downloader.Token;



public class TokenBuilderTestCase {
		
	@Test
	public static void getTokenFromFormattedStringTEST() {
		Token token = TokenBuilder.getTokenFromFormattedString("aaa,bbb.");
		assertEquals(token.getTokenString(),"aaa");
		assertEquals(token.getSecretString(),"bbb");
	}
}
