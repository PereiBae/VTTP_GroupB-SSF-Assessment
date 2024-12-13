package vttp.batch5.ssf.noticeboard.services;

import jakarta.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

import java.io.StringReader;
import java.util.Date;
import java.util.List;

@Service
public class NoticeService {

	@Autowired
	private NoticeRepository noticeRepo;

	@Value("${rest.api.link}")
	private String REST_API;

	// TODO: Task 3
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	public String postToNoticeServer(String title, String poster, Date postDate, List<String> categories, String text) throws Exception {

		JsonArray jsonArray = categoryJson(categories);

		JsonObject requestPayload= Json.createObjectBuilder()
				.add("title",title)
				.add("poster", poster)
				.add("postDate", postDate.getTime())
				.add("categories",jsonArray)
				.add("text", text)
				.build();
		System.out.println("Request payload built");
		System.out.println(requestPayload.toString());

		String url = UriComponentsBuilder.fromUriString(REST_API)
				.build()
				.toUriString();
		System.out.println("url built");

		// Create a GET request
		RequestEntity<String> request = RequestEntity
				.post(url)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(requestPayload.toString());
		System.out.println("Request entity built");

		// Use the RestTemplate to send the request
		RestTemplate restTemplate = new RestTemplate();

		// Fetch the response
		System.out.println("Executing request");
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		System.out.println("Response Status: " + response.getStatusCode());
		System.out.println("Response Body: " + response.getBody());

		if(response.getStatusCode().equals(HttpStatus.OK)) {
			// Notice is successful
			System.out.println("Notice Posted Successfully");
			String payload = response.getBody();
			JsonReader jsonReader = Json.createReader(new StringReader(payload));
			JsonObject jsonObject = jsonReader.readObject();
			noticeRepo.insertNotices(jsonObject);
            return jsonObject.getString("id");
		}

		// Handle unsuccessful responses
		String payload = response.getBody();
		JsonReader jsonReader = Json.createReader(new StringReader(payload));
		JsonObject jsonObject = jsonReader.readObject();
		String errorMessage = jsonObject.getString("message");
		throw new Exception(errorMessage);
	}

	// Convert List to a JsonArray
	private JsonArray categoryJson(List<String> categories) {
		JsonArray jsonArray = null;
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
		for(int i = 0; i < categories.size(); i++) {
			jsonArrayBuilder.add(categories.get(i));
		}
		return jsonArray = jsonArrayBuilder.build();
	}

	public boolean getRandomKey(){
		try{
			String healthCheck = noticeRepo.getRandomKey();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
