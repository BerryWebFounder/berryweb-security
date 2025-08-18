package com.berryweb.security.controller;

import com.berryweb.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 실제 운영에서는 DB에서 가져와야 함 (미리 인코딩된 비밀번호)
    private static final String ENCODED_PASSWORD = "$2a$10$GRLdNijSQMUvl/au9ofL.eDDmxTlHQGe3TGV2l3RuV8SbLKOJ.1Kq"; // "password"

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        // 사용자 검증 (실제로는 UserDetailsService 사용)
        if (!"admin".equals(request.getUsername()) ||
                !passwordEncoder.matches(request.getPassword(), ENCODED_PASSWORD)) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String accessToken = jwtTokenProvider.createToken(
                request.getUsername(),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String refreshToken = jwtTokenProvider.createRefreshToken(request.getUsername());

        log.info("User '{}' logged in successfully", request.getUsername());

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "tokenType", "Bearer"
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody RefreshRequest request) {
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(request.getRefreshToken());
        String newAccessToken = jwtTokenProvider.createToken(
                username,
                Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        log.info("Token refreshed for user '{}'", username);

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "tokenType", "Bearer"
        ));
    }

    // DTO 클래스들
    public static class LoginRequest {
        private String username;
        private String password;

        // getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RefreshRequest {
        private String refreshToken;

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

}
