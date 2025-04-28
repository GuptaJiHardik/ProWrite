package com.pro.write.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.write.backend.model.Resume;
import com.pro.write.backend.repository.ResumeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

@Service
public class FormalService {

    private final WebClient webClient;
    private final ResumeRepository resumeRepository;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public FormalService(WebClient.Builder webClientBuilder, ResumeRepository resumeRepository) {
        this.webClient = webClientBuilder.build();
        this.resumeRepository = resumeRepository;
    }

    public String formalReply(String userId, String metaData, MultipartFile resumeFile) throws Exception {
        String resumeText = null;

        if (resumeFile != null && !resumeFile.isEmpty()) {
            // new resume uploaded
            resumeText = extractTextFromPdf(resumeFile);

            // save or update resume in database
            Resume resume = Resume.builder()
                    .userId(userId)
                    .resumeText(resumeText)
                    .build();
            resumeRepository.save(resume);
        } else {
            // no file uploaded -> try fetching saved resume
            Optional<Resume> optionalResume = resumeRepository.findById(userId);
            if (optionalResume.isPresent()) {
                resumeText = optionalResume.get().getResumeText();
            }
            // else resumeText will stay null → handled below
        }

        String prompt = createPrompt(metaData, resumeText);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        String uri = UriComponentsBuilder.fromHttpUrl(geminiApiUrl)
                .queryParam("key", geminiApiKey)
                .toUriString();

        String response = webClient.post()
                .uri(uri)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> new RuntimeException("API error: " + body))
                )
                .bodyToMono(String.class)
                .block();

        return extractResponseContent(response);
    }

    private String extractTextFromPdf(MultipartFile pdfFile) throws Exception {
        try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            System.out.println("Extracted PDF text: " + text);
            return text;
        }
    }


    private String createPrompt(String metaData, String resumeText) {
        StringBuilder str = new StringBuilder();
        str.append("Please generate a formal and professional reply to the following message. Maintain a polite, respectful tone and ensure clarity and conciseness. ");
        str.append("Do not include a subject line.\n\n");
        str.append("Input text: ").append(metaData).append("\n\n");

        if (resumeText != null && !resumeText.isBlank()) {
            str.append("Resume text: ").append(resumeText).append("\n\n");
        } else {
            str.append("Resume text: (No resume available)\n\n");
        }

        return str.toString();
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates").get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }
    }
}
