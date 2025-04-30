package com.example.cumulus.Controllers;

import com.example.cumulus.DTOs.AssessmentResult;
import com.example.cumulus.DTOs.MakeModuleDTO;
import com.example.cumulus.DTOs.WorkStreamInput;
import com.example.cumulus.Services.AssessmentResultService;
import com.example.cumulus.Services.WorkstreamInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class RESTControllerASSESSMENTMODULE {

    @Autowired
    private AssessmentResultService assessmentResultService;

    @Autowired
    private WorkstreamInputService workStreamInputService;

    /*@PostMapping("assessment-module")
    public Mono<ResponseEntity<?>> loadAssessmentModule(@RequestBody Map<String, String> payload) {

        String workstreamID = payload.get("workstreamID");

        Mono<AssessmentResult> workstreamMono = assessmentResultService.getWorkstreamByID(workstreamID);
        Mono<WorkStreamInput> workStreamInputMono = workStreamInputService.getWorkstreamInputByWorkstreamID(workstreamID);

        return Mono.zip(workstreamMono, workStreamInputMono)
                .map(tuple -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("workstream", tuple.getT1());
                    response.put("inputs", tuple.getT2());
                    return ResponseEntity.ok(response);
                });
    }*/

    @PostMapping("initialiseAssessmentModule")
    public Mono<ResponseEntity<?>> initialiseAssessmentModule(@RequestBody MakeModuleDTO dto) {
        return assessmentResultService.initialiseAssessmentModule(dto)
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
