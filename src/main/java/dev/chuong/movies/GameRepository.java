package dev.chuong.movies;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

/* DATABASE LAYER
 * Inherit MongoRepository class to find games in db
 * */
public interface GameRepository extends MongoRepository<Game, Integer> {
    // You can add custom queries here later, like findByName
    Page<Game> findAll(Pageable pageable);
}
