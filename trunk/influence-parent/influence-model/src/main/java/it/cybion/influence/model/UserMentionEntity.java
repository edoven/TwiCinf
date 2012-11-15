package it.cybion.influence.model;


/*{
            "start": 0,
            "end": 14,
            "name": "Cristiano Toni",
            "screenName": "cristianotoni",
            "id": 18632224
        }
*/
public class UserMentionEntity {
	private int start;
	private int end;
	private String name;
	private String screenName;
	private String id;
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
