package com.example.cumulus.Services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklistedTokens = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, List<String>> userTokens = new HashMap<>();

    //add token to the blacklist
    public Mono<Void> blacklistToken(String token) {
        return Mono.fromRunnable(() -> blacklistedTokens.add(token));
    }

    //blacklist all previous tokens for a user
    public void blacklistPreviousTokens(String username) {
        List<String> tokens = userTokens.get(username);
        if (tokens != null) {
            blacklistedTokens.addAll(tokens);
            tokens.clear(); //clear old tokens after blacklisting
        }
    }

    //check if a token is blacklisted
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    //store newly issued token for user tracking
    public void storeToken(String username, String token) {
        userTokens.computeIfAbsent(username, k -> new ArrayList<>()).add(token);
    }
}