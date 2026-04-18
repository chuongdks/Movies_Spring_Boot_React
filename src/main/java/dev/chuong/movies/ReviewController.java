package dev.chuong.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/* REVIEW API CONTROLLER LAYER
 * Get Requests from users and return a Response
 * Only a single POST method
 * */
@RestController
@RequestMapping("/api/v1/reviews")
// @CrossOrigin(origins = "*") // Allows all origins during development
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Map<String, String> payload) {
        // receive JSON data from user --> convert to map where 2 keys are String, String
        return new ResponseEntity<Review>(reviewService.createReview(payload.get("reviewBody"), payload.get("imdbId")), HttpStatus.CREATED);  // send 201 instead of 200
    }
}
