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

            resumeText = extractTextFromPdf(resumeFile);

            Resume resume = Resume.builder()
                    .userId(userId)
                    .resumeText(resumeText)
                    .build();
            resumeRepository.save(resume);
        } else {

            Optional<Resume> optionalResume = resumeRepository.findById(userId);
            if (optionalResume.isPresent()) {
                resumeText = optionalResume.get().getResumeText();
            }
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

        str.append("You are generating a formal, professional message from the user (me) to a recruiter who sent a job opportunity.\n\n");

        str.append("🛠CONTEXT:\n");
        str.append("- The user may have selected a job description, recruiter message, or role name as input (metaData).\n");
        str.append("- Resume text may be provided for relevant experience and skills.\n\n");

        str.append("INSTRUCTIONS:\n");
        str.append("- Write in the first person (use 'I', 'my', not 'the user').\n");
        str.append("- Start with a short greeting like 'Hi' or 'Dear'. Use 'Hi' if no recruiter name is mentioned.\n");
        str.append("- Highlight only relevant experience from resume or projects.\n");
        str.append("- Include tools, technologies, or projects if they match the job requirement.\n");
        str.append("- If something is not known or weak (e.g., React), politely mention it.\n");
        str.append("- Keep the tone confident, polite, and professional.\n");
        str.append("- Avoid generic phrases, flattery, or excessive enthusiasm.\n");
        str.append("- End with a short, polite call to action like: 'Let me know if you'd like to connect.'\n\n");

        str.append("INPUT:\n");
        str.append("Recruiter's Message or Role Info:\n");
        str.append(metaData).append("\n\n");

        if (resumeText != null && !resumeText.isBlank()) {
            str.append("Resume Text:\n");
            str.append(resumeText).append("\n\n");
        } else {
            str.append("Resume Text: (No resume provided)\n\n");
        }

        str.append("✍️ Generate the final reply below:\n");

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
