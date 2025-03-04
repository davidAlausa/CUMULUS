package com.example.cumulus.Repositories;
import com.example.cumulus.DTOs.UserDetails;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserDetailsRepository extends ReactiveMongoRepository<UserDetails, String> {
    Mono<UserDetails> findByEmail(String email);
}