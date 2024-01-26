package vttp2023.batch4.paf.assessment.bedandbreakfastapp;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import vttp2023.batch4.paf.assessment.bedandbreakfastapp.models.Bookings;
import vttp2023.batch4.paf.assessment.bedandbreakfastapp.services.ListingsService;

@SpringBootApplication
public class BedandbreakfastappApplication implements CommandLineRunner{

	@Autowired
	MongoTemplate template;

	@Autowired
    private JdbcTemplate Jtemplate;

	@Autowired
	ListingsService listingsSvc;
	
	public static void main(String[] args) {
		SpringApplication.run(BedandbreakfastappApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception{

		/* MatchOperation matchOperation = Aggregation.match(Criteria.where("address.country").regex("xxx","i").and("address.suburb").ne(null).ne(""));

		ProjectionOperation projectionOperation = Aggregation.project().and("address.suburb").as("_id");

		Aggregation pipeline = Aggregation.newAggregation(matchOperation,
		projectionOperation);

		AggregationResults<Document> results2 = template.aggregate(pipeline, "listings",
		Document.class);

		List<Document> docs2 = results2.getMappedResults();
		System.out.println("!!!" + docs2.toString()); */

		/* db.listings_and_reviews.aggregate([
			{ $match: {
				"$and":[{ "address.suburb": "Fairlight"},
						{ "price":{ $lte: 100}},
						{ "accommodates":{$gte: 2}},
						{ "min_nights":{$lte:5}}
						]}
			},
			{ $project: {
				_id:1,
				name:1,
				accommodates:1,
				price:1}
			},
			{$sort: {
				price:-1
			}}
		]); */

		/* MatchOperation matchOperation = Aggregation.match(Criteria.where("address.suburb").regex("Fairlight","i").and("price").lte(100).and("accommodates").gte(2).and("min_nights").lte(5)); 

		ProjectionOperation projectionOperation = Aggregation.project("_id","name","accommodates","price");

		SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.DESC, "price"));

		Aggregation pipeline = Aggregation.newAggregation(matchOperation,
		projectionOperation,sortOperation);

		AggregationResults<Document> results = template.aggregate(pipeline, "listings",
		Document.class);

		List<Document> docs = results.getMappedResults();
		System.out.println("!!!++" + docs.toString());  */

		/* SqlRowSet rs = Jtemplate.queryForRowSet(queries.SQL_Find_User, "barney@gmail.com","Barney Rubble");
		
		int resultInt = 0;

        while (rs.next()){
            resultInt = rs.getInt("count(*)");
        } 
		System.out.println("llllllll///"+resultInt); */

		/* Bookings bookings = new Bookings();
		bookings.setName("yy");
		bookings.setEmail("yy@");
		bookings.setDuration(4);
		bookings.setListingId("21212121");

		listingsSvc.createBooking(bookings); */

	}
}
