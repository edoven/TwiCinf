package it.cybion.influencers.twitter.persistance;

public class UserNotPresentException extends Exception {

	private static final long serialVersionUID = 119591563307862779L;

	public UserNotPresentException(String message) {
		super(message);
	}
}
