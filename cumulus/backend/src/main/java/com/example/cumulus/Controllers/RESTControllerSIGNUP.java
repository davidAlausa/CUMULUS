package com.example.cumulus.Controllers;

import com.example.cumulus.DTOs.UserDetails;
import com.example.cumulus.Services.UserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/")
public class RESTControllerSIGNUP {

    private final UserDetailsService userDetailsService;
    public RESTControllerSIGNUP(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @PostMapping("/signup")
    public Mono<ResponseEntity<Map<String, String>>> handleSignUp(@RequestBody UserDetails request) {

        if (request.getPassword().equals(request.getRetypePassword())) {
            try {
                return userDetailsService.createUser(request)
                        .thenReturn(ResponseEntity.ok(Map.of("message", "Sign-up successful!")));
            }
            catch (Exception e) {
                return Mono.just(ResponseEntity.badRequest().body(Map.of("error", "Sign-up failed: " + e.getMessage())));
            }
        } else {
            return Mono.just(ResponseEntity.badRequest().body(Map.of("error", "Passwords do not match")));
        }
    }
}
