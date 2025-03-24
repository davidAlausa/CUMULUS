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
public class RESTControllerSIGNUP {

    private final UserService userService;
    public RESTControllerSIGNUP(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/signup")
    public Mono<ResponseEntity<Map<String, String>>> handleSignUp(@RequestBody UserProfile request) {

        if (request.getPassword().equals(request.getRetypePassword())) {
            try {
                return userService.createUser(request)
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
