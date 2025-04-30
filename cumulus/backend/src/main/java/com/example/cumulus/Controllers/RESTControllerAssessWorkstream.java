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

    @PostMapping("/assess-workstream")
    public Mono<Map<String, Object>> assess(@RequestBody ChatGPTRequest request) {

        if (request.getMessages() == null || request.getMessages().isEmpty()) {
            return Mono.error(new RuntimeException("Invalid request: messages list is empty"));
        }

        List<Message> baseMessages = new ArrayList<>();
        String systemPrompt = "You are an AI that evaluates workstreams for cloud adoption using the Well-Architected Framework. "
                + "Your goal is to assess a given workstream and provide a structured response in strict JSON format. NO JSON BACKTICKS "
                + "Use accessible language for non-technical users.";

        baseMessages.add(new Message("system", systemPrompt));
        baseMessages.addAll(request.getMessages());

        Map<String, Object> finalResponse = new HashMap<>();

        return openAIAPIService.chatWithGPT(appendMessage(baseMessages, "Provide the best cloud provider choice and justification making reference to the users inputs. JSON format should look like this: {\n"
                        + "  \"assessmentModule\": {\n"
                        + "    \"cloudProvider\": \"<AWS | GCP | Azure>\",\n"
                        + "    \"justification\": \"<Detailed explanation of why this provider is best based on user input>\",\n"
                        + "  },\n"))
                .flatMap(assessmentResponse -> {
                    finalResponse.put("assessmentModule", parseResponse(assessmentResponse));

                    return openAIAPIService.chatWithGPT(appendMessage(baseMessages, "Estimate initial and monthly cost for the chosen cloud provider. Using accessible language that a non-technical user would understand, give a detailed cost breakdown for each involved cloud provider resource to justify the cost provided. Compare with the monthly prices in AWS, GCP, and Azure. JSON format should look like this: \"costEstimator\": {\n"
                            + "    \"initialCost\": <Total initial cost estimate in euros (double)>,\n"
                            + "    \"costPerMonth\": <Estimated monthly cost in euros (double)>,\n"
                            + "    \"costBreakdown\": [\n"
                            + "      [\"<Essential Cloud Provider Resource (RESOURCE NAME ONLY)>\", \"<Short 10-word max description>\", \"<Calculation breakdown>\"],\n"
                            + "      [\"<Essential Cloud Provider Resource (RESOURCE NAME ONLY)>\", \"<Short 10-word max description>\", \"<Calculation breakdown>\"],\n"
                            + "      [\"<Essential Cloud Provider Resource (RESOURCE NAME ONLY)>\", \"<Short 10-word max description>\", \"<Calculation breakdown>\"]\n"
                            + "    ],\n"
                            + "    \"priceSavings\": {\n"
                            + "      \"AWS\": <Estimated AWS monthly cost (double)>,\n"
                            + "      \"GCP\": <Estimated GCP monthly cost (double)>,\n"
                            + "      \"Azure\": <Estimated Azure monthly cost (double)>\n"
                            + "    }\n"
                            + "  },\n"));
                })
                .flatMap(costResponse -> {
                    finalResponse.put("costEstimator", parseResponse(costResponse));

                    return openAIAPIService.chatWithGPT(appendMessage(baseMessages, "Using accessible language that a non-technical user would understand, provide a detailed migration plan with migration time in minutes, difficulty level (Low, Medium Hard), at least 10 in depth steps to guide the user into getting it implemented, and key cloud provider resources that will be used. JSON Format should look like this: \"migrationPlanner\": {\n"
                            + "    \"migrationTime\": <Estimated time to migrate in hours>,\n"
                            + "    \"migrationDifficulty\": \"<Very Low | Low | Medium | High | Very High>\",\n"
                            + "    \"steps\": [\n"
                            + "      \"<Step 1: Action + brief clarification>\",\n"
                            + "      \"<Step 2: Action + brief clarification>\",\n"
                            + "      \"<Step 3: Action + brief clarification>\"\n"
                            + "    ],\n"
                            + "    \"keyResources\": [\n"
                            + "      [\"<Resource Name>\", \"<50-word max description of function and benefit>\"],\n"
                            + "      [\"<Resource Name>\", \"<50-word max description of function and benefit>\"]\n"
                            + "    ]\n"
                            + "  },\n"));
                })
                .flatMap(migrationResponse -> {
                    finalResponse.put("migrationPlanner", parseResponse(migrationResponse));

                    return openAIAPIService.chatWithGPT(appendMessage(baseMessages, "Using accessible language that a non-technical user would understand, provide a confidence level (0-100) with a detailed justification for your ratings with a minimum of 200 words. This justifification will include a comparison over other cloud providers for the use case and why the chosen cloud provider excels over the other two for the use case, and how realistic the migration times and difficulties may be when done by their own. JSON format should look like: \\\"justification\\\": {\\n\"\n" +
                            "                + \"    \\\"confidenceLevel\\\": <Confidence rating out of 100 on how sure the AI is on obtaining accurate information related to the users inputs>,\\n\"\n" +
                            "                + \"    \\\"reason\\\": \\\"<Justification for this confidence level. This will include a comparison over other cloud providers for the use case, a price calculation for all resources and plans involved in the assessment and how realistic the migration times and difficulties may be. Min 200 words>\\\"\\n\"\n" +
                            "                + \"  }\\n\"\n" +
                            "                + \"}\\n\"\n" +
                            "                + \""));
                })
                .map(confidenceResponse -> {
                    finalResponse.put("justification", parseResponse(confidenceResponse));
                    return finalResponse;
                });

    }

    private List<Message> appendMessage(List<Message> baseMessages, String prompt) {
        List<Message> updatedMessages = new ArrayList<>(baseMessages);
        updatedMessages.add(new Message("user", prompt));
        return updatedMessages;
    }

    private Object parseResponse(ChatGPTResponse response) {
        return response.getChoices().get(0).getMessage().getContent();
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
