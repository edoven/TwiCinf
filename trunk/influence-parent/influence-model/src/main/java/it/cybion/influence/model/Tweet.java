package it.cybion.influence.model;

import org.joda.time.DateTime;


public class Tweet {
	
	//private DateTime createdAt;
	private String id;
	private String text;
//	private String source;
//	private boolean isTruncated;
//	private long inResponseTo;
	private User user;
//	
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String toString()
	{
		return "(id:"+this.id+")(user:"+user.getScreenName()+") "+this.text;
	}
}
