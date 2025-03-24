package com.example.cumulus.Services;

import com.example.cumulus.Models.UserProfile;
import com.example.cumulus.Repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class PasswordHashingService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Mono<Void> hashExistingPasswords() {
        return userProfileRepository.findAll()
                .collectList()
                .flatMap(users -> {
                    for (UserProfile user : users) {
                        String plainTextPassword = user.getPassword();
                        if (!plainTextPassword.startsWith("$2a$")) {
                            String hashedPassword = passwordEncoder.encode(plainTextPassword);
                            user.setPassword(hashedPassword);
                        }
                    }
                    return userProfileRepository.saveAll(users).then();
                });
    }
}