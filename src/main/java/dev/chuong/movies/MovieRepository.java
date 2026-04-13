package dev.chuong.movies;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* DATABASE LAYER
 * Inherit MongoRepository class to find movies in db
 * */
@Repository
public interface MovieRepository extends MongoRepository<Movie, ObjectId> {
    // automatic query for imdbId or any properties name in ur model class
    Optional<Movie> findMovieByImdbId(String imdbId);   // Query Derivation.
}
