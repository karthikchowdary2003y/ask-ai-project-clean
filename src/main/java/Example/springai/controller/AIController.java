package Example.springai.controller;

import org.springframework.web.bind.annotation.*;

import Example.springai.service.GroqService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final GroqService groqService;

    public AIController(GroqService groqService) {
        this.groqService = groqService;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String prompt) {
        return groqService.askAI(prompt);
    }
}