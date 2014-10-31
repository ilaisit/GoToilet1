package diaryServer;

public class UserData {
	private String userName;
	private String pass;
	private ELoginStatus userType;
	private String userId;

	public UserData() {

	}

	public UserData(String userName, String userPass, String id, String userType) {
		this.userName = userName;
		this.pass = userPass;
		this.setUserId(id);
		switch (userType) {
		case "1":
			this.userType = ELoginStatus.EUserType_Garden;
			break;
		case "2":
			this.userType = ELoginStatus.EUserType_Parent;
			break;
		default:
			this.userType = ELoginStatus.EInvalid;
			break;
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public ELoginStatus getUserType() {
		return userType;
	}

	public void setUserType(ELoginStatus userType) {
		this.userType = userType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
