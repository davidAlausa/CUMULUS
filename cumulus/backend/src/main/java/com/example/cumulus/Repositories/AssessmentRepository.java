package com.example.cumulus.Repositories;

import com.example.cumulus.Entities.Assessment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends ReactiveMongoRepository<Assessment, String> {
}
