package com.example.cumulus.Services;

import com.example.cumulus.DTOs.AssessmentDTO;
import com.example.cumulus.DTOs.QuestionDTO;
import com.example.cumulus.Entities.*;
import com.example.cumulus.Repositories.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssessmentService {
    private final QuestionRepository questionRepository;
    private final QuestionAttributeRepository questionAttributeRepository;
    private final AssessmentRepository assessmentRepository;
    private final ProviderRepository providerRepository;
    private final ResourceRepository resourceRepository;
    public AssessmentService(QuestionRepository questionRepository, QuestionAttributeRepository questionAttributeRepository, AssessmentRepository assessmentRepository, ProviderRepository providerRepository, ResourceRepository resourceRepository) {
        this.questionRepository = questionRepository;
        this.questionAttributeRepository = questionAttributeRepository;
        this.assessmentRepository = assessmentRepository;
        this.providerRepository = providerRepository;
        this.resourceRepository = resourceRepository;
    }

    public Mono<AssessmentDTO> fetchSixCoreQuestions(){
        return Mono.zip(
                questionRepository.findAll().collectList(),
                questionAttributeRepository.findAll().collectList()
        ).map(tuple -> {

            List<Question> allQuestions = tuple.getT1();
            List<QuestionAttribute> questionAttributes = tuple.getT2();

            Map<String, Question> coreQuestionsMap = allQuestions.stream()
                    .filter(q -> "Core".equalsIgnoreCase(q.getType()))
                    .collect(Collectors.toMap(Question::getId, q -> q));

            Map<String, List<Question>> groupedByAttribute = new HashMap<>();
            for (QuestionAttribute qa : questionAttributes) {
                String questionId = qa.getQuestionId();
                Question q = coreQuestionsMap.get(questionId);
                if (q != null) {
                    groupedByAttribute
                            .computeIfAbsent(qa.getAttributeId(), k -> new ArrayList<>())
                            .add(q);
                }
            }

            List<QuestionDTO> bestQuestions = groupedByAttribute.values().stream()
                    .flatMap(questions -> questions.stream()
                            .sorted(Comparator.comparingInt(Question::getScore).reversed())
                            .limit(6))
                    .map(q -> {
                        QuestionDTO dto = new QuestionDTO();
                        dto.setQuestion(q);
                        return dto;
                    })
                    .collect(Collectors.toList());

            AssessmentDTO assessmentDTO = new AssessmentDTO();
            assessmentDTO.setQuestions(bestQuestions);
            return assessmentDTO;
        });
    }

    public Mono<AssessmentDTO> fetchSecondBatchQuestions(AssessmentDTO dto){
        List<QuestionDTO> sortedByScore = dto.getQuestions().stream()
                .sorted(Comparator.comparingInt((QuestionDTO q) -> q.getQuestion().getScore()).reversed())
                .toList();

        List<QuestionDTO> twoHighest = sortedByScore.stream()
                .limit(2)
                .toList();

        List<String> twoHighestQuestionIds = twoHighest.stream()
                .map(qdto -> qdto.getQuestion().getId())
                .collect(Collectors.toList());

        return Flux.fromIterable(twoHighestQuestionIds)
                .flatMap(questionAttributeRepository::findByQuestionId)
                .map(QuestionAttribute::getAttributeId)
                .distinct()
                .collectList()

                .flatMap(attributeIds -> {
                    return questionAttributeRepository.findByAttributeIdIn(attributeIds)
                            .flatMap(attr -> questionRepository.findById(attr.getQuestionId()))
                            .filter(question -> "Standard".equalsIgnoreCase(question.getType()))
                            .collectList();
                })
                .map(standardQuestions -> {
                    List<Question> sortedStandard = standardQuestions.stream()
                            .sorted(Comparator.comparingInt(Question::getScore).reversed())
                            .limit(6) // Pick 6
                            .toList();

                    List<QuestionDTO> secondBatch = sortedStandard.stream()
                            .map(q -> {
                                QuestionDTO qdto = new QuestionDTO();
                                qdto.setQuestion(q);
                                return qdto;
                            })
                            .collect(Collectors.toList());
                    AssessmentDTO secondBatchDTO = new AssessmentDTO();
                    secondBatchDTO.setQuestions(secondBatch);
                    return secondBatchDTO;
                });
    }

    public Mono<Boolean> saveQuestionScore(AssessmentDTO dto){
        return Mono.just(dto)
                .flatMap(assessmentDTO -> {
                    List<QuestionDTO> questions = assessmentDTO.getQuestions();
                    List<Mono<Boolean>> saveMonos = new ArrayList<>();

                    for (QuestionDTO questionDTO : questions) {
                        Question question = questionDTO.getQuestion();
                        int score = question.getScore();
                        Mono<Boolean> saveMono = questionRepository.save(question)
                                .map(savedQuestion -> savedQuestion.getScore() == score);
                        saveMonos.add(saveMono);
                    }

                    return Mono.zip(saveMonos, results -> Arrays.stream(results).allMatch(result -> (Boolean) result));
                });
    }

    public Mono<Assessment> getAssessment(String ID) {
        return assessmentRepository.findById(ID)
                .switchIfEmpty(Mono.error(new RuntimeException("Assessment not found")));
    }
    public Mono<List<Provider>> getCloudProviders() {
        return providerRepository.findAll()
                .collectList()
                .map(ArrayList::new);
    }
    public Mono<List<Resource>> getResources(Assessment assessment) {
        Collection<String> resourceIds = assessment.getProviderResources().values();
        return resourceRepository.findAllById(resourceIds)
                .collectList();
    }
}
