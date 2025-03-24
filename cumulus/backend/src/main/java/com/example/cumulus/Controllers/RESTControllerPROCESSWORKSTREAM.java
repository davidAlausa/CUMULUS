package com.example.cumulus.Controllers;

import com.example.cumulus.Configs.JWTUtil;
import com.example.cumulus.DTOs.AssessmentResult;
import com.example.cumulus.DTOs.WorkStreamInput;
import com.example.cumulus.Services.AssessmentResultService;
import com.example.cumulus.Services.UserService;
import com.example.cumulus.Services.WorkstreamInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class RESTControllerPROCESSWORKSTREAM {

    @Autowired
    private AssessmentResultService assessmentResultService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private WorkstreamInputService workStreamInput;
    public RESTControllerPROCESSWORKSTREAM(AssessmentResultService assessmentResultService, UserService userService, JWTUtil jwtUtil, WorkstreamInputService workStreamInput) {
        this.assessmentResultService = assessmentResultService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.workStreamInput = workStreamInput;
    }

    @PostMapping("process-workstream")
    public Mono<ResponseEntity<?>> processWorkstream(@RequestBody AssessmentResult request, @RequestHeader("Authorization") String authHeader) {

        System.out.println("Incoming Request: " + request);

        authHeader = authHeader.substring(7);

        return userService.findIDByEmail(jwtUtil.extractUsername(authHeader))
                .flatMap(userID -> {
                    request.setUserID(userID.getId());
                    return assessmentResultService.process(request, userID.getId());
                })
                .map(workstreamID -> {
                    Map<String, String> response = new HashMap<>();
                    if (workstreamID!=null) {
                        response.put("message", "Workstream processed successfully");
                        response.put("workstreamID", workstreamID);
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("error", "Failed to process workstream");
                        return ResponseEntity.internalServerError().body(response);
                    }
                });
    }

    @PostMapping("save-inputs")
    public Mono<ResponseEntity<?>> saveInputs(@RequestBody WorkStreamInput inputs) {

        System.out.println("Incoming Request: " + inputs);


        return workStreamInput.saveWorkstreamInput(inputs)
                .map(saved -> {
                    Map<String, String> response = new HashMap<>();
                    if (saved) {
                        response.put("message", "Inputs saved successfully");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("error", "Failed to save inputs");
                        return ResponseEntity.internalServerError().body(response);
                    }
                });
    }


}
