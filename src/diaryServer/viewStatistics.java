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

import dataTypes.Statistics;
import enums.EStatisticType;

public class viewStatistics extends HttpServlet {

	final String FAILURE = "fail";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		parseRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		parseRequest(req, resp);
	}

	private void parseRequest(HttpServletRequest request,
			HttpServletResponse response) {

		// public List<Statistics> getStatisticsForKidId(String kidId,
		// EStatisticType statisticType, int daysFromToday)

		System.out.println("Trying to view statistics");
		String jsonReq = extractJsonFromRequest(request);
		JSONObject currentStat = new JSONObject(jsonReq);

		JSONWriter writer = null;
		try {
			writer = new JSONWriter(response.getWriter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.object();

		// The KEY received from CLIENT is "UserID". expecting to receive the
		// userID = kindergartenID
		String kidId = currentStat.getString("kidId");
		String type = currentStat.getString("statisticType");
		int days = currentStat.getInt("daysFromToday");
		System.out.println("days ");
		System.out.println(days);
		List<Statistics> statList = dbManager.getInstance()
				.getStatisticsForKidId(kidId,
						Enum.valueOf(EStatisticType.class, type), days);

		// Enum.valueOf(EStatisticType.class, type)
		System.out.println("list size ");
		System.out.println(statList.size());
		JSONObject responseDetailsJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < statList.size(); i++) {
			JSONObject formDetailsJson = new JSONObject();
			formDetailsJson.put("date", statList.get(i).getDate());
			formDetailsJson.put("value", statList.get(i).getValue());
			jsonArray.put(formDetailsJson);

		}
		responseDetailsJson.put("arrayValues", jsonArray);
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
}
