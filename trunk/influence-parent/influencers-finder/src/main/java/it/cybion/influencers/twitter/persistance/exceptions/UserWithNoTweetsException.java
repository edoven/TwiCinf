package it.cybion.influencers.twitter.persistance.exceptions;


public class UserWithNoTweetsException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 78166813989439040L;

	public UserWithNoTweetsException()
	{
		// TODO Auto-generated constructor stub
	}

	public UserWithNoTweetsException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserWithNoTweetsException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UserWithNoTweetsException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
