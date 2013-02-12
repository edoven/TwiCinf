package it.cybion.influencers.twitter.web;


import java.util.Map;



public class LimitReachedForCurrentRequestException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5467500139203265884L;
	private Map<String, Integer> limits;

	public LimitReachedForCurrentRequestException()
	{
		// TODO Auto-generated constructor stub
	}

	public LimitReachedForCurrentRequestException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public LimitReachedForCurrentRequestException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public LimitReachedForCurrentRequestException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public LimitReachedForCurrentRequestException(Map<String, Integer> requestType2limit)
	{
		this.limits = requestType2limit;
	}

	public Map<String, Integer> getLimits()
	{
		return limits;
	}

}
