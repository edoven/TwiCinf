package it.cybion.influence.model;

import it.cybion.influence.util.DataParser;

import java.util.List;


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
	// private boolean wasRetweetedByMe;  --> Removed, cause: useless
	private Tweet retweetedStatus;
	private List<UserMentionEntity> userMentionEntities;
	private List<UrlEntity> urlEntities;
	private List<HashtagEntity> hashtagEntities;
	private User user;

	
	
	public long getCreatedAt() {	
		return DataParser.parseTwitterData(createdAt);
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

	/*
	 * REMOVED
	 *
	public boolean wasRetweetedByMe() {
		return wasRetweetedByMe;
	}

	public void setWasRetweetedByMe(boolean wasRetweetedByMe) {
		this.wasRetweetedByMe = wasRetweetedByMe;
	}
	*/
	
	public Tweet getRetweetedStatus() {
		return retweetedStatus;
	}


	public void setRetweetedStatus(Tweet retweetedStatus) {
		this.retweetedStatus = retweetedStatus;
	}


	public List<UserMentionEntity> getUserMentionEntities() {
		return userMentionEntities;
	}


	public void setUserMentionEntities(List<UserMentionEntity> userMentionEntities) {
		this.userMentionEntities = userMentionEntities;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tweet tweet = (Tweet) o;

        if (isFavorited != tweet.isFavorited) return false;
        if (isTruncated != tweet.isTruncated) return false;
        if (retweetCount != tweet.retweetCount) return false;
        //if (wasRetweetedByMe != tweet.wasRetweetedByMe) return false;
        if (createdAt != null ? !createdAt.equals(tweet.createdAt) : tweet.createdAt != null)
            return false;
        if (hashtagEntities != null ? !hashtagEntities.equals(tweet.hashtagEntities) : tweet.hashtagEntities != null)
            return false;
        if (id != null ? !id.equals(tweet.id) : tweet.id != null) return false;
        if (inReplyToStatusId != null ? !inReplyToStatusId.equals(tweet.inReplyToStatusId) : tweet.inReplyToStatusId != null)
            return false;
        if (inReplyToUserId != null ? !inReplyToUserId.equals(tweet.inReplyToUserId) : tweet.inReplyToUserId != null)
            return false;
        if (source != null ? !source.equals(tweet.source) : tweet.source != null)
            return false;
        if (text != null ? !text.equals(tweet.text) : tweet.text != null)
            return false;
        if (urlEntities != null ? !urlEntities.equals(tweet.urlEntities) : tweet.urlEntities != null)
            return false;
        if (user != null ? !user.equals(tweet.user) : tweet.user != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = createdAt != null ? createdAt.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (isTruncated ? 1 : 0);
        result = 31 * result + (inReplyToStatusId != null ? inReplyToStatusId.hashCode() : 0);
        result = 31 * result + (inReplyToUserId != null ? inReplyToUserId.hashCode() : 0);
        result = 31 * result + (isFavorited ? 1 : 0);
        result = 31 * result + retweetCount;
        //result = 31 * result + (wasRetweetedByMe ? 1 : 0);
        result = 31 * result + (urlEntities != null ? urlEntities.hashCode() : 0);
        result = 31 * result + (hashtagEntities != null ? hashtagEntities.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "createdAt='" + createdAt + '\'' +
                ", id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", isTruncated=" + isTruncated +
                ", inReplyToStatusId='" + inReplyToStatusId + '\'' +
                ", inReplyToUserId='" + inReplyToUserId + '\'' +
                ", isFavorited=" + isFavorited +
                ", retweetCount=" + retweetCount +
                //", wasRetweetedByMe=" + wasRetweetedByMe +
                ", urlEntities=" + urlEntities +
                ", hashtagEntities=" + hashtagEntities +
                ", user=" + user +
                '}';
    }
}
