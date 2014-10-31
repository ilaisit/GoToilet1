package diaryServer;


public class Helpers {
	public static final String GET_DB_SELECTION_QUERY = "USE gotoilet;";
	public static final String GET_USERS_QUERY = "SELECT * FROM users;";
	public static final String GET_KIDS_QUERY = "SELECT * FROM kids";
	public static final String GET_EVENTS_FOR_KID_QUERY = "SELECT * FROM EventsLog WHERE kid_id=\"%%KID_ID%%\" AND date_time > \"%%DATE_TIME%%\"";
	public final static String KID_ID = "%%KID_ID%%";
	public final static String DATE_TIME = "%%DATE_TIME%%";


}
