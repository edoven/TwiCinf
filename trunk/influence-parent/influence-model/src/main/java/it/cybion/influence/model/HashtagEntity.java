package it.cybion.influence.model;

public class HashtagEntity {
	
	private int start;
	private int end;
	private String text;
	
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashtagEntity that = (HashtagEntity) o;

        if (end != that.end) return false;
        if (start != that.start) return false;
        if (text != null ? !text.equals(that.text) : that.text != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HashtagEntity{" +
                "start=" + start +
                ", end=" + end +
                ", text='" + text + '\'' +
                '}';
    }
}
