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

import dataTypes.EventData;
/**
 * the request must contain kidID and and daysFromToday
 * @author ilaisit
 *
 */
public class viewKid extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String FAILURE = "fail";
	final String KID_EVENTS = "data";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		parseRequest(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		parseRequest(req, resp);
	}

	private void parseRequest(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("Trying to view kid");
		String jsonReq = extractJsonFromRequest(request);
		JSONObject currentKid = new JSONObject(jsonReq);

		JSONWriter writer = null;
		try {
			writer = new JSONWriter(response.getWriter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("parsing kid events");
		// The KEY received from CLIENT is "UserID". expecting to receive the
		// userID = KidID
		String kidID = currentKid.getString("kidId");
		//recive value from client p - that says how many days ago he wants data for the kids events
		int daysFromToday = currentKid.getInt("daysFromToday");
		List<EventData> kidEvents = dbManager.getInstance().getEventsForKid(kidID, daysFromToday);
		System.out.println("there are ");
		System.out.println(kidEvents.size());
		System.out.println("events for this kid");
		
		JSONObject responseDetailsJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < kidEvents.size(); i++) {
			JSONObject formDetailsJson = new JSONObject();
			formDetailsJson.put("kidId", kidEvents.get(i)
					.getKidId());
			formDetailsJson.put("isKaki", kidEvents.get(i)
					.getIsKaki());
			formDetailsJson.put("dateTime", convertDate(kidEvents.get(i)
					.getDateTime()));
			formDetailsJson.put("insertingUserId", kidEvents
					.get(i).getInsertingUserId());
			formDetailsJson.put("isPipi", kidEvents.get(i)
					.getIsPipi());
			formDetailsJson.put("comments", kidEvents.get(i)
					.getComments());
			formDetailsJson.put("kidIsInitiator", kidEvents
					.get(i).isKidIsInitiator());
			formDetailsJson.put("successResult", kidEvents.get(i).getSuccessResult());

			jsonArray.put(formDetailsJson);
		}
		responseDetailsJson.put("arrayValues", jsonArray);
		writer.object();
		writer.key("data");
		writer.value(responseDetailsJson);
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
	
	private String convertDate (String input) {
		StringBuilder sb = new StringBuilder();
		String year = input.substring(0,4);
		String month = input.substring(5,7);
		String day = input.substring(8, 10);
		String hour = input.substring(11, 13);
		String min = input.substring(14, 16);
		
		sb.append(day);
		sb.append(".");
		sb.append(month);
		sb.append(".");
		sb.append(year);
		sb.append(" ");
		sb.append(hour);
		sb.append(":");
		sb.append(min);
		sb.append(" ");
		String res = sb.toString();
		return res;
	}

}
