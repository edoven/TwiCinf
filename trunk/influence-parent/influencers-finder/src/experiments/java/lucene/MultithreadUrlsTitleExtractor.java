package lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class MultithreadUrlsTitleExtractor extends Thread{
	
	private static List<String> urls;
	private static Map<String , String> ulrs2Titles = new HashMap<String,String>();
	
	private int threadId;
	private int fistElement;
	private int lastElement;
	
	
	private MultithreadUrlsTitleExtractor(int threadId , int fistElement, int lastElement) {
		System.out.println("Created thread with id="+threadId+" - fistElement="+fistElement+" - lastElement="+lastElement);
		this.threadId = threadId;
		this.fistElement = fistElement;
		this.lastElement = lastElement;
	}
	
	public void run() {        
        for (int i=fistElement; i<lastElement; i++) {
        	System.out.println("(thread "+threadId+") "+(i-fistElement)+"/"+(lastElement-fistElement));
        	String url = urls.get(i);
        	String title = getTitleFromUrl(url);
        	synchronized (ulrs2Titles) {
        		ulrs2Titles.put(url, title);
        	}
        }       	
	}
	
	public static Map<String,String> getTitles(List<String> urlsList) {
		urls = urlsList;
		
		int threadCount;
		if (urlsList.size()<20)
			threadCount = 2;
		else
			threadCount = 20;
		int resto = urls.size() % threadCount;
		int elementsPerThread = (urls.size()-resto) / threadCount;
		System.out.println("urlsCount=" + urlsList.size());
		System.out.println("threadCount=" + threadCount);
		System.out.println("elementsPerThread="+elementsPerThread);
		System.out.println("resto="+resto);
		
		

		List<Thread> threads = new ArrayList<Thread>();
		int lastElement = 0;
    	for (int i=0; i<threadCount; i++) {
    		int elementsForCurrentThread = elementsPerThread;
    		if (i<resto)
    			elementsForCurrentThread++;
    		System.out.println("("+i+") firstElement="+lastElement+" lastElement="+ (lastElement+elementsForCurrentThread));		
    		Thread t = new MultithreadUrlsTitleExtractor(i, lastElement, (lastElement+elementsForCurrentThread));
    		lastElement = (lastElement + elementsForCurrentThread);
    		threads.add(t);
    		t.start();   		
    	}
   	
    	for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}   
    	}	
    	return ulrs2Titles;
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
		
	//<span class="caption-text">commento</span>
	private String getTitleFromInstagram(String urlString) {		
		try {
			Document doc = Jsoup.connect(urlString).get();
			return doc.getElementsByClass("caption-text").text();
		} catch (IOException e) {
			return "";
		}		
	}
	
}
