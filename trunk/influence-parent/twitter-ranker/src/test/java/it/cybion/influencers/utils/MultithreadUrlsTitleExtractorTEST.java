package it.cybion.influencers.utils;


import it.cybion.influencers.ranking.utils.urlsexpansion.MultithreadUrlsTitleExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;



public class MultithreadUrlsTitleExtractorTEST
{

	@Test
	public void getTitlesTEST()
	{
		String url1 = "http://www.repubblica.it";
		String url2 = "http://www.yahoo.it";
		String url3 = "http://www.google.com";
		String url4 = "https://dev.twitter.com/console";
		String url5 = "https://github.com";
		String url6 = "http://blog.lifehacker.com/";
		List<String> urls = new ArrayList<String>();
		urls.add(url1);
		urls.add(url2);
		urls.add(url3);
		urls.add(url4);
		urls.add(url5);
		urls.add(url6);

		Map<String, String> urls2titles = MultithreadUrlsTitleExtractor.getTitles(urls);
//		System.out.println("urls2titles=" + urls2titles);
		for (String url : urls2titles.keySet())
			System.out.println(url + " - " + urls2titles.get(url));
	}
}
