package vttp2023.batch4.paf.assessment.bedandbreakfastapp.controllers;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp2023.batch4.paf.assessment.bedandbreakfastapp.Utils;
import vttp2023.batch4.paf.assessment.bedandbreakfastapp.models.Accommodation;
import vttp2023.batch4.paf.assessment.bedandbreakfastapp.models.Bookings;
import vttp2023.batch4.paf.assessment.bedandbreakfastapp.services.ListingsService;


@Controller
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class BnBController {

	// You may add additional dependency injections

	@Autowired
	private ListingsService listingsSvc;
	
	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/suburbs")
	@ResponseBody
	public ResponseEntity<String> getSuburbs() {
		List<String> suburbs = listingsSvc.getAustralianSuburbs();
		JsonArray result = Json.createArrayBuilder(suburbs).build();
		return ResponseEntity.ok(result.toString());
	}
	
	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/search")
	@ResponseBody
	public ResponseEntity<String> search(@RequestParam MultiValueMap<String, String> params) {

		String suburb = params.getFirst("suburb");
		int persons = Integer.parseInt(params.getFirst("persons"));
		int duration = Integer.parseInt(params.getFirst("duration"));
		float priceRange = Float.parseFloat(params.getFirst("price_range"));

		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		listingsSvc.findAccommodatations(suburb, persons, duration, priceRange)
			.stream()
			.forEach(acc -> 
				arrBuilder.add(
					Json.createObjectBuilder()
						.add("id", acc.getId())
						.add("name", acc.getName())
						.add("price", acc.getPrice())
						.add("accommodates", acc.getAccomodates())
						.build()
				)
			);

		return ResponseEntity.ok(arrBuilder.build().toString());
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/accommodation/{id}")
	@ResponseBody
	public ResponseEntity<String> getAccommodationById(@PathVariable String id) {

		Optional<Accommodation> opt = listingsSvc.findAccommodatationById(id);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(Utils.toJson(opt.get()).toString());
	}

	// TODO: Task 6
	
	@PostMapping("accommodation")
	public ResponseEntity<String> processBooking(@RequestBody String payload) {
		
		Bookings bookings = new Bookings();
		JsonReader r = Json.createReader(new StringReader(payload));
      	JsonObject j = r.readObject();

		bookings.setName(j.getString("name"));
		bookings.setEmail(j.getString("email"));
		bookings.setDuration(j.getInt("nights"));
		bookings.setListingId(j.getString("id"));
		
		/* try {
			listingsSvc.createBooking(bookings);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(500).body("{DataAccessException Error}");
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(500).body("{NoSuchFieldException Error}");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(500).body("{SecurityException Error}");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(500).body("{IllegalArgumentException Error}");
		} */

		return ResponseEntity.ok("{}");
	}
	

}
