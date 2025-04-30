package com.example.cumulus.Repositories;

import com.example.cumulus.Entities.Attribute;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AttributeRepository extends ReactiveMongoRepository<Attribute, String> {
}
