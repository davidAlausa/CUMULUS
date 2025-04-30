package com.example.cumulus.Repositories;

import com.example.cumulus.Entities.ResourceAttribute;
import io.netty.util.AsyncMapping;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ResourceAttributeRepository extends ReactiveMongoRepository<ResourceAttribute, String> {

    Mono<ResourceAttribute> findByResourceId(String id);
}
