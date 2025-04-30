package com.example.cumulus.Repositories;

import com.example.cumulus.Entities.AnswerText;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AnswerTextRepository extends ReactiveMongoRepository<AnswerText, String> {

}
