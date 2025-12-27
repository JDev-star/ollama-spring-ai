package com.example.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NullPointerController {

    @GetMapping("/null")
    public String status() {
        throw new NullPointerException();
    }
}