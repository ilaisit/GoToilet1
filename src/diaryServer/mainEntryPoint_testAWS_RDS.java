package diaryServer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class mainEntryPoint_testAWS_RDS {

	public static void main(String[] args) {
		try {
			//Testing user login
			loginReturnData resultData = null;
			
			resultData = dbManager.getInstance().login("oz@gmail.com", "1234");
			System.out.println("should be true: " + (resultData.getUserId().equals("-1") && resultData.getLoginStatus() == ELoginStatus.EInvalid));
			
			resultData = dbManager.getInstance().login("מיכל ירוקה", "1234");
			System.out.println(resultData.getUserId().equals("-1") && resultData.getLoginStatus() == ELoginStatus.EInvalid);
			
			resultData = dbManager.getInstance().login("מיכל ירוקה", "michal");
			System.out.println("should be true: " + (resultData.getUserId().equals("0") && resultData.getLoginStatus() == ELoginStatus.EUserType_Garden));
			
			resultData = dbManager.getInstance().login("hanalevi1@gmail.com", "hanal");
			System.out.println("should be true: " + (resultData.getUserId().equals("10") && resultData.getLoginStatus() == ELoginStatus.EUserType_Parent));
			
			//Testing getting kids list
			List<KidData> kids = null;
			kids = dbManager.getInstance().getListOfKids("1"); //מיכל ירוקה
			System.out.println("should be true: " + (kids.size() > 0));

			kids = dbManager.getInstance().getListOfKids("10"); //hanalevi1@gmail.com
			System.out.println("should be true: " + (kids == null));

			kids = dbManager.getInstance().getListOfKids("-1"); //invalid
			System.out.println("should be true: " + (kids == null));

			dbManager.getInstance().getEventsForKid("1", 7);
			dbManager.getInstance().getEventsForKid("2", 1);
			dbManager.getInstance().getEventsForKid("3", 30);
			
			dbManager.getInstance().insertNewEvent(new EventData("", "1", "1", new ArrayList<IndependenceStages>(), true, ""));
			dbManager.getInstance().insertNewEvent(new EventData("", "1", "1", new ArrayList<IndependenceStages>(), true, ""));

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbManager.getInstance().close();
		}
	}
}
