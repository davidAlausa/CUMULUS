package com.example.cumulus.Repositories;

import com.example.cumulus.Entities.Resource;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ResourceRepository extends ReactiveMongoRepository<Resource, String> {
    Flux<Resource> findByProviderId(String providerId);
    Flux<Resource> findAllById(Iterable<String> ids);

}
