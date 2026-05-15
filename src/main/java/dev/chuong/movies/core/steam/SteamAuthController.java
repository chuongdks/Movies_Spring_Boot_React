package dev.chuong.movies.core.steam;

import dev.chuong.movies.core.auth.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class SteamAuthController {
    @Autowired
    private AccountService accountService;
    // Replace these with env in production
    private static final String STEAM_OPENID_URL = "https://steamcommunity.com/openid/login";
    private static final String BACKEND_BASE = "http://localhost:8080";
    private static final String FRONTEND_BASE = "http://localhost:5173";

    /*  ── Helper Functions ──────────────────────────────────────────────────────── */
    // Builds the Steam OpenID redirect URL with a given return_to callback
    private String buildSteamOpenIdUrl(String returnTo) {
        return STEAM_OPENID_URL
                + "?openid.ns=http://specs.openid.net/auth/2.0"
                + "&openid.mode=checkid_setup"
                + "&openid.return_to=" + returnTo   // link that the URL will be redirect after logging in
                + "&openid.realm=" + BACKEND_BASE
                + "&openid.identity=http://specs.openid.net/auth/2.0/identifier_select"
                + "&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select";
    }

    // Extracts the 17-digit Steam ID from the OpenID claimed_id parameter (https://openid.net/specs/openid-authentication-2_0.html)
    // Example: https://steamcommunity.com/openid/id/76561198085222061
    private String extractSteamId(HttpServletRequest request) {
        String claimedId = request.getParameter("openid.claimed_id");
        if (claimedId == null || !claimedId.contains("/")) {
            throw new RuntimeException("Invalid Steam callback — no claimed_id found.");
        }
        return claimedId.substring(claimedId.lastIndexOf("/") + 1);
    }

    /* ── Fresh Steam login  ───────────────────────────────────────── */
    // Step 1:
    // GET /api/v1/auth/login
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        // Construct the Steam OpenID request URL
        String redirectUrl = buildSteamOpenIdUrl(BACKEND_BASE + "/api/v1/auth/callback");
        response.sendRedirect(redirectUrl);
    }

    // Step 2:
    // GET /api/v1/auth/callback
    @GetMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String steamId = extractSteamId(request);
        // Redirect to React Dashboard.jsx and let it handles the sync
        response.sendRedirect(FRONTEND_BASE + "/dashboard?steamid=" + steamId);
    }

    /* ── Account linking flow ────────────────────────────────────────────
     * Step 1: Frontend calls this to kick off the Steam OAuth for linking.
     * The logged-in username is embedded in the return_to URL so the link-callback knows which Account to update.
     * GET /api/v1/auth/steam/link?username=john
     */
    @GetMapping("/steam/link")
    public void initiateLink(@RequestParam String username, HttpServletResponse response) throws IOException {
        String returnTo = BACKEND_BASE + "/api/v1/auth/steam/link-callback?username=" + username;
        String redirectUrl = buildSteamOpenIdUrl(returnTo);
        response.sendRedirect(redirectUrl);
    }

    /* Step 2: Steam sends the user back here after they authenticate.
     * Extract the steamId, call AccountService to persist the link, then send user back to frontend with a result flag.
     * GET /api/v1/auth/steam/link-callback?username=alice&openid.claimed_id=...
     */
    @GetMapping("/steam/link-callback")
    public void linkCallback(@RequestParam String username, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String steamId = extractSteamId(request);

            // Persist: set Account.steamId in MongoDB
            accountService.linkSteam(username, steamId);

            // Also store steamId so SteamLibrary autoloads the same as normal login
            // (Frontend stores this in localStorage after reading the URL param)
            response.sendRedirect(FRONTEND_BASE + "/steam?linked=true&steamid=" + steamId);

        } catch (RuntimeException e) {
            // e.g. account not found, or steamId already linked to another account
            String reason = e.getMessage().replace(" ", "%20");
            response.sendRedirect(FRONTEND_BASE + "/steam?linked=false&reason=" + reason);
        }
    }
}