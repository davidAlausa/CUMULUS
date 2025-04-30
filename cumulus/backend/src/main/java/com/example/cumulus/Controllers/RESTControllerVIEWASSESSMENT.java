package com.example.cumulus.Controllers;

import com.example.cumulus.Entities.Assessment;
import com.example.cumulus.Services.AssessmentResultService;
import com.example.cumulus.Services.AssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/")
public class RESTControllerVIEWASSESSMENT {

    private final AssessmentService assessmentService;

    public RESTControllerVIEWASSESSMENT(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PostMapping("getAssessment")
    public Mono<ResponseEntity<?>> getAssessment(@RequestBody Map<String, String> payload) {
        return assessmentService.getAssessment(payload.get("assessmentId"))
                .map(result -> {
                    if (result != null) {
                        return ResponseEntity.ok(result);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("getCloudProviders")
    public Mono<ResponseEntity<?>> getCloudProviders() {
        return assessmentService.getCloudProviders()
                .map(result -> {
                    if (result != null) {
                        return ResponseEntity.ok(result);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("getResources")
    public Mono<ResponseEntity<?>> getResources(@RequestBody Assessment assessment) {
        return assessmentService.getResources(assessment)
                .map(result -> {
                    if (result != null) {
                        return ResponseEntity.ok(result);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
