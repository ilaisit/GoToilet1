package diaryServer;

public class KidDataInternal {
	private String kidName;
	private String kidId;
	private String kidPicPath;
	private String parentId;
	private String gardenId;

	public KidDataInternal() {
	}

	public KidDataInternal(String kidName, String kidId, String kidPicPath, String parentId, String gardenId) {
		this.kidName = kidName;
		this.kidId = kidId;
		this.kidPicPath = kidPicPath;
		this.parentId = parentId;
		this.gardenId = gardenId;
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

	public String getKidPicPath() {
		return kidPicPath;
	}

	public void setKidPicPath(String kidPicPath) {
		this.kidPicPath = kidPicPath;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getGardenId() {
		return gardenId;
	}

	public void setGardenId(String gardenId) {
		this.gardenId = gardenId;
	}

	public KidData toKidData() {
		return new KidData(kidId, kidName, kidPicPath);
	}

}
