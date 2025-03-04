package com.example.cumulus.Services;

import com.example.cumulus.DTOs.RefreshToken;
import com.example.cumulus.Repositories.RefreshTokenRepository;
import com.example.cumulus.Repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    private static final long REFRESH_TOKEN_DURATION = 7 * 24 * 60 * 60; // 7 days in seconds

    public Mono<String> createRefreshToken(String email) {
        return userDetailsRepository.findByEmail(email)
                .flatMap(user -> {
                    String token = UUID.randomUUID().toString();
                    Instant expiryDate = Instant.now().plusSeconds(REFRESH_TOKEN_DURATION);

                    RefreshToken refreshToken = new RefreshToken(token, expiryDate, user);
                    return refreshTokenRepository.save(refreshToken).map(RefreshToken::getToken);
                });
    }

    public Mono<Boolean> isValidRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(rt -> rt.getExpiryDate().isAfter(Instant.now()))
                .defaultIfEmpty(false);
    }

    public Mono<String> getUsernameFromToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(rt -> rt.getUser().getEmail())
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid refresh token")));
    }

    public Mono<Void> revokeRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .flatMap(refreshTokenRepository::delete);
    }
}
