package com.example.cumulus.Controllers;

import com.example.cumulus.Configs.JWTUtil;
import com.example.cumulus.DTOs.*;
import com.example.cumulus.Services.AssessmentResultService;
import com.example.cumulus.Services.OpenAIAPIService;
import com.example.cumulus.Services.UserService;
import com.example.cumulus.Services.WorkstreamInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/api/")
public class RESTControllerPROCESSWORKSTREAM {

    @Autowired
    private AssessmentResultService assessmentResultService;

    @Autowired
    private WorkstreamInputService workStreamInput;

    @Autowired
    private OpenAIAPIService openAIAPIService;

    public RESTControllerPROCESSWORKSTREAM(AssessmentResultService assessmentResultService, WorkstreamInputService workStreamInput) {
        this.assessmentResultService = assessmentResultService;

        this.workStreamInput = workStreamInput;
    }

    @PostMapping("process-workstream")
    public Mono<ResponseEntity<?>> processWorkstream(@RequestBody AssessmentDTO dto) {
        return assessmentResultService.processAssessment(dto)
                .map(result -> {
                    if (result != null) {
                        System.out.println("Received Result: " + result);
                        return ResponseEntity.ok(result);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No suitable provider found.");
                    }
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body("No suitable provider found."));
    }

    @PostMapping("save-inputs")
    public Mono<ResponseEntity<?>> saveInputs(@RequestBody SaveInputDTO dto, @RequestHeader("Authorization") String authHeader) {
        return workStreamInput.saveWorkstreamInput(dto, authHeader)
                .map(result -> {
                    if (result != null) {
                        return ResponseEntity.ok(result);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No suitable provider found.");
                    }
                })
                .defaultIfEmpty
                        (ResponseEntity.status(HttpStatus.NOT_FOUND).body("No suitable provider found."));
    }

    @GetMapping("evolve-question-list")
    public Mono<ResponseEntity<String>> evolveQuestionList() {
        return openAIAPIService.evolveWithGPT()
                .map(success -> {
                    if (success) {
                        return ResponseEntity.ok("New question successfully evolved and saved.");
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Failed to evolve a new question.");
                    }
                })
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("An error occurred: " + e.getMessage())
                ));
    }
}
