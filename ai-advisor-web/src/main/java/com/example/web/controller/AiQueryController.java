package com.example.web.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiQueryController {

    private final ChatClient chatClient;

    public AiQueryController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public record AiQueryRequest(String q) {}
    public record AiQueryResponse(String answer) {}

    @PostMapping(value = "/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AiQueryResponse query(@RequestBody AiQueryRequest body) {
        String answer = chatClient.prompt()
                .system("Return plain text only. No markdown. No code fences.")
                .user(body.q())
                .call()
                .content();
        return new AiQueryResponse(answer);
    }
}