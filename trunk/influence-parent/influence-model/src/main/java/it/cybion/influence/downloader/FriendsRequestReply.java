package it.cybion.influence.downloader;


public class FriendsRequestReply {
	private int requestLimit;
	private long[] friendsIds;
	
	public FriendsRequestReply(int requestLimit, long[] friendsIds) {
		super();
		this.requestLimit = requestLimit;
		this.friendsIds = friendsIds;
	}

	public int getRequestLimit() {
		return requestLimit;
	}

	public long[] getFriendsIds() {
		return friendsIds;
	}
}
