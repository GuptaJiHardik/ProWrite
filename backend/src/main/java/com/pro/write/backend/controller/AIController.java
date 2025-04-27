package com.pro.write.backend.controller;

import com.pro.write.backend.model.AIRequest;
import com.pro.write.backend.model.AIResponse;
import com.pro.write.backend.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AIController {

    private final AIService aiService;

    @PostMapping("/generate")
    public AIResponse generateFormalText(@RequestBody AIRequest request) {
        String generatedText = aiService.generateFormalResponse(request.getPrompt(), request.getResumeText());
        return new AIResponse(generatedText);
    }
}
