package com.example.cumulus.Services;

import com.example.cumulus.DTOs.*;
import com.example.cumulus.Entities.Provider;
import com.example.cumulus.Entities.ProviderAttribute;
import com.example.cumulus.Entities.QuestionAttribute;
import com.example.cumulus.Entities.Resource;
import com.example.cumulus.Repositories.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssessmentResultService {

    private final AssessmentResultRepository repository;
    private final QuestionAttributeRepository questionAttributeRepository;
    private final ProviderAttributeRepository providerAttributeRepository;
    private final ProviderRepository providerRepository;
    private final OpenAIAPIService openAIAPIService;
    private final ResourceRepository resourceRepository;

    public AssessmentResultService(AssessmentResultRepository repository, QuestionAttributeRepository questionAttributeRepository, ProviderAttributeRepository providerAttributeRepository, ProviderRepository providerRepository, OpenAIAPIService openAIAPIService, ResourceRepository resourceRepository) {
        this.repository = repository;
        this.questionAttributeRepository = questionAttributeRepository;
        this.providerAttributeRepository = providerAttributeRepository;
        this.providerRepository = providerRepository;
        this.openAIAPIService = openAIAPIService;
        this.resourceRepository = resourceRepository;
    }

    public Mono<Map<String, Object>> processAssessment(AssessmentDTO dto) {
        System.out.println("Received AssessmentDTO: " + dto);

        List<QuestionDTO> top6Questions = dto.getQuestions().stream()
                .sorted(Comparator.comparingInt(q -> -q.getQuestion().getScore()))
                .limit(6)
                .toList();

        System.out.println("Top 6 Questions: " + top6Questions);

        List<String> top6QuestionIds = top6Questions.stream()
                .map(qdto -> qdto.getQuestion().getId())
                .toList();

        return Flux.fromIterable(top6QuestionIds)
                .flatMap(questionAttributeRepository::findByQuestionId)
                .map(QuestionAttribute::getAttributeId)
                .collectList()
                .flatMap(attributeIds -> {
                    System.out.println("Attribute IDs linked to Top 6 Questions: " + attributeIds);

                    return providerAttributeRepository.findByAttributeIdIn(attributeIds)
                            .collectList()
                            .flatMap(providerAttributes -> {
                                Map<String, Integer> providerScores = providerAttributes.stream()
                                        .collect(Collectors.groupingBy(
                                                ProviderAttribute::getProviderId,
                                                Collectors.summingInt(ProviderAttribute::getRank)
                                        ));

                                System.out.println("Provider Scores: " + providerScores);

                                Map<String, Object> result = new HashMap<>();
                                result.put("attributeIds", attributeIds);
                                result.put("providerScores", providerScores);

                                return Mono.just(result);
                            });
                });
    }
    public Mono<AssessmentResult> initialiseAssessmentModule(MakeModuleDTO dto) {
        Mono<Provider> providerMono = providerRepository.findById(dto.getProviderId());
        Mono<String> descriptionMono = providerRepository.findDescriptionById(dto.getProviderId());
        Mono<List<Resource>> resourcesMono = Mono.just(dto.getResources());

        return Mono.zip(providerMono, descriptionMono, resourcesMono)
                .flatMap(tuple -> {
                    Provider provider = tuple.getT1();
                    String description = tuple.getT2();
                    List<Resource> allResources = tuple.getT3();

                    AssessmentResult assessmentModule = new AssessmentResult();
                    assessmentModule.setCloudProvider(provider.getName());
                    assessmentModule.setProviderDescription(description);

                    double costPerMonth = 0.0;
                    List<HashMap<?, ?>> details = new ArrayList<>();
                    Map<String, Double> priceSavings = new HashMap<>();
                    List<Resource> keyResources = new ArrayList<>();

                    for (Resource resource : allResources) {
                        if (resource.getProviderId().equals(dto.getProviderId())) {
                            keyResources.add(resource);
                            details.add(resource.getDetails());

                            Map<String, Object> rdetails = (Map<String, Object>) resource.getDetails();
                            if (rdetails != null) {
                                double resourceCost = 0.0;
                                for (Map.Entry<String, Object> entry : rdetails.entrySet()) {
                                    String key = entry.getKey();
                                    Object value = entry.getValue();
                                    if (key.equalsIgnoreCase("CPM")) {
                                        resourceCost += Double.parseDouble(value.toString());
                                    } else if (key.equalsIgnoreCase("CPH")) {
                                        resourceCost += Double.parseDouble(value.toString()) * 730;
                                    }
                                }
                                costPerMonth += resourceCost;
                                priceSavings.merge(provider.getName(), resourceCost, Double::sum);
                            }
                        }
                    }

                    CostEstimator costEstimator = new CostEstimator();
                    costEstimator.setCostPerMonth(costPerMonth);
                    costEstimator.setCostBreakdown(details);

                    assessmentModule.setCostEstimator(costEstimator);

                    MigrationPlanner migrationPlanner = new MigrationPlanner();
                    migrationPlanner.setKeyResources(keyResources);

                    // GPT 1: Migration Time
                    List<Message> messages = List.of(
                            new Message("system", "You are an expert cloud migration advisor."),
                            new Message("user", "Based on the following details, what is the estimated time to migrate the resources to the cloud provider " + provider.getName() + "? " + ": " + keyResources.toString() + ". Please answer STRICTLY in numbers (signifies minutes)")
                    );

                    Mono<Integer> timeMono = openAIAPIService.chatWithGPT(messages)
                            .map(response -> {
                                String answer = response.getChoices().get(0).getMessage().getContent();
                                try {
                                    return Integer.parseInt(answer.trim());
                                } catch (NumberFormatException e) {
                                    return 0;
                                }
                            });

                    // GPT 2: Migration Difficulty
                    List<Message> messages2 = List.of(
                            new Message("system", "You are an expert cloud migration advisor."),
                            new Message("user", "How hard would it be to migrate without professional help to " + provider.getName() + "? " + keyResources.toString() + " (Answer: Hard, Medium, or Easy)")
                    );

                    Mono<String> difficultyMono = openAIAPIService.chatWithGPT(messages2)
                            .map(response -> response.getChoices().get(0).getMessage().getContent().trim());

                    // GPT 3: Justification
                    List<Message> messages3 = List.of(
                            new Message("system", "You are an expert cloud migration advisor."),
                            new Message("user", "Give justification for picking " + provider.getName() + " over other cloud providers. Less than 100 words.")
                    );

                    Mono<String> justificationMono = openAIAPIService.chatWithGPT(messages3)
                            .map(response -> response.getChoices().get(0).getMessage().getContent().trim());

                    return Mono.zip(timeMono, difficultyMono, justificationMono)
                            .map(results -> {
                                migrationPlanner.setMigrationTime(results.getT1());
                                migrationPlanner.setMigrationDifficulty(results.getT2());
                                assessmentModule.setMigrationPlanner(migrationPlanner);
                                assessmentModule.setJustification(results.getT3());
                                return assessmentModule;
                            });
                });
    }

}

