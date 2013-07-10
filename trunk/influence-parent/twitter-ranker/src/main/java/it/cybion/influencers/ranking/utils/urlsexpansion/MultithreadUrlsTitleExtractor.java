package it.cybion.influencers.ranking.utils.urlsexpansion;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MultithreadUrlsTitleExtractor extends Thread
{

	private static final Logger logger = Logger.getLogger(MultithreadUrlsTitleExtractor.class);
	
	private static volatile List<String> urls;
	private static volatile Map<String, String> ulrs2Titles;

	public static Map<String, String> getTitles(List<String> urlsList)
	{
		urls = urlsList;
		ulrs2Titles = new HashMap<String, String>();
		List<Thread> threads = new ArrayList<Thread>();
		//1 thread per url
		for (int i = 0; i < urlsList.size(); i++)
		{
			Thread thread = new MultithreadUrlsTitleExtractor(i);
			thread.start();
			threads.add(thread);
		}

		for (Thread thread : threads)
		{
			try
			{
				thread.join();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return ulrs2Titles;
	}

	private MultithreadUrlsTitleExtractor(int threadId)
	{
		this.setName(Integer.toString(threadId));
	}

	public void run()
	{
		String url = urls.get(Integer.parseInt(Thread.currentThread().getName()));
		ulrs2Titles.put(url, getTitleFromUrl(url));
	}

	private String getTitleFromUrl(String urlString)
	{
		if (urlString.contains("instagr"))
			return getTitleFromInstagram(urlString);
		try
		{
			Document doc = Jsoup.connect(urlString).followRedirects(true).get();
			String title = doc.title();
			logger.debug("url:"+urlString+" - title:"+title);
//			if (title==null || title.equals(""))
//				logger.info("url="+urlString+" - title="+title);
			return title;
		} 
		catch (IOException e)
		{
			logger.debug("Error with jsoup and url: "+urlString);
			return "";
		}
		catch (IllegalArgumentException e)
		{

			logger.debug("Error with jsoup and url: "+urlString);
			return "";
		}
	}

	private String getTitleFromInstagram(String urlString)
	{
		Document doc;
		String title;
		try
		{
			doc = Jsoup.connect(urlString).timeout(3000).get();
			title = doc.getElementsByClass("caption-text").text();
			logger.debug("url:"+urlString+" - title:"+title);
			return title;
		} catch (IOException e)
		{
			logger.debug("Error with jsoup and url: "+urlString);
			return "";
		}
	}

}
