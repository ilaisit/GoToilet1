package diaryServer;

public class KidData {
	private String kidName;
	private String kidId;
	private String imageLink;

	/**
	 * @return the imageLink
	 */
	public String getImageLink() {
		return imageLink;
	}

	/**
	 * @param imageLink the imageLink to set
	 */
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public KidData() {

	}

	public KidData(String kidId, String kidName, String imageLink) {
		this.kidId = kidId;
		this.kidName = kidName;
		this.imageLink = imageLink;
	}

	public String getKidName() {
		return kidName;
	}

	public void setKidName(String kidName) {
		this.kidName = kidName;
	}

	public String getKidId() {
		return kidId;
	}

	public void setKidId(String kidId) {
		this.kidId = kidId;
	}
}
