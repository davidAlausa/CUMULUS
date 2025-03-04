package com.example.cumulus.Controllers;

import com.example.cumulus.DTOs.UserDetails;
import com.example.cumulus.Services.UserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/")
public class RESTControllerSIGNIN {

   private final UserDetailsService userDetailsService;
    public RESTControllerSIGNIN(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @PostMapping("/signin")
    public Mono<ResponseEntity<Map<String, String>>> handleSignIn(@RequestBody UserDetails request) {
        return userDetailsService.findByEmail(request.getEmail())
                .flatMap(userDetails -> {
                    if (userDetails.getPassword().equals(request.getPassword())) {
                        return Mono.just(ResponseEntity.ok(Map.of("message", "Sign-in successful!")));
                    } else {
                        return Mono.just(ResponseEntity.badRequest().body(Map.of("error", "Sign-in failed: Incorrect password")));
                    }
                })
                .defaultIfEmpty(ResponseEntity.badRequest().body(Map.of("error", "Sign-in failed: User not found")));

    }
}
