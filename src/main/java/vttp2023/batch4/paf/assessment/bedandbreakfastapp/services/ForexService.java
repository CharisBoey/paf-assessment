package vttp2023.batch4.paf.assessment.bedandbreakfastapp.services;

import java.io.StringReader;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ForexService {

	public String base_url="https://api.frankfurter.app/latest";

	// TODO: Task 5 
	public float convert(String from, String to, float amount) {
		String url = UriComponentsBuilder
					.fromUriString(base_url)
					.queryParam("from", from)
					.queryParam("to", to)
					.queryParam("amount", amount)
					.toUriString();

		RequestEntity<Void> request = RequestEntity.get(url).build();
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> response = null;
		float convertedAmt = -1000f;
		try{
			/* {
				"amount": 100,
				"base": "AUD",
				"date": "2024-01-25",
				"rates": {
				"SGD": 88.23
				} */
			response = template.exchange(request, String.class);
			String jsonString = response.getBody();

			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject jsonMainObject = jsonReader.readObject();
			JsonObject jsonRates = jsonMainObject.getJsonObject("rates");
			convertedAmt = (float) jsonRates.getJsonNumber("SGD").doubleValue();
			//data.(json.Number).Float64()
			

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1000f;
		}
		return convertedAmt;
	}
}

