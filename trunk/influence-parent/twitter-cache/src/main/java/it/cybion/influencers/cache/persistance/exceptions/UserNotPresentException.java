package it.cybion.influencers.cache.persistance.exceptions;


public class UserNotPresentException extends Exception
{

	private static final long serialVersionUID = 119591563307862779L;

	public UserNotPresentException(String message)
	{
		super(message);
	}
}
