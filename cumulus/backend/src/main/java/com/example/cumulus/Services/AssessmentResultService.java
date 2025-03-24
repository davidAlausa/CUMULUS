package com.example.cumulus.Services;

import com.example.cumulus.DTOs.AssessmentResult;
import com.example.cumulus.Repositories.AssessmentResultRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentResultService {

    private final AssessmentResultRepository repository;

    public AssessmentResultService(AssessmentResultRepository repository) {
        this.repository = repository;
    }

    public Mono<String> process(AssessmentResult workstream, String userId) {
        if (workstream == null) {
            return Mono.empty();
        }

        workstream.setUserID(userId);
        workstream.setDateAssessed(Instant.now());

        System.out.println("Saving Workstream: " + workstream);

        return repository.save(workstream)
                .doOnSuccess(saved -> System.out.println("Saved Successfully: " + saved))
                .doOnError(error -> System.err.println("Error Saving: " + error.getMessage()))
                .map(AssessmentResult::getId)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to process workstream.")));
    }

    public Mono<AssessmentResult> getWorkstreamByID(String workstreamID) {
        return repository.findById(workstreamID);
    }
}

