package com.example.cumulus.Configs;

import com.example.cumulus.Services.TokenBlacklistService;
import com.example.cumulus.Services.UserService;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

@Component
public class JWTRequestFilter implements WebFilter {

    @Lazy
    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    public JWTRequestFilter(JWTUtil jwtUtil, @Lazy UserService userService, TokenBlacklistService tokenBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (exchange.getRequest().getURI().getPath().equals("/logout")) {
            return chain.filter(exchange);
        }
        if (exchange.getRequest().getURI().getPath().equals("/refresh-token")) {
            return chain.filter(exchange);
        }

        if (exchange.getRequest().getURI().getPath().equals("/api/session/authenticate")) {
            return chain.filter(exchange);
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Missing or invalid Authorization header: " + authorizationHeader);
            return chain.filter(exchange);
        }

        String jwt = authorizationHeader.substring(7);
        System.out.println("Extracted Token: " + jwt);
        String email = jwtUtil.extractUsername(jwt);

        if (tokenBlacklistService.isBlacklisted(jwt)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return userService.findByUsername(email)
                .filter(userDetails -> jwtUtil.validateToken(jwt, userDetails.getUsername()))
                .flatMap(userDetails -> {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    SecurityContext securityContext = new SecurityContextImpl(authentication);
                    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}
