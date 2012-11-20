package it.cybion.influence.model;

import it.cybion.influence.util.DataParser;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 
 * BEWARE: overridden method compare only compares ids 
 * (to speedup the MetricsCalculator's method genreateUser()
 * that returns the users list (user=tweet author) without duplicates)
 * 
 */



/*
 * EXAMPLE :
 * 
 * 
	"id":425699035,
	"name":"PerugiaToday",
	"screenName":"PerugiaToday",
	"location":"Perugia",
	"description":"",
	"isContributorsEnabled":false,
useless	"profileImageUrl":"http://a0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg",
useless	"profileImageUrlHttps":"https://si0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg",
	"url":"http://www.perugiatoday.it/",
	"isProtected":false,
	"followersCount":123,
useless	"profileBackgroundColor":"C0DEED",
useless	"profileTextColor":"333333",
useless	"profileLinkColor":"0084B4",
useless	"profileSidebarFillColor":"DDEEF6",
useless	"profileSidebarBorderColor":"C0DEED",
useless	"profileUseBackgroundImage":true,
??? useless ???	"showAllInlineMedia":false,
	"friendsCount":93,
TO_INSERT	"createdAt":"Dec 1, 2011 10:49:25 AM",
	"favouritesCount":0,
useless	"utcOffset":-1,
useless	"profileBackgroundImageUrl":"http://a0.twimg.com/images/themes/theme1/bg.png",
useless	"profileBackgroundImageUrlHttps":"https://si0.twimg.com/images/themes/theme1/bg.png",
useless	"profileBackgroundTiled":false,
	"lang":"it",
	"statusesCount":996,
useless	"isGeoEnabled":false,
useless	"isVerified":false,
useless	"translator":false,
	"listedCount":3,
useless	"isFollowRequestSent":false
*/

public class User {
	
	private long id;
	private String name;
	private String screenName;
	private String location;
	private String description;
	private boolean isContributorsEnabled;
	private URL url;
	private boolean isProtected;
	private int followersCount;
	private int friendsCount;
	private String createdAt; //BEWARE! getCreatedAt returns a long
	private int favouritesCount;
	private String lang;
	private int statusesCount;
	private int listedCount;
	

	/*
	 * This map needs to be calculated
	 * When a user is created from a JSON
	 * this map is null.
	 */
	private Map<String, Integer> hashtags2count = null;
	
	/*
	 * BEWARE!
	 * To calcluate!
	 * From the tweet json we can only get the followerCount and the friendCount.
	 * We need the twitter API to get the user's followers/friends lists of IDs.
	 */
	private List<User> followers; 	
	private List<User> friends;		
	
	
	/*
	 * "BUSINESS" METHODS
	 */
	
	public void addHashtag(String hashtag) {
		if (hashtags2count == null)
			hashtags2count = new HashMap<String, Integer>();
		if (hashtags2count.containsKey(hashtag))
			hashtags2count.put(hashtag, (hashtags2count.get(hashtag) + 1) );
		else
			hashtags2count.put(hashtag, 1 );
	}
	
	
	
	/*
	 * GETTERS AND SETTERS
	 */

    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isContributorsEnabled() {
		return isContributorsEnabled;
	}
	public void setContributorsEnabled(boolean isContributorsEnabled) {
		this.isContributorsEnabled = isContributorsEnabled;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public boolean isProtected() {
		return isProtected;
	}
	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}
	public int getFollowersCount() {
		return followersCount;
	}
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	
	
	/*
	 * TODO: change!
	 */
	public long getCreatedAt() {
		return DataParser.parseTwitterData(createdAt); //Sooo BAD!
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public int getFavouritesCount() {
		return favouritesCount;
	}
	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public int getStatusesCount() {
		return statusesCount;
	}
	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}
	public int getListedCount() {
		return listedCount;
	}
	public void setListedCount(int listedCount) {
		this.listedCount = listedCount;
	}
	public List<User> getFollowers() {
		return followers;
	}
	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}
	public List<User> getFriends() {
		return friends;
	}
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
	public Map<String, Integer> getHashtags2count() {
		return hashtags2count;
	}
	public void setHashtags2count(Map<String, Integer> hashtags2count) {
		this.hashtags2count = hashtags2count;
	}
	
	
	
	
	/*
	 * EQUALS, HASHCODE, TOSTRING
	 */

    @Override
    public boolean equals(Object o) {
        if (this == o) 
        	return true;
        if (o == null || getClass() != o.getClass()) 
        	return false;
        User user = (User) o;
        if (id != user.id) 
        	return false;
        

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (screenName != null ? screenName.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isContributorsEnabled ? 1 : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (isProtected ? 1 : 0);
        result = 31 * result + followersCount;
        result = 31 * result + friendsCount;
        result = 31 * result + favouritesCount;
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        result = 31 * result + statusesCount;
        result = 31 * result + listedCount;
        result = 31 * result + (followers != null ? followers.hashCode() : 0);
        result = 31 * result + (friends != null ? friends.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", isContributorsEnabled=" + isContributorsEnabled +
                ", url=" + url +
                ", isProtected=" + isProtected +
                ", followersCount=" + followersCount +
                ", friendsCount=" + friendsCount +
                ", favouritesCount=" + favouritesCount +
                ", lang='" + lang + '\'' +
                ", statusesCount=" + statusesCount +
                ", listedCount=" + listedCount +
                ", followers=" + followers +
                ", friends=" + friends +
                '}';
    }
    
    
	
	
}
