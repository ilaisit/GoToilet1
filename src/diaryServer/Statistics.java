package diaryServer;

public class Statistics {

	private String date;
	private String value;
	
	public Statistics() {
		
	}

	public Statistics(String date, String value) {
		
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", date, value);
	}
}
