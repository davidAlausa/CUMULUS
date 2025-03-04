package com.example.cumulus.Services;

import com.example.cumulus.DTOs.UserDetails;
import com.example.cumulus.Repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class PasswordHashingService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Mono<Void> hashExistingPasswords() {
        return userDetailsRepository.findAll()
                .collectList() // Converts Flux<UserDetails> to Mono<List<UserDetails>>
                .flatMap(users -> {
                    for (UserDetails user : users) {
                        String plainTextPassword = user.getPassword();
                        if (!plainTextPassword.startsWith("$2a$")) {
                            String hashedPassword = passwordEncoder.encode(plainTextPassword);
                            user.setPassword(hashedPassword);
                        }
                    }
                    // Save all updated users reactively
                    return userDetailsRepository.saveAll(users).then();
                });
    }
}