package it.cybion.influence.downloader;

import java.util.List;

public interface RequestHandler {

	/*
	 * BEWARE: this ignores pagination. It only get the first (up to) 5000 friends .
	 */	
	public List<String> getFriendsIds(String userScreenName);
	
	public int getLimit();

}