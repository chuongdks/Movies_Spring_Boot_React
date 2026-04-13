package dev.chuong.movies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "movies")    // mark the class as MongoDB document, point to the "movies" table
@Data                               // create for every field: Getters, Setters, toString(),equals(),...
@AllArgsConstructor                 // create constructors with no args (Movie())
@NoArgsConstructor                  // create constructor that accepts a value for every single field in the class
public class Movie {
    @Id                             // unique identifier for the doc of the DB
    private ObjectId id;
    private String imdbId;
    private String title;
    private String releaseDate;
    private String trailerLink;
    private String poster;
    private List<String> genres;
    private List<String> backdrops;
    @DocumentReference              // Relationship handling annotation
    private List<Review> reviewIds;     // 1 movie can have many reviews, Foreign Key
}
