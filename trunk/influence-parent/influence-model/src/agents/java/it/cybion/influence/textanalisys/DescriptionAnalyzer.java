package it.cybion.influence.textanalisys;

import it.cybion.influence.IO.MongoDbPersistenceFacade;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class DescriptionAnalyzer {
	
	public static void main(String[] args) throws UnknownHostException {
		MongoDbPersistenceFacade persistenceFacade = new MongoDbPersistenceFacade("localhost", "users", "twitterUsers");
		List<String> docs = persistenceFacade.getAllDocs();
		int good = 0;
		int empty = 0;
		for (String doc : docs) {
			DBObject json = (DBObject)JSON.parse(doc);
			if (json.containsField("comment")) {
				String description = (String)json.get("description");
				String name = (String)json.get("name");
				String screenName = (String)json.get("screen_name");
				int followersCount = (Integer)json.get("followers_count");
				int userId = (Integer)json.get("id");
				description = description.toLowerCase();
				if (description.length()<1) {
					empty++;
					//System.out.println("----------"+description);
				}
				else {
					if (description.contains("food") || 
						description.contains("cibo") ||
						description.contains("vino") ||
						description.contains("wine") ||
						description.contains("ristorante") ||
						description.contains("chef") ||
						description.contains("restourant") ||
						description.contains("sommelier") ||
						description.contains("degustazioni") ||
						description.contains("gastro")  ||
						description.contains("cuoc") ||
						description.contains("cucin") ||
						description.contains("cook") ||
						description.contains("gust") ||
						description.contains("vigna") ||
						description.contains("formagg") ||
						description.contains("gourmet") ||
						description.contains("bere") ||
						description.contains("vini") ||
						description.contains("cucina") ||
						description.contains("ricett") ||
						description.contains("distill") ||
						description.contains("birr") ||
						description.contains("restaurant") || 
						description.contains("sapor") || 
						description.contains("agricol") || 
						description.contains("mangi")) { 
							good++;
							description = description.replaceAll("\n", "/").
													  replaceAll(System.getProperty("line.separator"), "/");
							
							//System.out.println("("+screenName+" - "+name+" - "+followersCount+")__INIZIO__"+description+"__FINE__");
							System.out.println(userId);
					}
					
//					else {
//						description = description.replaceAll("\n", "/").
//							  replaceAll(System.getProperty("line.separator"), "/");
//						//System.out.println("("+screenName+" - "+name+" - "+followersCount+")__INIZIO__"+description+"__FINE__");
//					}
				}
			}
		}
		System.out.println("good="+good);
		System.out.println("empty="+empty);

			
		
		
	}
	
	
}
