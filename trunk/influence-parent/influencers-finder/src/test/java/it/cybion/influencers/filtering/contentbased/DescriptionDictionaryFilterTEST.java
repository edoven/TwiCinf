package it.cybion.influencers.filtering.contentbased;


import it.cybion.influencers.filtering.contentbased.DescriptionDictionaryFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;


public class DescriptionDictionaryFilterTEST {

	@Test
	public void filterTEST() {
		Map<Long, String> user2description = new HashMap<Long, String>();
		user2description.put(1l, "Me piace da beve e da magna'");
		user2description.put(2l, "Politico e giornalista, curatore del blog blabla");
		user2description.put(3l, "Vino");
		user2description.put(4l, "seguitemi seguitemi");
		user2description.put(5l, "Chef presso il ristorante xxx");
		user2description.put(6l, "Er vino della cantina sociale");
		
		List<String> dictionary = new ArrayList<String>();
		dictionary.add("vino");
		dictionary.add("chef");
		dictionary.add("ristorante");
		
		DescriptionDictionaryFilter filter = new DescriptionDictionaryFilter(dictionary,user2description);
		List<Long> goodUsers = filter.filter();
		
		assertEquals(goodUsers.size(), 3);
		assertEquals(goodUsers.contains(3l) , true);
		assertEquals(goodUsers.contains(5l) , true);
		assertEquals(goodUsers.contains(6l) , true);
	}

}
