package vttp2023.batch4.paf.assessment.bedandbreakfastapp.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.bedandbreakfastapp.Utils;
import vttp2023.batch4.paf.assessment.bedandbreakfastapp.models.Accommodation;
import vttp2023.batch4.paf.assessment.bedandbreakfastapp.models.AccommodationSummary;


@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 * db.listings_and_reviews.aggregate([
			{ $match: {
				"$and":[{ "address.country":{
								$regex:"australia",
								$options:"i"
								}        
						},
						{ "address.suburb":{ $ne : null, $ne:"" }       
						}]}
						
			},
			{ $project: {
				"_id":"$address.suburb"}
			}
		]);
	 */

	// ANSWER ---------------------------------------------------------------------------------------------
	// db.listings.aggregate([
	// 	{$group:{_id:"$address.suburb"}}
	// ])
	//TO PREVENT DUPLICATES
	// ANSWER END -----------------------------------------------------------------------------------------
	
	public List<String> getSuburbs(String country){

		MatchOperation matchOperation = Aggregation.match(Criteria.where("address.country").regex(country,"i").and("address.suburb").ne(null).ne(""));

		ProjectionOperation projectionOperation = Aggregation.project().and("address.suburb").as("_id");

		Aggregation pipeline = Aggregation.newAggregation(matchOperation,
		projectionOperation);

		AggregationResults<Document> results = template.aggregate(pipeline, "listings",
		Document.class);

		List<Document> docs = results.getMappedResults();
		System.out.println("SUBURB! " + docs.toString());

		List<String> suburbs = new LinkedList<>();

		for (Document d:docs){
			suburbs.add(d.getString("_id"));
		}

        return suburbs;
    }

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 * db.listings_and_reviews.aggregate([
			{ $match: {
				"$and":[{ "address.suburb":{
										$regex:"FairligHt",
										$options:"i"
										}        
								},
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
		]);
	 *
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {
		MatchOperation matchOperation = Aggregation.match(Criteria.where("address.suburb").regex(suburb,"i").and("price").lte(priceRange).and("accommodates").gte(persons).and("min_nights").lte(duration)); 

		ProjectionOperation projectionOperation = Aggregation.project("_id","name","accommodates","price");

		SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.DESC, "price"));

		Aggregation pipeline = Aggregation.newAggregation(matchOperation,
		projectionOperation,sortOperation);

		AggregationResults<Document> results = template.aggregate(pipeline, "listings",
		Document.class);

		List<Document> docs = results.getMappedResults();
		List<AccommodationSummary> accSum = docs.stream().map(t->AccommodationSummary.JSONToObj(t))
		.toList();
		return accSum;
                
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
