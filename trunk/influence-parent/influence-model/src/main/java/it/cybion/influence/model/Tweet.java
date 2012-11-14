package it.cybion.influence.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


//import org.joda.time.DateTime;


public class Tweet {
	
	private String createdAt;
	private String id;
	private String text;
	private String source;
	private boolean isTruncated;
	private String inReplyToStatusId;
	private String inReplyToUserId;
	private boolean isFavorited;
	private int retweetCount;
	private boolean wasRetweetedByMe; //the getter method is wasRetweetedByMe (not isWasRetweetedByMe)
//	List<T> userMentionEntities
	List<UrlEntity> urlEntities;
	List<HashtagEntity> hashtagEntities;
	private User user;

	
	
	public long getCreatedAt() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US);
		Date date = null;
		try {
			date = sdf.parse(createdAt);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		if (date==null)
			return -1;
		else
			return date.getTime();		
	}
	

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
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

	

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public boolean isTruncated() {
		return isTruncated;
	}

	public void setTruncated(boolean isTruncated) {
		this.isTruncated = isTruncated;
	}

	public String getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public void setInReplyToStatusId(String inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public String getInReplyToUserId() {
		return inReplyToUserId;
	}

	public void setInReplyToUserId(String inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	public boolean isFavorited() {
		return isFavorited;
	}

	public void setFavorited(boolean isFavorited) {
		this.isFavorited = isFavorited;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public boolean wasRetweetedByMe() {
		return wasRetweetedByMe;
	}

	public void setWasRetweetedByMe(boolean wasRetweetedByMe) {
		this.wasRetweetedByMe = wasRetweetedByMe;
	}
	
	public List<UrlEntity> getUrlEntities() {
		return urlEntities;
	}

	public void setUrlEntities(List<UrlEntity> urlEntities) {
		this.urlEntities = urlEntities;
	}
	
	public List<HashtagEntity> getHashtagEntities() {
		return hashtagEntities;
	}

	public void setHashtagEntities(List<HashtagEntity> hashtagEntities) {
		this.hashtagEntities = hashtagEntities;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	

	public String toString()
	{
		return "(id:"+this.id+")(user:"+user.getScreenName()+") "+this.text;
	}
}
