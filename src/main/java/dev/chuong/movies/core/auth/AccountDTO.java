package dev.chuong.movies.core.auth;
import lombok.Data;

/* ── REQUEST DTOs ─────────────────────────────────────────────────────────────
 * These map directly to the JSON body the React frontend sends.
 * LoginRequest    → POST /api/v1/auth/login
 * RegisterRequest → POST /api/v1/auth/register
 * ─────────────────────────────────────────────────────────────────────────── */
// Sent by AuthModal.jsx → handleLogin()
// Body: { "username": "alice", "password": "secret123" }
@Data
class LoginRequest {
    private String username;
    private String password;
}

// Sent by AuthModal.jsx → handleRegister()
// Body: { "username": "alice", "email": "a@b.com", "password": "secret123" }
@Data
class RegisterRequest {
    private String username;
    private String email;
    private String password;
}

/* ── RESPONSE DTO ─────────────────────────────────────────────────────────────
 * Returned to the frontend after a successful login or register.
 * AuthContext.jsx expects: { token, user: { username, email, role } }
 * ─────────────────────────────────────────────────────────────────────────── */
@Data
class AuthResponse {
    private String token;
    private UserInfo user;

    // Nested object, maps to user: { username, email, role } in the frontend
    @Data
    static class UserInfo {
        private String username;
        private String email;
        private String role;

        public UserInfo(String username, String email, String role) {
            this.username = username;
            this.email    = email;
            this.role     = role;
        }
    }

    public AuthResponse(String token, Account appUser) {
        this.token = token;
        this.user  = new UserInfo(appUser.getUsername(), appUser.getEmail(), appUser.getRole());
    }
}
