package com.example.cumulus.Services;
import com.example.cumulus.DTOs.UserDetails;
import com.example.cumulus.Repositories.UserDetailsRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class UserDetailsService implements ReactiveUserDetailsService {
    private final UserDetailsRepository userDetailsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserDetailsService (UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }
    public Mono<UserDetails> createUser(UserDetails ud){
        ud.setPassword(bCryptPasswordEncoder.encode(ud.getPassword()));
        return userDetailsRepository.insert(ud);
    }
    public Mono<com.example.cumulus.DTOs.UserDetails> findByEmail(String email) {
        return userDetailsRepository.findByEmail(email);
    }
    public Mono<org.springframework.security.core.userdetails.UserDetails> findByUsername(String email) {
        return userDetailsRepository.findByEmail(email)
                .map(user -> User.withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities("USER")
                        .build());

    }

}
