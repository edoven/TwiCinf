package it.cybion.influencers.cache;


import it.cybion.influencers.cache.persistance.PersistanceFacade;
import it.cybion.influencers.cache.persistance.exceptions.DataRangeNotCoveredException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotFollowersEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotFriendsEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotPresentException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotProfileEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserWithNoTweetsException;
import it.cybion.influencers.cache.web.TwitterWebFacade;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;
import it.cybion.influencers.cache.web.implementations.twitter4j.SearchedByDateTweetsResultContainer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import twitter4j.TwitterException;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;



/*
 * I'm using recursive methods!
 * They are less "performance-oriented"...but so elegant. :)  
 */

public class TwitterCache
{

	private static final Logger logger = Logger.getLogger(TwitterCache.class);

	TwitterWebFacade twitterWebFacade;
	PersistanceFacade persistanceFacade;

	public TwitterCache(TwitterWebFacade twitterWebFacade, PersistanceFacade persistanceFacade)
	{
		this.twitterWebFacade = twitterWebFacade;
		this.persistanceFacade = persistanceFacade;
	}

	public void donwloadUsersProfiles(List<Long> userIds) throws TwitterException
	{

		List<Long> usersToDownload = new ArrayList<Long>();
		for (Long userId : userIds)
		{
			try
			{
				persistanceFacade.getDescription(userId);
			} catch (UserNotPresentException e)
			{
				usersToDownload.add(userId);
			} catch (UserNotProfileEnrichedException e)
			{
				usersToDownload.add(userId);
			}
		}
		logger.info("donwloadUsersProfiles - Downloading profiles for " + usersToDownload.size() + " users.");
		List<String> downloadedUsersJsons = twitterWebFacade.getUsersJsons(usersToDownload);
		for (String userJson : downloadedUsersJsons)
			persistanceFacade.putUser(userJson);
	}

