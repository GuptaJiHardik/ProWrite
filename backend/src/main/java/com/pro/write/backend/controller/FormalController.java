package com.pro.write.backend.controller;

import com.pro.write.backend.service.FormalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/text")
public class FormalController {

    private final FormalService formalService;

    public FormalController(FormalService formalService) {
        this.formalService = formalService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateReply(
            @RequestParam String userId,
            @RequestParam String metaData,
            @RequestParam(required = false) MultipartFile resumeFile) {
        try {
            String reply = formalService.formalReply(userId, metaData, resumeFile);
            return ResponseEntity.ok(reply);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
