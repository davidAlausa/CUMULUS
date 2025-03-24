package com.example.cumulus.Controllers;

import com.example.cumulus.DTOs.ChatGPTRequest;
import com.example.cumulus.DTOs.ChatGPTResponse;
import com.example.cumulus.Services.OpenAIAPIService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class RESTControllerTEST {

    private final OpenAIAPIService openAIAPIService;

    public RESTControllerTEST(OpenAIAPIService openAIAPIService) {
        this.openAIAPIService = openAIAPIService;
    }
    @PostMapping("/getgptresponse")
    public Mono<ChatGPTResponse> chat(@RequestBody ChatGPTRequest request) {
        return openAIAPIService.chatWithGPT(request.getMessages());
    }

    @GetMapping("/getrequest")
    public String getRequest() {
        return "https://media-cldnry.s-nbcnews.com/image/upload/t_fit-1500w,f_auto,q_auto:best/msnbc/Components/Photos/070802/070802_orangutan_hmed_10a.jpg";
    }
}
