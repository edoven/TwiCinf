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
	
	
	private int fistElement;
	private int lastElemen;
	
	
	private MultithreadUrlsTitleExtractor(int fistElement, int lastElement) {
		this.fistElement = fistElement;
		this.lastElemen = lastElement;
	}
	
	public void run() {        
        for (int i=fistElement; i<lastElemen; i++) {
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
		if (urlsList.size()<10)
			threadCount = 2;
		else
			threadCount = 10;
		int resto = urls.size() % threadCount;
		int elementsPerThread = (urls.size()-resto) / threadCount;
		System.out.println("threadCount=" + threadCount);
		System.out.println("resto="+resto);
		System.out.println("elementsPerThread="+elementsPerThread);
		

		List<Thread> threads = new ArrayList<Thread>();
    	for (int i=0; i<threadCount; i++) {
    		int firstElement = i*elementsPerThread;
    		int lastElement = i*elementsPerThread + elementsPerThread;
    		if (i==(threadCount-1))
    			lastElement = urls.size();
    		System.out.println("("+i+") firstElement="+firstElement+" lastElement="+lastElement);
    		Thread t = new MultithreadUrlsTitleExtractor(firstElement, lastElement);
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
		try {
			Document doc = Jsoup.connect(urlString).get();
			return doc.getElementsByTag("title").text();
		} catch (IOException e) {
			return "";
		}		
	}
	
}
