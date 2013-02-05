package urlexpansion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UrlExpansion1 {
	
	public static void main(String[] args) {
		List<String> urls = new ArrayList<String>();
		urls.add("http://t.co/MG0cV27H");
		urls.add("http://t.co/BhN8KEJz");
		urls.add("http://t.co/xzZn4wtR");
		urls.add("http://t.co/2NCDo6ss");
		urls.add("http://t.co/pINXLPt8");
		urls.add("http://t.co/qrSLaWYO");
		
		for (String url : urls) {
			System.out.println(getTitle(url));
		}
	}
	
	
	
//	public static String getTitle(String urlString) {
//		URL url;
//		InputStream inputStream = null;
//		//DataInputStream dis;
//		BufferedReader bufferReader;
//		String line;
//		String title = "";
//
//		try {
//		    url = new URL(urlString);
//
//		    inputStream = url.openStream();
//		    bufferReader = new BufferedReader(new InputStreamReader(inputStream));
//		    while ((line = bufferReader.readLine()) != null) {
//		    	if (line.contains("<title>")) {
//		    		title = line;
//		    		break;
//		    	}
//		    }
//		} catch (MalformedURLException mue) {
//		     mue.printStackTrace();
//		} catch (IOException ioe) {
//		     ioe.printStackTrace();
//		} finally {
//		    try {
//		    	inputStream.close();
//		    } catch (IOException ioe) {
//		        // nothing to see here
//		    }
//		}
//		return title;
//	}
	
	
	public static String getTitle(String urlString) {
		try {
			Document doc = Jsoup.connect(urlString).get();
			return doc.getElementsByTag("title").text();	
		} catch (IOException e) {
			return "";
		}	
	}
	
	
	public static String getMeta(String urlString) {
		try {
			Document doc = Jsoup.connect(urlString).get();
			Elements metas = doc.getElementsByTag("meta");
			Iterator<Element> iterator =  metas.iterator();
			while (iterator.hasNext()) {
				Element element = iterator.next();
				System.out.println(element.toString());
			}
			return "";	
		} catch (IOException e) {
			return "";
		}	
	}
}
