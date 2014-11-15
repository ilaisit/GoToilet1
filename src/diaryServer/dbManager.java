package diaryServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import dataTypes.EventData;
import dataTypes.Helpers;
import dataTypes.IndependenceStages;
import dataTypes.KidData;
import dataTypes.KidDataInternal;
import dataTypes.Statistics;
import dataTypes.SuccessDbStatistics;
import dataTypes.UserData;
import enums.EAssistantLevel;
import enums.EIndependenceStages;
import enums.ELogLevel;
import enums.ELoginStatus;
import enums.EStatisticType;

public class dbManager {

	private static final String CLASS_NAME = "dbManager";
	private final String goToiletURL = "jdbc:mysql://ec2-54-171-116-53.eu-west-1.compute.amazonaws.com:3306";
	private final String driver = "com.mysql.jdbc.Driver";
	private final String userName = "root";
	private final String password = "ThisisPass123#";
	private Connection selectConnectionObject, insertConnectionObject;
	private static Object _syncObj = new Object();
	private static dbManager _instance = null;
	private List<UserData> userDataList;
	private List<KidDataInternal> kidsDataList;

	private dbManager() {
		try {
			userDataList = new ArrayList<UserData>();
			kidsDataList = new ArrayList<KidDataInternal>();
			Class.forName(driver).newInstance();
			selectConnectionObject = DriverManager.getConnection(goToiletURL,
					userName, password);
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME,
					"Connection was successful, db is initialized");
		} catch (Exception ex) {
			Logger.getInstance().Log(
					ELogLevel.critical,
					CLASS_NAME,
					CLASS_NAME,
					"Critical error, cannot login to the DB! "
							+ ex.getMessage());
			return;
		}
		Statement selectUsersStatement = null;
		try {
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME,
					"Getting users from DB");
			selectUsersStatement = selectConnectionObject.createStatement();
			selectUsersStatement.executeQuery(Helpers.GET_DB_SELECTION_QUERY);
			ResultSet returnedUsers = selectUsersStatement
					.executeQuery(Helpers.GET_USERS_QUERY);
			while (returnedUsers.next()) {
				userDataList.add(new UserData(returnedUsers
						.getString("user_name"), returnedUsers
						.getString("user_pass"), returnedUsers
						.getString("user_id"), returnedUsers
						.getString("user_type")));
			}
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME,
					"Done.");
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME,
					CLASS_NAME,
					"Critical error, cannot get users! " + ex.getMessage());
		}

		try {
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME,
					"Getting kids from DB");
			selectUsersStatement.executeQuery(Helpers.GET_DB_SELECTION_QUERY);
			ResultSet returnedUsers = selectUsersStatement
					.executeQuery(Helpers.GET_KIDS_QUERY);
			while (returnedUsers.next()) {
				kidsDataList.add(new KidDataInternal(returnedUsers
						.getString("kid_name"), returnedUsers
						.getString("idkids"), returnedUsers
						.getString("pic_path"), returnedUsers
						.getString("parent_id"), returnedUsers
						.getString("garden_id")));
			}
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, CLASS_NAME,
					"Done.");
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME,
					CLASS_NAME,
					"Critical error, cannot get kids! " + ex.getMessage());
		} finally {
			if (selectUsersStatement != null) {
				try {
					selectUsersStatement.close();
				} catch (Exception e) {
					Logger.getInstance().Log(ELogLevel.error, CLASS_NAME,
							CLASS_NAME,
							"failed to close the statement" + e.getMessage());
				}
			}
		}

	}

	private boolean initInsertConnection() {
		boolean connectionSucceeded = false;
		try {
			insertConnectionObject = DriverManager.getConnection(goToiletURL,
					userName, password);
			insertConnectionObject.setAutoCommit(false);
			Statement selectUsersStatement = insertConnectionObject
					.createStatement();
			selectUsersStatement.executeQuery(Helpers.GET_DB_SELECTION_QUERY);
			connectionSucceeded = true;
		} catch (SQLException e) {
			Logger.getInstance().Log(ELogLevel.error, CLASS_NAME,
					"initConnection",
					"Failed to open connection" + e.getMessage());
			connectionSucceeded = false;
		}
		return connectionSucceeded;
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
	 * This method performs the login action based on the given username and
	 * pasword
	 * 
	 * @param userName
	 *            case insensitive user name
	 * @param pass
	 *            case sensitive user password
	 * @return null if not initialized, loginReturnData class holding the
	 *         connecting success status and the id otherwise
	 */
	public loginReturnData login(String userName, String pass) {
		String methodName = "login";
		try {
			for (UserData currentUser : userDataList) {
				if (currentUser.getUserName().equalsIgnoreCase(userName)) {
					if (currentUser.getPass().equals(pass)) {
						return new loginReturnData(currentUser.getUserType(),
								currentUser.getUserId());
					}
				}
			}
		} catch (Exception ex) {
			Logger.getInstance().Log(
					ELogLevel.error,
					CLASS_NAME,
					methodName,
					"Failed to login the given user: " + userName + ". error: "
							+ ex.getMessage());
			return null;
		}
		return new loginReturnData(ELoginStatus.EInvalid, "-1");
	}

	/**
	 * Call this method with a new event and it will be inserted to the db. This
	 * method does not verify the details!
	 * 
	 * @param newEvent
	 *            the event data, date will be null if there is no required time
	 * @return -1 if not initialized or failed, the id of the new event
	 *         otherwise
	 */
	public int insertNewEvent(EventData newEvent) {
		String methodName = "insertNewEvent";
		if (!initInsertConnection()) {
			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME,
					methodName, "The DB is not initialized!");
			return -1;
		}
		int nextEventIdForReal = 0;
		PreparedStatement selectUsersStatement = null, insertStatement = null;
		try {
			selectUsersStatement = insertConnectionObject
					.prepareStatement(Helpers.GET_EVENTS_COUNT_QUERY);
			ResultSet nextEventId = selectUsersStatement.executeQuery();
			if (!nextEventId.next()) {
				java.util.Random rans = new Random();
				nextEventIdForReal = rans.nextInt(99999);
			} else {
				nextEventIdForReal = nextEventId.getInt("Total") * 2 + 1;
			}
			selectUsersStatement.close();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:00");

			insertStatement = insertConnectionObject
					.prepareStatement(Helpers.INSERT_NEW_EVENT_QUERY);
			insertStatement.setString(1, "" + nextEventIdForReal);
			insertStatement.setString(
					2,
					(newEvent.getDateTime().isEmpty() ? sdf.format(
							Calendar.getInstance().getTime()).toString()
							: newEvent.getDateTime()));
			insertStatement.setString(3, newEvent.getInsertingUserId());
			insertStatement.setString(4, newEvent.getKidId());
			insertStatement.setString(5,
					newEvent.getCreatedIndependenceStagesSerialized());
			insertStatement.setString(6,
					newEvent.isKidIsInitiator() == true ? "1" : "0");
			insertStatement.setString(7, newEvent.getComments());
			insertStatement.setString(8, newEvent.getIsPipi() == true ? "1"
					: "0");
			insertStatement.setString(9, newEvent.getIsKaki() == true ? "1"
					: "0");
			insertStatement.setString(10, newEvent.getSuccessResult());

			int retVal = insertStatement.executeUpdate();
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName,
					"The new line ID: " + retVal);
		} catch (Exception ex) {
			Logger.getInstance().Log(
					ELogLevel.error,
					CLASS_NAME,
					methodName,
					"Error occured during inserting new event. Event data: "
							+ newEvent.toString() + ", error: "
							+ ex.getMessage());
			nextEventIdForReal = -1;
		} finally {
			if (selectUsersStatement != null) {
				try {
					selectUsersStatement.close();
				} catch (Exception ex) {
					Logger.getInstance().Log(
							ELogLevel.error,
							CLASS_NAME,
							methodName,
							"failed to close the select statement, error: "
									+ ex.getMessage());
				}
			}
			if (insertStatement != null) {
				try {
					insertStatement.close();
				} catch (Exception ex) {
					Logger.getInstance().Log(
							ELogLevel.error,
							CLASS_NAME,
							methodName,
							"failed to close the insert statement, error: "
									+ ex.getMessage());
				}
			}
			try {
				insertConnectionObject.commit();
				insertConnectionObject.close();
			} catch (Exception ex) {
				Logger.getInstance().Log(
						ELogLevel.error,
						CLASS_NAME,
						methodName,
						"failed to close the connection, error: "
								+ ex.getMessage());
			}
		}
		return nextEventIdForReal;
	}

	/**
	 * this method based on the given parameters returns a list of events
	 * 
	 * @param kidId
	 *            the id of the kid the his data is needed
	 * @param daysFromToday
	 *            number of days to bring history - the hour that will be used
	 *            is 00:00
	 * @return null if not initialized or wrong parameters, list of event data
	 */
	public List<EventData> getEventsForKid(String kidId, int daysFromToday) {
		String methodName = "getEventForUser";
		List<EventData> retVal = new ArrayList<EventData>();

		if (kidId.isEmpty() || daysFromToday <= 0) {
			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME,
					methodName, "The DB is not initialized!");
			return null;
		}

		PreparedStatement selectUsersStatement = null;
		try {
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName,
					"Getting events for kid: " + kidId);
			KidDataInternal selectedKid = null;
			for (KidDataInternal currentKid : kidsDataList) {
				if (currentKid.getKidId().equals(kidId)) {
					selectedKid = currentKid;
					break;
				}
			}

			if (selectedKid == null) {
				Logger.getInstance().Log(ELogLevel.error, CLASS_NAME,
						methodName, "given kid id was not found: " + kidId);
				return null;
			}
			Calendar cl = Calendar.getInstance();
			cl.add(Calendar.DAY_OF_MONTH, -daysFromToday);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

			selectUsersStatement = selectConnectionObject
					.prepareStatement(Helpers.GET_EVENTS_FOR_KID_QUERY);
			selectUsersStatement.setString(1, selectedKid.getKidId());
			selectUsersStatement.setString(2, sdf.format(cl.getTime()));

			ResultSet returnedEvents = selectUsersStatement.executeQuery();
			while (returnedEvents.next()) {
				List<IndependenceStages> stages = new ArrayList<IndependenceStages>();
				String[] stagesAsStringArray = returnedEvents.getString(
						"independence_stages").split(";");
				if (stagesAsStringArray.length > 0
						&& !stagesAsStringArray[0].isEmpty()) {
					for (String currentStage : stagesAsStringArray) {
						String[] currentStageSplitted = currentStage.split(",");
						if (currentStageSplitted.length > 0
								&& !currentStageSplitted[0].isEmpty()) {
							stages.add(new IndependenceStages(Enum.valueOf(
									EIndependenceStages.class,
									currentStageSplitted[0]), Enum.valueOf(
									EAssistantLevel.class,
									currentStageSplitted[1])));
						}
					}
				}
				retVal.add(new EventData(
						returnedEvents.getString("date_time"),
						returnedEvents.getString("inserting_user_id"),
						returnedEvents.getString("kid_id"),
						stages,
						returnedEvents.getString("kid_is_initiator").equals("1") ? true
								: false, returnedEvents.getString("comments"),
						returnedEvents.getString("isKaki").equals("1") ? true
								: false,
						returnedEvents.getString("isPipi").equals("1") ? true
								: false, 
						returnedEvents.getString("walkStatus")));
			}
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName,
					"Done.");
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME,
					methodName,
					"Critical error, cannot get users! " + ex.getMessage());
		} finally {
			if (selectUsersStatement != null) {
				try {
					selectUsersStatement.close();
				} catch (SQLException e) {
					Logger.getInstance().Log(ELogLevel.error, CLASS_NAME,
							methodName,
							"failed to close the statement: " + e.getMessage());
				}
			}
		}

		return retVal;
	}

	/**
	 * based on the given user id returns a list of the kids that he is related
	 * to
	 * 
	 * @param userId
	 *            the user id that the related kids is needed
	 * @return null if not initialized or wrong parameters, list of kids
	 *         otherwise
	 */
	public List<KidData> getListOfKids(String userId) {
		String methodName = "getListOfKids";
		List<KidData> retVal = new ArrayList<KidData>();
		try {
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName,
					"Getting list of kids for: " + userId);
			UserData givenUser = getDataForUser(userId);
			if (givenUser == null) {
				return null;
			}

			for (KidDataInternal currentInternalKid : kidsDataList) {
				if (givenUser.getUserId().equals(
						currentInternalKid.getGardenId())
						|| givenUser.getUserId().equals(
								currentInternalKid.getParentId())) {
					retVal.add(currentInternalKid.toKidData());
				}
			}
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName,
					"Done.");
		} catch (Exception ex) {
			retVal = null;
			Logger.getInstance().Log(ELogLevel.critical, CLASS_NAME,
					methodName,
					"Critical error, cannot get users! " + ex.getMessage());
		}

		return retVal;
	}

	/**
	 * based on the given UserId returns the details related to the user
	 * 
	 * @param userId
	 *            the given user Id
	 * @return a user data object, or null if not found or an error occurred
	 */
	public UserData getDataForUser(String userId) {
		String methodName = "getDataForUser";
		UserData retVal = null;
		try {
			for (UserData currentUser : userDataList) {
				if (currentUser.getUserId().equals(userId)) {
					retVal = currentUser;
					break;
				}
			}
		} catch (Exception ex) {
			Logger.getInstance().Log(
					ELogLevel.error,
					CLASS_NAME,
					methodName,
					"failed to get data for given user id: " + userId
							+ ". error: " + ex.getMessage());
		}
		return retVal;
	}

	/**
	 * gets the desired statistic for the kid id
	 * 
	 * @param kidId
	 *            the given kid id
	 * @param statisticType
	 *            the requested statistic type
	 * @return a list of statistics
	 */
	public List<Statistics> getStatisticsForKidId(String kidId,
			EStatisticType statisticType, int daysFromToday) {
		
		List<Statistics> retVal = new ArrayList<Statistics>();
		String methodName = "getStatisticForKidId";

		PreparedStatement selectUsersStatement = null;
		try {
			KidData data = getDataForKid(kidId);
			if (data == null) {
				Logger.getInstance().Log(ELogLevel.error, CLASS_NAME,
						methodName,
						"The selected given kid id does not exist: " + kidId);
				return null;
			}
			List<EventData> eventsForKid = getEventsForKid(kidId, daysFromToday);
			SuccessDbStatistics successRetVal = new SuccessDbStatistics();

			switch (statisticType) {
			case successes:
				for (EventData currentEvent : eventsForKid) {
					int success = Integer.parseInt(currentEvent
							.getSuccessResult());
					
					//only in case of real failure
					if(currentEvent.getIsKaki() || currentEvent.getIsPipi()) {
						successRetVal.add(currentEvent.getDateTime().split(" ")[0],
								success == 1 ? 1 : 0, success == 0 ? 1 : 0);
					}
				}
				retVal = successRetVal.toSuccessStatistics();
				break;
			case takes:
				for (EventData currentEvent : eventsForKid) {
					int success = Integer.parseInt(currentEvent
							.getSuccessResult());
					
					//no matter if there was a success or a failure we need to count it
					successRetVal.add(currentEvent.getDateTime().split(" ")[0],
							success == 1 ? 1 : 0, success == 0 ? 1 : 0);
				}
				retVal = successRetVal.toTakesStatistics();
				break;
			case timeSpaces:
			default:
				Logger.getInstance().Log(
						ELogLevel.error,
						CLASS_NAME,
						methodName,
						"The selected type of statistics is not supported yet. statistic type: "
								+ statisticType);
				break;
			}
		} catch (Exception ex) {
			Logger.getInstance().Log(
					ELogLevel.error,
					CLASS_NAME,
					methodName,
					"Error getting th statistics for the given kid id: "
							+ kidId + "and desired staistic type: "
							+ statisticType + ". Error: " + ex.getMessage());
			retVal = null;
		} finally {
			if (selectUsersStatement != null) {
				try {
					selectUsersStatement.close();
				} catch (Exception e) {
					Logger.getInstance().Log(ELogLevel.error, CLASS_NAME,
							methodName,
							"failed to close the statement: " + e.getMessage());
				}
			}
		}
		return retVal;
	}

	public KidData getDataForKid(String kidId) {
		KidData retVal = null;
		for (KidDataInternal currentKid : kidsDataList) {
			if (currentKid.getKidId().equals(kidId)) {
				retVal = currentKid.toKidData();
				break;
			}
		}
		return retVal;
	}
}