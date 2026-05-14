package dev.chuong.movies.core.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/* DATABASE LAYER
 * Inherit MongoRepository class to find movies in db
 * */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findBySteamId64(String steamId64);
}
