package runnable.model;


public class Tweet {
	private String id;
	private String text;
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String toString()
	{
		return "(id:"+this.id+")(user:"+user.getScreenName()+") "+this.text;
	}
}
