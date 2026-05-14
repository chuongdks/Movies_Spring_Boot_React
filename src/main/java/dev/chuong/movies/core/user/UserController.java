package dev.chuong.movies.core.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/* Users API CONTROLLER LAYER
 * Get Requests from users and return a Response
 * */
@RestController
@RequestMapping("/api/v1/users")
// @CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{steamId}")
    public ResponseEntity<Optional<User>> getUserDetails(@PathVariable String steamId) {
        return new ResponseEntity<>(userService.getSingleUser(steamId), HttpStatus.OK);
    }
}
