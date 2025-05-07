package com.example.cumulus.Controllers;

import com.example.cumulus.DTOs.AssessmentDTO;
import com.example.cumulus.DTOs.ChatGPTRequest;
import com.example.cumulus.DTOs.ChatGPTResponse;
import com.example.cumulus.DTOs.Message;
import com.example.cumulus.Services.AssessmentService;
import com.example.cumulus.Services.OpenAIAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class RESTControllerAssessWorkstream {

    private final OpenAIAPIService openAIAPIService;
    private final AssessmentService assessmentService;

    public RESTControllerAssessWorkstream(OpenAIAPIService openAIAPIService, AssessmentService assessmentService) {
        this.openAIAPIService = openAIAPIService;
        this.assessmentService = assessmentService;
    }

    @GetMapping("/getquestions")
    public Mono<ResponseEntity<?>> getQuestions() {
        return assessmentService.fetchSixCoreQuestions()
                .map(assessmentDTO -> {
                    if (assessmentDTO.getQuestions() == null || assessmentDTO.getQuestions().isEmpty()) {
                        return ResponseEntity.badRequest().body("No questions found");
                    }
                    return ResponseEntity.ok(assessmentDTO);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body("Error fetching questions: " + e.getMessage())));
    }

    @PostMapping("/sendfirstsix")
    public Mono<ResponseEntity<?>> sendFirstSix(@RequestBody AssessmentDTO assessmentDTO) {
        return assessmentService.saveQuestionScore(assessmentDTO)
                .flatMap(saved -> {
                    if (saved) {
                        return assessmentService.fetchSecondBatchQuestions(assessmentDTO)
                                .map(ResponseEntity::ok);
                    } else {
                        return Mono.just(ResponseEntity.badRequest().build());
                    }
                });
    }
}
