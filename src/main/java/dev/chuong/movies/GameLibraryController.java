package dev.chuong.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libraries")
@CrossOrigin(origins = "*") // Allows all origins during development
public class GameLibraryController {
    @Autowired
    private GameLibraryService libraryService;

    @PostMapping("/sync/{steamId}")
    public ResponseEntity<List<Game>> getAllGamesFromUserId(@PathVariable String steamId) {
        return new ResponseEntity<List<Game>>(libraryService.syncLibrary(steamId), HttpStatus.OK);
    }

    @GetMapping("/top30")
    public ResponseEntity<List<Game>> getTopGames() {
        return new ResponseEntity<>(libraryService.getTopGamesByPlaytime(), HttpStatus.OK);
    }
}
