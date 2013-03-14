package it.cybion.influencers.cache.persistance.exceptions;


public class UserNotFollowersEnrichedException extends Exception
{

	private static final long serialVersionUID = -9091858577478126024L;

	public UserNotFollowersEnrichedException(String message)
	{
		super(message);
	}
}
