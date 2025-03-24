package com.example.cumulus.Controllers;

import com.example.cumulus.Models.UserProfile;
import com.example.cumulus.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/")
public class RESTControllerSIGNIN {

   private final UserService userService;
    public RESTControllerSIGNIN(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/signin")
    public Mono<ResponseEntity<Map<String, String>>> handleSignIn(@RequestBody UserProfile request) {
        return userService.findByEmail(request.getEmail())
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
