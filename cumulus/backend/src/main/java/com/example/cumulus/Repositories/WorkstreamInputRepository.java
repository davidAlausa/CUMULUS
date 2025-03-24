package com.example.cumulus.Repositories;

import com.example.cumulus.DTOs.WorkStreamInput;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WorkstreamInputRepository extends ReactiveMongoRepository<WorkStreamInput, String> {
    Mono<WorkStreamInput> findWorkStreamInputByWorkstreamID(String workstreamID);
}
