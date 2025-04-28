package com.pro.write.backend.controller;

import org.springframework.web.bind.annotation.*;
import com.pro.write.backend.service.FormalService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/text")
public class FormalController {

    private final FormalService formalService;

    public FormalController(FormalService formalService) {
        this.formalService = formalService;
    }

    @PostMapping("/generate")
    public String generateReply(
            @RequestParam("metaData") String metaData,
            @RequestParam(value = "resumeFile", required = false) MultipartFile resumeFile) throws Exception {

        String response =formalService.formalReply(metaData,resumeFile);
        return response;
    }
}
