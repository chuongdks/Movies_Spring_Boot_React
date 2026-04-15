package dev.chuong.movies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    private ObjectId id;
    private String body;
    private String entityId; // Movie imdbId or a Steam appid

    // since id are auto generate --> cant pass id to the class --> create constructor takes only the body
    public Review(String body, String entityId) {
        this.body = body;
        this.entityId = entityId;
    }
}

