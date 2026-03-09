package dev.chuong.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/* BUSINESS LOGIC LAYER
 * Get all movies using db from MovieRepository
 * */
@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    // use template to talk to db (other method is Repository)
    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId) {
        // Review review = new Review(reviewBody); reviewRepository.insert(review);
        Review review = reviewRepository.insert(new Review(reviewBody));

        // query
        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();

        return review;
    }
}
