package dev.chuong.movies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String steamId64; // Use the 17-digit Steam ID as the Primary Key
    private String personaName; // The user's display name
    private Integer gameCount;

    @DocumentReference
    private List<Game> games; // One-to-Many: A user has many games
    @DocumentReference
    private List<Movie> watchedMovies;
}
