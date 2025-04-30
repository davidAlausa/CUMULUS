package com.example.cumulus.Services;

import com.example.cumulus.DTOs.WorkStreamInput;
import com.example.cumulus.Repositories.WorkstreamInputRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WorkstreamInputService {

    private final WorkstreamInputRepository workstreamInputRepository;
    public WorkstreamInputService(WorkstreamInputRepository workstreamInputRepository) {
        this.workstreamInputRepository = workstreamInputRepository;
    }

    public Mono<Boolean> saveWorkstreamInput(WorkStreamInput workStreamInput) {
        if (workStreamInput == null) {
            return Mono.just(false);
        }

        System.out.println("Saving Workstream Input: " + workStreamInput);

        workStreamInput.setWorkstreamID(workStreamInput.getWorkstreamID());

        return workstreamInputRepository.insert(workStreamInput)
                .doOnSuccess(saved -> System.out.println("Saved Successfully: " + saved))
                .doOnError(error -> System.err.println("Error Saving: " + error.getMessage()))
                .map(saved -> true)
                .defaultIfEmpty(false);
    }

    public Mono<WorkStreamInput> getWorkstreamInputByWorkstreamID(String workstreamID) {
        return workstreamInputRepository.findWorkStreamInputByWorkstreamID(workstreamID);
    }

}
