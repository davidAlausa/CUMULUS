package com.example.cumulus.Repositories;

import com.example.cumulus.Entities.Provider;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProviderRepository extends ReactiveMongoRepository<Provider, String> {
    Mono<String> findDescriptionById(String id);
}
