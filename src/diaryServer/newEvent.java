package diaryServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONWriter;

import dataTypes.EventData;

/**
 * REQUEST STRUCTUR FOR NEW EVENT IS DONE WHEN ASKING FOR /newEvent (url page)
 * STRCUTRE: (key=value)
 * 	"KidID" = "12345"(string)
 *  "comments" = "bla"(string)
 *  "dateTime" = "11.11.11"(string)
 *  "isKaki" = True/False (boolean)
 *  "isPipi" = True/false(boolean)

 * @author ilaisit
 *
 */
public class newEvent extends HttpServlet {

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
		System.out.println("making now new kid event");
		System.out.println("Trying to view kid");
		String jsonReq = extractJsonFromRequest(request);
		JSONObject currentEventJson = new JSONObject(jsonReq);

		JSONWriter writer = null;
		try {
			writer = new JSONWriter(response.getWriter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("parsing the input event");
		// independence stages is not handlet meanwhile! maybe next version
		EventData newEvent = new EventData();
		newEvent.setKidId(currentEventJson.getString("kidID"));
		newEvent.setComments(currentEventJson.getString("comments"));
		newEvent.setDateTime(currentEventJson.getString("dateTime"));
		newEvent.setInsertingUserId(currentEventJson
				.getString("insertingUserId"));
		newEvent.setIsKaki(currentEventJson.getBoolean("isKaki"));
		newEvent.setIsPipi(currentEventJson.getBoolean("isPipi"));

		// temp is never used, we don't use the 'entry id'
		int temp = dbManager.getInstance().insertNewEvent(newEvent);

		// pulls events from last 24 for the kid
		List<EventData> kidEvents = dbManager.getInstance().getEventsForKid(
				newEvent.getKidId(), 1);
		JSONObject outputList = new JSONObject();

		// write back the answer to ido (atifa in json)
		for (EventData eventData : kidEvents) {
			outputList.put("dateTime", eventData.getDateTime());
			outputList.put("insertingUserId", eventData.getInsertingUserId());
			outputList.put("kidId", eventData.getKidId());
			// *Note: meanwhile, no createdIndependenceStages are sent
			// outputList.put("createdIndependenceStages",
			// eventData.getCreatedIndependenceStages());
			outputList.put("kidIsInitiator", eventData.isKidIsInitiator());
			outputList.put("comments", eventData.getComments());
			outputList.put("isKaki", eventData.getIsKaki());
			outputList.put("isPipi", eventData.getIsPipi());
		}

		// the key is KID_EVENTS_LIST,
		// and the value is the list of the events of the kid
		writer.object();
		writer.key(KID_EVENTS);
		writer.value(outputList);
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
