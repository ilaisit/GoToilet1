package diaryServer;
public class Helpers {
	public static final String GET_DB_SELECTION_QUERY = "USE gotoilet;";
	public static final String GET_EVENTS_COUNT_QUERY = "SELECT COUNT(*) As Total FROM EventsLog;";
	public static final String GET_USERS_QUERY = "SELECT * FROM users LIMIT 300;";
	public static final String GET_KIDS_QUERY = "SELECT * FROM kids LIMIT 300";
	public static final String GET_EVENTS_FOR_KID_QUERY = "SELECT * FROM EventsLog WHERE kid_id=? AND date_time > ? LIMIT 300";
	public final static String INSERT_NEW_EVENT_QUERY = "INSERT INTO EventsLog VALUES (?,?,?,?,?,?,?,?,?,?);";
//	public final static String ID_EVENTS_LOG = "%%ID_EVENTS_LOG%%";
//	public final static String DATE_TIME = "%%DATE_TIME%%";
//	public final static String INSERTING_USER_ID = "%%INSERTING_USER_ID%%";
//	public final static String KID_ID = "%%KID_ID%%";
//	public final static String INDEPENDENCE_STAGES = "%%INDEPENDENCE_STAGES%%";
//	public final static String KID_IS_INITIATOR = "%%KID_IS_INITIATOR%%";
//	public final static String COMMENTS = "%%COMMENTS%%";
//	public final static String IS_PIPI = "%%ISPIPI%%";
//	public final static String IS_KAKI = "%%ISKAKI%%";
//	public final static String WALK_STATUS = "%%WALK_STATUS%%";

	/**
	 * all statistics queries
	 */
	public final static String GET_DATES_FOR_EVENTS_FOR_KID_QUERY = "SELECT date_time FROM EventsLog WHERE kid_id='%%KID_ID%%' GROUP BY date_time ORDER BY date_time;";
	public final static String GET_ALL_EVENTS_AND_SUCCESS_QUERY = "SELECT (SELECT COUNT(*) FROM EventsLog WHERE kid_id='%%KID_ID%%' AND walkStatus='1' OR walkStatus='0' AND date_time='%%DATE_TIME%%') AS all (SELECT COUNT(*) FROM EventsLog WHERE kid_id='%%KID_ID%%' AND walkStatus='0' date_time='%%DATE_TIME%%') as success;";
}
