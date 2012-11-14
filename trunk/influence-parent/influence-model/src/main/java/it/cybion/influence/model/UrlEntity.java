package it.cybion.influence.model;

import java.net.URL;

/*
 * 
 * BEWARE: the jason name is urlEntities not UrlEntities
 * 
 */


public class UrlEntity {
	private int start;
	private int end;
	private URL url;
	private URL expandedURL;
	private String displayURL;
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public URL getExpandedURL() {
		return expandedURL;
	}
	public void setExpandedURL(URL expandedURL) {
		this.expandedURL = expandedURL;
	}
	public String getDisplayURL() {
		return displayURL;
	}
	public void setDisplayURL(String displayURL) {
		this.displayURL = displayURL;
	}

}
