package com.example.cumulus.Services;

import com.example.cumulus.Configs.JWTUtil;
import com.example.cumulus.DTOs.RefreshToken;
import com.example.cumulus.Models.UserProfile;
import com.example.cumulus.Repositories.RefreshTokenRepository;
import com.example.cumulus.Repositories.UserProfileRepository;
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
    private UserProfileRepository userProfileRepository;

    @Autowired
    private JWTUtil jwtTokenUtil;

    public static final long REFRESH_TOKEN_DURATION = 7 * 24 * 60 * 60; // 7 days

    public Mono<String> createRefreshToken(String email) {
        return userProfileRepository.findByEmail(email)
                .flatMap(userProfile -> {
                    String refreshToken = jwtTokenUtil.generateRefreshToken(userProfile.getEmail());
                    Instant expiryDate = Instant.now().plusSeconds(REFRESH_TOKEN_DURATION);

                    RefreshToken refreshTokenEntity = new RefreshToken(refreshToken, expiryDate, userProfile);

                    System.out.println("Saving Refresh Token: " + refreshToken);

                    return refreshTokenRepository.save(refreshTokenEntity)
                            .map(savedToken -> {
                                System.out.println("Saved Refresh Token in DB: " + savedToken.getToken());
                                return savedToken.getToken();
                            });                });
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
