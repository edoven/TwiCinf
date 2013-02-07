package it.cybion.influencers.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class MultithreadUrlsTitleExtractor extends Thread{
	
	private static volatile List<String> urls;
	private static volatile Map<String , String> ulrs2Titles;
	
	public static Map<String,String> getTitles(List<String> urlsList) {
		urls = urlsList;
		ulrs2Titles = new HashMap<String,String>();		
		int threadCount = 40;
		List<Thread> threads = new ArrayList<Thread>();
    	for (int i=0; i<threadCount; i++) {
    		Thread thread = new MultithreadUrlsTitleExtractor(i);
    		thread.start();
    		threads.add(thread);
    	}
    		
    	for (Thread thread : threads) {
    		try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}    
    	return ulrs2Titles;
	}
	
	private MultithreadUrlsTitleExtractor(int threadId) {
		System.out.println("Created thread with id="+threadId);
	}
		
	public void run() {  

		while (urls.size()>0) {
			String url = urls.remove(urls.size()-1);
			ulrs2Titles.put(url, getTitleFromUrl(url));
		}
	}
	
	private String getTitleFromUrl(String urlString) {		
		if (urlString.contains("instagr"))
			return getTitleFromInstagram(urlString);
		try {
			Document doc = Jsoup.connect(urlString).get();
			return doc.getElementsByTag("title").text();
		} catch (IOException e) {
			return "";
		}		
	}
			
	private String getTitleFromInstagram(String urlString) {		
		try {
			Document doc = Jsoup.connect(urlString).get();
			return doc.getElementsByClass("caption-text").text();
		} catch (IOException e) {
			return "";
		}		
	}
	
}
