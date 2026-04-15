package dev.chuong.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/* BUSINESS LOGIC LAYER
 * Get all movies using db from MovieRepository
 * */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUserOrUpdateLibrary(String steamId, String steamName, Integer gamesCount,List<Game> games) {
        Optional<User> existingUser = userRepository.findBySteamId64(steamId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setGames(games); // Update their library with fresh sync data
            user.setGameCount(gamesCount);
            return userRepository.save(user);
        } else {
            User newUser = new User();
            newUser.setSteamId64(steamId);
            newUser.setPersonaName(steamName);
            newUser.setGameCount(gamesCount);
            newUser.setGames(games);
            return userRepository.save(newUser);
        }
    }

    public Optional<User> getSingleUser(String steamId) {
        return userRepository.findBySteamId64(steamId);
    }
}
