package com.example.cumulus.Repositories;
import com.example.cumulus.Models.UserProfile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserProfileRepository extends ReactiveMongoRepository<UserProfile, String> {
    Mono<UserProfile> findByEmail(String email);
    Mono<UserProfile> findIDByEmail(String email);
}
