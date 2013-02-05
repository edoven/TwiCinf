package lucene;

import java.util.ArrayList;
import java.util.List;


public class MultithreadUrlsTitleExtractorTEST extends Thread{
	
	public static void main(String[] args) {
		List<String> urls = new ArrayList<String>();
		urls.add("http://www.repubblica.it");
		urls.add("http://www.libero.it");
		urls.add("http://www.ciao.it");
		urls.add("http://www.hello.it");
		urls.add("http://www.google.it");
		urls.add("http://www.yahoo.it");
		urls.add("http://www.msn.it");
		urls.add("http://www.google.com");
		urls.add("http://www.java.com");
		urls.add("http://www.ibm.com");
		urls.add("http://www.asus.com");
		
		System.out.println(MultithreadUrlsTitleExtractor.getTitles(urls));
	}
	
}
