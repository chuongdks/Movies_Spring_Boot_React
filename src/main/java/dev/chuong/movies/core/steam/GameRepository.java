package dev.chuong.movies.core.steam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/* DATABASE LAYER
 * Inherit MongoRepository class to find games in db
 * */
public interface GameRepository extends MongoRepository<Game, Integer> {
    // You can add custom queries here later, like findByName
    Page<Game> findAll(Pageable pageable);
}