	public String getDescription(Long userId) throws TwitterException
	{
		try
		{
			String description = persistanceFacade.getDescription(userId);
			logger.debug("User with id " + userId + " is in the cache and has profile informations. Let's fetch it!");
			return description;
		} catch (UserNotPresentException e)
		{
			logger.debug("User with id " + userId + " not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		} catch (UserNotProfileEnrichedException e)
		{
			logger.debug("User with id " + userId + " has no profile informations. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		}
	}

	public Map<Long, String> getDescriptions(List<Long> userIds) throws TwitterException
	{
		Map<Long, String> userToDescription = new HashMap<Long, String>();
		List<Long> usersToDownload = new ArrayList<Long>();
		for (Long userId : userIds)
		{
			try
			{
				String description = persistanceFacade.getDescription(userId);
				userToDescription.put(userId, description);
			} catch (UserNotPresentException e)
			{
				usersToDownload.add(userId);
			} catch (UserNotProfileEnrichedException e)
			{
				usersToDownload.add(userId);
			}
		}
		List<String> downloadedUsersJsons = twitterWebFacade.getUsersJsons(usersToDownload);
		for (String userJson : downloadedUsersJsons)
			persistanceFacade.putUser(userJson);
		for (String donwloadedUserJson : downloadedUsersJsons)
		{
			/*
			 * It's not good to use mongDb object (DBObject) to extract the id
			 * from the user json string! TODO: use another way to do this!
			 */
			DBObject downloadedUserObject = (DBObject) JSON.parse(donwloadedUserJson);
			long userId = new Long((Integer) downloadedUserObject.get("id"));
			try
			{
				String description = persistanceFacade.getDescription(userId);
				userToDescription.put(userId, description);
			} catch (UserNotPresentException e)
			{
				logger.info("ERROR! User with id " + userId + " can't be added to caching system.");
				System.exit(0);
			} catch (UserNotProfileEnrichedException e)
			{
				logger.info("ERROR! User with id " + userId + " can't be added to caching system.");
				System.exit(0);
			}
		}
		return userToDescription;
	}

	public Map<Long, String> getDescriptionsAndStatuses(List<Long> userIds) throws TwitterException
	{
		Map<Long, String> user2DescriptionAndStatus = new HashMap<Long, String>();
		List<Long> usersToDownload = new ArrayList<Long>();
		for (Long userId : userIds)
		{
			try
			{
				String descriptionAndStatus = persistanceFacade.getDescriptionAndStatus(userId);
				user2DescriptionAndStatus.put(userId, descriptionAndStatus);
			} catch (UserNotPresentException e)
			{
				usersToDownload.add(userId);
			} catch (UserNotProfileEnrichedException e)
			{
				usersToDownload.add(userId);
			}
		}
		List<String> downloadedUsersJsons = twitterWebFacade.getUsersJsons(usersToDownload);
		for (String userJson : downloadedUsersJsons)
			persistanceFacade.putUser(userJson);
		for (String donwloadedUserJson : downloadedUsersJsons)
		{
			/*
			 * It's not good to use mongDb object (DBObject) to extract the id
			 * from the user json string! TODO: use another way to do this!
			 */
			DBObject downloadedUserObject = (DBObject) JSON.parse(donwloadedUserJson);
			long userId = new Long((Integer) downloadedUserObject.get("id"));
			try
			{
				String descriptionAndStatus = persistanceFacade.getDescriptionAndStatus(userId);
				user2DescriptionAndStatus.put(userId, descriptionAndStatus);
			} catch (UserNotPresentException e)
			{
				logger.info("ERROR! User with id " + userId + " can't be added to caching system.");
				System.exit(0);
			} catch (UserNotProfileEnrichedException e)
			{
				logger.info("ERROR! User with id " + userId + " can't be added to caching system.");
				System.exit(0);
			}
		}
		return user2DescriptionAndStatus;
	}

	public List<Long> getFollowers(Long userId) throws TwitterException
	{
		List<Long> followers;
		try
		{
			followers = persistanceFacade.getFollowers(userId);
			logger.debug("User with id " + userId + " is already followers enriched. Let's fetch it from the cache.");
			return followers;
		} catch (UserNotPresentException e)
		{
			logger.debug("User with id " + userId + " is not in the cache. It needs to be downloaded.");
			String user = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(user);
			return getFollowers(userId);
		} catch (UserNotFollowersEnrichedException e)
		{
			logger.debug("User with id=" + userId + " is in the cache but not followers-enriched. Followers have to be downloaded.");
			followers = twitterWebFacade.getFollowersIds(userId);
			try
			{
				persistanceFacade.putFollowers(userId, followers);
			} catch (UserNotPresentException e1)
			{
				logger.info("ERROR! User with id " + userId + " can't be added to caching system.");
				System.exit(0);
			}
			return getFollowers(userId);
		}
	}

	public int getFollowersCount(Long userId) throws TwitterException
	{
		try
		{
			int followersCount = persistanceFacade.getFollowersCount(userId);
			logger.debug("User with id " + userId + " is in the cache and has profile informations. Let's fetch it!");
			return followersCount;
		} catch (UserNotPresentException e)
		{
			logger.debug("User with id " + userId + " not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getFollowersCount(userId);
		} catch (UserNotProfileEnrichedException e)
		{
			logger.debug("User with id " + userId + " has no profile informations. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getFollowersCount(userId);
		}
	}

	public List<Long> getFriends(Long userId) throws TwitterException
	{
		List<Long> friends;
		try
		{
			friends = persistanceFacade.getFriends(userId);
			logger.debug("User with id " + userId + " is already friends enriched. Let's fetch it from the cache.");
			return friends;
		} catch (UserNotPresentException e)
		{
			logger.debug("User with id=" + userId + " is not in the cache. It has to be downloaded.");
			String user = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(user);
			return getFriends(userId);
		} catch (UserNotFriendsEnrichedException e)
		{
			logger.debug("User with id=" + userId + " is in the cache but not friends-enriched. Friends have to be downloaded.");
			friends = twitterWebFacade.getFriendsIds(userId);
			try
			{
				persistanceFacade.putFriends(userId, friends);
			} catch (UserNotPresentException e1)
			{
				logger.debug("ERROR! User with id " + userId + " can't be added to caching system.");
				System.exit(0);
			}
			return getFriends(userId);
		}
	}

	public int getFriendsCount(Long userId) throws TwitterException
	{
		try
		{
			int friendsCount = persistanceFacade.getFriendsCount(userId);
			logger.debug("User with id " + userId + " is in the cache and has profile informations. Let's fetch it!");
			return friendsCount;
		} catch (UserNotPresentException e)
		{
			logger.debug("User with id " + userId + " not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getFriendsCount(userId);
		} catch (UserNotProfileEnrichedException e)
		{
			logger.debug("User with id " + userId + " has no profile informations. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getFriendsCount(userId);
		}
	}

	public List<Long> getNotFollowersAndFriendsEnriched(List<Long> usersIds)
	{
		List<Long> notEnriched = new ArrayList<Long>();
		for (Long userId : usersIds)
		{

			try
			{
				persistanceFacade.getFollowers(userId);
				persistanceFacade.getFriends(userId);
			} catch (UserNotPresentException e)
			{
				notEnriched.add(userId);
				continue;
			} catch (UserNotFollowersEnrichedException e)
			{
				notEnriched.add(userId);
				continue;
			} catch (UserNotFriendsEnrichedException e)
			{
				notEnriched.add(userId);
			}

		}
		return notEnriched;
	}

	public String getScreenName(Long userId) throws TwitterException
	{
		try
		{
			String description = persistanceFacade.getScreenName(userId);
			logger.debug("User with id " + userId + " is in the cache and has profile informations. Let's fetch it!");
			return description;
		} catch (UserNotPresentException e)
		{
			logger.debug("User with id " + userId + " not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		} catch (UserNotProfileEnrichedException e)
		{
			logger.debug("User with id " + userId + " has no profile informations. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getDescription(userId);
		}
	}

	public List<String> getLast200Tweets(long userId) throws TwitterException, ProtectedUserException
	{
		try
		{
			return persistanceFacade.getUpTo200Tweets(userId);
		} catch (UserWithNoTweetsException e)
		{
			List<String> jsonTweets = twitterWebFacade.getTweetsWithMaxId(userId, -1);
			persistanceFacade.putTweets(jsonTweets);
			return getLast200Tweets(userId);
		}
	}
		
	public List<String> getTweetsByDate(long userId, Date fromDate, Date toDate) throws TwitterException, ProtectedUserException
	{
		try
		{
			List<String> tweets = persistanceFacade.getTweetsByDate(userId,fromDate,toDate);
			logger.info("tweets get from mongodb");
			return tweets;
		}
		catch (UserWithNoTweetsException e)
		{	
			logger.info("downloading tweets");
			SearchedByDateTweetsResultContainer result = twitterWebFacade.getTweetsByDate(userId, fromDate,toDate);
			persistanceFacade.putTweets(result.getBadTweets());
			persistanceFacade.putTweets(result.getGoodTweets());
			return result.getGoodTweets();
		}
		catch (DataRangeNotCoveredException e)
		{
			logger.info("downloading tweets");
			SearchedByDateTweetsResultContainer result = twitterWebFacade.getTweetsByDate(userId, fromDate, toDate);
			persistanceFacade.putTweets(result.getBadTweets());
			persistanceFacade.putTweets(result.getGoodTweets());
			return result.getGoodTweets();
		}
	}
	

	public String getUser(Long userId) throws TwitterException
	{
		try
		{
			String user = persistanceFacade.getUser(userId);
			logger.debug("User with id " + userId + " is in the cache. Let's fetch it!");
			return user;
		} catch (UserNotPresentException e)
		{
			logger.debug("User with id " + userId + " not cached. Let's donwload it!");
			String userJson = twitterWebFacade.getUserJson(userId);
			persistanceFacade.putUser(userJson);
			return getUser(userId);
		}
	}
	
	public Long getUserId(String screenName) throws TwitterException
	{
		try
		{
			return persistanceFacade.getUserId(screenName);
		}
		catch (UserNotPresentException e)
		{
			String userJson = twitterWebFacade.getUserJson(screenName) ;
			persistanceFacade.putUser(userJson);
			try
			{
				return persistanceFacade.getUserId(screenName);
			}
			catch (UserNotPresentException e1)
			{
				logger.info("Error! user with screenName "+screenName+" should have been in the cache but it is not. Check uppercase/lowercase!");
				System.exit(0);
				return -1L;
			}
			
		}
	}
	
	
	public List<Long> getUserIds(List<String> screenNames) 
	{
		List<Long> userIds = new ArrayList<Long>();
		for (String screenName : screenNames)
		{
			try
			{
				userIds.add( persistanceFacade.getUserId(screenName) );
			}
			catch (UserNotPresentException e)
			{
				try
				{
					persistanceFacade.putUser( twitterWebFacade.getUserJson(screenName) );
					try
					{
						userIds.add( persistanceFacade.getUserId(screenName) );
					}
					catch (UserNotPresentException e1)
					{
						logger.info("Error! user with screenName "+screenName+" should have been in the cache but it is not. Check uppercase/lowercase!");
						System.exit(0);
					}
				}
				catch (TwitterException e2)
				{
					logger.info("Can't get id for user with screenName="+screenName);
				}
				
			}
			
		} 
		return userIds;
	}

}
