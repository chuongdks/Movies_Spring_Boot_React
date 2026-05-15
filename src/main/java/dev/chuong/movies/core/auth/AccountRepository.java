package dev.chuong.movies.core.auth;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* DATABASE LAYER
 * Inherits MongoRepository to query the "app_users" collection
 */
@Repository
public interface AccountRepository extends MongoRepository<Account, ObjectId>{
    // Look up a user by their username during Login
    Optional<Account> findByUsername(String username);

    // check if email/username is taken during registration
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    // Used by linkSteam(), prevent one Steam account being linked to two users
    boolean existsBySteamId(String steamId);
}
