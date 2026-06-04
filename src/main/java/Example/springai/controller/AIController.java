package com.chstbot.aichatbot.controller;

import org.springframework.web.bind.annotation.*;
import com.chstbot.aichatbot.service.AiService;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "https://ask-ai-frontend-hazel.vercel.app")
public class AiController {

    private AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String prompt) {
        return aiService.askAi(prompt);
    }
}
