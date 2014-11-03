package diaryServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

public class handleConnection extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final static String CLASS_NAME = "handleConnection";
	// KEYS
	private final String FAILURE = "fail";
	private final String RESP_TYPE = "type";
	private final String KID_EVENTS_LIST = "data";
	private final String KIDS_LIST = "data";

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

	public void parseRequest2(HttpServletRequest request,
			HttpServletResponse response) {

		List<EventData> kidEvents = dbManager.getInstance().getEventsForKid(
				"1", 8);

		JSONWriter writer = null;
		try {
			writer = new JSONWriter(response.getWriter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject responseDetailsJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < kidEvents.size(); i++) {
			JSONObject formDetailsJson = new JSONObject();
			formDetailsJson.put("kidId", kidEvents.get(i).getKidId());
			formDetailsJson.put("isKaki", kidEvents.get(i).getIsKaki());

			jsonArray.put(formDetailsJson);
		}
		responseDetailsJson.put("forms", jsonArray);

		writer.object();
		writer.key("hi");
		writer.value(responseDetailsJson);
		writer.endObject();

	}

	public void parseRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String methodName = "parseRequest";
		// PrintWriter out = null;
		response.setCharacterEncoding("UTF-8");
		Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "hello again");
		String jsonReq = extractJsonFromRequest(request);
		JSONObject curLogin = new JSONObject(jsonReq);
		loginTry curr = new loginTry();

		curr.setUserName(curLogin.getString("name"));
		curr.setPassword(curLogin.getString("pass"));
		Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "trying to parse " + curLogin.getString("name"));
		Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "pass is " + curLogin.getString("pass"));

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
		loginReturnData loginData = dbManager.getInstance().login(
				curr.getUserName(), curr.getPassword());

		String userID;
		if (loginData == null) {
			Logger.getInstance().Log(ELogLevel.error, CLASS_NAME, methodName, "login data could not be read");
			return;
		} else {
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "login status is not null");
		}
		JSONObject responseDetailsJson;
		JSONArray jsonArray;
		switch (loginData.getLoginStatus()) {
		case EInvalid: // invalid login
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "its -1!");
			writer.value("0");
			break;
		case EUserType_Garden: // input: name of kindergarden
			writer.value("1");
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "its kindergarten!");
			userID = loginData.getUserId();
			List<KidData> kidsList = dbManager.getInstance().getListOfKids(
					userID);
			
			writer.key("userID");
			writer.value(userID);
	

			responseDetailsJson = new JSONObject();
			jsonArray = new JSONArray();

			for (int i = 0; i < kidsList.size(); i++) {
				JSONObject formDetailsJson = new JSONObject();
				formDetailsJson.put("kidId", kidsList.get(i).getKidId());
				formDetailsJson.put("kidName", kidsList.get(i).getKidName());
				formDetailsJson
						.put("imageLink", kidsList.get(i).getImageLink());

				jsonArray.put(formDetailsJson);
			}
			responseDetailsJson.put("arrayValues", jsonArray);
			writer.key(KIDS_LIST);
			writer.value(responseDetailsJson);

			// outputList = new JSONObject();
			// for (KidData kid : kidsList) {
			// outputList.put("kidName", kid.getKidName());
			// outputList.put("kidId", kid.getKidId());
			// outputList.put("imageLink", kid.getImageLink());
			// }
			// writer.key(KIDS_LIST);
			// writer.value(outputList);
			// writer.value(outputList);
			break;
		case EUserType_Parent: // e-mail - means it is specific father, and i
								// will output events for his kid
			writer.value("2");
			Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "its parent!");
			userID = loginData.getUserId();
			

			// now i will find the kidID, in order to getEventsForKid
			List<KidData> kidsList1 = dbManager.getInstance().getListOfKids(
					userID);
			if (!kidsList1.isEmpty()) {
				Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "the parent has at least one kid: " + kidsList1.size());
				List<EventData> kidEvenetsLastDay = dbManager.getInstance()
						.getEventsForKid(kidsList1.get(0).getKidId(), 1);
				
				KidData curKid = kidsList1.get(0);
				writer.key("kidName");
				writer.value(curKid.getKidName());
				Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "kidName is " + curKid.getKidName());
				writer.key("imageLink");
				writer.value(curKid.getImageLink());
				Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "image link is: " + curKid.getImageLink());
				
				
				
				if (kidEvenetsLastDay.size() == 0) {
					Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "no events for the kid ");
				}
				Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName, "the kid has this number of events in the last day : " + kidEvenetsLastDay.size());

				writer.key("userID");
				writer.value(userID);

				responseDetailsJson = new JSONObject();
				jsonArray = new JSONArray();

				for (int i = 0; i < kidEvenetsLastDay.size(); i++) {
					JSONObject formDetailsJson = new JSONObject();
					formDetailsJson.put("kidId", kidEvenetsLastDay.get(i)
							.getKidId());
					formDetailsJson.put("isKaki", kidEvenetsLastDay.get(i)
							.getIsKaki());
					formDetailsJson.put("dateTime", convertDate(kidEvenetsLastDay.get(i)
							.getDateTime()));
					formDetailsJson.put("insertingUserId", kidEvenetsLastDay
							.get(i).getInsertingUserId());
					formDetailsJson.put("isPipi", kidEvenetsLastDay.get(i)
							.getIsPipi());
					formDetailsJson.put("comments", kidEvenetsLastDay.get(i)
							.getComments());
					formDetailsJson.put("kidIsInitiator", kidEvenetsLastDay
							.get(i).isKidIsInitiator());
					formDetailsJson.put("successResult", kidEvenetsLastDay.get(i).getSuccessResult());

					jsonArray.put(formDetailsJson);
				}
				responseDetailsJson.put("arrayValues", jsonArray);
				writer.key(KID_EVENTS_LIST);
				writer.value(responseDetailsJson);

				// int i = 0;
				// for (EventData eventData : kidEvenetsLastDay) {
				// JSONObject outputList1= new JSONObject();
				// outputList1.put("dateTime", eventData.getDateTime());
				// outputList1.put("insertingUserId",
				// eventData.getInsertingUserId());
				// outputList1.put("kidId", eventData.getKidId());
				// // *NOTE: meanwhile, we are not sending IndependenceStages
				// // outputList.put("createdIndependenceStages",
				// // eventData.getCreatedIndependenceStages());
				// outputList1.put("kidIsInitiator",
				// eventData.isKidIsInitiator());
				// outputList1.put("comments", eventData.getComments());
				// outputList1.put("isKaki", eventData.getIsKaki());
				// outputList1.put("isPipi", eventData.getIsPipi());
				//
				// // the key is KID_EVENTS_LIST,
				// // and the value is the list of the events of the kid
				// writer.key("id");
				// writer.value(i);
				// i++;
				// }

				break;
			}
		}
		writer.endObject();
	}

	private String extractJsonFromRequest(HttpServletRequest request) {
		String methodName = "extractJsonFromRequest";
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = request.getReader();
		} catch (IOException e) {
			Logger.getInstance().Log(
					ELogLevel.error,
					CLASS_NAME,
					methodName,
					"Hi! could not extract json from request: "
							+ e.getMessage());
			e.printStackTrace();
			return FAILURE;

		}
		String str;
		try {
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
		} catch (IOException e) {
			Logger.getInstance().Log(
					ELogLevel.error,
					CLASS_NAME,
					methodName,
					"could not read br - could not extract json: "
							+ e.getMessage());
			e.printStackTrace();
			return FAILURE;
		}
		return sb.toString();
	}

	private String convertDate(String input) {
		String methodName = "convertDate";
		String date = "2014-11-01 08:00:00";
		Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName,
				"before: " + date);
		StringBuilder sb = new StringBuilder();
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		String hour = date.substring(11, 13);
		String min = date.substring(14, 16);
		sb.append(hour);
		sb.append(":");
		sb.append(min);
		sb.append(" ");
		sb.append(day);
		sb.append(".");
		sb.append(month);
		sb.append(".");
		sb.append(year);
		String res = sb.toString();
		Logger.getInstance().Log(ELogLevel.debug, CLASS_NAME, methodName,
				"after: " + res);
		return res;
	}
}
