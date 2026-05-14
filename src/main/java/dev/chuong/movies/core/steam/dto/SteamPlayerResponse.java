package dev.chuong.movies.core.steam.dto;

import lombok.Data;

import java.util.List;

/* DTO for the Steam Player API
 * Steam API
 * */
@Data
public class SteamPlayerResponse {
    private ResponseData response;

    @Data
    public static class ResponseData {
        private List<Player> players;
    }

    @Data
    public static class Player {
        private String steamid;
        private String personaname;
        private String profileurl;
        private String avatarfull; // This is the high-res 184x184 avatar
        private Integer personastate; // 0: Offline, 1: Online, etc.
    }
}
