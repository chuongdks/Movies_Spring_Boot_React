package dev.chuong.movies.core.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/* CONTROLLER LAYER
 * Handles normal (username/password) authentication.
 *
 * Note: SteamAuthController.java already owns GET /api/v1/auth/login
 * for the Steam OpenID redirect. Spring differentiates by HTTP method,
 * so POST /api/v1/auth/login here has NO conflict with that GET.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AccountController {
    @Autowired
    private AccountService authService;

    /* POST /api/v1/auth/register
     * Body: { "username": "john", "email": "a@b.com", "password": "secret123" }
     * Returns: { "token": "lmao...", "user": { "username", "email", "role" } }
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED); // 201
        } catch (RuntimeException e) {
            // Username or email already taken or something else in the Service layer
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.CONFLICT); // 409
        }
    }

    /* POST /api/v1/auth/login
     * Body: { "username": "alice", "password": "secret123" }
     * Returns: { "token": "eyJ...", "user": { "username", "email", "role" } }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return new ResponseEntity<>(response, HttpStatus.OK); // 200
        } catch (RuntimeException e) {
            // Bad credentials
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.UNAUTHORIZED); // 401
        }
    }

    /* DELETE /api/v1/auth/steam/link?username=alice
     * Param: String username
     * Returns: { "message": "Steam account unlinked." }
     * Lets the frontend unlink a Steam account from a normal account
     */
    @DeleteMapping("/steam/link")
    public ResponseEntity<?> unlinkSteam(@RequestParam String username) {
        try {
            authService.unlinkSteam(username);
            return new ResponseEntity<>(Map.of("message", "Steam account unlinked."), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
