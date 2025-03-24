package com.example.cumulus.Controllers;

import com.example.cumulus.Authentications.authenticationRequest;
import com.example.cumulus.Authentications.authenticationResponse;
import com.example.cumulus.Configs.JWTUtil;
import com.example.cumulus.Repositories.RefreshTokenRepository;
import com.example.cumulus.Services.RefreshTokenService;
import com.example.cumulus.Services.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class RESTControllerAUTHENTICATION {

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtTokenUtil;

    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<authenticationResponse>> createAuthenticationToken(@RequestBody authenticationRequest authenticationRequest) {
        return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
                )
                .flatMap(auth -> Mono.defer(() -> userDetailsService.findByUsername(authenticationRequest.getEmail())))
                .flatMap(userDetails -> {
                    tokenBlacklistService.blacklistPreviousTokens(userDetails.getUsername());
                    String accessToken = jwtTokenUtil.generateToken(userDetails.getUsername());
                    tokenBlacklistService.storeToken(userDetails.getUsername(), accessToken);

                    return refreshTokenService.createRefreshToken(userDetails.getUsername())
                            .map(refreshToken -> ResponseEntity.ok(new authenticationResponse(accessToken, refreshToken)));
                })
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new authenticationResponse(null, null)));
                });
    }





    @PostMapping("/refresh-token")
    public Mono<ResponseEntity<?>> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null) {
            return Mono.just(ResponseEntity.status(401).body("Invalid or expired refresh token"));
        }
        refreshTokenRepository.findAll().collectList()
                .subscribe(tokens -> System.out.println("Stored Tokens: " + tokens));

        return refreshTokenService.isValidRefreshToken(refreshToken)
                .flatMap(isValid -> {
                    if (!isValid) {
                        System.out.println("Invalid or expired refresh token");
                        return Mono.just(ResponseEntity.status(401).body("Invalid or expired refresh token"));
                    }
                    System.out.println("Refresh Token is valid");
                    return refreshTokenService.getUsernameFromToken(refreshToken)
                            .map(jwtTokenUtil::generateToken)
                            .map(newAccessToken -> ResponseEntity.ok(Map.of("accessToken", newAccessToken)));
                });
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<?>> logout(@RequestHeader("Authorization") String accessToken, @RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (accessToken == null) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header is required for logout"));
        }

        Mono<Void> revokeTokenMono = (refreshToken != null) ? refreshTokenService.revokeRefreshToken(refreshToken) : Mono.empty();
        Mono<Void> blacklistTokenMono = (accessToken.startsWith("Bearer "))
                ? tokenBlacklistService.blacklistToken(accessToken.substring(7))
                : Mono.empty();

        return Mono.when(revokeTokenMono, blacklistTokenMono)
                .thenReturn(ResponseEntity.ok("Logged out successfully"));
    }


}
