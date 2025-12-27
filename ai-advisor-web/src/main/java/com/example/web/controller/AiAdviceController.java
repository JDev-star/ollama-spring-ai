package com.example.web.controller;

import com.example.advisor.starter.AiAdviceStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai/advice")
public class AiAdviceController {

    private final AiAdviceStore store;

    public AiAdviceController(AiAdviceStore store) {
        this.store = store;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> get(@PathVariable("id") String id) {
        String advice = store.get(id);
        if (advice == null) {
            return ResponseEntity.accepted().body("Not ready");
        }
        return ResponseEntity.ok(advice);
    }
}
