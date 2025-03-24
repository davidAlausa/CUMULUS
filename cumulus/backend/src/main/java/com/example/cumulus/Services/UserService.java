package com.example.cumulus.Services;

import com.example.cumulus.Models.UserProfile;
import com.example.cumulus.Repositories.UserProfileRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class UserService implements ReactiveUserDetailsService {
    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public Mono<UserProfile> createUser(UserProfile userProfile) {
        userProfile.setPassword(bCryptPasswordEncoder.encode(userProfile.getPassword()));
        return userProfileRepository.insert(userProfile);
    }

    public Mono<UserProfile> findByEmail(String email) {
        return userProfileRepository.findByEmail(email);
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return userProfileRepository.findByEmail(email)
                .map(userProfile -> new User(
                        userProfile.getEmail(),
                        userProfile.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("USER"))
                ));
    }

    public Mono<UserProfile> findIDByEmail(String email){
        return userProfileRepository.findIDByEmail(email);
    }
}
