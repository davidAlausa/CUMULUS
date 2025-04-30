package com.example.cumulus.Repositories;

import com.example.cumulus.Entities.Question;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface QuestionRepository extends ReactiveMongoRepository<Question, String> {
    Flux<Question> findAllByType(String type);
}
