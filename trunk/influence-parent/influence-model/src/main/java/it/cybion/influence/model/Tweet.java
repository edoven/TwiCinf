package it.cybion.influence.model;


import java.util.Date;
import java.util.List;


public class Tweet {
	
	private Date createdAt;
	private String id;
	private String text;
	private String source;
	private boolean isTruncated;
	private String inReplyToStatusId;
	private String inReplyToUserId;
	private boolean isFavorited;
	private int retweetCount;
	private Tweet retweetedStatus;
	private List<UserMentionEntity> userMentionEntities;
	private List<UrlEntity> urlEntities;
	private List<HashtagEntity> hashtagEntities;
	private User user;

	
	public Date getCreatedAt() 
	{	
		return createdAt;
	}
	

	public void setCreatedAt(Date createdAt) 
	{
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


	/*
	 * Never used but can be optimized (maybe hashCode=id)
	 * if necessary.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result
				+ ((hashtagEntities == null) ? 0 : hashtagEntities.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((inReplyToStatusId == null) ? 0 : inReplyToStatusId
						.hashCode());
		result = prime * result
				+ ((inReplyToUserId == null) ? 0 : inReplyToUserId.hashCode());
		result = prime * result + (isFavorited ? 1231 : 1237);
		result = prime * result + (isTruncated ? 1231 : 1237);
		result = prime * result + retweetCount;
		result = prime * result
				+ ((retweetedStatus == null) ? 0 : retweetedStatus.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result
				+ ((urlEntities == null) ? 0 : urlEntities.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime
				* result
				+ ((userMentionEntities == null) ? 0 : userMentionEntities
						.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tweet other = (Tweet) obj;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (hashtagEntities == null) {
			if (other.hashtagEntities != null)
				return false;
		} else if (!hashtagEntities.equals(other.hashtagEntities))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inReplyToStatusId == null) {
			if (other.inReplyToStatusId != null)
				return false;
		} else if (!inReplyToStatusId.equals(other.inReplyToStatusId))
			return false;
		if (inReplyToUserId == null) {
			if (other.inReplyToUserId != null)
				return false;
		} else if (!inReplyToUserId.equals(other.inReplyToUserId))
			return false;
		if (isFavorited != other.isFavorited)
			return false;
		if (isTruncated != other.isTruncated)
			return false;
		if (retweetCount != other.retweetCount)
			return false;
		if (retweetedStatus == null) {
			if (other.retweetedStatus != null)
				return false;
		} else if (!retweetedStatus.equals(other.retweetedStatus))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (urlEntities == null) {
			if (other.urlEntities != null)
				return false;
		} else if (!urlEntities.equals(other.urlEntities))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (userMentionEntities == null) {
			if (other.userMentionEntities != null)
				return false;
		} else if (!userMentionEntities.equals(other.userMentionEntities))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Tweet [createdAt=" + createdAt + ", id=" + id + ", text="
				+ text + ", source=" + source + ", isTruncated=" + isTruncated
				+ ", inReplyToStatusId=" + inReplyToStatusId
				+ ", inReplyToUserId=" + inReplyToUserId + ", isFavorited="
				+ isFavorited + ", retweetCount=" + retweetCount
				+ ", retweetedStatus=" + retweetedStatus
				+ ", userMentionEntities=" + userMentionEntities
				+ ", urlEntities=" + urlEntities + ", hashtagEntities="
				+ hashtagEntities + ", user=" + user + "]";
	}
	
}
