package it.cybion.influence.model;

import java.net.URL;
import java.util.List;

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
//	date createdAt
	private int favouritesCount;
	private String lang;
	private int statusesCount;
	private int listedCount;
	
	private List<User> followers;
	private List<User> friends;
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
	
	
	
}
