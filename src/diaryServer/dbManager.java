package diaryServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class dbManager {

	private static final String CLASS_NAME = "dbManager";
	private final String goToiletURL = "jdbc:mysql://ec2-54-171-116-53.eu-west-1.compute.amazonaws.com:3306";
	private final String driver = "com.mysql.jdbc.Driver";
	private final String userName = "root";
	private final String password = "ThisisPass123#";
	private boolean isInitialized = false;
	private Connection connectionObject;
	private static Object _syncObj = new Object();
	private static dbManager _instance = null;
	private List<UserData> userDataList;
	private List<KidDataInternal> kidsDataList;

	private dbManager() {
		try {
			userDataList = new ArrayList<UserData>();
			kidsDataList = new ArrayList<KidDataInternal>();
			Class.forName(driver).newInstance();
			connectionObject = DriverManager.getConnection(goToiletURL, userName, password);
			isInitialized = true;
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME, "Connection was successful, db is initialized");
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME, CLASS_NAME, "Critical error, cannot login to the DB! " + ex.getMessage());
			isInitialized = false;
		}
		try {
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME, "Getting users from DB");
			Statement selectUsersStatement = connectionObject.createStatement();
			selectUsersStatement.executeQuery(Helpers.GET_DB_SELECTION_QUERY);
			ResultSet returnedUsers = selectUsersStatement.executeQuery(Helpers.GET_USERS_QUERY);
			while (returnedUsers.next()) {
				userDataList.add(new UserData(returnedUsers.getString("user_name"), returnedUsers.getString("user_pass"), returnedUsers.getString("user_id"),
						returnedUsers.getString("user_type")));
			}
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME, "Done.");
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME, CLASS_NAME, "Critical error, cannot get users! " + ex.getMessage());
		}

		try {
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME, "Getting kids from DB");
			Statement selectUsersStatement = connectionObject.createStatement();
			ResultSet returnedUsers = selectUsersStatement.executeQuery(Helpers.GET_KIDS_QUERY);
			while (returnedUsers.next()) {
				kidsDataList.add(new KidDataInternal(returnedUsers.getString("kid_name"), returnedUsers.getString("idkids"), returnedUsers
						.getString("pic_path"), returnedUsers.getString("parent_id"), returnedUsers.getString("garden_id")));
			}
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME, "Done.");
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME, CLASS_NAME, "Critical error, cannot get kids! " + ex.getMessage());
		}
	}

	public static dbManager getInstance() {
		if (_instance != null) {
			return _instance;
		}
		synchronized (_syncObj) {
			if (_instance != null) {
				return _instance;
			}
			_instance = new dbManager();
		}

		return _instance;
	}

	/**
	 * This method performs the login action based on the given username and pasword
	 * 
	 * @param userName
	 *            case insensitive user name
	 * @param pass
	 *            case sensitive user password
	 * @return null if not initialized, loginReturnData class holding the connecting success status and the id otherwise
	 */
	public loginReturnData login(String userName, String pass) {
		if (!isInitialized) {
			return null;
		}
		for (UserData currentUser : userDataList) {
			if (currentUser.getUserName().equalsIgnoreCase(userName)) {
				if (currentUser.getPass().equals(pass)) {
					return new loginReturnData(currentUser.getUserType(), currentUser.getUserId());
				}
			}
		}
		return new loginReturnData(ELoginStatus.EInvalid, "-1");
	}

	/**
	 * Call this method with a new event and it will be inserted to the db. This method does not verify the details!
	 * 
	 * @param the
	 *            event data
	 * @return -1 if not initialized or failed, the id of the new event otherwise
	 */
	public int insertNewEvent(EventData newEvent) {
		if (!isInitialized) {
			return -1;
		}
		String methodName = "insertNewEvent";
		int newEntryId = -1;

		try {
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.error, CLASS_NAME, methodName,
					"Error occured during inserting new event. Event data: " + newEvent.toString() + ", error: " + ex.getMessage());
			newEntryId = -1;
		}
		return newEntryId;
	}

	/**
	 * this method based on the given parameters returns a list of events
	 * 
	 * @param kidId
	 *            the id of the kid the his data is needed
	 * @param daysFromToday
	 *            number of days to bring history - the hour that will be used is 00:00
	 * @return null if not initialized or wrong parameters, list of event data
	 */
	public List<EventData> getEventsForKid(String kidId, int daysFromToday) {
		String methodName = "getEventForUser";
		List<EventData> retVal = new ArrayList<EventData>();

		if (kidId.isEmpty() || daysFromToday <= 0) {
			return null;
		}

//		try {
//			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "Getting events for kid: " + kidId);
//			KidDataInternal selectedKid = null;
//			for (KidDataInternal currentKid : kidsDataList) {
//				if (currentKid.getKidId().equals(kidId)) {
//					selectedKid = currentKid;
//					break;
//				}
//			}
//
//			if (selectedKid == null) {
//				return null;
//			}
//			
//			Statement selectUsersStatement = connectionObject.createStatement();
//			Calendar cl = Calendar.getInstance();
//			cl.add(Calendar.DAY_OF_MONTH, -daysFromToday);
//			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//
//			String query = Helpers.GET_EVENTS_FOR_KID_QUERY.replace(Helpers.KID_ID, selectedKid.getKidId()).replace(Helpers.DATE_TIME, sdf.format(cl.getTime()));
//			selectUsersStatement.executeQuery(query);
//			ResultSet returnedUsers = selectUsersStatement.executeQuery(Helpers.GET_USERS_QUERY);
//			while (returnedUsers.next()) {
//				List<IndependenceStages> stages = new ArrayList<IndependenceStages>();
//				String[] stagesAsStringArray = returnedUsers.getString("independence_stages").split(";");
//				for(String currentStage : stagesAsStringArray) {
//					String[] currentStageSplitted = currentStage.split(",");
//					stages.add(new IndependenceStages(Enum.valueOf(EIndependenceStages.class, currentStageSplitted[0]), 
//							Enum.valueOf(EAssistantLevel.class, currentStageSplitted[1])));
//				}
//				retVal.add(new EventData(returnedUsers.getString("date_time"), 
//						returnedUsers.getString("inserting_user_id"),
//						returnedUsers.getString("kid_id"), 
//						stages, 
//						returnedUsers.getString("kid_is_initiator") == "true" ? true : false, 
//						returnedUsers.getString("comments")));
//			}
//			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME, "Done.");
//			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "Done.");
//		} catch (Exception ex) {
//			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME, methodName, "Critical error, cannot get users! " + ex.getMessage());
//		}

		return retVal;
	}

	/**
	 * based on the given user id returns a list of the kids that he is related to
	 * 
	 * @param userId
	 *            the user id that the related kids is needed
	 * @return null if not initialized or wrong parameters, list of kids otherwise
	 */
	public List<KidData> getListOfKids(String userId) {
		String methodName = "getListOfKids";
		if (!isInitialized) {
			return null;
		}
		List<KidData> retVal = new ArrayList<KidData>();
		try {
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "Getting list of kids for: " + userId);
			UserData givenUser = null;
			for (UserData currentUser : userDataList) {
				if (currentUser.getUserId().equals(userId)) {
					givenUser = currentUser;
					break;
				}
			}
			if (givenUser == null || givenUser.getUserType() != ELoginStatus.EUserType_Garden) {
				return null;
			}

			for (KidDataInternal currentInternalKid : kidsDataList) {
				if (givenUser.getUserId().equals(currentInternalKid.getGardenId())) {
					retVal.add(currentInternalKid.toKidData());
				}
			}
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "Done.");
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME, methodName, "Critical error, cannot get users! " + ex.getMessage());
		}

		return retVal;
	}

	public void close() {
		String methodName = "close";
		try {
			connectionObject.close();
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.error, CLASS_NAME, methodName,
					"Cannot close the connection, the connection will be released automatically. Error: " + ex.getMessage());
		}
	}
}
