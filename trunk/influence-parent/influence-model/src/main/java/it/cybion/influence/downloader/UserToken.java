package it.cybion.influence.downloader;

public class UserToken {
	
	private String token;
	private String secret;
	
	public UserToken(String token, String secret ) {
		this.token = token;
		this.secret = secret;
	}
	
	public String getToken() {
		return token;
	}
	public String getSecret() {
		return secret;
	}
	
	
}
