package dev.chuong.movies;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/* DATABASE LAYER
 * Inherit MongoRepository class to find games in db
 * */
public interface GameRepository extends MongoRepository<Game, Integer> {
    // You can add custom queries here later, like findByName
}
