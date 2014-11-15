package diaryServer;

import enums.ELoginStatus;


public class loginReturnData {
	private ELoginStatus loginStatus;
	private String userId;
	
	public loginReturnData() {
		
	}
	
	public loginReturnData(ELoginStatus loginStatus, String userId) {
		this.loginStatus = loginStatus;
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return String.format("uer id %s returned status %s", userId, loginStatus);
	}

	public ELoginStatus getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(ELoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
