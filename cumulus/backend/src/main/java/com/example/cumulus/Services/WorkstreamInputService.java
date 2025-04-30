package com.example.cumulus.Services;

import com.example.cumulus.Configs.JWTUtil;
import com.example.cumulus.DTOs.QuestionDTO;
import com.example.cumulus.DTOs.SaveInputDTO;
import com.example.cumulus.DTOs.WorkStreamInput;
import com.example.cumulus.Entities.Assessment;
import com.example.cumulus.Entities.Resource;
import com.example.cumulus.Models.UserProfile;
import com.example.cumulus.Repositories.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WorkstreamInputService {

    private final WorkstreamInputRepository workstreamInputRepository;
    private final AssessmentRepository assessmentRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceAttributeRepository resourceAttributeRepository;
    private final UserProfileRepository userProfileRepository;
    private final JWTUtil jwtTokenUtil;
    public WorkstreamInputService(WorkstreamInputRepository workstreamInputRepository, AssessmentRepository assessmentRepository, ResourceRepository resourceRepository, ResourceAttributeRepository resourceAttributeRepository, UserProfileRepository userProfileRepository, JWTUtil jwtTokenUtil) {
        this.workstreamInputRepository = workstreamInputRepository;
        this.assessmentRepository = assessmentRepository;
        this.resourceRepository = resourceRepository;
        this.resourceAttributeRepository = resourceAttributeRepository;
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }



    public Mono<Assessment> saveWorkstreamInput(SaveInputDTO dto, String authHeader) {

        Assessment assessment = new Assessment();
        assessment.setDateCreated(Instant.now());
        assessment.setUserID(String.valueOf(userProfileRepository.findByEmail(jwtTokenUtil.extractUsername(authHeader.substring(7))).map(UserProfile::getId)));

        Map<String, String> questionAnswer = dto.getQuestions().stream()
                .collect(Collectors.toMap(q -> q.getQuestion().getText(), QuestionDTO::getAnswerText));
        assessment.setQuestionAnswer(questionAnswer);

        assessment.setProviderScore(dto.getProviderScore());
        assessment.setAttributeIds(dto.getAttributeIds());

        return calculateProviderResources(dto.getProviderScore(), dto.getAttributeIds())
                .flatMap(providerResources -> {
                    assessment.setProviderResources(providerResources);
                    return assessmentRepository.save(assessment);
                });
    }


    public Mono<WorkStreamInput> getWorkstreamInputByWorkstreamID(String workstreamID) {
        return workstreamInputRepository.findWorkStreamInputByWorkstreamID(workstreamID);
    }

    public Mono<Map<String, String>> calculateProviderResources(
            Map<String, Integer> providerScores, List<String> userAttributeIds) {

        List<Mono<Map<String, String>>> providerResults = providerScores.entrySet().stream()
                .map(entry -> {
                    String providerId = entry.getKey();

                    return resourceRepository.findByProviderId(providerId)
                            .filter(resource -> List.of("Compute", "Storage", "Database")
                                    .contains(resource.getCategory()))
                            .collectMultimap(Resource::getCategory)
                            .flatMap(categoryMap -> {
                                List<Mono<Map.Entry<String, String>>> categoryBestResources = categoryMap.entrySet().stream()
                                        .map(categoryEntry -> {
                                            String category = categoryEntry.getKey();
                                            List<Resource> resourcesInCategory = (List<Resource>) categoryEntry.getValue();

                                            return Flux.fromIterable(resourcesInCategory)
                                                    .flatMap(resource -> resourceAttributeRepository.findByResourceId(resource.getId())
                                                            .map(attr -> {
                                                                long score = attr.getAttributeIds().stream()
                                                                        .filter(userAttributeIds::contains)
                                                                        .count();
                                                                return Map.entry(resource.getId(), score);
                                                            }))
                                                    .collectList()
                                                    .mapNotNull(scoreList -> scoreList.stream()
                                                            .max(Map.Entry.comparingByValue())
                                                            .map(best -> Map.entry(providerId + "_" + category, best.getKey()))
                                                            .orElse(null));
                                        })
                                        .collect(Collectors.toList());

                                return Flux.merge(categoryBestResources)
                                        .filter(Objects::nonNull)
                                        .collectMap(Map.Entry::getKey, Map.Entry::getValue);
                            });
                })
                .collect(Collectors.toList());

        return Flux.merge(providerResults)
                .reduce(new HashMap<>(), (map1, map2) -> {
                    map1.putAll(map2);
                    return map1;
                });
    }




}
