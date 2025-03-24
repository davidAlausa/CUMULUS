package com.example.cumulus.Controllers;

import com.example.cumulus.Configs.JWTUtil;
import com.example.cumulus.Services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/loggedin/")
public class RESTControllerUSERDASHBOARD {
    private final UserService userService;
    private final JWTUtil jwtTokenUtil;
    public RESTControllerUSERDASHBOARD(UserService userService, JWTUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @GetMapping("/user")
    public Mono<ResponseEntity<Map<String, String>>> getName(ServerHttpRequest request) {
        return Mono.justOrEmpty(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .flatMap(token -> {
                    try {
                        return Mono.justOrEmpty(jwtTokenUtil.extractUsername(token));
                    } catch (io.jsonwebtoken.ExpiredJwtException e) {
                        System.out.println("JWT is expired: " + e.getMessage());
                        return Mono.empty();
                    }
                })
                .flatMap(userService::findByUsername)
                .flatMap(user -> userService.findByEmail(user.getUsername()))
                .map(userProfile -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("firstName", userProfile.getFirstName());
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }





}
