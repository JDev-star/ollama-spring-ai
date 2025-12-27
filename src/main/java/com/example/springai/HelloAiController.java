package com.example.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloAiController {

    private final ChatClient chatClient;

    public HelloAiController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai")
    public String ai(@RequestParam(defaultValue = "Give one tip to fix Java NullPointerException") String q) {
        return chatClient.prompt()
                .user(q)
                .call()
                .content();
    }
}

