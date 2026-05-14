package dev.chuong.movies.core.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/* BUSINESS LOGIC LAYER
 * Get all movies using db from MovieRepository
 * */
@Service
public class MovieService {
    @Autowired  // auto init the class, movieRepository = new MovieRepository()
    private MovieRepository movieRepository;

    public List<Movie> allMovies() {
        return movieRepository.findAll();
    }

//    public Optional<Movie> singleMovie(ObjectId id) {
//        return movieRepository.findById(id);
//    }

    public Optional<Movie> singleMovieImdbId(String imdbId) {
        return movieRepository.findMovieByImdbId(imdbId);
    }

}
