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

public class viewGarden extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//KEYS
	final String FAILURE = "fail";
	final String RESP_TYPE = "type";
	final String KIDS_LIST = "data";


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
		System.out.println("Trying to view garden");
		String jsonReq = extractJsonFromRequest(request);
		JSONObject currentGarden = new JSONObject(jsonReq);
		
		JSONWriter writer = null;
		try {
			writer = new JSONWriter(response.getWriter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("parsing kindergarten");
		// The KEY received from CLIENT is "UserID". expecting to receive the userID = kindergartenID
		String userID = currentGarden.getString("userID");
		List<KidData> kidsList = dbManager.getInstance().getListOfKids(
				userID);
		JSONObject outputList = new JSONObject();
		for (KidData kid : kidsList) {
			outputList.put("kidName", kid.getKidName());
			outputList.put("kidId", kid.getKidId());
			outputList.put("imageLink", kid.getImageLink());
		}
		writer.object();
		writer.key(KIDS_LIST);
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
			System.out.println("could not read br");
			e.printStackTrace();
			return FAILURE;
		}
		return sb.toString();
	}

}
