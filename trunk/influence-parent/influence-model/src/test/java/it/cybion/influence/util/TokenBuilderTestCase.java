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

    //TODO add a test this method too: TokenBuilder.getTokenFromFile(consumerTokenFilePath);

}
