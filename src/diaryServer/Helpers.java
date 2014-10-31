package diaryServer;

public class Helpers {
	public static final String GET_DB_SELECTION_QUERY = "USE gotoilet;";
	public static final String GET_EVENTS_COUNT_QUERY = "SELECT COUNT(*) As Total FROM EventsLog;";
	public static final String GET_USERS_QUERY = "SELECT * FROM users;";
	public static final String GET_KIDS_QUERY = "SELECT * FROM kids";
	public static final String GET_EVENTS_FOR_KID_QUERY = "SELECT * FROM EventsLog WHERE kid_id=\"%%KID_ID%%\" AND date_time > \"%%DATE_TIME%%\"";
	public final static String INSERT_NEW_EVENT_QUERY = "INSERT INTO EventsLog VALUES ('%%ID_EVENTS_LOG%%', '%%DATE_TIME%%','%%INSERTING_USER_ID%%','%%KID_ID%%','%%INDEPENDENCE_STAGES%%','%%KID_IS_INITIATOR%%','%%COMMENTS%%','%%ISPIPI%%','%%ISKAKI%%');";
	public final static String ID_EVENTS_LOG = "%%ID_EVENTS_LOG%%";
	public final static String DATE_TIME = "%%DATE_TIME%%";
	public final static String INSERTING_USER_ID = "%%INSERTING_USER_ID%%";
	public final static String KID_ID = "%%KID_ID%%";
	public final static String INDEPENDENCE_STAGES = "%%INDEPENDENCE_STAGES%%";
	public final static String KID_IS_INITIATOR = "%%KID_IS_INITIATOR%%";
	public final static String COMMENTS = "%%COMMENTS%%";
	public final static String IS_PIPI = "%%ISPIPI%%";
	public final static String IS_KAKI = "%%ISKAKI%%";
}
