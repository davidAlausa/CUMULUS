package com.example.cumulus.Services;

import com.example.cumulus.DTOs.ChatGPTRequest;
import com.example.cumulus.DTOs.ChatGPTResponse;
import com.example.cumulus.DTOs.Message;
import com.example.cumulus.Entities.Question;
import com.example.cumulus.Entities.QuestionAttribute;
import com.example.cumulus.Repositories.AttributeRepository;
import com.example.cumulus.Repositories.QuestionAttributeRepository;
import com.example.cumulus.Repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
public class OpenAIAPIService {
    private final WebClient webClient;
    private final QuestionRepository questionRepository;
    private final QuestionAttributeRepository questionAttributeRepository;
    private final AttributeRepository attributeRepository;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    public OpenAIAPIService(WebClient.Builder webClientBuilder, QuestionRepository questionRepository, QuestionAttributeRepository questionAttributeRepository, AttributeRepository attributeRepository) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.questionRepository = questionRepository;
        this.questionAttributeRepository = questionAttributeRepository;
        this.attributeRepository = attributeRepository;
    }

    public Mono<ChatGPTResponse> chatWithGPT(List<Message> messages) {
        ChatGPTRequest request = new ChatGPTRequest("gpt-3.5-turbo", messages);

        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGPTResponse.class);
    }

    public Mono<ChatGPTResponse> assessWithGPT(List<Message> messages) {
        ChatGPTRequest request = new ChatGPTRequest("gpt-3.5-turbo", messages);

        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGPTResponse.class);
    }

    public Mono<Boolean> evolveWithGPT() {
        return questionRepository.findAllByType("Core")
                .collectList()
                .flatMap(coreQuestions -> {
                    if (coreQuestions.isEmpty()) {
                        return Mono.error(new IllegalStateException("No core questions found"));
                    }

                    Question lowestQuestion = coreQuestions.stream()
                            .min(Comparator.comparingInt(Question::getScore))
                            .orElseThrow();

                    return questionAttributeRepository.findByQuestionId(lowestQuestion.getId())
                            .next()
                            .flatMap(questionAttribute -> {
                                String attributeId = questionAttribute.getAttributeId();

                                return attributeRepository.findById(attributeId)
                                        .flatMap(attribute -> {
                                            String attributeText = attribute.getName();

                                            List<Message> messages = List.of(
                                                    new Message("system", "You are an expert cloud migration advisor."),
                                                    new Message("user", "Generate a new simple yes/no/idk/probably/probably not question for a cloud workload assessment tool. The question should be based on the following attribute: \"" + attributeText + "\". Make it clear, concise, and relevant to helping small/medium businesses choose a cloud provider. Use simple language that a non-technical user would understand. The question should be a single sentence and not a list of questions. The question should be in English. The question should be aimed towards determining what type of workload is the user assessing.")
                                            );

                                            ChatGPTRequest gptRequest = new ChatGPTRequest("gpt-3.5-turbo", messages);

                                            return webClient.post()
                                                    .uri(apiUrl)
                                                    .header("Authorization", "Bearer " + apiKey)
                                                    .header("Content-Type", "application/json")
                                                    .bodyValue(gptRequest)
                                                    .retrieve()
                                                    .bodyToMono(ChatGPTResponse.class)
                                                    .flatMap(chatResponse -> {
                                                        String questionText = chatResponse.getChoices().get(0).getMessage().getContent().trim();
                                                        Question newQuestion = new Question(questionText);
                                                        newQuestion.setType("Standard");

                                                        return questionRepository.save(newQuestion)
                                                                .flatMap(savedQuestion -> {
                                                                    QuestionAttribute link = new QuestionAttribute();
                                                                    link.setQuestionId(savedQuestion.getId());
                                                                    link.setAttributeId(attributeId);
                                                                    return questionAttributeRepository.save(link)
                                                                            .thenReturn(true);
                                                                });
                                                    });
                                        });
                            });
                })
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(false);
                });

    }
}
