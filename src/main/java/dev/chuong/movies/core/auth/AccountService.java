package dev.chuong.movies.core.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/* BUSINESS LOGIC LAYER
 * Handles user registration and login.
 * Follows the same Service pattern as MovieService, ReviewService, etc.
 */
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // BCrypt password hasher — cost factor 12 is a good balance of security vs speed
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    /* ── REGISTER ────────────────────────────────────────────────────────────
     * 1. Check username/email if they are already taken
     * 2. Hash password with BCrypt
     * 3. Save the new user to MongoDB
     * 4. Return a JWT + user info so the frontend logs them in immediately
     */
    public AuthResponse register(RegisterRequest request) {

        // Validate uniqueness using MongoDB methods
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken.");
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }

        // Hash password before storing, never plain text the password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Create and persist the new user
        Account newUser = new Account(request.getUsername(), request.getEmail(), hashedPassword);
        accountRepository.save(newUser);

        // Generate a JWT for immediate login after registration
        String token = jwtUtil.generateToken(newUser.getUsername());

        return new AuthResponse(token, newUser);
    }

    /* ── LOGIN ───────────────────────────────────────────────────────────────
     * 1. Look up user by username
     * 2. Compare submitted password against the stored BCrypt hash
     * 3. Return a JWT + user info if valid
     */
    public AuthResponse login(LoginRequest request) {

        // Find user first, if failed -> throw a generic error
        Account user = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password."));

        // BCrypt compare: hashes the raw password and checks against stored hash of the user
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password.");
        }

        // return JWT token if success
        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponse(token, user);
    }

    /* ── Link Steam account ────────────────────────────────────────────────────
    * Called by SteamAuthController after a successful Steam OAuth callback.
    * Finds the Account by username and saves the Steam ID onto it.
    */
    public Account linkSteam(String username, String steamId) {

        // Guard: make sure the account exists
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Account not found: " + username));
        // Guard: prevent the same Steam account being linked to two different accounts
        if (accountRepository.existsBySteamId(steamId)) {
            throw new RuntimeException("This Steam account is already linked to another user.");
        }
        // Guard: don't overwrite an existing link silently
        if (account.getSteamId() != null && !account.getSteamId().isEmpty()) {
            throw new RuntimeException("This account already has a Steam account linked.");
        }

        // Finally set the steamId for the account
        account.setSteamId(steamId);

        return accountRepository.save(account);
    }

    /* ── Unlink Steam account ──────────────────────────────────────────────────
    * Disconnect Steam from their account.
    */
    public Account unlinkSteam(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Account not found: " + username));
        account.setSteamId(null);
        return accountRepository.save(account);
    }
}
