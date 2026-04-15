package dev.chuong.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@Service
public class GameLibraryService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserService userService;

    public List<Game> syncLibrary(String steamId) {
        // 1. Fetch Player Details (Name, Avatar)
        String playerUrl = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=9112DDCE1D934012805D50F495CE4F6F&steamids=" + steamId;
        SteamPlayerResponse playerResult = restTemplate.getForObject(playerUrl, SteamPlayerResponse.class);

        String steamName = "SteamUser"; // Default
        if (playerResult != null && !playerResult.getResponse().getPlayers().isEmpty()) {
            steamName = playerResult.getResponse().getPlayers().get(0).getPersonaname();
        }

        // 2. Fetch Owned Games from Players
        String gamesUrl = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=9112DDCE1D934012805D50F495CE4F6F&steamid=" + steamId + "&format=json&include_appinfo=true&include_played_free_games=true";

        // RestTemplate reads the JSON into the SteamLibraryResponse wrapper
        GameSteamLibraryResponse result = restTemplate.getForObject(gamesUrl, GameSteamLibraryResponse.class);

        if (result != null && result.getResponse() != null) {
            List<Game> games = result.getResponse().getGames();
            Integer gamesCount = result.getResponse().getGame_count();

            gameRepository.saveAll(games); // Save to MongoDB
            userService.createUserOrUpdateLibrary(steamId, steamName, gamesCount, games);

            return games;
        }
        return Collections.emptyList();
    }
}
