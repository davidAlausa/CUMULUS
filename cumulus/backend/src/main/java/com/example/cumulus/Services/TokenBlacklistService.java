package com.example.cumulus.Services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklistedTokens = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, List<String>> userTokens = new HashMap<>();

    public Mono<Void> blacklistToken(String token) {
        return Mono.fromRunnable(() -> blacklistedTokens.add(token));
    }

    public void blacklistPreviousTokens(String username) {
        List<String> tokens = userTokens.get(username);
        if (tokens != null) {
            blacklistedTokens.addAll(tokens);
            tokens.clear();
        }
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    public void storeToken(String username, String token) {
        userTokens.computeIfAbsent(username, k -> new ArrayList<>()).add(token);
    }
}