package dev.chuong.movies.core.steam;
import dev.chuong.movies.core.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "games")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    private Integer appid; // use the Steam appid as Primary Key
    private String name;
    private Integer playtime_2weeks;
    private Integer playtime_forever;
    private String img_icon_url;
    private String img_logo_url;
    @DocumentReference
    private List<Review> reviewIds; // Links specific reviews to this game

    // Helper methods
    public String getFullLogoUrl() {
        if (img_logo_url == null || img_logo_url.isEmpty()) return null;
        return String.format("https://media.steampowered.com/steamcommunity/public/images/apps/%d/%s.jpg",
                appid, img_logo_url);
    }

    public String getName() {
        return (name == null || name.isEmpty()) ? "Unknown Steam App (" + appid + ")" : name;
    }
}
