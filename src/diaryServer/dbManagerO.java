package diaryServer;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class dbManagerO {

	private static final String CLASS_NAME = "dbManager";

	private dbManagerO() {

	}

	private static Object _syncObj = new Object();
	private static dbManagerO _instance = null;

	public static dbManagerO getInstance() {
		if (_instance != null) {
			return _instance;
		}
		synchronized (_syncObj) {
			if (_instance != null) {
				return _instance;
			}
			_instance = new dbManagerO();
		}

		return _instance;
	}

	/**
	 * This method performs the login action based on the given username and pasword
	 * 
	 * @param userName
	 * @param pass
	 * @return the user Id
	 */
	public loginReturnData login(String userName, String pass) {
		return new loginReturnData(ELoginStatus.EUserType_Parent, "1");
	}

	/**
	 * Call this method with a new event and it will be inserted to the db. This method does not verify the details!
	 * 
	 * @param the
	 *            event data
	 * @return the id of the new event, -1 if failed
	 */
	public int insertNewEvent(EventData newEvent) {
		String methodName = "insertNewEvent";
		int newEntryId = -1;

		try {
			String queryText = "";
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.error, CLASS_NAME, methodName,
					"Error occured during inserting new event. Event data: " + newEvent.toString() + ", error: " + ex.getMessage());
			newEntryId = -1;
		}
		return newEntryId;
	}

	public List<EventData> getEventForKid(String kidId, int daysFromToday) {
		String methodName = "getEventForUser";
		List<EventData> retVal = new ArrayList<EventData>();
		try {
			java.util.Random rand = new Random();
			for (int counter = 0; counter < rand.nextInt(100); counter++) {
				retVal.add(new EventData("", "2", kidId, getRandomIndependenceStages(), (rand.nextInt(2) == 0 ? true : false), "comments " + counter));
			}
		} catch (Exception ex) {
			Logger.getInstance().Log(ELogLevel.error, CLASS_NAME, methodName,
					"failed to return events for kid " + kidId + ", days from today received " + daysFromToday);
			retVal = new ArrayList<EventData>();
		}

		return retVal;
	}

	public List<KidData> getListOfKids(String userId) {
		String methodName = "getListOfKids";
		List<KidData> retVal = new ArrayList<KidData>();
		try {
			java.util.Random randNum = new Random();
			for(int counter = 0; counter < randNum.nextInt(100); counter++) {
				retVal.add(new KidData("" + counter, "name " + counter));
			}
		}catch(Exception ex) {
			Logger.getInstance().Log(ELogLevel.error, CLASS_NAME, methodName, "Error getting the list of kids. user Id given: " + userId);
			retVal = new ArrayList<KidData>();
		}
		
		return retVal;
	}
	
	private List<IndependenceStages> getRandomIndependenceStages() {
		EIndependenceStages[] allIndependenceStages = EIndependenceStages.values();
		EAssistantLevel[] allAssistant = EAssistantLevel.values();
		List<IndependenceStages> retVal = new ArrayList<IndependenceStages>();
		java.util.Random randHelper = new Random();
		for (int counter = 0; counter < randHelper.nextInt(allIndependenceStages.length); counter++) {
			retVal.add(new IndependenceStages(allIndependenceStages[randHelper.nextInt(allIndependenceStages.length)], allAssistant[randHelper
					.nextInt(allAssistant.length)]));
		}

		return retVal;
	}
}
