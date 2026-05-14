package dev.chuong.movies.core.steam;

import dev.chuong.movies.core.steam.dto.SteamLibraryResponse;
import dev.chuong.movies.core.steam.dto.SteamPlayerResponse;
import dev.chuong.movies.core.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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

    @Value("${steam.api.key}")
    private String steamApiKey; // Spring injects the value here

    public List<Game> syncLibrary(String steamId) {
        try {
            // 1. Fetch Player Details (Name, Avatar)
            String playerUrl = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + steamApiKey + "&steamids=" + steamId;
            SteamPlayerResponse playerResult = restTemplate.getForObject(playerUrl, SteamPlayerResponse.class);

            String steamName = "SteamUser"; // Default
            if (playerResult != null && !playerResult.getResponse().getPlayers().isEmpty()) {
                steamName = playerResult.getResponse().getPlayers().get(0).getPersonaname();
            }

            // 2. Fetch Owned Games from Players
            String gamesUrl = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" + steamApiKey + "&steamid=" + steamId + "&format=json&include_appinfo=true&include_played_free_games=true";
            SteamLibraryResponse result = restTemplate.getForObject(gamesUrl, SteamLibraryResponse.class);

            if (result != null && result.getResponse() != null && result.getResponse().getGames() != null) {
                List<Game> games = result.getResponse().getGames();
                Integer gamesCount = result.getResponse().getGame_count();

                gameRepository.saveAll(games); // Save to MongoDB
                userService.createUserOrUpdateLibrary(steamId, steamName, gamesCount, games);

                return games;
            } else {
                // Handle private profile: Log the error and return the existing user without wiping their data
                System.out.println("Sync failed for Steam ID: "+ steamId + ". Profile is private or has no games.");
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException e) {
            // Catch 4xx errors (e.g., 401 Unauthorized, 404 Not Found, 429 Too Many Requests)
            System.err.println("Client Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (HttpServerErrorException e) {
            // Catch 5xx errors (Steam's servers are down)
            System.err.println("Steam Server Error: " + e.getStatusCode());
            return Collections.emptyList();
        } catch (Exception e) {
            // Catch anything else (Network timeouts, etc.)
            System.err.println("Unexpected Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Page<Game> getTopGamesByPlaytime(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 30, Sort.by(Sort.Direction.DESC, "playtime_forever"));
        return gameRepository.findAll(pageable);
    }
}
