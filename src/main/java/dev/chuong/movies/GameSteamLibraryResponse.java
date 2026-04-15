package dev.chuong.movies;

import lombok.Data;
import java.util.List;

@Data
public class GameSteamLibraryResponse {
    private ResponseData response; // Matches the outer "response" key

    @Data
    public static class ResponseData {
        private int game_count;
        private List<Game> games; // maps to Game.java
    }
}
