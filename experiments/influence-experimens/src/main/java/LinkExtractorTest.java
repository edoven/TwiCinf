import java.net.UnknownHostException;
import java.util.List;

import twitter4j.TwitterException;

import it.cybion.influencers.cache.TwitterFacadeFactory;
import it.cybion.influencers.crawler.utils.linkextractor.LinkExtractor;



public class LinkExtractorTest
{
	public static void main(String[] args) throws UnknownHostException, TwitterException
	{
		LinkExtractor linkExtractor = new LinkExtractor( TwitterFacadeFactory.getTwitterFacade());
		List<String> url = linkExtractor.getLinks(215728045L);
		for (String string : url)
			System.out.println(string);
	}
}
