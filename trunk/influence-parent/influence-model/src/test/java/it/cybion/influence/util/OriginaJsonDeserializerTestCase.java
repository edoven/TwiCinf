package it.cybion.influence.util;

import it.cybion.influence.IO.InputReader;
import it.cybion.influence.model.*;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.testng.Assert.assertEquals;


public class OriginaJsonDeserializerTestCase {
	
	private OriginalJsonDeserializer jsonDeserializer;

    private static final Logger logger = Logger.getLogger(OriginaJsonDeserializerTestCase.class);
	
	@BeforeClass
	public void setup() throws IOException
	{
		jsonDeserializer = new OriginalJsonDeserializer();
        logger.info("started");
	}

    @AfterClass
	public void shutdown()
	{
    	jsonDeserializer = null;
	}


	@Test
	public void shouldDeserializeJsonToObject1() throws ParseException
	{
		String jsonString = null;
		
		try {
			jsonString = InputReader.fileContentToSingleLine("src/test/resources/raw_json_user1.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		User user = jsonDeserializer.deserializeJsonStringsToUser(jsonString);
        assertEquals(user.getId(), 6830542);
        assertEquals(user.getName(), "Francesca Fiorini");
        assertEquals(user.getScreenName(), "Frannina");
        assertEquals(user.getLocation(), "Parma");
        assertEquals(user.isContributorsEnabled(), false);
        assertEquals(user.getUrl().toString(), "http://uccidiungrissino.com");
        assertEquals(user.isProtected(), false);
        assertEquals(user.getFollowersCount(), 8205);
        assertEquals(user.getFriendsCount(), 822);
        assertEquals(user.getCreatedAt(), DataParser.parseOriginalTwitterData("Fri Jun 15 10:30:09 +0000 2007"));
        
        assertEquals(user.getFavouritesCount(), 585);
        assertEquals(user.getLang(), "en");
        assertEquals(user.getStatusesCount(), 54594);
        assertEquals(user.getListedCount(), 123);
	}
    
}
