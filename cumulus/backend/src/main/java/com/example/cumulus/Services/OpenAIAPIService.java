package com.example.cumulus.Services;

import com.example.cumulus.DTOs.ChatGPTRequest;
import com.example.cumulus.DTOs.ChatGPTResponse;
import com.example.cumulus.DTOs.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIAPIService {
    private final WebClient webClient;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    public OpenAIAPIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
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

}
