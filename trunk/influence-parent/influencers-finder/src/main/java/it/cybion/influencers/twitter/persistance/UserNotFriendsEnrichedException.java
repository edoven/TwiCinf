package it.cybion.influencers.twitter.persistance;

public class UserNotFriendsEnrichedException extends Exception {

	private static final long serialVersionUID = -4338152039942911308L;

	public UserNotFriendsEnrichedException(String message) {
		super(message);
	}

}
