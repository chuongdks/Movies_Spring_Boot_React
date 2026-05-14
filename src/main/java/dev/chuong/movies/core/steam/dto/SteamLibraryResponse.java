package dev.chuong.movies.core.steam.dto;

import dev.chuong.movies.core.steam.Game;
import lombok.Data;
import java.util.List;

@Data
public class SteamLibraryResponse {
    private ResponseData response; // Matches the outer "response" key

    @Data
    public static class ResponseData {
        private int game_count;
        private List<Game> games; // maps to Game.java
    }
}
