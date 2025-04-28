package com.pro.write.backend.controller;

import org.springframework.web.bind.annotation.*;
import com.pro.write.backend.service.FormalService;

@RestController
@RequestMapping("/api/text")
public class FormalController {

    private final FormalService formalService;

    public FormalController(FormalService formalService) {
        this.formalService = formalService;
    }

    @PostMapping("/generate")
    public String generateReply(@RequestBody String metaData) {
        return formalService.formalReply(metaData);
    }
}
