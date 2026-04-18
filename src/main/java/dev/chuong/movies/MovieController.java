package dev.chuong.movies;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/* MOVIE API CONTROLLER LAYER
* Get Requests from users and return a Response
* */
@RestController
@RequestMapping("/api/v1/movies")   // requests to this directory will be handled by this script
// @CrossOrigin(origins = "*") // Allows all origins during development
public class MovieController {
    @Autowired  // use this annotation to init the class
    private MovieService movieService;  // use MovieService to get all the movies

    /* Get Methods */
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<List<Movie>>(movieService.allMovies(), HttpStatus.OK);
    }

//    @GetMapping("/{id}")    // /api/v1/movies/$id
//    public ResponseEntity<Optional<Movie>> getSingleMovie(@PathVariable ObjectId id) {
//        return new ResponseEntity<Optional<Movie>>(movieService.singleMovie(id), HttpStatus.OK);
//    }

    @GetMapping("/{imdbId}")    // /api/v1/movies/$id
    public ResponseEntity<Optional<Movie>> getSingleMovie(@PathVariable String imdbId) {
        return new ResponseEntity<Optional<Movie>>(movieService.singleMovieImdbId(imdbId), HttpStatus.OK);
    }
}
