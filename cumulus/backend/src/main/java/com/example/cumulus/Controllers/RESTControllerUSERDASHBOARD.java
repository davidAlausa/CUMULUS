package com.example.cumulus.Controllers;

import com.example.cumulus.Configs.JWTUtil;
import com.example.cumulus.Services.UserDetailsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/loggedin/api/")
public class RESTControllerUSERDASHBOARD {
    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtTokenUtil;
    public RESTControllerUSERDASHBOARD(UserDetailsService userDetailsService, JWTUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @GetMapping("/user")
    public Mono<ResponseEntity<String>> getName(ServerHttpRequest request) {

        return Mono.justOrEmpty(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION)) // Extract token
                .filter(authHeader -> authHeader.startsWith("Bearer ")) // Ensure it's a Bearer token
                .map(authHeader -> authHeader.substring(7)) // Remove "Bearer "
                .flatMap(token -> Mono.justOrEmpty(jwtTokenUtil.extractUsername(token))) // Wrap in Mono
                .flatMap(userDetailsService::findByUsername) // Find user by username
                .cast(com.example.cumulus.DTOs.UserDetails.class) // Ensure correct DTO
                .map(user -> ResponseEntity.ok(user.getFirstName())) // Return first name
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); // Return 401 if user not found
    }



}
