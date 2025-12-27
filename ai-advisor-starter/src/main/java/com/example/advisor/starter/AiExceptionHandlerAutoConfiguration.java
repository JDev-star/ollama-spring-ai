package com.example.advisor.starter;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.DefaultChatOptions;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AutoConfiguration
@ConditionalOnClass(RestControllerAdvice.class)
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(
        prefix = "ai.advisor",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@org.springframework.boot.context.properties.EnableConfigurationProperties(AiAdvisorProperties.class)
public class AiExceptionHandlerAutoConfiguration {

    @Bean
    public AiAdviceStore aiAdviceStore() {
        return new AiAdviceStore();
    }

    @RestControllerAdvice
    public static class AiExceptionHandler {

        private final ChatClient chatClient;
        private final AiAdviceStore store;

        private final ExecutorService executor = Executors.newFixedThreadPool(2);

        public AiExceptionHandler(ChatClient chatClient, AiAdviceStore store) {
            this.chatClient = chatClient;
            this.store = store;
        }

        @ExceptionHandler(NullPointerException.class)
        public ResponseEntity<String> handle(Exception ex) {
            String requestId = UUID.randomUUID().toString();

            executor.submit(() -> {
                DefaultChatOptions options = new DefaultChatOptions();
                options.setTemperature(0.0);
                options.setMaxTokens(120);

                String stacktrace = Arrays.stream(ex.getStackTrace())
                        .limit(5)
                        .map(StackTraceElement::toString)
                        .reduce("", (a, b) -> a + "\n" + b);

                String advice = chatClient.prompt()
                        .system("Return concise plain-text advice. Max 5 bullet points. No markdown.")
                        .user(
                                "Exception type: " + ex.getClass().getName() + "\n" +
                                        "Message: " + ex.getMessage() + "\n" +
                                        "Top stacktrace:\n" + stacktrace
                        )
                        .options(options)
                        .call()
                        .content();

                store.put(requestId, advice);
            });

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("AI advice is being generated. Request-Id: " + requestId);
        }
    }
}