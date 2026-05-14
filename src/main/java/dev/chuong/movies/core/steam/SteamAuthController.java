package dev.chuong.movies.core.steam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class SteamAuthController {
    private static final String STEAM_OPENID_URL = "https://steamcommunity.com/openid/login";

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        // Construct the Steam OpenID request URL
        String redirectUrl = STEAM_OPENID_URL +
                "?openid.ns=http://specs.openid.net/auth/2.0" +
                "&openid.mode=checkid_setup" +
                "&openid.return_to=http://localhost:8080/api/v1/auth/callback" + // Your backend callback (url to redirect to after login)
                "&openid.realm=http://localhost:8080" +
                "&openid.identity=http://specs.openid.net/auth/2.0/identifier_select" +
                "&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select";

        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. Extract the steamid from the returning 'openid.claimed_id'
        // Example: https://steamcommunity.com/openid/id/76561198085222061
        String claimedId = request.getParameter("openid.claimed_id");
        String steamId = claimedId.substring(claimedId.lastIndexOf("/") + 1);

        // 2. Logic: Save user to MongoDB or update existing record
        // userService.saveOrUpdateSteamUser(steamId);

        // 3. Redirect back to your React Frontend
        // We pass the steamId as a query param for now (in production, use a JWT or Session Cookie)
        response.sendRedirect("http://localhost:5173/dashboard?steamid=" + steamId);
    }
}
