package com.example.cumulus.Repositories;

import com.example.cumulus.Entities.Question;
import com.example.cumulus.Entities.QuestionAttribute;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface QuestionAttributeRepository extends ReactiveMongoRepository<QuestionAttribute, String> {
    Flux<QuestionAttribute> findByQuestionId(String questionId);
    Flux<QuestionAttribute> findByAttributeIdIn(List<String> attributeIds);

}
