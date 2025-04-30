package com.example.cumulus.Repositories;

import com.example.cumulus.Entities.ProviderAttribute;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ProviderAttributeRepository extends ReactiveMongoRepository<ProviderAttribute, String> {
    Flux<ProviderAttribute> findByAttributeIdIn(List<String> attributeIds);
}
