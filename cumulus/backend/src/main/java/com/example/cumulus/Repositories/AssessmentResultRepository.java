package com.example.cumulus.Repositories;

import com.example.cumulus.DTOs.AssessmentResult;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentResultRepository extends ReactiveMongoRepository<AssessmentResult, String> {
}

