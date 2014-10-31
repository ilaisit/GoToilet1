package diaryServer;

//import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONWriter;

public class handleConnection extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// KEYS
	final String FAILURE = "fail";
	final String RESP_TYPE = "type";
	final String KID_EVENTS_LIST = "data";
	final String KIDS_LIST = "data";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		parseRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		parseRequest(req, resp);
	}

	private void parseRequest(HttpServletRequest request,
			HttpServletResponse response) {
		// PrintWriter out = null;
		System.out.println("hello again");
		String jsonReq = extractJsonFromRequest(request);
		JSONObject curLogin = new JSONObject(jsonReq);
		loginTry curr = new loginTry();

		curr.setUserName(curLogin.getString("name"));
		curr.setPassword(curLogin.getString("pass"));

		JSONWriter writer = null;
		try {
			writer = new JSONWriter(response.getWriter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		writer.object();
		writer.key(RESP_TYPE);
		// now, checks with DB which type of login it is
		loginReturnData loginData = dbManagerO.getInstance().login(
				curr.getUserName(), curr.getPassword());
		writer.value(loginData.getLoginStatus());
		String userID;
		JSONObject outputList;
		switch (loginData.getLoginStatus()) {
		case EInvalid: // invalid login
			System.out.println("its -1!");
			break;
		case EUserType_Garden: // input: name of kindergarden
			System.out.println("its kindergarten!");
			userID = loginData.getUserId();
			List<KidData> kidsList = dbManagerO.getInstance().getListOfKids(
					userID);
			outputList = new JSONObject();
			for (KidData kid : kidsList) {
				outputList.put("kidName", kid.getKidName());
				outputList.put("kidId", kid.getKidId());
				outputList.put("imageLink", kid.getImageLink());
			}
			writer.key(KIDS_LIST);
			writer.value(outputList);
			break;
		case EUserType_Parent: // e-mail - means it is specific father, and i
								// will output events for his kid
			System.out.println("its parent!");
			userID = loginData.getUserId();
			List<EventData> kidEvenetsLastWeek = dbManagerO.getInstance()
					.getEventForKid(userID, 7);
			outputList = new JSONObject();
			for (EventData eventData : kidEvenetsLastWeek) {
				outputList.put("dateTime", eventData.getDateTime());
				outputList.put("insertingUserId",
						eventData.getInsertingUserId());
				outputList.put("kidId", eventData.getKidId());
				//*NOTE: meanwhile, we are not sending IndependenceStages
			//	outputList.put("createdIndependenceStages",
				//		eventData.getCreatedIndependenceStages());
				outputList.put("kidIsInitiator", eventData.isKidIsInitiator());
				outputList.put("comments", eventData.getComments());
				outputList.put("isKaki", eventData.getIsKaki());
				outputList.put("isPipi", eventData.getIsPipi());
				
			}
			// the key is KID_EVENTS_LIST,
			// and the value is the list of the events of the kid
			writer.key(KID_EVENTS_LIST);
			writer.value(outputList);
			break;

		}
		writer.endObject();
	}

	private String extractJsonFromRequest(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = request.getReader();
		} catch (IOException e) {
			System.out.println("Hi! could not extract json from request");
			e.printStackTrace();
			return FAILURE;

		}
		String str;
		try {
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
		} catch (IOException e) {
			System.out.println("could not read br - could not extract json");
			e.printStackTrace();
			return FAILURE;
		}
		return sb.toString();
	}
}
